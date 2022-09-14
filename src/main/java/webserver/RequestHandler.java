package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private final Controller controller = new Controller();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = controller.requestMapping(httpRequest);
            writeResponse(dos, httpResponse);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void writeResponse(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.write(httpResponse.getHeader());
            dos.write(httpResponse.getBody());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
