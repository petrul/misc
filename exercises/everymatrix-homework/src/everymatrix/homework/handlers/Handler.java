package everymatrix.homework.handlers;

import com.sun.net.httpserver.HttpExchange;
import everymatrix.homework.Registry;
import everymatrix.homework.exceptions.BadRequestException;
import everymatrix.homework.exceptions.NoHandlerFoundException;
import everymatrix.homework.exceptions.SessionExpiredException;

import java.io.IOException;

/**
 * A Handler is able to parse an URL for its arguments
 * and also actually handle the request via its {@link #handle(HttpExchange)}
 * method.
 */
public abstract class Handler {

    /**
     * parses request to decide upon the handler to use
     * @throws NoHandlerFoundException if bad url or data
     */
    public static Handler chooseHandler(Registry registry, Request request) throws NoHandlerFoundException, IOException, BadRequestException {
        final String method = request.getMethod();
        final String url = request.getUrl();
        System.out.println(String.format("[%s] %s %s", Thread.currentThread().getName(), method, url));
        
       if (Request.METHOD_POST.equals(method)) {
           // the app manages only one POST: #2. a customer’s stake on a bet offer
           // the url should look like this :
           // POST /<betofferid>/stake?sessionkey=<sessionkey>

           PostBetHandler postBetHandler = PostBetHandler.fromRequest(registry, request);
           return postBetHandler;
       } else {
           if (Request.METHOD_GET.equals(method)) {
               // GET: two cases here: 1. create session or 3. get highest stakes

               try {
                   CreateSessionHandler createSessionHandler = CreateSessionHandler.fromRequest(registry, request);
                   return createSessionHandler;
               } catch (NoHandlerFoundException e) {
                   // could not build the handler; ignore, maybe we can build the second one
               }

               GetHighStakesHandler getHighStakesHandler = GetHighStakesHandler.fromRequest(registry, request);
               return getHighStakesHandler;

           } else {
               throw new NoHandlerFoundException("no handler for requested method");
           }

       }
       
    }

    /**
     * actual handler
     * @param exchange used for access to response output stream.
     */
    abstract public void handle(HttpExchange exchange) throws IOException, SessionExpiredException, BadRequestException;

}
