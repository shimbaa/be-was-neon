package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    //    private static final String TEXT_HTML = "text/html";
//    private static final String TEXT_CSS = "text/css";
//    private static final String IMAGE_SVG_XML = "image/svg+xml";
//    private static final String X_ICO = "x-ico";

    private Socket connection;
    private Map<Uri, WebHandler> handlerMap = new HashMap<>();

    public RequestHandler(Socket connection) {
        this.connection = connection;
        handlerMap.put(Uri.DEFAULT_HOME, new HomeHandler());
        handlerMap.put(Uri.INDEX_HTML_HOME, new HomeHandler());
        handlerMap.put(Uri.USER_CREATE_FORM, new UserRegisterFormHandler());
        handlerMap.put(Uri.USER_CREATE, new UserCreateHandler());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        // 요청을 보낸 클라이언트의 포트 정보이다. 이걸 우리가 알아야 응답을 어디로 보내 줄지 아는 거다.
        // 클라이언트가 서버에 연결을 요청할 때 임의의 포트가 부여된다. (51~~~~)

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = createHttpRequest(in);

            String requestUri = httpRequest.getUri();

            handlerMap.get(Uri.from(requestUri)).process(httpRequest, out); // out 을 HttpResponse 객체로 바꿀 예정

//            /**
//             * 분기 index.html
//             */
//            if (requestUri.equals("/index.html") || requestUri.equals("/")) {
//                String pathname = "./src/main/resources/static/index.html";
//                try (FileInputStream fis = new FileInputStream(new File(pathname))) {
//                    DataOutputStream dos = new DataOutputStream(out);
//
//                    byte[] body = fis.readAllBytes();
//
//                    response200HeaderByType(dos, body.length, "text/html");
//                    responseBody(dos, body);
//                }
//            }

//            /**
//             *
//             * 분기 회원가입 클릭시
//             */
//            if (requestUri.equals("/registration")) {
//                String pathname = "./src/main/resources/static/registration/index.html";
//                try (FileInputStream fis = new FileInputStream(new File(pathname))) {
//                    DataOutputStream dos = new DataOutputStream(out);
//
//                    byte[] body = fis.readAllBytes();
//
//                    response200HeaderByType(dos, body.length, "text/html");
//                    responseBody(dos, body);
//                }
//            }

            /**
             * 분기 쿼리 스트링 (회원가입 폼 처리)
             */
//            if (requestUri.startsWith("/user/create")) {
//                HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
//                /?userId=shim9597&name=%EC%8B%AC%EB%B0%94&password=11&email=shim9597%40gmail.com

//                Map<String, String> parameters = new HashMap<>();
//
//                String queryString = requestUri.replaceAll("/user/create\\?", "");
//                String[] pairs = queryString.split("&");
//                for (String pair : pairs) {
//                    int idx = pair.indexOf("=");
//                    String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
//                    String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
//                    parameters.put(key, value);
//                }
//
//                //파싱 확인용 코드
//                for (Entry<String, String> entry : parameters.entrySet()) {
//                    logger.debug("key : {}, value : {}", entry.getKey(), entry.getValue());
//                }
//
//                User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"),
//                        parameters.get("email"));
//
//                logger.debug("userInfo : {}", user);
//            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //            try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
//
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
            dos.writeBytes("\r\n");
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
