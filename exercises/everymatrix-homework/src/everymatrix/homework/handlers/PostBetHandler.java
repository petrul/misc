package everymatrix.homework.handlers;

import com.sun.net.httpserver.HttpExchange;
import everymatrix.homework.Registry;
import everymatrix.homework.exceptions.BadRequestException;
import everymatrix.homework.exceptions.NoHandlerFoundException;
import everymatrix.homework.exceptions.SessionExpiredException;
import everymatrix.homework.model.Bet;
import everymatrix.homework.model.BetOffer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A @{@link Handler} which is in charge of posting bets.
 * Responds to URLs of type POST /<betofferid>/stake?sessionkey=<sessionkey>
 */
public class PostBetHandler extends Handler {

    private int stake;
    private int betofferId;
    private String sessionKey;
    private Registry registry;

    public PostBetHandler(Registry registry, int stake, int betofferId, String sessionKey) {
        this.stake = stake;
        this.betofferId = betofferId;
        this.sessionKey = sessionKey;
        this.registry = registry;
    }

    /**
     * @throws NoHandlerFoundException if bad url or bad body content
     */
    public static PostBetHandler fromRequest(Registry registry, Request request) throws IOException, NoHandlerFoundException, BadRequestException {
        final String url = request.getUrl();
        final String post_regex = "^/([\\d]+)/stake\\?sessionkey=([\\w\\d]+)$";
        final Pattern pattern = Pattern.compile(post_regex);
        final Matcher matcher = pattern.matcher(url);
        final String sBetoffer;
        final String sSesskey;

        if (matcher.find()) {
            
            sBetoffer = matcher.group(1);
            sSesskey = matcher.group(2);
            
            final String body = request.getBody();
            final int stake;
            final int betoffer;
            try {
                 stake = Integer.parseInt(body);
                 betoffer = Integer.parseInt(sBetoffer);
            } catch (NumberFormatException e) {
                throw new BadRequestException(
                        String.format(
                        "NumberFormatException on either stake %s or betoffer %s;", body, sBetoffer), e);
            }
            return new PostBetHandler(registry, stake, betoffer, sSesskey);
        } else
            throw new NoHandlerFoundException("no PostBetHandler could be built from url " + url + ", expected " + post_regex);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException, SessionExpiredException {
        BetOffer bo = new BetOffer(this.betofferId);
        if (!this.registry.hasSession(this.sessionKey))
            throw new SessionExpiredException("session does not exist or has expired " + this.sessionKey);
        Integer customerId = this.registry.getSession2customer().get(this.sessionKey);
        Bet bet = new Bet(bo, this.sessionKey, customerId, this.stake);
        
        this.registry.placeBet(bet);

        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().close();
    }


    public int getStake() {
        return stake;
    }

    public int getBetofferId() {
        return betofferId;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
