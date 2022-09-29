package controller;

import http.HttpRequest;
import http.HttpResponse;
import service.UserService;

public class SignupController implements Controller {

    private final UserService userService = new UserService();

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        userService.createUser(httpRequest.getBodyToMap());
        return new HttpResponse.Builder(302)
                .setHeader("Location", "/index.html")
                .build();
    }
}
