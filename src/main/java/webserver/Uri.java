package webserver;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Uri {

    DEFAULT_HOME("/"),
    USER_CREATE_FORM("/registration"),
    USER_CREATE("/user/create"),
    DATA("TEMPORAL_VALUE");


    private final String pattern;

//    private static final String regex = "^[^.]+\\.[^.]+$";

    Uri(String pattern) {
        this.pattern = pattern;
    }

    public static Uri from(HttpRequest httpRequest) {

        String requestUri = httpRequest.getUri();

//        Pattern compile = Pattern.compile(regex);
//        Matcher matcher = compile.matcher(requestUri);
//
//        if (matcher.find()) {
//            return DATA;
//        }

        if (requestUri.startsWith("/user/create")) {
            return USER_CREATE;
        }

        return Arrays.stream(values())
                .filter(uri -> uri.pattern.equals(requestUri))
                .findAny()
                .orElse(DATA);
    }
}
