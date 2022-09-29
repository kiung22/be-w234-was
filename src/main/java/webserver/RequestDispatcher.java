package webserver;

import http.HttpMethod;
import http.HttpResponse;
import http.HttpRequest;
import service.IndexService;
import service.LoginService;
import service.MemoService;
import service.Service;
import service.SignupService;
import service.StaticFileService;
import service.UserListService;

import java.util.HashMap;
import java.util.Map;

public class Controller {

    private final Map<String, Service> getMapper = new HashMap<>();
    private final Map<String, Service> postMapper = new HashMap<>();
    private final StaticFileService staticFileService = new StaticFileService();

    public Controller() {
        getMapper.put("/index.html", new IndexService());
        getMapper.put("/user/list.html", new UserListService());

        postMapper.put("/user/create", new SignupService());
        postMapper.put("/user/login", new LoginService());
        postMapper.put("/memo", new MemoService());
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
