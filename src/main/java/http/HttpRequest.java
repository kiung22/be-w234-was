package http;

import java.util.Collections;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> query;

    public HttpRequest(
            HttpMethod method,
            String path,
            Map<String, String> query
    ) {
        this.method = method;
        this.path = path;
        this.query = query;
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
}
