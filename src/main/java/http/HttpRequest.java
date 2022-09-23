package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> query;
    private final Map<String, String> header;
    private final Map<String, String> body;
    private final Map<String, Cookie> cookie;

    public HttpRequest(
            HttpMethod method,
            String path,
            Map<String, String> query,
            Map<String, String> header,
            Map<String, String> body
    ) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.header = header;
        this.body = body;
        if (this.header.containsKey("Cookie")) {
            Map<String, String> cookieStringMap = HttpRequestParser.parseCookies(this.header.get("Cookie"));
            cookie = cookieStringMap.entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> new Cookie(e.getKey(), e.getValue())));
            System.out.println(cookie);
        } else {
            cookie = new HashMap<>();
        }
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

    public Map<String, String> getBody() {
        return Collections.unmodifiableMap(body);
    }

    public Map<String, Cookie> getCookie() {
        return Collections.unmodifiableMap(cookie);
    }
}
