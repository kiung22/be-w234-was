package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    HttpMethod method;
    String path;

    public HttpRequest(InputStream inputStream) {
        // InputStream을 BufferReader로 읽고 파싱하여 인스턴스 변수 초기화
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            String[] tokens = line.split(" ");
            method = HttpMethod.valueOf(tokens[0]);
            path = tokens[1];
            logger.info("Http Method: {}", method);
            logger.info("Http Path: {}", path);
            while (!"".equals(line)) {
                logger.debug(line);
                line = br.readLine();
                if (line == null) break;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
