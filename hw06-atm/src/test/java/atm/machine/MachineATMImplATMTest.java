package atm.machine;

import atm.money.Aggregation;
import atm.money.Banknote;
import atm.processor.Processor;
import atm.processor.ProcessorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class MachineATMImplATMTest {
    private MachineATM atm;

    @BeforeEach
    void setUp() {
        Processor processor = new ProcessorImpl();
        atm = new MachineATMImpl(processor);
    }

    @Test
    void giveMoneyFirstTime() {
        // given
        Banknote banknote50 = new Banknote(50, "FIFTY");
        Banknote banknote100 = new Banknote(100, "HUNDRED");
        Banknote banknote500 = new Banknote(500, "FIVE HUNDRED");
        Banknote banknote1000 = new Banknote(1000, "ONE THOUSAND");
        TreeMap<Banknote, Integer> treeMap = new TreeMap<>();
        treeMap.put(banknote50, 3);
        treeMap.put(banknote100, 15);
        treeMap.put(banknote500, 100);
        treeMap.put(banknote1000, 5);
        Aggregation expected = new Aggregation(treeMap);

        // when
        Aggregation actual = atm.giveMoney(expected);

        // then
        assertEquals(actual.getBanknotes().entrySet(), expected.getBanknotes().entrySet());
    }

    @Test
    void giveMoneyNotFirstTime() {
        // given
        Banknote banknote50 = new Banknote(50, "FIFTY");
        Banknote banknote100 = new Banknote(100, "HUNDRED");
        Banknote banknote500 = new Banknote(500, "FIVE HUNDRED");
        Banknote banknote1000 = new Banknote(1000, "ONE THOUSAND");
        TreeMap<Banknote, Integer> treeMap = new TreeMap<>();
        treeMap.put(banknote50, 3);
        treeMap.put(banknote100, 15);
        treeMap.put(banknote500, 100);
        treeMap.put(banknote1000, 5);
        Aggregation aggregation = new Aggregation(treeMap);
        atm.giveMoney(aggregation);

        Banknote banknoteToAdd100 = new Banknote(100, "HUNDRED");
        Banknote banknoteToAdd500 = new Banknote(500, "FIVE HUNDRED");
        Banknote banknoteToAdd200New = new Banknote(200, "TWO HUNDRED");
        TreeMap<Banknote, Integer> treeMapToAdd = new TreeMap<>();
        treeMapToAdd.put(banknoteToAdd100, 4);
        treeMapToAdd.put(banknoteToAdd500, 3);
        treeMapToAdd.put(banknoteToAdd200New, 10);
        Aggregation aggregationToAdd = new Aggregation(treeMapToAdd);

        TreeMap<Banknote, Integer> treeMapExpected = new TreeMap<>();
        treeMapExpected.put(banknote50, 3);
        treeMapExpected.put(banknote100, 19);
        treeMapExpected.put(banknoteToAdd200New, 10);
        treeMapExpected.put(banknote500, 103);
        treeMapExpected.put(banknote1000, 5);
        Aggregation expected = new Aggregation(treeMapExpected);

        // when
        Aggregation actual = atm.giveMoney(aggregationToAdd);

        // then
        assertEquals(expected.getBanknotes().entrySet(), actual.getBanknotes().entrySet());
    }

    @Test
    void takeMoneyIfNotPossible() {
        // given
        Banknote banknote50 = new Banknote(50, "FIFTY");
        Banknote banknote100 = new Banknote(100, "HUNDRED");
        Banknote banknote500 = new Banknote(500, "FIVE HUNDRED");
        Banknote banknote1000 = new Banknote(1000, "ONE THOUSAND");
        TreeMap<Banknote, Integer> treeMap = new TreeMap<>();
        treeMap.put(banknote50, 3);
        treeMap.put(banknote100, 15);
        treeMap.put(banknote500, 100);
        treeMap.put(banknote1000, 5);
        Aggregation aggregation = new Aggregation(treeMap);
        atm.giveMoney(aggregation);

        // when
        Aggregation actual = atm.takeMoney(7235);

        // then
        assertNull(actual);
    }

    @Test
    void takeMoney() {
        // given
        Banknote banknote50 = new Banknote(50, "FIFTY");
        Banknote banknote100 = new Banknote(100, "HUNDRED");
        Banknote banknote500 = new Banknote(500, "FIVE HUNDRED");
        Banknote banknote1000 = new Banknote(1000, "ONE THOUSAND");
        TreeMap<Banknote, Integer> treeMap = new TreeMap<>();
        treeMap.put(banknote50, 3);
        treeMap.put(banknote100, 15);
        treeMap.put(banknote500, 100);
        treeMap.put(banknote1000, 5);
        Aggregation aggregation = new Aggregation(treeMap);
        atm.giveMoney(aggregation);

        TreeMap<Banknote, Integer> expectedTreeMap = new TreeMap<>();
        expectedTreeMap.put(banknote100, 4);
        expectedTreeMap.put(banknote500, 89);
        expectedTreeMap.put(banknote1000, 5);
        Aggregation expected = new Aggregation(expectedTreeMap);

        TreeMap<Banknote, Integer> treeMapAtm = new TreeMap<>();
        treeMapAtm.put(banknote50, 3);
        treeMapAtm.put(banknote100, 11);
        treeMapAtm.put(banknote500, 11);
        treeMapAtm.put(banknote1000, 0);
        Aggregation expectedATM = new Aggregation(treeMapAtm);

        // when
        Aggregation actual = atm.takeMoney(49900);
        Aggregation actualATM = atm.showStatus();

        // then
        assertEquals(expected.getBanknotes(), actual.getBanknotes());
        assertEquals(expectedATM.getBanknotes(), actualATM.getBanknotes());
    }

    @Test
    void showStatus() {
        // given
        Banknote banknote50 = new Banknote(50, "FIFTY");
        Banknote banknote100 = new Banknote(100, "HUNDRED");
        Banknote banknote500 = new Banknote(500, "FIVE HUNDRED");
        Banknote banknote1000 = new Banknote(1000, "ONE THOUSAND");
        TreeMap<Banknote, Integer> treeMap = new TreeMap<>();
        treeMap.put(banknote50, 3);
        treeMap.put(banknote100, 15);
        treeMap.put(banknote500, 100);
        treeMap.put(banknote1000, 5);
        Aggregation expected = new Aggregation(treeMap);
        atm.giveMoney(expected);

        // when
        Aggregation actual = atm.showStatus();

        // then
        assertEquals(expected.getBanknotes(), actual.getBanknotes());
    }
}