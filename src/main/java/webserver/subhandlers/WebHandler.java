package webserver.subhandlers;

import java.io.IOException;
import java.io.OutputStream;
import webserver.HttpRequest;

public interface WebHandler {

    void process(HttpRequest httpRequest, OutputStream out) throws IOException;
}