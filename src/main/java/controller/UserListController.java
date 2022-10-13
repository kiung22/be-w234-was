package controller;

import http.HttpRequest;
import http.HttpResponse;
import service.UserService;

public class UserListController implements Controller {

    private final UserService userService;

    public UserListController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        String html = userService.insertUserList();
        return new HttpResponse.Builder(200)
                .setBody(html.getBytes())
                .build();
    }
}
