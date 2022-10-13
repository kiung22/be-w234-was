package controller;

import http.Cookie;
import http.HttpRequest;
import http.HttpResponse;
import service.UserService;

public class LoginController implements Controller {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (userService.login(httpRequest.getBodyToMap())) {
           return new HttpResponse.Builder(302)
                   .setCookie(new Cookie("logined", "true").setPath("/"))
                   .setHeader("Location", "/index.html")
                   .build();
        }
        return new HttpResponse.Builder(302)
                .setCookie(new Cookie("logined", "false").setPath("/"))
                .setHeader("Location", "/user/login_failed.html")
                .build();
    }
}
