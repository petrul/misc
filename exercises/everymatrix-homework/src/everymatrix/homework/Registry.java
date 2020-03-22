package everymatrix.homework;

import everymatrix.homework.exceptions.BadRequestException;
import everymatrix.homework.model.Bet;
import everymatrix.homework.model.BetOffer;

import java.util.*;

/**
 * Acts as in-memory "database".
 * Contains the data structures storing the open sessions, stakes and bets
 */
public class Registry {

    int sessionExpiryMillis;

    // keeps track of open sessions and their expiry date
    Map<String, Date> sessions = new HashMap<>();

    // keeps track of customerid for each session id
    Map<String, Integer> session2customer = new HashMap<>();

    //
    Map<BetOffer, List<Bet>> bets = new HashMap<>();


    public Registry () {
        // 10 minutes default expiry time
        this( 10 * 60 * 1000);
    }

    public Registry(int sessionExpiryMillis) {
        this.sessionExpiryMillis = sessionExpiryMillis;

        // initialize with 100 bet offers, numbered from 1 to 100
        for (int i = 0; i < 100; i++) {
            final BetOffer betOffer = new BetOffer(i);
            LinkedList<Bet> stakes = new LinkedList<Bet>();
            bets.put(betOffer, stakes);
        }
    }

    synchronized public boolean hasSession(String sessionId) {
        if (!sessions.containsKey(sessionId)) return false;

        Date timestamp = sessions.get(sessionId);
        if ( new Date().getTime() - timestamp.getTime() > this.sessionExpiryMillis) {
            // session has expired
            this.sessions.remove(sessionId);
            this.session2customer.remove(sessionId);
            return false;
        }

        return true;
    }

    synchronized public void createSession(String sessionId, int customerId) {
        this.sessions.put(sessionId, new Date());
        this.session2customer.put(sessionId, customerId);
    }

    public Map<BetOffer, List<Bet>> getBets() {
        return bets;
    }
    
    public Map<String, Integer> getSession2customer() {
        return session2customer;
    }

    synchronized public void placeBet(Bet bet) {
        List<Bet> betList = this.bets.get(bet.getBetOffer());
        betList.add(bet);
    }

    public List<Bet> getHighStakes(int betOfferId) throws BadRequestException {
        Map<BetOffer, List<Bet>> bets = this.getBets();
        BetOffer bo = new BetOffer(betOfferId);
        if (!bets.containsKey(bo))
            throw new BadRequestException(String.format("bet offer %d does not exist", betOfferId), null);

        List<Bet> betList = bets.get(bo);
        betList.sort(new Comparator<Bet>() {
            @Override
            public int compare(Bet b1, Bet b2) {
                return b2.getStake() - b1.getStake();
            }
        });

        final HashSet<Integer> selectedCustomers = new HashSet<>();
        final ArrayList<Bet> top20 = new ArrayList<>(20);

        Iterator<Bet> it = betList.iterator();
        while (it.hasNext() && top20.size() < 20) {
            Bet bet = it.next();
            if (! selectedCustomers.contains(bet.getCustomerId())) {
                top20.add(bet);
                selectedCustomers.add(bet.getCustomerId());
            }
        }

        return top20;
    }
}
