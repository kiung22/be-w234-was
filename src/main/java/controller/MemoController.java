package service;

import entity.Memo;
import http.HttpRequest;
import http.HttpResponse;
import repository.MemoRepository;

import java.util.Map;

public class MemoService implements Service {

    private final MemoRepository memoRepository = new MemoRepository();

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (checkLogin(httpRequest)) {
            createMemo(httpRequest.getBodyToMap());
            return new HttpResponse.Builder(302)
                    .setHeader("Location", "/index.html")
                    .build();
        }
        return new HttpResponse.Builder(302)
                .setHeader("Location", "/user/login.html")
                .build();
    }

    private void createMemo(Map<String, String> body) {
        if (validateBody(body)) {
            Memo memo = new Memo();
            memo.setWriter(body.get("writer"));
            memo.setTitle(body.get("title"));
            memo.setContent(body.get("content"));
            memoRepository.save(memo);
        } else {
            throw new IllegalArgumentException("memo 정보가 잘못 되었습니다.");
        }
    }

    private boolean validateBody(Map<String, String> body) {
        String writer = body.get("writer");
        String title = body.get("title");
        String content = body.get("content");
        return writer != null && !writer.isEmpty() &&
                title != null && !title.isEmpty() &&
                content != null && !content.isEmpty();
    }

    private boolean checkLogin(HttpRequest httpRequest) {
        return "true".equals(httpRequest.getCookie().get("logined").getValue());
    }
}
