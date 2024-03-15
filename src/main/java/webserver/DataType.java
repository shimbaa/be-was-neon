package webserver;

public enum DataType {
    //    html css js ico png jpg
    HTML("html"),
    CSS("css"),
    JS("js"),
    ICO("ico"),
    PNG("png"),
    JPG("jpg"),
    SVG("svg");

    private final String label;

    DataType(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}
