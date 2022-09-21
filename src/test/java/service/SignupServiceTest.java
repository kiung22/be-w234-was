package service;

import db.Database;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SignupServiceTest {

    SignupService signupService = new SignupService();

    @Test
    @DisplayName("올바른 유저정보를 받아서 유저 생성에 성공합니다.")
    void CreateUser() {
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "test");
        userInfo.put("password", "test");
        userInfo.put("name", "test");
        userInfo.put("email", "test");
        HttpRequest httpRequest = new HttpRequest(
                HttpMethod.POST,
                "/user/create",
                new HashMap<>(),
                new HashMap<>(),
                userInfo
        );

        HttpResponse response = signupService.run(httpRequest);
        User user = Database.findUserById("test");
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
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "test");
        userInfo.put("password", "test");
        HttpRequest httpRequest = new HttpRequest(
                HttpMethod.POST,
                "/user/create",
                new HashMap<>(),
                new HashMap<>(),
                userInfo
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> signupService.run(httpRequest));
        assertThat(exception.getMessage()).isEqualTo("유저 정보가 잘못 되었습니다.");
    }
}
