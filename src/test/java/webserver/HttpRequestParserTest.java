package webserver;

import http.HttpRequestParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class HttpRequestParserTest {

    @Test
    @DisplayName("status line parsing 테스트")
    void parseStatusLine() {
        Map<String, String> map = HttpRequestParser.parseStatusLine("GET /index.html HTTP/1.1");
        assertThat(map.get("method")).isEqualTo("GET");
        assertThat(map.get("path")).isEqualTo("/index.html");
        assertThat(map.get("queryString")).isEqualTo("");
    }

    @Test
    @DisplayName("query가 있는 status line parsing 테스트")
    void parseStatusLineContainingQuery() {
        Map<String, String> map = HttpRequestParser.parseStatusLine("GET /user/create?userId=test&password=password HTTP/1.1");
        assertThat(map.get("method")).isEqualTo("GET");
        assertThat(map.get("path")).isEqualTo("/user/create");
        assertThat(map.get("queryString")).isEqualTo("userId=test&password=password");
    }

    @Test
    @DisplayName("query parsing 테스트")
    void parseQuery() {
        Map<String, String> query = HttpRequestParser.parseQueryString(
                "userId=test&password=password&name=test&email=test%40slipp.net"
        );

        assertThat(query.get("userId")).isEqualTo("test");
        assertThat(query.get("password")).isEqualTo("password");
        assertThat(query.get("name")).isEqualTo("test");
        assertThat(query.get("email")).isEqualTo("test@slipp.net");
    }
}
