package webserver;

public class HttpResponse {

    private int statusCode;
    private byte[] header;
    private byte[] body;

    public HttpResponse(byte[] body, int statusCode) {
        this.statusCode = statusCode;
        this.body = body;
        this.header = String.format("HTTP/1.1 %d OK\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: %d\r\n\r\n", statusCode, body.length).getBytes();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
