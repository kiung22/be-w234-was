package webserver;

import http.HttpMethod;
import http.HttpResponse;
import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerTest {

    Controller controller = new Controller();

    @Test
    @DisplayName("index 요청에 대해 index.html를 응답합니다.")
    void indexRequest() {
        HttpRequest httpRequest = new HttpRequest(
                HttpMethod.GET,
                "/index.html",
                new HashMap<>()
        );
        HttpResponse httpResponse = controller.requestMapping(httpRequest);

        assertThat(httpResponse.getStatus().getStatusCode()).isEqualTo(200);
        assertThat(httpResponse.getBody()).isEqualTo(new File("./webapp/index.html"));
    }

    @Test
    @DisplayName("path가 잘못된 요청에 대해 404.html를 응답합니다.")
    void NotFoundRequest() {
        HttpRequest httpRequest = new HttpRequest(
                HttpMethod.GET,
                "/bad-request",
                new HashMap<>());
        HttpResponse httpResponse = controller.requestMapping(httpRequest);

        assertThat(httpResponse.getStatus().getStatusCode()).isEqualTo(404);
        assertThat(httpResponse.getBody()).isEqualTo(new File("./webapp/404.html"));
    }
}
