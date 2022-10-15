package proxy.testclass;

public class TestLoggingImpl implements TestLogging {
    @Override
    public void calculation(int param1) {
        System.out.printf("[annotated] method calculation with param int: %d work%n", param1);
    }

    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.printf("[annotated] method calculation with param int: %d int: %d String: %s work%n", param1, param2, param3);
    }

    @Override
    public void calculation(int param1, int param2, double param3) {
        System.out.printf("[not annotated] method calculation with param int: %d int: %d double: %s work%n", param1, param2, param3);
    }

    @Override
    public void calculation() {
        System.out.println("[annotated] method calculation with no param work");
    }
}
