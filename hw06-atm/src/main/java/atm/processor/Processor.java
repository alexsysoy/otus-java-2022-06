package atm.processor;

import atm.machine.Cassette;
import atm.money.Aggregation;

import java.util.TreeSet;

public interface Processor {
    Aggregation showStatus(TreeSet<Cassette> cassettes);
    TreeSet<Cassette> loadCassette(Aggregation aggregation);
    TreeSet<Cassette> uploadCassette(TreeSet<Cassette> cassettes, Aggregation aggregation);
    Aggregation checkIfPossibleToTakeMoney(TreeSet<Cassette> cassettes, int amount);
    TreeSet<Cassette> withDrawAmount(TreeSet<Cassette> cassettes, Aggregation aggregation);
}
