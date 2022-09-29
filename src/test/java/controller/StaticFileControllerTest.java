package controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class StaticFileControllerTest {

    StaticFileController staticFileController = new StaticFileController();

    private byte[] getFile(String path) throws IOException {
        return Files.readAllBytes(Path.of(path));
    }

    @Test
    @DisplayName("static file이 존재하면 해당 파일을 응답합니다.")
    void responseStaticFile() throws IOException {
        HttpRequest httpRequest = new HttpRequest.Builder(
                HttpMethod.GET,
                "/index.html"
        ).build();

        HttpResponse response = staticFileController.run(httpRequest);
        assertThat(response.getStatus().getStatusCode()).isEqualTo(200);
        assertThat(response.getHeader().get("Content-Type")).isEqualTo("text/html;charset-utf-8");
        assertThat(response.getBody()).isEqualTo(getFile("./webapp/index.html"));
    }

    @Test
    @DisplayName("static file을 찾을 수 없으면 404.html을 응답합니다.")
    void notFound() throws IOException {
        HttpRequest httpRequest = new HttpRequest.Builder(
                HttpMethod.GET,
                "/not-found.html"
        ).build();

        HttpResponse response = staticFileController.run(httpRequest);
        assertThat(response.getStatus().getStatusCode()).isEqualTo(404);
        assertThat(response.getHeader().get("Content-Type")).isEqualTo("text/html;charset-utf-8");
        assertThat(response.getBody()).isEqualTo(getFile("./webapp/404.html"));
    }

    @Test
    @DisplayName("css 파일인 경우에는 Content-Type이 text/css로 응답합니다.")
    void contentTypeCss() throws IOException {
        HttpRequest httpRequest = new HttpRequest.Builder(
                HttpMethod.GET,
                "/css/styles.css"
        ).build();

        HttpResponse response = staticFileController.run(httpRequest);
        assertThat(response.getStatus().getStatusCode()).isEqualTo(200);
        assertThat(response.getHeader().get("Content-Type")).isEqualTo("text/css");
        assertThat(response.getBody()).isEqualTo(getFile("./webapp/css/styles.css"));
    }
}
