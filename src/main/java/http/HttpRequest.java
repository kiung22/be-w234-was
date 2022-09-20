package http;

import java.util.Collections;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> query;
    private final Map<String, String> header;
    private final Map<String, String> body;

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
}
