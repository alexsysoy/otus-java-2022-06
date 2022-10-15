package proxy;


import proxy.handler.ProxyHandler;
import proxy.testclass.TestLogging;

public class Main {
    public static void main(String[] args) {
        TestLogging testLogging = ProxyHandler.createTestLoggingClass();
        testLogging.calculation(42);
        testLogging.calculation(12, 1, "Ура!");
        testLogging.calculation(1,77, 5.0);
        testLogging.calculation();
    }
}
