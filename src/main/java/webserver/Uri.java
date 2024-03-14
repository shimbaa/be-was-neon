package webserver;

import java.util.Arrays;

public enum Uri {

    DEFAULT_HOME("/"),
    INDEX_HTML_HOME("/index.html"),
    USER_CREATE_FORM("/registration"),
    USER_CREATE("/user/create"),
    DATA_OR_NONE(null);


    private final String pattern;

    Uri(String pattern) {
        this.pattern = pattern;
    }

    public static Uri from(String requestUri) {
        return Arrays.stream(values())
                .filter(uri -> uri.pattern.equals(requestUri))
                .findAny()
                .orElse(DATA_OR_NONE);
    }
}
