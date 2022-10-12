package service;

import entity.User;
import exception.FileReadFailedException;
import http.HttpRequest;
import repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkLogin(HttpRequest httpRequest) {
        return "true".equals(httpRequest.getCookie().get("logined").getValue());
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
        userRepository.save(user);
    }

    private boolean validateUserInfo(Map<String, String> userInfo) {
        return userInfo.get("userId") == null || userInfo.get("userId").isEmpty() ||
                userInfo.get("password") == null || userInfo.get("password").isEmpty() ||
                userInfo.get("name") == null || userInfo.get("name").isEmpty() ||
                userInfo.get("email") == null || userInfo.get("email").isEmpty();
    }

    public boolean login(Map<String, String> body) {
        String userId = body.get("userId");
        String password = body.get("password");
        if (userId == null || password == null) {
            return false;
        }
        User user = userRepository.findById(userId);
        return user != null && password.equals(user.getPassword());
    }

    public String insertUserList() {
        String path = "./webapp/user/list.html";
        try {
            String html = Files.readString(Path.of(path));
            StringBuilder stringBuilder = new StringBuilder(html);
            return stringBuilder.insert(stringBuilder.lastIndexOf("</tbody>"), getUserListToString()).toString();
        } catch (IOException e) {
            throw new FileReadFailedException(path);
        }
    }

    private String getUserListToString() {
        List<User> userList = userRepository.findAll();
        AtomicInteger i = new AtomicInteger(2);
        return userList.stream()
                .map(user -> String.format(
                        "<tr><th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>",
                        i.incrementAndGet(),
                        user.getUserId(),
                        user.getName(),
                        user.getEmail()
                ))
                .collect(Collectors.joining("\n"));
    }
}
