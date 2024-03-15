package webserver.subhandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class HomeHandler implements WebHandler {

    private static final Logger logger = LoggerFactory.getLogger(HomeHandler.class);

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String pathname = "./src/main/resources/static/index.html";
        try (FileInputStream fis = new FileInputStream(new File(pathname))) {

            byte[] body = fis.readAllBytes();

            httpResponse.response200HeaderByType(body.length);
            httpResponse.responseBody(body);
        }
    }
}