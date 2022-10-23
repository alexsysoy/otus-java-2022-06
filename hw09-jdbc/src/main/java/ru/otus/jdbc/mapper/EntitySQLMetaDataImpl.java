package ru.otus.jdbc.mapper;


public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        String tableName = entityClassMetaData.getName();
        return String.format("select * from %s", tableName);
    }

    @Override
    public String getSelectByIdSql() {
        String key = entityClassMetaData.getIdField().getName();
        String firstColumnName = entityClassMetaData.getFieldsWithoutId().get(0).getName();
        String tableName = entityClassMetaData.getName();
        return String.format("select %s, %s from %s where %s  = ?", key, firstColumnName, tableName, key);
    }

    @Override
    public String getInsertSql() {
        String tableName = entityClassMetaData.getName();
        String firstColumnName = entityClassMetaData.getFieldsWithoutId().get(0).getName();
        return String.format("insert into %s(%s) values (?)", tableName, firstColumnName);
    }

    @Override
    public String getUpdateSql() {
        String key = entityClassMetaData.getIdField().getName();
        String firstColumnName = entityClassMetaData.getFieldsWithoutId().get(0).getName();
        String tableName = entityClassMetaData.getName();
        return String.format("update %s set %s = ? where %s = ?", tableName, firstColumnName, key);
    }
}
