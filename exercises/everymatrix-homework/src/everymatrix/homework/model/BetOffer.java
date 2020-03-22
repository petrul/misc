package everymatrix.homework.model;

import java.util.Objects;

/**
 * a BetOffer is a typed wrapper for an int representing a bet offer
 */
public class BetOffer {

    private int id;

    public BetOffer(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BetOffer betOffer = (BetOffer) o;
        return id == betOffer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
