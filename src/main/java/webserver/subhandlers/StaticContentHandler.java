package webserver.subhandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.DataType;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class StaticContentHandler implements WebHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticContentHandler.class);

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestUri = httpRequest.getUri();
        String pathname = "./src/main/resources/static" + requestUri;
        try (FileInputStream fis = new FileInputStream(new File(pathname))) {

            byte[] body = fis.readAllBytes();

            Optional<String> contentType = DataType.getContentTypeFromRequestUri(requestUri);

            if (contentType.isPresent()) {
                httpResponse.response200HeaderByType(body.length);
                httpResponse.responseBody(body);
            }
        }
    }
}