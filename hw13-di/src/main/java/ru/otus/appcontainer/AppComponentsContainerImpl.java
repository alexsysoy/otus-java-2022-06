package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exception.AppException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object component : appComponents) {
            if (Arrays.stream(component.getClass().getInterfaces()).anyMatch(inter -> inter == componentClass)) {
                return (C) component;
            }
            if (component.getClass() == componentClass) {
                return (C) component;
            }
        }
        throw new AppException(String.format("There isn't component with class: %s", componentClass.getName()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        var result = appComponentsByName.get(componentName);
        if (result != null) {
            return (C) result;
        }
        throw new AppException(String.format("There isn't component with name: %s", componentName));
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var configMethods = new PriorityQueue<Method>(Comparator.comparing(o -> o.getAnnotation(AppComponent.class).order()));

        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .forEach(configMethods::add);

        fillComponents(configMethods, createConfigInstance(configClass));
    }

    private void fillComponents(PriorityQueue<Method> methods, Object configInstance) {
        Method configMethod;
        Object[] args;
        while (!methods.isEmpty()) {
            configMethod = methods.poll();
            args = new Object[configMethod.getParameterCount()];

            var params = configMethod.getParameters();
            for (int i = 0; i < configMethod.getParameterCount(); i++) {
                args[i] = getComponent(params[i].getType());
            }
            createComponentInContext(configInstance, configMethod, args);
        }
    }

    private Object createConfigInstance(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void createComponentInContext(Object configInstance, Method configMethod, Object... args) {
        try {
           Object componentInstance;
            componentInstance = configMethod.invoke(configInstance, args);
            var componentName = configMethod.getAnnotation(AppComponent.class).name();
            checkNotDuplicateComponentName(componentName);

            if (isContainMoreThenOneElement(componentInstance)) {
                Iterator<Object> iterator = appComponents.iterator();
                Object temp;
                while (iterator.hasNext()) {
                    temp = iterator.next();
                    if (temp.getClass() == componentInstance.getClass()) {
                        iterator.remove();
                    }
                }
                System.out.println("FFF" + appComponents);
            } else {
                appComponents.add(componentInstance);
                appComponentsByName.put(componentName, componentInstance);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkNotDuplicateComponentName(String name) {
        if (appComponentsByName.containsKey(name)) {
            throw new AppException(String.format("Component with name %s is present in the context", name));
        }
    }

    private boolean isContainMoreThenOneElement(Object componentInstance) {
        for (Object component : appComponents) {
            if (component.getClass() == componentInstance.getClass()) {
                return true;
            }
        }
        return false;
    }

    private Object getComponent(Class<?> clazz) {
        for (Object component : appComponents) {
            if (Arrays.asList(component.getClass().getInterfaces()).contains(clazz)) {
                return component;
            }
        }
        throw new AppException("Get component exception");
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
