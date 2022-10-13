package service;

import entity.User;
import http.HttpMethod;
import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user = new User("test", "test", "test", "test");

    @Test
    @DisplayName("쿠키를 읽어 로그인 확인에 성공합니다.")
    void checkLogin() {
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "logined=true");
        HttpRequest httpRequest = new HttpRequest.Builder(HttpMethod.POST, "/user/login")
                .setHeader(header)
                .build();

        boolean result = userService.checkLogin(httpRequest);
        assertThat(result).isTrue();
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("유저 생성에 성공합니다.")
    void createUser() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "test");
        userInfo.put("password", "test");
        userInfo.put("name", "test");
        userInfo.put("email", "test");

        userService.createUser(userInfo);

        verify(userRepository, times(1)).save(refEq(user));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("잘못된 유저 정보로 인해 유저 생성에 실패합니다.")
    void failedToCreateUser() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "test");
        userInfo.put("password", "test");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(userInfo));
        assertThat(exception.getMessage()).isEqualTo("유저 정보가 잘못 되었습니다.");
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("로그인에 성공합니다.")
    void login() {
        Map<String, String> body = new HashMap<>();
        body.put("userId", "test");
        body.put("password", "test");

        when(userRepository.findById(anyString())).thenReturn(user);

        boolean result = userService.login(body);
        assertThat(result).isTrue();
        verify(userRepository, times(1)).findById("test");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 인해 로그인에 실패합니다.")
    void incorrectPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("userId", "test");
        body.put("password", "test2");

        when(userRepository.findById(anyString())).thenReturn(user);

        boolean result = userService.login(body);
        assertThat(result).isFalse();
        verify(userRepository, times(1)).findById("test");
        verifyNoMoreInteractions(userRepository);
    }
}
