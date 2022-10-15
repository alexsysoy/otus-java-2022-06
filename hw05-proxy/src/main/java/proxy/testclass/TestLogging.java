package proxy.testclass;

import proxy.annotation.Log;

public interface TestLogging {
    @Log
    void calculation(int param1);
    @Log
    void calculation(int param1, int param2, String param3);
    void calculation(int param1, int para2, double param3);
    @Log
    void calculation();
}
