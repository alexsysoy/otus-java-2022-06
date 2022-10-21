package atm.money;

import atm.machine.Cassette;

import java.util.TreeMap;
import java.util.TreeSet;

public class Aggregation {
    private final TreeMap<Banknote, Integer> banknotes;

    public Aggregation(TreeMap<Banknote, Integer> banknotes) {
        this.banknotes = banknotes;
    }

    public Aggregation(TreeSet<Cassette> cassettes) {
        TreeMap<Banknote, Integer> banknotes = new TreeMap<>();
        cassettes.forEach(cassette -> banknotes.put(cassette.getBanknote(), cassette.getQuantity()));
        this.banknotes = banknotes;
    }

    public TreeMap<Banknote, Integer> getBanknotes() {
        return banknotes;
    }

    @Override
    public String toString() {
        return "Aggregation{" +
                "banknotes=" + banknotes +
                '}';
    }
}
