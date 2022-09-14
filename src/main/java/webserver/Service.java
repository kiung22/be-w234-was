package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Service {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    public void createUser(HashMap<String, String> userInfo) throws Exception {
        if (validateUserInfo(userInfo)) throw new Exception("유저 정보가 잘 못 되었습니다.");
        User user = new User(
                userInfo.get("userId"),
                userInfo.get("password"),
                userInfo.get("name"),
                userInfo.get("email")
        );
        Database.addUser(user);
        logger.debug("Create User: {}", user);
    }

    private boolean validateUserInfo(HashMap<String, String> userInfo) {
        return userInfo.get("userId") == null || userInfo.get("userId").isEmpty() ||
                userInfo.get("password") == null || userInfo.get("password").isEmpty() ||
                userInfo.get("name") == null || userInfo.get("name").isEmpty() ||
                userInfo.get("email") == null || userInfo.get("email").isEmpty();
    }
}
