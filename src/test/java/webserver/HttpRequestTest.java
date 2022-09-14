package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


class HttpRequestTest {

    @Test
    @DisplayName("HttpRequest 객체가 잘 생성됩니다.")
    void createHttpRequest() {
        InputStream inputStream = new ByteArrayInputStream("GET /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*".getBytes());
        HttpRequest httpRequest = new HttpRequest(inputStream);

        assertThat(httpRequest.method).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.path).isEqualTo("/index.html");
    }
}
