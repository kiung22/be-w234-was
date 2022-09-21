package service;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SignupService implements Service {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        createUser(httpRequest.getBody());
        return new HttpResponse(302)
                .setHeader("Location", "/index.html");
    }

    public void createUser(Map<String, String> userInfo) {
        if (validateUserInfo(userInfo)) {
            throw new IllegalArgumentException("유저 정보가 잘못 되었습니다.");
        }
        User user = new User(
                userInfo.get("userId"),
                userInfo.get("password"),
                userInfo.get("name"),
                userInfo.get("email")
        );
        Database.addUser(user);
        logger.debug("Create User: {}", user);
    }

    private boolean validateUserInfo(Map<String, String> userInfo) {
        return userInfo.get("userId") == null || userInfo.get("userId").isEmpty() ||
                userInfo.get("password") == null || userInfo.get("password").isEmpty() ||
                userInfo.get("name") == null || userInfo.get("name").isEmpty() ||
                userInfo.get("email") == null || userInfo.get("email").isEmpty();
    }
}
