package proxy.handler;

import proxy.annotation.Log;
import proxy.testclass.TestLogging;
import proxy.testclass.TestLoggingImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;

public class ProxyHandler {

    private ProxyHandler() {
    }

    public static TestLogging createTestLoggingClass() {
        InvocationHandler handler = new InvocationHandlerImpl(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(ProxyHandler.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class InvocationHandlerImpl implements InvocationHandler {
        private final TestLogging testLogging;

        private InvocationHandlerImpl(TestLogging testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isAnnotationPresent(Log.class)) {
                System.out.printf("[logging] executed method: %s, params: %s%n", method.getName(), getArgs(args));
            }
            return method.invoke(testLogging, args);
        }

        private List getArgs(Object[] args) {
            if (args != null) {
                return Arrays.stream(args).toList();
            } else {
                return EMPTY_LIST;
            }
        }
    }
}
