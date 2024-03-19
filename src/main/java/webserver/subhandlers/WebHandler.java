package webserver.subhandlers;

import java.io.IOException;
import webserver.HttpRequest;
import webserver.HttpResponse;

public interface WebHandler {

    void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}