package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class); // 어떤 클래스에서 로그가 발생했는지 추적할 수 있음.
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            // 해당 serverSocket 은 대기용이다.
            // 연결 요청이 들어오면 Socket 을 새로 생성하고, 생성된 해당 소켓이 요청에 대한 연결을 담당하는 것이다.
            // (참고로 대기 소켓(서버소켓)은 포트가 할당 되지만, 새로 만들어지는 소켓은 포트가 없다. 그래서 100만명씩 치킨이벤트 대응도 되는 거다(?))
            // 클라이언트 요청은 모두 대기 소켓으로 가지만 os 레벨 에서 어느 연결 소켓으로 보내줘야 하는지 알려주는 거다
            logger.info("Web Application Server started {} port.", port);
            //출력되는 로그
            //10:13:22.914 [INFO ] [main] [webserver.WebServer] - Web Application Server started 8080 port.

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) { // connection 에 값이 들어오면 스레드를 생성하고 실행가능 상태로 바꾼다
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(new RequestHandler(connection));
            }
        }
    }
}
