package atm.machine;

import atm.money.Aggregation;

public interface MachineATM {
    Aggregation giveMoney(Aggregation aggregation);
    Aggregation takeMoney(int amount);
    Aggregation showStatus();
}
