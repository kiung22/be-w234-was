package controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.UserRepository;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SignupControllerTest {

    private final SignupController signupController = new SignupController();
    private UserRepository userRepository = new UserRepository();
    private static final HashMap<String, String> header = new HashMap<>();

    static {
        header.put("Content-Type", "application/x-www-form-urlencoded");
    }

    @Test
    @DisplayName("올바른 유저정보를 받아서 유저 생성에 성공합니다.")
    void CreateUser() {
        HttpRequest httpRequest = new HttpRequest.Builder(
                HttpMethod.POST,
                "/user/create"
        )
                .setHeader(header)
                .setBody("userId=test&password=test&name=test&email=test")
                .build();

        HttpResponse response = signupController.run(httpRequest);
        User user = userRepository.findById("test");
        assertThat(user.getUserId()).isEqualTo("test");
        assertThat(user.getPassword()).isEqualTo("test");
        assertThat(user.getName()).isEqualTo("test");
        assertThat(user.getEmail()).isEqualTo("test");
        assertThat(response.getStatus().getStatusCode()).isEqualTo(302);
        assertThat(response.getHeader().get("Location")).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("잘못된 유저정보를 받아서 Exception을 일으킵니다.")
    void throwExceptionWhenCreateUser() {
        HttpRequest httpRequest = new HttpRequest.Builder(
                HttpMethod.POST,
                "/user/create"
        )
                .setHeader(header)
                .setBody("userId=test&password=test")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> signupController.run(httpRequest));
        assertThat(exception.getMessage()).isEqualTo("유저 정보가 잘못 되었습니다.");
    }
}
