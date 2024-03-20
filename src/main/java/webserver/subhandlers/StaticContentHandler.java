package webserver.subhandlers;

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

    private static final String STATIC_RESOURCES_PATH = "./src/main/resources/static";

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestUri = httpRequest.getRequestUri();

        Optional<String> contentType = DataType.getContentTypeFromRequestUri(requestUri);

        String pathname = STATIC_RESOURCES_PATH + requestUri;
        if (contentType.isPresent()) {
            logger.debug("path name with extender : {}", pathname);

            try (FileInputStream fis = new FileInputStream(pathname)) {

                byte[] body = fis.readAllBytes();
                httpResponse.response200HeaderByType(body.length, contentType.get());
                httpResponse.responseBody(body);
            }
        }

        if (contentType.isEmpty()) {
            if (!pathname.endsWith("/")) {
                pathname += "/";
            }
            pathname += "index.html";
            logger.debug("path name with NO extender : {}", pathname);
            try (FileInputStream fis = new FileInputStream(pathname)) {

                byte[] body = fis.readAllBytes();
                httpResponse.response200HeaderByType(body.length, DataType.HTML.contentType());
                httpResponse.responseBody(body);
            }
        }
    }
}