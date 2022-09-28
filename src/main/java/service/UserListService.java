package service;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserListService implements Service {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final UserRepository userRepository = new UserRepository();

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        try {
            String html = insertUserList();
            return new HttpResponse.Builder(200)
                    .setBody(html.getBytes())
                    .build();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new HttpResponse.Builder(500)
                    .setBody(new File("./webapp/500.html"))
                    .build();
        }
    }

    private String insertUserList() throws IOException {
        String html = Files.readString(Path.of("./webapp/user/list.html"));
        StringBuilder stringBuilder = new StringBuilder(html);
        return stringBuilder.insert(stringBuilder.lastIndexOf("</tbody>"), getUserListToString()).toString();
    }

    private String getUserListToString() {
        Collection<User> userList = userRepository.findAll();
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
