package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceTest {

    Service service = new Service();

    @Test
    @DisplayName("올바른 유저정보를 받아서 유저 생성에 성공합니다.")
    void CreateUser() throws Exception {
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "test");
        userInfo.put("password", "test");
        userInfo.put("name", "test");
        userInfo.put("email", "test");

        service.createUser(userInfo);
        User user = Database.findUserById("test");
        assertThat(user.getUserId()).isEqualTo("test");
        assertThat(user.getPassword()).isEqualTo("test");
        assertThat(user.getName()).isEqualTo("test");
        assertThat(user.getEmail()).isEqualTo("test");
    }

    @Test
    @DisplayName("잘못된 유저정보를 받아서 Exception을 일으킵니다.")
    void throwExceptionWhenCreateUser() {
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "test");
        userInfo.put("password", "test");

        Exception exception = assertThrows(Exception.class, () -> {
            service.createUser(userInfo);
        });
        assertThat(exception.getMessage()).isEqualTo("유저 정보가 잘 못 되었습니다.");
    }
}
