package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.subhandlers.LoginHandler;
import webserver.subhandlers.StaticContentHandler;
import webserver.subhandlers.UserCreateHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = HttpResponse.create(out);

            logger.debug("HTTP request : {}", httpRequest);

            if (httpRequest.getRequestUri().startsWith("/user/create")) {
                new UserCreateHandler().process(httpRequest, httpResponse);
            }
            if (httpRequest.getRequestUri().startsWith("/user/login")) {
                new LoginHandler().process(httpRequest, httpResponse);
            }
            new StaticContentHandler().process(httpRequest, httpResponse);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
