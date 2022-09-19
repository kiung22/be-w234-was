package http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpStatus status;
    private Map<String, String> header = new HashMap<>();
    private File body;
    private String version = "1.1";

    public HttpResponse(File body, int statusCode) {
        this.status = HttpStatus.valueOfStatusCode(statusCode);
        this.body = body;
        this.header.put("Content-Type", "text/html;charset-utf-8");
        this.header.put("Content-Length", String.valueOf(body.length()));
    }

    public HttpStatus getStatus() {
        return status;
    }
    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public File getBody() {
        return body;
    }

    public HttpResponse addHeader(String key, String value) {
        this.header.put(key, value);
        return this;
    }
}
