package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String TEXT_HTML = "text/html";
    private static final String TEXT_CSS = "text/css";
    private static final String IMAGE_SVG_XML = "image/svg+xml";
    private static final String X_ICO = "x-ico";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        // 출력되는 로그
//        10:13:27.801 [DEBUG] [Thread-0] [webserver.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 54450
//        10:19:45.707 [DEBUG] [Thread-1] [webserver.RequestHandler] - New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 54491
        //왜 두개가 뜨는가?

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            //========================================//

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));

            String line = br.readLine();
            logger.debug("request line : {}", line);

            String[] tokens = line.split(" ");

            String resource = tokens[1];

            System.out.println("resource : " + resource);

            String pathname = "./src/main/resources/static" + resource;
            try (FileInputStream fis = new FileInputStream(new File(pathname))){

                logger.debug("filepath : {}", pathname);
                DataOutputStream dos = new DataOutputStream(out);

                byte[] body = fis.readAllBytes();

                if (resource.contains("html")) {
                    response200HeaderByType(dos, body.length, TEXT_HTML);
                    responseBody(dos, body);
                }

                if (resource.contains("css")) {
                    response200HeaderByType(dos, body.length, TEXT_CSS);
                    responseBody(dos, body);
                }

                if (resource.contains("img")) {
                    response200HeaderByType(dos, body.length, IMAGE_SVG_XML);
                    responseBody(dos, body);
                }

                if (resource.contains("ico")) {
                    response200HeaderByType(dos, body.length, X_ICO);
                    responseBody(dos, body);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



            while (!line.equals("")) {
                line = br.readLine();
                logger.debug("header : {}", line);
            }
            //========================================//

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200HeaderByType(DataOutputStream dos, int lengthOfBodyContent, String fileFormat) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + fileFormat + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n"); // dos 객체에 헤더를 한땀한땀 쓴다
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
