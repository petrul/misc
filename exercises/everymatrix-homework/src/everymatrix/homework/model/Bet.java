package everymatrix.homework.model;

import java.util.Date;

/**
 * representes a stake taken on a BetOffer
 */
public class Bet {

    private BetOffer betOffer;
    private String sessionId;
    private int customerId;
    private int stake;
    private Date timestamp;

    public Bet(BetOffer betOffer, String sessionId, int customerId, int stake) {
        this.betOffer = betOffer;
        this.sessionId = sessionId;
        this.customerId = customerId;
        this.stake = stake;
        this.timestamp = new Date();
    }

    public BetOffer getBetOffer() {
        return betOffer;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getStake() {
        return stake;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "betOffer=" + betOffer +
                ", sessionId='" + sessionId + '\'' +
                ", customerId=" + customerId +
                ", stake=" + stake +
                ", timestamp=" + timestamp +
                '}';
    }
}
