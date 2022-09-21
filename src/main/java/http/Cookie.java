package http;

public class Cookie {

    private final String name;
    private final String value;
    private String path;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Cookie setPath(String path) {
        this.path = path;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        String cookieString = name + "=" + value;
        if (path != null) {
            cookieString = cookieString + "; path=" + path;
        }
        return cookieString;
    };
}
