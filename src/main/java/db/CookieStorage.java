package db;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import model.User;

public class CookieStorage {
    private static final Map<String, User> cookies = new ConcurrentHashMap<>();

    /**
     * 로그인에 성공한 사용자에게 쿠키를 부여한다
     * @param user 로그인에 성공한 사용자
     * @return 쿠키의 UUID
     */
    public static String grantCookieToLoggedInUser(User user) {
        String sessionId = UUID.randomUUID().toString();
        cookies.put(sessionId, user);
        return sessionId;
    }

    public static Optional<User> findUserBySessionId(String sessionId) {
        return Optional.ofNullable(cookies.get(sessionId));
    }
}