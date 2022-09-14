package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {

    Service service = new Service();

    public HttpResponse requestMapping(HttpRequest httpRequest) throws Exception {
        if (httpRequest.getMethod() == HttpMethod.GET) {
            if (httpRequest.getPath().equals("/user/create")) {
                service.createUser(httpRequest.getQuery());
                return new HttpResponse(
                        getFile("index.html"),
                        200
                );
            }
            return new HttpResponse(
                    getFile(httpRequest.getPath()),
                    200
            );
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
