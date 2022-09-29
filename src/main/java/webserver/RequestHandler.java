package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import http.HttpMethod;
import http.HttpRequestParser;
import http.HttpResponse;
import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    private final RequestDispatcher requestDispatcher = new RequestDispatcher();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = readRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = requestDispatcher.requestMapping(httpRequest);
            writeResponse(dos, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpRequest readRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = br.readLine();

        Map<String, String> statusLineMap = HttpRequestParser.parseStatusLine(line);
        HttpMethod method = HttpMethod.valueOf(statusLineMap.get("method"));
        String path = statusLineMap.get("path");
        HttpRequest.Builder httpRequestBuilder = new HttpRequest.Builder(method, path);
        logger.info("Http Method: {}", method);
        logger.info("Http Path: {}", path);

        String queryString = statusLineMap.get("queryString");
        if (queryString != null && !queryString.isEmpty()) {
            Map<String, String> query = HttpRequestParser.parseQueryString(queryString);
            httpRequestBuilder.setQuery(query);
            logger.info("Http Query: {}", query);
        }

        Map<String, String> header = new HashMap<>();
        line = br.readLine();
        while (line != null && !line.isEmpty()) {
            HttpRequestParser.Pair pair = HttpRequestParser.parseHeader(line);
            header.put(pair.getKey(), pair.getValue());
            line = br.readLine();
        }
        if (!header.isEmpty()) {
            httpRequestBuilder.setHeader(header);
            logger.info("Http Header: {}", header);
        }

        if (header.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            char[] bodyChar = new char[contentLength];
            br.read(bodyChar, 0, contentLength);
            String body = String.copyValueOf(bodyChar);
            httpRequestBuilder.setBody(body);
            logger.info("Http Body: {}", body);
        }
        return httpRequestBuilder.build();
    }

    private void writeResponse(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.write(httpResponse.getStatusLineToBytes());
        dos.write(httpResponse.getHeaderToBytes());
        if (httpResponse.getBody() != null) {
            dos.write(httpResponse.getBody());
        }
        dos.flush();
    }
}
