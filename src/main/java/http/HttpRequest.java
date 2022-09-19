package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestUtils;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private HttpMethod method;
    private String path;
    private Map<String, String> query;

    public HttpRequest(InputStream inputStream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            String[] tokens = line.split(" ");
            method = HttpMethod.valueOf(tokens[0]);
            String[] queryTokens = tokens[1].split("\\?");
            path = queryTokens[0];
            query = new HashMap<>();
            if (queryTokens.length > 1) {
                query = HttpRequestUtils.parseQueryString(queryTokens[1]);
            }
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

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQuery() {
        return query;
    }
}
