package service;

import entity.Memo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.MemoRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class MemoServiceTest {

    @Mock
    private MemoRepository memoRepository;

    @InjectMocks
    private MemoService memoService;

    @Test
    @DisplayName("메모 생성에 성공합니다.")
    void createMemo() {
        Map<String, String> body = new HashMap<>();
        body.put("writer", "test");
        body.put("title", "test");
        body.put("content", "test");

        memoService.createMemo(body);

        Memo memo = new Memo();
        memo.setWriter("test");
        memo.setTitle("test");
        memo.setContent("test");
        verify(memoRepository, times(1)).save(refEq(memo));
        verifyNoMoreInteractions(memoRepository);
    }

    @Test
    @DisplayName("유효성 검사에 실패하여 메모 생성에 실패합니다.")
    void invalidMemo() {
        Map<String, String> body = new HashMap<>();
        body.put("writer", "");
        body.put("content", "df");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> memoService.createMemo(body));
        assertThat(exception.getMessage()).isEqualTo("memo 정보가 잘못 되었습니다.");
        verifyNoInteractions(memoRepository);
    }
}
