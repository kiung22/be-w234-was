package service;

import db.Database;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class LoginServiceTest {

    LoginService loginService = new LoginService();

    @BeforeAll
    static void initializeDatabase() {
        Database.addUser(new User(
                "test",
                "test",
                "test",
                "test"
        ));
    }

    @Test
    @DisplayName("올바른 유저정보를 받아서 로그인에 성공합니다.")
    void login() {
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "test");
        userInfo.put("password", "test");
        HttpRequest httpRequest = new HttpRequest(
                HttpMethod.POST,
                "/user/login",
                new HashMap<>(),
                new HashMap<>(),
                userInfo
        );

        HttpResponse response = loginService.run(httpRequest);
        assertThat(response.getStatus().getStatusCode()).isEqualTo(302);
        assertThat(response.getHeader().get("Location")).isEqualTo("/index.html");
        assertThat(response.getCookie().get("logined").getValue()).isEqualTo("true");
        assertThat(response.getCookie().get("logined").getPath()).isEqualTo("/");
    }

    @Test
    @DisplayName("잘못된 유저정보를 받아서 로그인에 실패합니다.")
    void loginFailed() {
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "incorrect");
        userInfo.put("password", "incorrect");
        HttpRequest httpRequest = new HttpRequest(
                HttpMethod.POST,
                "/user/login",
                new HashMap<>(),
                new HashMap<>(),
                userInfo
        );

        HttpResponse response = loginService.run(httpRequest);
        assertThat(response.getStatus().getStatusCode()).isEqualTo(302);
        assertThat(response.getHeader().get("Location")).isEqualTo("/user/login_failed.html");
        assertThat(response.getCookie().get("logined").getValue()).isEqualTo("false");
        assertThat(response.getCookie().get("logined").getPath()).isEqualTo("/");
    }
}
