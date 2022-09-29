package webserver;

import http.HttpMethod;
import http.HttpResponse;
import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class RequestDispatcherTest {

    RequestDispatcher requestDispatcher = new RequestDispatcher();

    private byte[] getFile(String path) throws IOException {
        return Files.readAllBytes(Path.of(path));
    }

    @Test
    @DisplayName("index 요청에 대해 index.html를 응답합니다.")
    void indexRequest() throws IOException {
        HttpRequest httpRequest = new HttpRequest.Builder(
                HttpMethod.GET,
                "/index.html"
        ).build();
        HttpResponse httpResponse = requestDispatcher.requestMapping(httpRequest);

        assertThat(httpResponse.getStatus().getStatusCode()).isEqualTo(200);
        assertThat(httpResponse.getBody()).isEqualTo(getFile("./webapp/index.html"));
    }

    @Test
    @DisplayName("path가 잘못된 요청에 대해 404.html를 응답합니다.")
    void NotFoundRequest() throws IOException {
        HttpRequest httpRequest = new HttpRequest.Builder(
                HttpMethod.GET,
                "/bad-request"
        ).build();
        HttpResponse httpResponse = requestDispatcher.requestMapping(httpRequest);

        assertThat(httpResponse.getStatus().getStatusCode()).isEqualTo(404);
        assertThat(httpResponse.getBody()).isEqualTo(getFile("./webapp/404.html"));
    }
}
