package everymatrix.homework.handlers;

import com.sun.net.httpserver.HttpExchange;
import everymatrix.homework.Registry;
import everymatrix.homework.Util;
import everymatrix.homework.exceptions.BadRequestException;
import everymatrix.homework.exceptions.NoHandlerFoundException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for REST call #1 : Create session
 * GET /<customerid>/session
 */
public class CreateSessionHandler extends Handler {

    private     int         customerId;
    private     Registry    registry;

    public CreateSessionHandler(Registry registry, int customerId) {
        this.registry = registry;
        this.customerId = customerId;
    }

    public static CreateSessionHandler fromRequest(Registry registry, Request request) throws NoHandlerFoundException, BadRequestException {
        final String    url     = request.getUrl();
        final String    regex   = "^/([\\d]+)/session$";
        final Pattern   pattern = Pattern.compile(regex);
        final Matcher   matcher = pattern.matcher(url);

        if (matcher.find()) {
            String sCustomerId = matcher.group(1);

            final int customerId;
            try {
                customerId = Integer.parseInt(sCustomerId);
            } catch (NumberFormatException e) {
                throw new BadRequestException(String.format(
                        "bad customerId [%s] specified in url [%s]", sCustomerId, url), e);
            }
            return new CreateSessionHandler(registry, customerId);
        } else
            throw new NoHandlerFoundException("no CreateSessionHandler could be built from url " + url + ", expected " + regex);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException  {

        final String sessId = Util.newRandomSessionKey();

        this.registry.createSession(sessId, this.customerId);

        exchange.sendResponseHeaders(200, sessId.getBytes().length);
        exchange.getResponseBody().write(sessId.getBytes());
    }
}
