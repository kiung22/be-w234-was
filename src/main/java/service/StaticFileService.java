package service;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;

public class StaticFileService implements Service {

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (!existFile(httpRequest.getPath())) {
            return new HttpResponse(
                    getFile("/404.html"),
                    404
            );
        }
        if (httpRequest.getPath().endsWith(".css")) {
            return new HttpResponse(
                    getFile(httpRequest.getPath()),
                    200
            ).addHeader("Content-Type", "text/css");
        }
        return new HttpResponse(
                getFile(httpRequest.getPath()),
                200
        );
    }

    private File getFile(String path) {
        return new File("./webapp" + path);
    }

    private boolean existFile(String path) {
        return new File("./webapp" + path).exists();
    }
}
