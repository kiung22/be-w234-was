package controller;

import http.HttpRequest;
import http.HttpResponse;
import service.MemoService;
import service.UserService;

public class MemoController implements Controller {

    private final MemoService memoService;
    private final UserService userService;

    public MemoController(
            MemoService memoService,
            UserService userService
    ) {
        this.memoService = memoService;
        this.userService = userService;
    }

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
