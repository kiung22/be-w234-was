package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

import static util.HttpRequestUtils.parseQueryString;

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
            query = new HashMap<>();
            if (queryTokens.length > 1) {
                query = (HashMap<String, String>) parseQueryString(queryTokens[1]);
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

    public HashMap<String, String> getQuery() {
        return query;
    }
}
