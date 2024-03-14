package webserver;

import java.io.IOException;
import java.io.OutputStream;

public interface WebHandler {

    void process(HttpRequest httpRequest, OutputStream out) throws IOException;
}