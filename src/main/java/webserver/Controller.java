package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {

    public HttpResponse requestMapping(HttpRequest httpRequest) throws IOException {
        if (httpRequest.method == HttpMethod.GET) {
            if (httpRequest.path.equals("/index.html")) {
                return new HttpResponse(
                        getFile("/index.html"),
                        200
                );
            }
        }
        return new HttpResponse(
                getFile("/404.html"),
                404
        );
    }

    private byte[] getFile(String path) throws IOException {
        return Files.readAllBytes(new File("./webapp" + path).toPath());
    }
}
