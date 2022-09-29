package controller;

import http.HttpRequest;
import http.HttpResponse;
import service.MemoService;
import service.UserService;

public class MemoController implements Controller {

    private final MemoService memoService = new MemoService();
    private final UserService userService = new UserService();

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (userService.checkLogin(httpRequest)) {
            memoService.createMemo(httpRequest.getBodyToMap());
            return new HttpResponse.Builder(302)
                    .setHeader("Location", "/index.html")
                    .build();
        }
        return new HttpResponse.Builder(302)
                .setHeader("Location", "/user/login.html")
                .build();
    }
}
