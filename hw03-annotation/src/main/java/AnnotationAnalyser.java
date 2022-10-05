import annotations.After;
import annotations.Before;
import annotations.Test;
import dto.TestStatistic;
import dto.TestsResources;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationAnalyser {

    public TestStatistic run(String nameClassForTest) {
        Class<?> clazz = findClassForTest(nameClassForTest);
        TestsResources resources = getDataForTest(clazz);
        return process(resources);
    }

    private TestStatistic process(TestsResources resources) {
        TestStatistic result = new TestStatistic();
        for (Method method : resources.tests()) {
            runBeforeMethod(resources);
            try {
                method.invoke(resources.instanceTestClass());
                result.successTests++;
            } catch (Exception e) {
                System.out.printf("Тест %s выполнился с ошибкой!\n", method.getName());
                result.failTests++;
            } finally {
                runAfterMethod(resources);
            }
            result.totalCountTests++;
        }
        return result;
    }

    private void runBeforeMethod(TestsResources resources) {
        runBasicMethod(resources.instanceTestClass(), resources.before());
    }

    private void runAfterMethod(TestsResources resources) {
        runBasicMethod(resources.instanceTestClass(), resources.after());
    }

    private void runBasicMethod(Object instance, Method method) {
        try {
            if (method != null) {
                method.invoke(instance);
            }
        } catch (Exception e) {
            String message = "Ошибка при попытке запуска метода: " + method.getName();
            throw new RuntimeException(message, e);
        }
    }

    private TestsResources getDataForTest(Class<?> clazz) {
        Object instanceTestClass = instantiate(clazz);
        Method[] methods = clazz.getMethods();
        Method before = null;
        Method after = null;
        List<Method> methodsForTest = new ArrayList<>();
        for (Method method : methods) {
            if (method.getAnnotations().length > 1) {
                throw new RuntimeException("На методе тестового класса более чем одна аннотация");
            }
            if (method.isAnnotationPresent(Before.class)) {
                before = method;
            }
            if (method.isAnnotationPresent(After.class)) {
                after = method;
            }
            if (method.isAnnotationPresent(Test.class)) {
                methodsForTest.add(method);
            }
        }
        return new TestsResources(instanceTestClass, before, after, methodsForTest);
    }

    private  <T> T instantiate(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.printf("Ошибка при создании объекта класса %s\n", type);
            throw new RuntimeException(e);
        }
    }

    private Class<?> findClassForTest(String nameClassForTest) {
        try {
            return Class.forName(nameClassForTest);
        } catch (ClassNotFoundException e) {
            System.out.printf("Класс с именем %s не найден\n", nameClassForTest);
            throw new RuntimeException(e);
        }
    }
}
