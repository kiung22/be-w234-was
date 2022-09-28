package service;

import entity.Memo;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.MemoRepository;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IndexService implements Service {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final MemoRepository memoRepository = new MemoRepository();

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        try {
            String html = insertMemoList();
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

    private String insertMemoList() throws IOException {
        String html = Files.readString(Path.of("./webapp/index.html"));
        StringBuilder stringBuilder = new StringBuilder(html);
        return stringBuilder.insert(stringBuilder.lastIndexOf("<ul class=\"list\">") + 17, getMemoListToString()).toString();
    }

    private String getMemoListToString() {
        List<Memo> memoList = memoRepository.findAll();
        Collections.reverse(memoList);
        return memoList.stream()
                .map(memo -> String.join(
                            "\n",
                            "<li><div class=\"wrap\">",
                            "<div class=\"main\">",
                            "<strong class=\"subject\">",
                            String.format("<a href=\"./qna/show.html\">%s </a>", memo.getTitle()),
                            "</strong>",
                            "<div class=\"auth-info\">",
                            "<i class=\"icon-add-comment\"></i>",
                            String.format("<span class=\"time\">%s</span>", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(memo.getCreatedBy())),
                            String.format("<a href=\"./user/profile.html\" class=\"author\">%s</a>", memo.getWriter()),
                            "</div>",
                            "<div class=\"reply\" title=\"댓글\">",
                            "<i class=\"icon-reply\"></i>",
                            "<span class=\"point\">0</span>",
                            "</div>",
                            "</div>",
                            "</div>",
                            "</li>"
                    )
                )
                .collect(Collectors.joining("\n"));
    }
}