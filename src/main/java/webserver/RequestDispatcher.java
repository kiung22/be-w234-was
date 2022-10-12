package webserver;

import controller.Controller;
import controller.ControllerContainer;
import controller.StaticFileController;
import http.HttpMethod;
import http.HttpResponse;
import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RequestDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
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
        try{
            if (httpRequest.getMethod() == HttpMethod.GET) {
                if (getMapper.containsKey(httpRequest.getPath())) {
                    return getMapper.get(httpRequest.getPath()).run(httpRequest);
                }
                return staticFileService.run(httpRequest);
            } else if (httpRequest.getMethod() == HttpMethod.POST) {
                return postMapper.get(httpRequest.getPath()).run(httpRequest);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new HttpResponse.Builder(500)
                    .setBody(new File("./webapp/500.html"))
                    .build();
        }
        return null;
    }
}
