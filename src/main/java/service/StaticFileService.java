package service;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;

public class StaticFileService implements Service {

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (!existFile(httpRequest.getPath())) {
            return new HttpResponse(404)
                    .setBody(getFile("/404.html"));
        }
        if (httpRequest.getPath().endsWith(".css")) {
            return new HttpResponse(200)
                    .setBody(getFile(httpRequest.getPath()))
                    .setHeader("Content-Type", "text/css");
        }
        return new HttpResponse(200)
                .setBody(getFile(httpRequest.getPath()));
    }

    private File getFile(String path) {
        return new File("./webapp" + path);
    }

    private boolean existFile(String path) {
        return new File("./webapp" + path).exists();
    }
}
