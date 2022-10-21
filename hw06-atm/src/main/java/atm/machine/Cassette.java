package atm.machine;

import atm.money.Banknote;

public class Cassette implements Comparable<Cassette>, Cloneable {
    private Banknote banknote;
    private int quantity;

    public Cassette(Banknote banknote, int quantity) {
        this.banknote = banknote;
        this.quantity = quantity;
    }

    public Banknote getBanknote() {
        return banknote;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int additionalQuantity) {
        quantity += additionalQuantity;
    }

    public void withdrawQuantity(int additionalQuantity) {
        quantity -= additionalQuantity;
    }

    @Override
    public int compareTo(Cassette o) {
        return this.banknote.compareTo(o.banknote);
    }


    @Override
    public Cassette clone() {
        try {
            Cassette clone = (Cassette) super.clone();
            clone.banknote = banknote.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Cassette{" +
                "banknote=" + banknote +
                ", quantity=" + quantity +
                '}';
    }
}
