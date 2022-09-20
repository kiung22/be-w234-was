package http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {

    private final HttpStatus status;
    private final Map<String, String> header = new HashMap<>();
    private File body;
    private static final String version = "1.1";

    public HttpResponse(int statusCode) {
        status = HttpStatus.valueOfStatusCode(statusCode);
    }

    public HttpStatus getStatus() {
        return status;
    }
    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeader() {
        return Collections.unmodifiableMap(header);
    }

    public File getBody() {
        return body;
    }

    public HttpResponse addHeader(String key, String value) {
        header.put(key, value);
        return this;
    }

    public HttpResponse setBody(File body) {
        this.body = body;
        header.put("Content-Type", "text/html;charset-utf-8");
        header.put("Content-Length", String.valueOf(body.length()));
        return this;
    }

    public byte[] getBodyToBytes() throws IOException {
        if (body == null) {
            return null;
        }
        return Files.readAllBytes(body.toPath());
    }

    public byte[] getHeaderToBytes() {
        String stringHeader = header.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\r\n")) + "\r\n\r\n";
        return stringHeader.getBytes();
    }

    public byte[] getStatusLineToBytes() {
        return String.format("HTTP/%s %d %s\r\n", version, status.getStatusCode(), status.getStatusMessage()).getBytes();
    }
}
