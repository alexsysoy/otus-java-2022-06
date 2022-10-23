package ru.otus.jdbc.mapper;

import ru.otus.jdbc.mapper.annotation.EntityId;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase(Locale.ROOT);
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            List<Class<?>> listParameters = new ArrayList<>();
            for (Field field : getAllFields(clazz)) {
                listParameters.add(field.getType());
            }
            Class<?>[] parameters = listParameters.toArray(Class[]::new);
            return clazz.getDeclaredConstructor(parameters);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Exception in method getConstruction", e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(EntityId.class))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<Field> getAllFields() {
        return getAllFields(clazz);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getFieldsWithoutId(clazz);
    }

    private List<Field> getAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
    }

    private List<Field> getFieldsWithoutId(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(EntityId.class))
                .collect(Collectors.toList());
    }
}
