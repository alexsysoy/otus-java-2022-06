package dto;

import java.lang.reflect.Method;
import java.util.List;

public record TestsResources(Class<?> clazz, Method before, Method after, List<Method> tests) {
}
