package http;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> query;
    private final Map<String, String> header;
    private final String body;
    private final Map<String, Cookie> cookie;

    private HttpRequest(Builder builder) {
        method = builder.method;
        path = builder.path;
        query = builder.query;
        header = builder.header;
        body = builder.body;
        cookie = builder.cookie;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQuery() {
        return Collections.unmodifiableMap(query);
    }

    public Map<String, String> getHeader() {
        return Collections.unmodifiableMap(header);
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getBodyToMap() {
        if (!"application/x-www-form-urlencoded".equals(header.get("Content-Type"))) {
            throw new RuntimeException("http request의 content type이 form-urlencoded이 아닙니다.");
        }
        return HttpRequestParser.parseBodyString(body);
    }

    public Map<String, Cookie> getCookie() {
        return Collections.unmodifiableMap(cookie);
    }

    public static class Builder {
        private final HttpMethod method;
        private final String path;
        private Map<String, String> query;
        private Map<String, String> header;
        private String body;
        private Map<String, Cookie> cookie;

        public Builder(HttpMethod method, String path) {
            this.method = method;
            this.path = path;
        }

        public Builder setQuery(Map<String, String> query) {
            this.query = query;
            return this;
        }

        public Builder setHeader(Map<String, String> header) {
            this.header = header;
            if (header.containsKey("Cookie")) {
                Map<String, String> cookieStringMap = HttpRequestParser.parseCookies(this.header.get("Cookie"));
                cookie = cookieStringMap.entrySet().stream()
                        .collect(Collectors.toMap(e -> e.getKey(), e -> new Cookie(e.getKey(), e.getValue())));
            }
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
