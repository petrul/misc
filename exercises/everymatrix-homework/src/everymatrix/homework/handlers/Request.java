package everymatrix.homework.handlers;

import com.sun.net.httpserver.HttpExchange;
import everymatrix.homework.Util;

import java.io.IOException;

/**
 * This class realizez a decoupling from HTTPServer's data structures.
 *
 * It is a replacement for HttpExchange in this code.
 *
 * The reason is testability. HttpExchange and its derivatives cannot be easily new'd in a test.
 */
public class Request {

    final public static String METHOD_GET = "GET";
    final public static String METHOD_POST = "POST";

    private String url;
    private String method;
    private String body;

    public Request(String url, String method, String body) {
        this.url = url;
        this.method = method;
        this.body = body;
    }

    public static Request fromHttpExchange(HttpExchange exchange) throws IOException {
        final String url = exchange.getRequestURI().toASCIIString();
        final String method = exchange.getRequestMethod();
        final String body = Util.streamToString(exchange.getRequestBody());

        return new Request(url, method, body);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
