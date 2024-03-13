package webserver;

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

    public String getUri() {
        String[] tokens = startLine.split(" ");
        return tokens[1];
    }
}
