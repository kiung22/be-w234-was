package webserver;

import java.io.*;
import java.net.Socket;
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
        while (line != null && !line.isEmpty()) {
            logger.debug(line);
            line = br.readLine();
        }
        return new HttpRequest(
                method,
                path,
                query
        );
    }

    private void writeResponse(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.write(httpResponse.getStatusLineToBytes());
        dos.write(httpResponse.getHeaderToBytes());
        dos.write(httpResponse.getBodyToBytes());
        dos.flush();
    }
}
