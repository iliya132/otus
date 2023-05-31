package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> metaData;
    private String allColumnNamesCsv;
    private String allColumnButIdNamesCsv;
    private String allColumnButIdsMarkersCsv;
    private String idName;
    private String allColumnsForUpdate;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        this.metaData = metaData;
    }

    private String getTableName() {
        return metaData.getName().toLowerCase();
    }

    @Override
    public String getSelectAllSql() {
        return "select " + getAllColumnNamesCsv() + " from " + getTableName() + ";";
    }

    @Override
    public String getSelectByIdSql() {
        return "select " + getAllColumnNamesCsv() + " from " + getTableName() + " where " + getIdName() + " = ?;";
    }

    private String getIdName() {
        if (this.idName == null) {
            this.idName = getColumnName(metaData.getIdField());
        }
        return this.idName;
    }

    private String getColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).value();
        } else {
            return field.getName();
        }
    }

    @Override
    public String getInsertSql() {
        return "insert into " + getTableName() + " (" + getAllColumnButIdNamesCsv() + ") values (" +
                getAllColumnButIdsMarkersCsv() + ");";
    }

    @Override
    public String getUpdateSql() {
        return "update " + getTableName() + " set " + getAllColumnsForUpdate() + " where " + getIdName() + " = ?";
    }

    private String getAllColumnNamesCsv() {
        if (allColumnNamesCsv == null) {
            this.allColumnNamesCsv = metaData
                    .getAllFields()
                    .stream()
                    .map(this::getColumnName)
                    .collect(Collectors.joining(", "));
        }
        return this.allColumnNamesCsv;
    }

    private String getAllColumnButIdNamesCsv() {
        if (allColumnButIdNamesCsv == null) {
            this.allColumnButIdNamesCsv = metaData.getAllFields().stream()
                    .filter(it -> !it.isAnnotationPresent(Id.class))
                    .map(this::getColumnName)
                    .collect(Collectors.joining(", "));
        }
        return this.allColumnButIdNamesCsv;
    }

    private String getAllColumnButIdsMarkersCsv() {
        if (allColumnButIdsMarkersCsv == null) {
            this.allColumnButIdsMarkersCsv = metaData.getAllFields().stream()
                    .filter(it -> !it.isAnnotationPresent(Id.class))
                    .map(it -> "?")
                    .collect(Collectors.joining(", "));
        }
        return this.allColumnButIdsMarkersCsv;
    }

    private String getAllColumnsForUpdate() {
        if(allColumnsForUpdate == null){
            allColumnsForUpdate = metaData.getAllFields()
                    .stream()
                    .map(it -> getColumnName(it) + " = ?")
                    .collect(Collectors.joining(", "));
        }
        return this.allColumnsForUpdate;
    }
}
