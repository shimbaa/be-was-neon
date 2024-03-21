package webserver.subhandlers;

import db.CookieStorage;
import db.Database;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginHandler implements WebHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

        String body = httpRequest.getBody();
        Map<String, String> postBodyMap = parsePostBody(body);
        String userId = postBodyMap.get("userId");
        String password = postBodyMap.get("password");

        Optional<User> findUser = Database.findUserById(userId);

        if (findUser.isPresent()) { // 해당 id 회원 존재
            User user = findUser.get();
            if (isPasswordEquals(password, user)) { // 로그인 성공
                String sessionId = CookieStorage.grantCookieToLoggedInUser(user);// 로그인에 성공한 사용자에게 cookie를 부여한다
                httpResponse.responseRedirectionWithCookie("302", "/", sessionId); // 부여받은 uuid 값을 브라우저에 쿠키 세션id 값으로 넘겨준다
            }
            if (!isPasswordEquals(password, user)) { //비밀번호 불일치
                httpResponse.responseRedirection("302", "/login/failed.html");
            }
        }
        if (findUser.isEmpty()) { // 해당 id 회원 존재 x
            httpResponse.responseRedirection("302", "/login/failed.html");
        }
    }

    private boolean isPasswordEquals(String password, User user) {
        return user.getPassword().equals(password);
    }

    private Map<String, String> parsePostBody(String body)
            throws UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<>();

        String queryString = body.replaceAll("/user/login\\?", "");
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
            String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
            parameters.put(key, value);
        }
        return parameters;
    }
}