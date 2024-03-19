package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private final String startLine;

    private final String header;

    private final List<String> body;

    public HttpRequest(String startLine, String header, List<String> body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public static HttpRequest create(InputStream in) throws IOException {
        // todo
        // 코드 정리
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
        String startLine = br.readLine();
        String header = br.readLine();

        logger.debug("start line : {}", startLine);
        logger.debug("header : {}", header);

        List<String> body = new ArrayList<>();
        String line;
        do {
            line = br.readLine();
            body.add(line);
        } while (!line.equals(""));
        for (String s : body) {
            logger.debug("body : {}", s);
        }

        return new HttpRequest(startLine, header, body);
    }

    public String getUri() {
        String[] tokens = startLine.split(" ");
        return tokens[1];
    }
}
