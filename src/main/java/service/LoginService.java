package service;

import http.Cookie;
import http.HttpRequest;
import http.HttpResponse;
import entity.User;
import repository.UserRepository;

import java.util.Map;

public class LoginService implements Service {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (login(httpRequest.getBodyToMap())) {
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

    private boolean login(Map<String, String> body) {
        String userId = body.get("userId");
        String password = body.get("password");
        if (userId == null || password == null) {
            return false;
        }
        User user = userRepository.findById(userId);
        return user != null && password.equals(user.getPassword());
    }
}
