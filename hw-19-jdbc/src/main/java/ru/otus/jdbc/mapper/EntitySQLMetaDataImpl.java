package ru.otus.jdbc.mapper;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String tableName;
    private final EntityClassMetaData<?> entityClassMetaData;

    private String updateSqlTemplateCache;
    private String selectByIdSqlTemplateCache;
    private String insertSqlTemplateCache;

    public EntitySQLMetaDataImpl(String tableName, EntityClassMetaData<?> entityClassMetaData) {

        this.tableName = tableName;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + tableName + ";";
    }

    @Override
    public String getSelectByIdSql() {
        if (selectByIdSqlTemplateCache == null) {
            selectByIdSqlTemplateCache = "select %s from %s where %s = ?;".formatted(getColumnNamesCsv(),
                    tableName,
                    getKeyColumnName());
        }
        return selectByIdSqlTemplateCache;
    }

    @Override
    public String getInsertSql() {
        if (insertSqlTemplateCache == null) {
            insertSqlTemplateCache =
                    "insert into %s (%s) values (%s);".formatted(tableName,
                            getColumnWithoutKeyNamesCsv(),
                            getAllFieldsSqlValues());
        }
        return insertSqlTemplateCache;
    }

    @Override
    public String getUpdateSql() {
        if (updateSqlTemplateCache == null) {
            updateSqlTemplateCache = "update %s set %s;".formatted(tableName, getAllColumnNamesForUpdate());
        }
        return updateSqlTemplateCache;
    }

    private String getColumnNamesCsv() {
        return String.join(", ", entityClassMetaData.getAllColumnsByFields().keySet().stream().toList());
    }
    private String getColumnWithoutKeyNamesCsv() {
        return String.join(", ", entityClassMetaData.getAllColumnsWithoutKeyByFields().keySet().stream().toList());
    }

    private String getKeyColumnName() {
        return entityClassMetaData.getIdColumnName();
    }

    private String getAllFieldsSqlValues() {
        return String.join(", ",
                entityClassMetaData
                        .getFieldsWithoutId()
                        .stream()
                        .map(it -> "?")
                        .toList());
    }

    private String getAllColumnNamesForUpdate() {
        return String.join(", ",
                entityClassMetaData.getAllColumnsByFields().keySet().stream().map(it -> it + " = ?").toList());
    }
}
