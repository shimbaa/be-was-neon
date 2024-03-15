package webserver.subhandlers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;

public class StaticContentHandler implements WebHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticContentHandler.class);
    private static final String TEXT_CSS = "text/css";
    private static final String IMAGE_SVG_XML = "image/svg+xml";
    private static final String X_ICO = "x-ico";

    @Override
    public void process(HttpRequest httpRequest, OutputStream out) throws IOException {

        String pathname = "./src/main/resources/static" + httpRequest.getUri();
        try (FileInputStream fis = new FileInputStream(new File(pathname))) {
            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = fis.readAllBytes();

//        html css js ico png jpg
            if (httpRequest.getUri().contains("css")) {
                response200HeaderByType(dos, body.length, TEXT_CSS);
                responseBody(dos, body);
            }

            if (httpRequest.getUri().contains("img")) {
                response200HeaderByType(dos, body.length, IMAGE_SVG_XML);
                responseBody(dos, body);
            }

            if (httpRequest.getUri().contains("ico")) {
                response200HeaderByType(dos, body.length, X_ICO);
                responseBody(dos, body);
            }

            if (httpRequest.getUri().contains("svg")) {
                response200HeaderByType(dos, body.length, "svg");
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