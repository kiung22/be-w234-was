package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerTest {

    Controller controller = new Controller();

    @Test
    @DisplayName("index 요청에 대해 index.html를 응답합니다.")
    void indexRequest() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("GET /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*".getBytes());
        HttpRequest httpRequest = new HttpRequest(inputStream);
        HttpResponse httpResponse = controller.requestMapping(httpRequest);

        assertThat(httpResponse.getStatusCode()).isEqualTo(200);
        assertThat(httpResponse.getBody()).isEqualTo(Files.readAllBytes(new File("./webapp/index.html").toPath()));
    }

    @Test
    @DisplayName("path가 잘못된 요청에 대해 404.html를 응답합니다.")
    void NotFoundRequest() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("GET /bad-request HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive\nAccept: */*".getBytes());
        HttpRequest httpRequest = new HttpRequest(inputStream);
        HttpResponse httpResponse = controller.requestMapping(httpRequest);

        assertThat(httpResponse.getStatusCode()).isEqualTo(404);
        assertThat(httpResponse.getBody()).isEqualTo(Files.readAllBytes(new File("./webapp/404.html").toPath()));
    }
}
