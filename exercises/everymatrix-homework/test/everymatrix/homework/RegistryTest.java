package everymatrix.homework;

import everymatrix.homework.exceptions.BadRequestException;
import everymatrix.homework.model.Bet;
import everymatrix.homework.model.BetOffer;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * test for @{@link Registry}
 */
public class RegistryTest {

    @Test
    public void testSessionExpiration() throws Exception {
        Registry reg = new Registry(10);

        String sessId = "asd12345";
        int custId = 54321;

        assert (!reg.hasSession(sessId));

        reg.createSession(sessId, custId);

        assert (reg.hasSession(sessId));

        Thread.sleep(11);

        assert (!reg.hasSession(sessId)); // session has expired
    }

    @Test
    public void testBetOffersProperlyInitialized() {
        Registry registry = new Registry(1000);
        Map<BetOffer, List<Bet>> bets = registry.getBets();
        assert bets.size() == 100;

        for (int i = 0; i < 100; i++) {
            BetOffer bo = new BetOffer(i);
            assert bets.containsKey(bo);
        }

        BetOffer bo101 = new BetOffer(101);
        assert !bets.containsKey(bo101);

    }

    @Test
    public void testPlaceBet() throws Exception {
        Registry registry = new Registry(10000);
        BetOffer bo = new BetOffer(1);
        
        int customerId = 14;

        String sessId = Util.newRandomSessionKey();

        Bet bet1 = new Bet(bo, sessId, customerId, 2);
        Bet bet2 = new Bet(bo, sessId, customerId, 4);
        Bet bet3 = new Bet(bo, sessId, customerId, 3);

        registry.placeBet(bet1);
        registry.placeBet(bet2);
        registry.placeBet(bet3);

        System.out.println(registry.bets.size());
        assert registry.bets.size() == 100; // 100 fixed bet offers
        assert registry.bets.get(bo).size() == 3;  // nr of bets that were placed for 
        
        List<Bet> highStakes = registry.getHighStakes(bo.getId());
        System.out.println(highStakes);

        assert highStakes.size() == 1;
        System.out.println(highStakes.get(0));
        assert highStakes.get(0).getStake() == 4; // the larger one
    }

    @Test
    public void testHighStakes() throws BadRequestException {
        Registry registry = new Registry(10000);
        int nrCustomers = 30;

        HashSet<String> sessions = new HashSet<>();

        for (int c = 0; c < nrCustomers; c++) {
            String sess = Util.newRandomSessionKey();
            sessions.add(sess);

            for (int bo = 0; bo < 100; bo++) {
                BetOffer betOffer = new BetOffer(bo);

                //every customer places two random bets on each bet offer
                Bet bet1 = new Bet(betOffer, sess, c, Math.abs(new Random().nextInt(100)));
                Bet bet2 = new Bet(betOffer, sess, c, Math.abs(new Random().nextInt(100)));

                registry.placeBet(bet1);
                registry.placeBet(bet2);

                // and a third, that tops all the others
                Bet bet3 = new Bet(betOffer, sess, c, 101);
                registry.placeBet(bet3);
            }
        }

        // now assert highest stakes are all 101 for all bet offers
        for (int bo = 0; bo < 100; bo++) {
            List<Bet> highStakes = registry.getHighStakes(bo);
            for (Bet b : highStakes) {
                assert b.getStake() == 101;
                assert sessions.contains(b.getSessionId());
            }
        }
    }
}