package everymatrix.homework.handlers;

import com.sun.net.httpserver.HttpExchange;
import everymatrix.homework.Registry;
import everymatrix.homework.exceptions.BadRequestException;
import everymatrix.homework.exceptions.NoHandlerFoundException;
import everymatrix.homework.model.Bet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Handler for REST call #3. "Get high stakes"
 *  Responds to GET /<betofferid>/highstakes
 */
public class GetHighStakesHandler extends Handler {

    private Registry registry;
    private int betOfferId;

    public GetHighStakesHandler(Registry registry, int betOfferId) {
        this.registry = registry;
        this.betOfferId = betOfferId;
    }

    /**
     * factory method for building an instance of this by parsing a request
     */
    public static GetHighStakesHandler fromRequest(Registry registry, Request request) throws NoHandlerFoundException, BadRequestException {
        final String url = request.getUrl();
        final String regex = "^/([\\d]+)/highstakes$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String sBetOfferId = matcher.group(1);
            final int betOfferId;
            try {
                betOfferId = Integer.parseInt(sBetOfferId);
            } catch (NumberFormatException e) {
                throw new BadRequestException(String.format(
                        "bad betOfferId [%s] specified in url [%s]", sBetOfferId, url), e);
            }
            return new GetHighStakesHandler(registry, betOfferId);
        } else
            throw new NoHandlerFoundException(
                    String.format("no GetHighStakesHandler could be built from url %s, expected %s", url, regex));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException, BadRequestException {
        List<Bet> top20 = this.registry.getHighStakes(this.betOfferId);

        OutputStream os = exchange.getResponseBody();

        StringBuilder sb = new StringBuilder();

        for (Bet bet : top20) {
            final String value = String.format("%d=%d,", bet.getCustomerId(), bet.getStake());

            sb.append(value);
        }

        final String str = sb.toString();

        exchange.sendResponseHeaders(200, str.length());
        os.write(str.getBytes());
        os.flush();
        os.close();
    }
}
