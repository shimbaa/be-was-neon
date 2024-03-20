package webserver.subhandlers;

import db.Database;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class UserCreateHandler implements WebHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserCreateHandler.class);

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Map<String, String> parameters = new HashMap<>();

        String body = httpRequest.getBody();
        parsePostBody(body, parameters);

        User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"),
                parameters.get("email"));

        logger.debug("userInfo : {}", user);

        Database.addUser(user);

        logger.debug("added user in DB : {}", Database.findUserById(user.getUserId()));

        httpResponse.responseRedirection("302", "/index.html");
    }

//    private void parseQueryString(HttpRequest httpRequest, Map<String, String> parameters)
//            throws UnsupportedEncodingException {
//        String requestUri = httpRequest.getRequestUri();
//        String queryString = requestUri.replaceAll("/user/create\\?", "");
//        String[] pairs = queryString.split("&");
//        for (String pair : pairs) {
//            int idx = pair.indexOf("=");
//            String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
//            String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
//            parameters.put(key, value);
//        }
//    }

    private void parsePostBody(String body, Map<String, String> parameters)
            throws UnsupportedEncodingException {
        String queryString = body.replaceAll("/user/create\\?", "");
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
            String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
            parameters.put(key, value);
        }
    }
}