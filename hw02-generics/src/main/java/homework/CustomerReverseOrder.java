package homework;

import java.util.ArrayDeque;

public class CustomerReverseOrder {
    public ArrayDeque<Customer> customers;

    public CustomerReverseOrder() {
        this.customers = new ArrayDeque<>();
    }

    public void add(Customer customer) {
        customers.push(customer);
    }

    public Customer take() {
        return customers.pollFirst();
    }
}
