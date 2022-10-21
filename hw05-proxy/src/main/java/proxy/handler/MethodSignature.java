package proxy.handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class MethodSignature {
    public String name;
    public Class<?>[] params;

    public MethodSignature(Method method) {
        this.name = method.getName();
        this.params = method.getParameterTypes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodSignature that)) return false;
        return name.equals(that.name) && Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "MethodSignature{" +
                "name='" + name + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
