package everymatrix.homework;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import everymatrix.homework.exceptions.SessionExpiredException;
import everymatrix.homework.exceptions.BadRequestException;
import everymatrix.homework.handlers.Handler;
import everymatrix.homework.exceptions.NoHandlerFoundException;
import everymatrix.homework.handlers.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App implements Runnable {

    Registry registry;

    public App(Registry registry) {
        this.registry = registry;
    }

    public static void main(String[] args) {
        Registry registry = new Registry(10 * 60 * 1000); // 10 min expiry
        new App(registry).run();
    }

    
    HttpServer httpServer;
    ExecutorService executor;

    @Override
    public void run() {

        
        InetAddress lo = InetAddress.getLoopbackAddress();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(lo, 8001);

        try {
            this.httpServer = HttpServer.create(inetSocketAddress, 0);
            HttpContext context = httpServer.createContext("/");
            HttpHandler httpHandler = new MyHttpHandler(this.registry);
            context.setHandler(httpHandler);
            this.executor = Executors.newFixedThreadPool(100);
            httpServer.setExecutor(executor);
            httpServer.start();

            System.out.println(String.format("[%s] started server... ", Thread.currentThread().getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.httpServer.stop(1);
        this.executor.shutdownNow();

        System.out.println(String.format("%s server stopped.", Thread.currentThread().getName()));
    }
}

class MyHttpHandler implements HttpHandler {
    Registry registry;

    public MyHttpHandler(Registry registry) {
        this.registry = registry;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            try {

                Handler handler = Handler.chooseHandler(this.registry, Request.fromHttpExchange(exchange));
                handler.handle(exchange);

            } catch (NoHandlerFoundException e) {
                final String message = "No handler found for url " + exchange.getRequestURI().toString();
                System.out.println(message);

                serve404PageNotFound(exchange, message);
            } catch (BadRequestException e ) {
                e.printStackTrace();
                final String message = "Bad request for url " + exchange.getRequestURI().toString() + ":" + e.getMessage();
                System.out.println(message);

                serve400BadRequest(exchange, message);
            } catch (SessionExpiredException e) {
                e.printStackTrace();
                final String message = "Session non-existent or expired for url " + exchange.getRequestURI().toString();
                System.out.println(message);

                serve400BadRequest(exchange, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            final String message = e.getMessage();

            tryServing500InternalServerError(exchange, e);
        }
    }

    private void serve400BadRequest(HttpExchange exchange, String message) throws IOException {
        serveCodeAndMessage(exchange, 400, message);
    }


    private void serve404PageNotFound(HttpExchange exchange, String message) throws IOException {
        serveCodeAndMessage(exchange, 404, message);
    }

    private void tryServing500InternalServerError(HttpExchange exchange, Exception e) {
        try {
            serveCodeAndMessage(exchange, 500, "Internal server error: " + e.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void serveCodeAndMessage(HttpExchange exchange, int httpCode, String message) throws IOException {

        StringBuilder response = new StringBuilder();
        response.append(message + " - " + exchange.getRequestURI().toString());
        final String str = response.toString();

        exchange.sendResponseHeaders(httpCode, str.getBytes().length);

        OutputStream body = exchange.getResponseBody();
        body.write(response.toString().getBytes());
        body.close();
    }

    public Registry getRegistry() {
        return registry;
    }
}