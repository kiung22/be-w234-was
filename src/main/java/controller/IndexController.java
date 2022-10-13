package controller;

import http.HttpRequest;
import http.HttpResponse;
import service.MemoService;

public class IndexController implements Controller {

    private final MemoService memoService;

    public IndexController(MemoService memoService) {
        this.memoService = memoService;
    }

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        String html = memoService.insertMemoList();
        return new HttpResponse.Builder(200)
                .setBody(html.getBytes())
                .build();
    }
}
