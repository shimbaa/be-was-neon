package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.subhandlers.HomeHandler;
import webserver.subhandlers.StaticContentHandler;
import webserver.subhandlers.UserCreateHandler;
import webserver.subhandlers.UserRegisterFormHandler;
import webserver.subhandlers.WebHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final Map<Uri, WebHandler> handlerMap = new HashMap<>();
    // 지금은 요청이 들어 올때 마다 생성 되고 초기화 되는 거 아닌가? 로그 찍어서 확인해보고 개선 할 수 있을지 고민 해보자

    public RequestHandler(Socket connection) {
        this.connection = connection;
        handlerMap.put(Uri.DEFAULT_HOME, new HomeHandler());
        handlerMap.put(Uri.USER_CREATE_FORM, new UserRegisterFormHandler());
        handlerMap.put(Uri.USER_CREATE, new UserCreateHandler());
        handlerMap.put(Uri.DATA, new StaticContentHandler());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        // 요청을 보낸 클라이언트의 포트 정보이다. 이걸 우리가 알아야 응답을 어디로 보내 줄지 아는 거다.
        // 클라이언트가 서버에 연결을 요청할 때 임의의 포트가 부여된다. (51~~~~)

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = createHttpRequest(in);


            handlerMap.get(Uri.from(httpRequest)).process(httpRequest, out); // out 을 HttpResponse 객체로 바꿀 예정

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpRequest createHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
        String startLine = br.readLine();
        String header = br.readLine();

        logger.debug("start line : {}", startLine);
        logger.debug("header : {}", header);

        List<String> body = new ArrayList<>();
        String line;
        do {
            line = br.readLine();
            body.add(line);
        } while (!line.equals(""));
        for (String s : body) {
            logger.debug("body : {}", s);
        }

        return new HttpRequest(startLine, header, body);
    }
}
