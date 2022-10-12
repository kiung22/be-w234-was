package controller;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MemoService;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;


public class IndexController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
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
