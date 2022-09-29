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
    private final MemoService memoService = new MemoService();

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        try {
            String html = memoService.insertMemoList();
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
}