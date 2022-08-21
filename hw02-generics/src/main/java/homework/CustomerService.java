package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    public TreeMap<Customer,String> customers;

    public CustomerService() {
        this.customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        return returnEntryOrNull(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return returnEntryOrNull(customers.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }

    private Map.Entry<Customer, String> returnEntryOrNull(Map.Entry<Customer, String> entry) {
        return entry != null ? Map.entry(entry.getKey().clone(), entry.getValue()) : null;
    }
}
