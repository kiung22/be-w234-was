package webserver;

import http.HttpMethod;
import http.HttpResponse;
import http.HttpRequest;
import service.SignupService;
import service.StaticFileService;

import java.util.HashMap;
import java.util.Map;

public class Controller {

    private final Map<String, SignupService> postMapper = new HashMap<>();
    private final StaticFileService staticFileService = new StaticFileService();

    public Controller() {
        postMapper.put("/user/create", new SignupService());
    }

    public HttpResponse requestMapping(HttpRequest httpRequest) {
        if (httpRequest.getMethod() == HttpMethod.GET) {
            return staticFileService.run(httpRequest);
        } else if (httpRequest.getMethod() == HttpMethod.POST) {
            return postMapper.get(httpRequest.getPath()).run(httpRequest);
        }
        return null;
    }
}
