package atm.machine;

import atm.money.Aggregation;
import atm.processor.Processor;

import java.util.TreeSet;

public class MachineATMImpl implements MachineATM {
    private final Processor processor;
    private TreeSet<Cassette> cassettes;

    public MachineATMImpl(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Aggregation giveMoney(Aggregation aggregation) {
        if (cassettes == null) {
            cassettes = processor.loadCassette(aggregation);
        } else {
            cassettes = processor.uploadCassette(cassettes, aggregation);
        }
        return new Aggregation(cassettes);
    }

    @Override
    public Aggregation takeMoney(int amount) {
        Aggregation aggregation = processor.checkIfPossibleToTakeMoney((TreeSet<Cassette>) cassettes.clone(), amount);
        if (aggregation == null) {
            System.out.printf("MESSAGE TO USER: AMOUNT: %d WITHDRAW IS IMPOSSIBLE", amount);
            return null;
        } else {
            cassettes = processor.withDrawAmount(cassettes, aggregation);
            return aggregation;
        }
    }

    @Override
    public Aggregation showStatus() {
        return processor.showStatus(cassettes);
    }
}
