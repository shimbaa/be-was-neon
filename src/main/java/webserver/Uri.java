package webserver;

import java.util.Arrays;
import java.util.Optional;

public enum Uri {
    /**
     * 변경 : / 으로 요청이 들어오면 resources/static/index.html 로 처리되도록 하자
     */
    DEFAULT_HOME("/"),
    /**
     * 변경 : /registration 으로 요청이 들어오면 resources/static/registration/index.html 로 처리되도록 하자
     */
    USER_CREATE_FORM("/registration"),
    USER_CREATE("/user/create"),
    /**
     * 변경 : /login 으로 요청이 들어오면 resources/static/login/index.html 로 처리되도록 하자
     */
    LOGIN_FORM("/login"),
    DATA("TEMPORAL_VALUE");

    private final String pattern;

    Uri(String pattern) {
        this.pattern = pattern;
    }

    public static Uri from(String requestUri) {

        Optional<DataType> optional = Arrays.stream(DataType.values())
                .filter(dataType -> requestUri.contains(dataType.label()))
                .findAny();

        if (optional.isPresent()) {
            return DATA;
        }

        if (requestUri.startsWith(USER_CREATE.pattern)) {
            return USER_CREATE;
        }

        Optional<Uri> uriOptional = Arrays.stream(values())
                .filter(uri -> uri.pattern.equals(requestUri))
                .findAny();

        return uriOptional.orElseThrow(IllegalArgumentException::new);
    }
}
