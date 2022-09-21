package service;

import db.Database;
import http.Cookie;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;

public class LoginService implements Service {

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (login(httpRequest.getBody())) {
           return new HttpResponse(302)
                   .setCookie(new Cookie("logined", "true").setPath("/"))
                   .setHeader("Location", "/index.html");
        }
        return new HttpResponse(302)
                .setCookie(new Cookie("logined", "false").setPath("/"))
                .setHeader("Location", "/user/login_failed.html");
    }

    private boolean login(Map<String, String> body) {
        String userId = body.get("userId");
        String password = body.get("password");
        if (userId == null || password == null) {
            return false;
        }
        User user = Database.findUserById(userId);
        return user != null && password.equals(user.getPassword());
    }
}
