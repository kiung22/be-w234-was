package webserver;

import http.HttpMethod;
import http.HttpResponse;
import http.HttpRequest;

import java.io.File;

public class Controller {

    Service service = new Service();

    public HttpResponse requestMapping(HttpRequest httpRequest) {
        if (httpRequest.getMethod() == HttpMethod.GET) {
            if ("/user/create".equals(httpRequest.getPath())) {
                service.createUser(httpRequest.getQuery());
                return new HttpResponse(
                        getFile("index.html"),
                        200
                );
            } else if (httpRequest.getPath().endsWith(".css")) {
                return new HttpResponse(
                        getFile(httpRequest.getPath()),
                        200
                ).addHeader("Content-Type", "text/css");
            }

            if (existFile(httpRequest.getPath())) {
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
        return null;
    }

    private File getFile(String path) {
        return new File("./webapp" + path);
    }

    private boolean existFile(String path) {
        return new File("./webapp" + path).exists();
    }
}
