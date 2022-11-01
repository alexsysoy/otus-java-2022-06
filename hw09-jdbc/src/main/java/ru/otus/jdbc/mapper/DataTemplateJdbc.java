package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.annotation.EntityId;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохраяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final Class<T> clazz;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, Class<T> clazz) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return setFieldResultObject(getResultObjectInstance(), rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        var result = dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            try {
                var list = new ArrayList<T>();
                while (rs.next()) {
                    list.add(setFieldResultObject(getResultObjectInstance(), rs));
                }
                return list;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
        return result.orElseThrow();
    }

    @Override
    public long insert(Connection connection, T client) {
        Object value = null;
        for (Field field : client.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(EntityId.class)) {
                field.setAccessible(true);
                try {
                    value = field.get(client);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), Collections.singletonList(value));
    }

    @Override
    public void update(Connection connection, T client) {
        Object value = null;
        Long key = null;
        for (Field field : client.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EntityId.class)) {
                field.setAccessible(true);
                try {
                    key = (Long) field.get(client);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!field.isAnnotationPresent(EntityId.class)) {
                field.setAccessible(true);
                try {
                    value = field.get(client);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), List.of(value, key));
    }

    private T getResultObjectInstance() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Exception in create instance method", e);
        }
    }

    private T setFieldResultObject(T object, ResultSet rs) {
        try {
            Field fieldInObject;
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                fieldInObject = object.getClass().getDeclaredField(rs.getMetaData().getColumnName(i));
                fieldInObject.setAccessible(true);
                fieldInObject.set(object, rs.getObject(i));
            }
            return object;
        } catch (Exception e) {
            throw new RuntimeException("Exception in setting fields", e);
        }
    }
}
