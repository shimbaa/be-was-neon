package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final String startLine;
    private List<String> header;
    private String body;

    public HttpRequest(InputStream in) throws IOException {
        // todo
        // 코드 정리
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
        this.startLine = br.readLine();

        if (getRequestMethod().equals("GET")) {
            parseGetRequest(br);
        }

        if (getRequestMethod().equals("POST")) {
            logger.debug("POST startLine : {}", startLine);
            setPostHeader(br);
            setPostBody(br);
        }
    }

    public String getRequestMethod() {
        String[] tokens = startLine.split(" ");
        return tokens[0];
    }

    public String getRequestUri() {
        String[] tokens = startLine.split(" ");
        return tokens[1];
    }

    public String getBody() {
        return body;
    }

    private void setPostBody(BufferedReader br) throws IOException {
        int contentLength = 0;
        Optional<String> any = header.stream()
                .filter(line -> line.startsWith("Content-Length"))
                .findAny();

        if (any.isPresent()) {
            String[] split = any.get().split(" ");
            contentLength = Integer.parseInt(split[1]);
        }

        char[] body = new char[contentLength];
        br.read(body);

        StringBuilder sb = new StringBuilder();
        for (char each : body) {
            sb.append(each);
        }
        this.body = sb.toString();
    }

    private void setPostHeader(BufferedReader br) throws IOException {
        List<String> header = new ArrayList<>();
        String line;
        do {
            line = br.readLine();
            header.add(line);
        } while (!line.isEmpty());
        this.header = header;

        printHeaderLog(header);
    }

    private void printHeaderLog(List<String> header) {
        for (String s : header) {
            logger.debug("header line : {}", s);
        }
    }

    private void parseGetRequest(BufferedReader br) throws IOException {
        List<String> header = new ArrayList<>();
        String line;
        do {
            line = br.readLine();
            header.add(line);
        } while (!line.equals(""));
        this.header = header;
    }

    @Override
    public String toString() {
        return "startLine='" + startLine + '\'' +
                ", header='" + header + '\'' +
                ", body=" + body +
                '}';
    }
}
