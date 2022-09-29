package webserver;

import http.HttpMethod;
import http.HttpResponse;
import http.HttpRequest;
import controller.IndexController;
import controller.LoginController;
import controller.MemoController;
import controller.Controller;
import controller.SignupController;
import controller.StaticFileController;
import controller.UserListController;

import java.util.HashMap;
import java.util.Map;

public class RequestDispatcher {

    private final Map<String, Controller> getMapper = new HashMap<>();
    private final Map<String, Controller> postMapper = new HashMap<>();
    private final StaticFileController staticFileService = new StaticFileController();

    public RequestDispatcher() {
        getMapper.put("/index.html", new IndexController());
        getMapper.put("/user/list.html", new UserListController());

        postMapper.put("/user/create", new SignupController());
        postMapper.put("/user/login", new LoginController());
        postMapper.put("/memo", new MemoController());
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
