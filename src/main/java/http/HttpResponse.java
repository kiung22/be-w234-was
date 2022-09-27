package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpStatus status;
    private final Map<String, String> header;
    private final Map<String, Cookie> cookie;
    private final byte[] body;
    private static final String VERSION = "1.1";
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=utf-8";

    private HttpResponse(Builder builder) {
        status = builder.status;
        header = builder.header;
        cookie = builder.cookie;
        body = builder.body;
    }

    public HttpStatus getStatus() {
        return status;
    }
    public String getVersion() {
        return VERSION;
    }

    public Map<String, String> getHeader() {
        return Collections.unmodifiableMap(header);
    }

    public Map<String, Cookie> getCookie() {
        return Collections.unmodifiableMap(cookie);
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] getHeaderToBytes() {
        String headerString = header.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\r\n"));
        String cookieString = "\r\n" + cookie.values().stream().map(e -> "Set-Cookie: " + e.toString())
                .collect(Collectors.joining("\r\n"));
        return (headerString + cookieString + "\r\n\r\n").getBytes();
    }

    public byte[] getStatusLineToBytes() {
        return String.format("HTTP/%s %d %s\r\n", VERSION, status.getStatusCode(), status.getStatusMessage()).getBytes();
    }

    public static class Builder {
        private final HttpStatus status;
        private final Map<String, String> header = new HashMap<>();
        private final Map<String, Cookie> cookie = new HashMap<>();
        private byte[] body;

        public Builder(int statusCode) {
            status = HttpStatus.valueOfStatusCode(statusCode);
        }

        public Builder setHeader(String key, String value) {
            header.put(key, value);
            return this;
        }

        public Builder setCookie(Cookie value) {
            cookie.put(value.getName(), value);
            return this;
        }

        public Builder setBody(File body) {
            try {
                this.body = Files.readAllBytes(body.toPath());
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            header.put("Content-Type", CONTENT_TYPE_TEXT_HTML);
            header.put("Content-Length", String.valueOf(body.length()));
            return this;
        }

        public Builder setBody(byte[] body) {
            this.body = body;
            header.put("Content-Type", CONTENT_TYPE_TEXT_HTML);
            header.put("Content-Length", String.valueOf(body.length));
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }
}
