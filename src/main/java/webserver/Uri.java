package webserver;

import java.util.Arrays;
import java.util.Optional;

public enum Uri {
    DEFAULT_HOME("/"),
    USER_CREATE_FORM("/registration"),
    USER_CREATE("/user/create"),
    DATA("TEMPORAL_VALUE");

    private final String pattern;

    Uri(String pattern) {
        this.pattern = pattern;
    }

    public static Uri from(HttpRequest httpRequest) {

        String requestUri = httpRequest.getUri();

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
