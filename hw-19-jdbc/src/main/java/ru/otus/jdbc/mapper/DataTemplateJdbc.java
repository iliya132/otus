package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> metaData;
    private final Map<String, String> fieldToColumnName = new HashMap<>();

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> metaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.metaData = metaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        var sql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, sql, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    var newInstance = createNewInstance();
                    metaData.getAllFields().forEach(field -> setValueFromResultSet(field, newInstance, rs));
                    return newInstance;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new DataTemplateJdbcException(e);
            }
        });
    }

    private void setValueFromResultSet(Field field, Object object, ResultSet resultSet) {
        var columnName = getFieldName(field);
        try {
            var res = resultSet.getObject(columnName);
            field.set(object, res);
        } catch (SQLException | IllegalAccessException e) {
            throw new DataTemplateJdbcException(e);
        }
    }

    private String getFieldName(Field field) {
        var cached = fieldToColumnName.get(field.getName());
        if (cached == null) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) {
                fieldToColumnName.put(field.getName(), field.getAnnotation(Column.class).value());
            } else {
                fieldToColumnName.put(field.getName(), field.getName());
            }
        }
        return fieldToColumnName.get(field.getName());
    }

    private T createNewInstance() {
        try {
            return metaData.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new DataTemplateJdbcException(e);
        }
    }

    @Override
    public List<T> findAll(Connection connection) {
        var sql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor.executeSelect(connection, sql, List.of(), resultSet -> {
            var result = new ArrayList<T>();
            try {
                while (resultSet.next()) {
                    var newInstance = createNewInstance();
                    metaData.getAllFields().forEach(field -> setValueFromResultSet(field, newInstance, resultSet));
                    result.add(newInstance);
                }
                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).orElse(new ArrayList<>());
    }

    @Override
    public long insert(Connection connection, T client) {
        var sql = entitySQLMetaData.getInsertSql();
        var params = new ArrayList<>();
        metaData.getFieldsWithoutId().forEach(field -> {
            field.setAccessible(true);
            try {
                params.add(field.get(client));
            } catch (IllegalAccessException e) {
                throw new DataTemplateJdbcException(e);
            }
        });
        return dbExecutor.executeStatement(connection, sql, params);
    }

    private List<Object> extractValues(T entity) {
        var result = new ArrayList<Object>();
        metaData.getAllFields().forEach(field -> {
            field.setAccessible(true);
            try {
                result.add(field.get(entity));
            } catch (IllegalAccessException e) {
                throw new DataTemplateJdbcException(e);
            }
        });
        return result;
    }

    @Override
    public void update(Connection connection, T client) {
        var sql = entitySQLMetaData.getUpdateSql();
        var keyValue = getKeyValue(client);
        var params = extractValues(client);
        params.add(keyValue);
        dbExecutor.executeStatement(connection, sql, params);
    }

    private Object getKeyValue(T entity) {
        try {
            return metaData.getIdField().get(entity);
        } catch (IllegalAccessException e) {
            throw new DataTemplateJdbcException(e);
        }
    }
}

class DataTemplateJdbcException extends RuntimeException {
    public DataTemplateJdbcException(Exception e) {
        super(e);
    }
}
