package proxy.testclass;

public interface TestLogging {
    void calculation(int param1);

    void calculation(int param1, int param2, String param3);

    void calculation(int param1, int para2, double param3);

    void calculation(int param1, double param2, int param3);

    void calculation();

    void anotherCalculation();

    void anotherCalculation(int param1, double param2);

    void anotherCalculation(double param2, int param1);
}
