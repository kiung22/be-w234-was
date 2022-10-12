package webserver;

import controller.*;
import http.HttpMethod;
import http.HttpResponse;
import http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestDispatcher {

    private final Map<String, Controller> getMapper = new HashMap<>();
    private final Map<String, Controller> postMapper = new HashMap<>();
    private final StaticFileController staticFileService = ControllerContainer.getStaticFileController();

    public RequestDispatcher() {
        getMapper.put("/index.html", ControllerContainer.getIndexController());
        getMapper.put("/user/list.html", ControllerContainer.getUserListController());

        postMapper.put("/user/create", ControllerContainer.getSignupController());
        postMapper.put("/user/login", ControllerContainer.getLoginController());
        postMapper.put("/memo", ControllerContainer.getMemoController());
    }

    public HttpResponse requestMapping(HttpRequest httpRequest) {
        if (httpRequest.getMethod() == HttpMethod.GET) {
            if (getMapper.containsKey(httpRequest.getPath())) {
                return getMapper.get(httpRequest.getPath()).run(httpRequest);
            }
            return staticFileService.run(httpRequest);
        } else if (httpRequest.getMethod() == HttpMethod.POST) {
            return postMapper.get(httpRequest.getPath()).run(httpRequest);
        }
        return null;
    }
}
