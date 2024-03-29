package proxy;


import proxy.handler.ProxyHandler;
import proxy.testclass.TestLogging;

public class Main {
    public static void main(String[] args) {
        TestLogging testLogging = ProxyHandler.createTestLoggingClass();
        testLogging.calculation(42);
        testLogging.calculation(12, 1, "Ура!");
        testLogging.calculation(1,77, 5.0);
        testLogging.calculation(1, 5.0, 77);
        testLogging.calculation();
        testLogging.anotherCalculation();
        testLogging.anotherCalculation(5, 12.0);
        testLogging.anotherCalculation(11.0, 5);
    }
}
