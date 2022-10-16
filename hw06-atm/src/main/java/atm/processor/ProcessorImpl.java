package atm.processor;

import atm.exception.NotEnoughMoneyException;
import atm.machine.Cassette;
import atm.money.Aggregation;
import atm.money.Banknote;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class ProcessorImpl implements Processor {
    @Override
    public Aggregation showStatus(TreeSet<Cassette> cassettes) {
        if (cassettes == null) {
            throw new RuntimeException("THERE IS NO CASSETTES!");
        }
        return new Aggregation(cassettes);
    }

    @Override
    public TreeSet<Cassette> loadCassette(Aggregation aggregation) {
        var result = new TreeSet<Cassette>();
        aggregation.getBanknotes().forEach((key, value) -> result.add(new Cassette(key, value)));
        return result;
    }

    @Override
    public TreeSet<Cassette> uploadCassette(TreeSet<Cassette> cassettes, Aggregation aggregation) {
        TreeSet<Cassette> changedCassette = new TreeSet<>();
        for (Map.Entry<Banknote, Integer> banknoteEntry : aggregation.getBanknotes().entrySet()) {
            boolean isCassettesContainBanknote = false;
            for (Cassette cassette : cassettes) {
                if (cassette.getBanknote().compareTo(banknoteEntry.getKey()) == 0) {
                    cassette.addQuantity(banknoteEntry.getValue());
                    isCassettesContainBanknote = true;
                    break;
                }
            }
            if (!isCassettesContainBanknote) {
                changedCassette.add(new Cassette(banknoteEntry.getKey(), banknoteEntry.getValue()));
            }
        }
        changedCassette.addAll(cassettes);
        return changedCassette;
    }

    @Override
    public Aggregation checkIfPossibleToTakeMoney(TreeSet<Cassette> cassettes, int amount) {
        if (cassettes == null || cassettes.isEmpty()) {
            throw new RuntimeException("THERE IS NO MONEY IN ATM!!!");
        }
        Aggregation result = new Aggregation(new TreeMap<>());
        try {
            result = recursionFindMapBanknote(cassettes, amount, result);
            return removeUnnecessary(result);
        } catch (NotEnoughMoneyException e) {
            return null;
        }
    }

    @Override
    public TreeSet<Cassette> withDrawAmount(TreeSet<Cassette> cassettes, Aggregation aggregation) {
        TreeSet<Cassette> changedCassette = new TreeSet<>();
        for (Map.Entry<Banknote, Integer> banknoteEntry : aggregation.getBanknotes().entrySet()) {
            boolean isCassettesContainBanknote = false;
            for (Cassette cassette : cassettes) {
                if (cassette.getBanknote().compareTo(banknoteEntry.getKey()) == 0) {
                    cassette.withdrawQuantity(banknoteEntry.getValue());
                    isCassettesContainBanknote = true;
                    break;
                }
            }
            if (!isCassettesContainBanknote) {
                changedCassette.add(new Cassette(banknoteEntry.getKey(), banknoteEntry.getValue()));
            }
        }
        changedCassette.addAll(cassettes);
        return changedCassette;
    }

    private Aggregation recursionFindMapBanknote(TreeSet<Cassette> cassettes, int amount, Aggregation aggregation) {
        if (cassettes.isEmpty() && amount != 0) {
            throw new NotEnoughMoneyException("This amount cannot be paid with banknotes located in an ATM");
        }
        Cassette cassette = cassettes.pollLast();
        assert cassette != null;
        int nominal = cassette.getBanknote().nominal();
        int quantity = cassette.getQuantity();
        int count = 0;
        while ((amount - nominal) >= 0 && quantity > 0) {
            amount -= nominal;
            quantity--;
            count++;
        }
        aggregation.getBanknotes().put(cassette.getBanknote(), count);
        if (amount == 0) {
            return aggregation;
        } else {
            return recursionFindMapBanknote(cassettes, amount, aggregation);
        }
    }

    private Aggregation removeUnnecessary(Aggregation aggregation) {
        TreeMap<Banknote, Integer> changedBanknotes = new TreeMap<>();
        aggregation.getBanknotes().forEach((k, v) -> {
            if (v > 0) {
                changedBanknotes.put(k, v);
            }
        });
        return new Aggregation(changedBanknotes);
    }
}
