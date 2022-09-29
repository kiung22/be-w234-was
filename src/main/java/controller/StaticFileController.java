package service;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;

public class StaticFileService implements Service {

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (!existFile(httpRequest.getPath())) {
            return new HttpResponse.Builder(404)
                    .setBody(getFile("/404.html"))
                    .build();
        }
        if (httpRequest.getPath().endsWith(".css")) {
            return new HttpResponse.Builder(200)
                    .setBody(getFile(httpRequest.getPath()))
                    .setHeader("Content-Type", "text/css")
                    .build();
        }
        return new HttpResponse.Builder(200)
                .setBody(getFile(httpRequest.getPath()))
                .build();
    }

    private File getFile(String path) {
        return new File("./webapp" + path);
    }

    private boolean existFile(String path) {
        return new File("./webapp" + path).exists();
    }
}
