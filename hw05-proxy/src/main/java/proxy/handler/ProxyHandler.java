package proxy.handler;

import proxy.annotation.Log;
import proxy.testclass.TestLogging;
import proxy.testclass.TestLoggingImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
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
        private final List<MethodSignature> annotatedMethods;

        private InvocationHandlerImpl(TestLogging testLogging) {
            this.testLogging = testLogging;
            this.annotatedMethods = getAnnotatedMethods(TestLoggingImpl.class);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isSignatureMatchesWithAnnotatedMethod(method)) {
                System.out.printf("[logging] executed method: %s, params: %s%n", method.getName(), (args != null) ? Arrays.asList(args) : EMPTY_LIST);
            }
            return method.invoke(testLogging, args);
        }

        private List<MethodSignature> getAnnotatedMethods(Class<?> clazz) {
            List<MethodSignature> result = new ArrayList<>();
            Arrays.stream(clazz.getMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .forEach(method -> result.add(new MethodSignature(method)));
            return result;
        }

        private boolean isSignatureMatchesWithAnnotatedMethod(Method method) {
            return annotatedMethods.contains(new MethodSignature(method));
        }
    }
}
