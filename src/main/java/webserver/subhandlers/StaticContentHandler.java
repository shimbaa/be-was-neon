package webserver.subhandlers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.DataType;
import webserver.HttpRequest;

public class StaticContentHandler implements WebHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticContentHandler.class);

    @Override
    public void process(HttpRequest httpRequest, OutputStream out) throws IOException {
        String requestUri = httpRequest.getUri();
        String pathname = "./src/main/resources/static" + requestUri;
        try (FileInputStream fis = new FileInputStream(new File(pathname))) {
            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = fis.readAllBytes();

            Optional<String> contentType = DataType.getContentTypeFromRequestUri(requestUri);

            if (contentType.isPresent()) {
                response200HeaderByType(dos, body.length, contentType.get());
                responseBody(dos, body);
            }
        }
    }

    private void response200HeaderByType(DataOutputStream dos, int lengthOfBodyContent, String fileFormat) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + fileFormat + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}