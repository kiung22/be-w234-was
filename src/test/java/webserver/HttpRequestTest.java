package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


class HttpRequestTest {

    @Test
    @DisplayName("index 요청에 대한 HttpRequest 객체가 잘 생성됩니다.")
    void indexRequest() {
        InputStream inputStream = new ByteArrayInputStream("GET /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*".getBytes());
        HttpRequest httpRequest = new HttpRequest(inputStream);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("회원가입이 잘 됩니다.")
    void signup() {
        InputStream inputStream = new ByteArrayInputStream("GET /user/create?userId=test&password=password&name=test&email=test%40slipp.net HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*".getBytes());
        HttpRequest httpRequest = new HttpRequest(inputStream);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getQuery().get("userId")).isEqualTo("test");
        assertThat(httpRequest.getQuery().get("password")).isEqualTo("password");
        assertThat(httpRequest.getQuery().get("name")).isEqualTo("test");
        assertThat(httpRequest.getQuery().get("email")).isEqualTo("test@slipp.net");
    }
}
