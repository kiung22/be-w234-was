package webserver;

import java.io.*;
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

    private final Controller controller = new Controller();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = readRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = controller.requestMapping(httpRequest);
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
        String queryString = statusLineMap.get("queryString");
        Map<String, String> query = HttpRequestParser.parseQueryString(queryString);
        logger.info("Http Method: {}", method);
        logger.info("Http Path: {}", path);
        logger.info("Http Query: {}", query);

        Map<String, String> header = new HashMap<>();
        line = br.readLine();
        while (line != null && !line.isEmpty()) {
            HttpRequestParser.Pair pair = HttpRequestParser.parseHeader(line);
            header.put(pair.getKey(), pair.getValue());
            line = br.readLine();
        }
        logger.info("Http Header: {}", header);
        Map<String, String> body;
        if (header.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            char[] bodyChar = new char[contentLength];
            br.read(bodyChar, 0, contentLength);
            String bodySting = String.copyValueOf(bodyChar);
            body = HttpRequestParser.parseBodyString(bodySting);
            logger.info("Http Body: {}", body);
        } else {
            body = new HashMap<>();
        }
        return new HttpRequest(
                method,
                path,
                query,
                header,
                body
        );
    }

    private void writeResponse(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.write(httpResponse.getStatusLineToBytes());
        dos.write(httpResponse.getHeaderToBytes());
        if (httpResponse.getBodyToBytes() != null) {
            dos.write(httpResponse.getBodyToBytes());
        }
        dos.flush();
    }
}
