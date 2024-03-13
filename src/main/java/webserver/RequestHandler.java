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

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String TEXT_HTML = "text/html";
    private static final String TEXT_CSS = "text/css";
    private static final String IMAGE_SVG_XML = "image/svg+xml";
    private static final String X_ICO = "x-ico";

    private Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = createHttpRequest(in);

            String uri = httpRequest.getUri();

            /**
             * 분기 index.html
             */
            if (uri.equals("/index.html")) {
                String pathname = "./src/main/resources/static/index.html";
                try (FileInputStream fis = new FileInputStream(new File(pathname))) {
                    DataOutputStream dos = new DataOutputStream(out);

                    byte[] body = fis.readAllBytes();

                    response200HeaderByType(dos, body.length, "text/html");
                    responseBody(dos, body);
                }
            }

            /**
             * 분기 index.html
             */
            if (uri.equals("/registration")) {
                String pathname = "./src/main/resources/static/registration/index.html";
                try (FileInputStream fis = new FileInputStream(new File(pathname))) {
                    DataOutputStream dos = new DataOutputStream(out);

                    byte[] body = fis.readAllBytes();

                    response200HeaderByType(dos, body.length, "text/html");
                    responseBody(dos, body);
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //            try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
//            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
//
//            //========================================//
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
//
//            String line = br.readLine();
//            logger.debug("request line : {}", line);
//            String[] tokens = line.split(" ");
//
//            String URI = tokens[1]; // HTTP HEAD -> GET 뒤에 붙는 놈
//
//            logger.info("requested URI : " + URI);
//
//            String pathname = "./src/main/resources/static" + URI;
//
//            if (URI.equals("/registration")) {
//                pathname = "./src/main/resources/static/registration/index.html";
//            }
//
//            // 여기 문자열이 동적으로 바껴야됨 (매핑하는 것)
//            try (FileInputStream fis = new FileInputStream(new File(pathname))) {
//
//                logger.debug("filepath : {}", pathname);
//                DataOutputStream dos = new DataOutputStream(out);
//
//                byte[] body = fis.readAllBytes();
//
//                if (URI.contains("registration")) {
//                    response200HeaderByType(dos, body.length, TEXT_HTML);
//                    responseBody(dos, body);
//                }
//
//                if (URI.contains("html")) {
//                    response200HeaderByType(dos, body.length, TEXT_HTML);
//                    responseBody(dos, body);
//                }
//
//                if (URI.contains("css")) {
//                    response200HeaderByType(dos, body.length, TEXT_CSS);
//                    responseBody(dos, body);
//                }
//
//                if (URI.contains("img")) {
//                    response200HeaderByType(dos, body.length, IMAGE_SVG_XML);
//                    responseBody(dos, body);
//                }
//
//                if (URI.contains("ico")) {
//                    response200HeaderByType(dos, body.length, X_ICO);
//                    responseBody(dos, body);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            while (!line.equals("")) {
//                line = br.readLine();
//                logger.debug("header : {}", line);
//            }
//            //========================================//
//
//        } catch (
//                IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
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
