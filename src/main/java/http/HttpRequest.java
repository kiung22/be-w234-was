package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private HttpMethod method;
    private String path;
    private HashMap<String, String> query;

    public HttpRequest(InputStream inputStream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            String[] tokens = line.split(" ");
            method = HttpMethod.valueOf(tokens[0]);
            String[] queryTokens = tokens[1].split("\\?");
            path = queryTokens[0];
            query = parseQuery(queryTokens);
            logger.info("Http Method: {}", method);
            logger.info("Http Path: {}", path);
            logger.info("Http Query: {}", query);
            while (!"".equals(line)) {
                logger.debug(line);
                line = br.readLine();
                if (line == null) break;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HashMap<String, String> parseQuery(String[] queryTokens) throws UnsupportedEncodingException {
        HashMap<String, String> queryMap = new HashMap<>();
        if (queryTokens.length < 2) {
            return queryMap;
        }
        String[] pairs = queryTokens[1].split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryMap.put(
                    URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
                    URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8)
            );
        }
        return queryMap;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, String> getQuery() {
        return query;
    }
}
