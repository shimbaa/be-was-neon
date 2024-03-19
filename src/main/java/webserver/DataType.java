package webserver;

import java.util.Arrays;
import java.util.Optional;

public enum DataType {
    //    html css js ico png jpg
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    ICO("ico", "x-ico"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpg"),
    SVG("svg", "image/svg+xml");

    private final String label;

    private final String contentType;

    DataType(String label, String contentType) {
        this.label = label;
        this.contentType = contentType;
    }

    public String label() {
        return this.label;
    }

    public String contentType() {
        return this.contentType;
    }

    public static Optional<String> getContentTypeFromRequestUri(String requestUri) {
        return Arrays.stream(values())
                .filter(dataType -> requestUri.contains(dataType.label))
                .findAny()
                .map(dataType -> dataType.contentType);
    }
}
