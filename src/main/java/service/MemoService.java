package service;

import entity.Memo;
import repository.MemoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public void createMemo(Map<String, String> body) {
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

    public String insertMemoList() throws IOException {
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
