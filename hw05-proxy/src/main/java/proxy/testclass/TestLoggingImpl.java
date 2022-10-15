package proxy.testclass;

import proxy.annotation.Log;

public class TestLoggingImpl implements TestLogging {
    @Override
    @Log
    public void calculation(int param1) {
        System.out.printf("[annotated] method calculation with param int: %d work%n%n", param1);
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        System.out.printf("[annotated] method calculation with param int: %d int: %d String: %s work%n%n", param1, param2, param3);
    }

    @Override
    public void calculation(int param1, int param2, double param3) {
        System.out.printf("[not annotated] method calculation with param int: %d int: %d double: %s work%n%n", param1, param2, param3);
    }

    @Override
    @Log
    public void calculation(int param1, double param2, int param3) {
        System.out.printf("[annotated] method calculation with param int: %d double: %s int: %d work%n%n", param1, param2, param3);
    }

    @Override
    @Log
    public void calculation() {
        System.out.printf("[annotated] method calculation with no param work%n%n");
    }

    @Override
    @Log
    public void anotherCalculation() {
        System.out.printf("[annotated] method anotherCalculation with no param work%n%n");
    }

    @Override
    @Log
    public void anotherCalculation(int param1, double param2) {
        System.out.printf("[annotated] method anotherCalculation with with param int: %d double: %.1f work%n%n", param1, param2);
    }

    @Override
    public void anotherCalculation(double param1, int param2) {
        System.out.printf("[not annotated] method anotherCalculation with param double: %.1f int: %d work%n%n", param1, param2);
    }
}
