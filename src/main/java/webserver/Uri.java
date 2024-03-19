package webserver;

import java.util.Arrays;
import java.util.Optional;

public enum Uri {
    /**
     * 전체 구조 변경 중
     * 없어 질 수 있는 클래스임
     */
    DEFAULT_HOME("/"),
    USER_CREATE_FORM("/registration"),
    USER_CREATE("/user/create"),
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
