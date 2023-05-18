package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> classMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> classMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.classMetaData = classMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                this::readResultSet);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectAllSql(),
                List.of(),
                this::readResultSetAsList).orElseThrow();
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                getValueList(client));
    }

    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getUpdateSql(),
                getValueList(client));
    }

    private T readResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                var newInstance = classMetaData.getConstructor().newInstance();
                setValues(newInstance, rs);
                return newInstance;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> readResultSetAsList(ResultSet rs) {
        try {
            var result = new ArrayList<T>();
            while (rs.next()) {
                var newInstance = classMetaData.getConstructor().newInstance(List.of());
                setValues(newInstance, rs);
                result.add(newInstance);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> getValueList(T object) {
        List<Object> values = new ArrayList<>();
        for (Field field : classMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                values.add(field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    private void setValues(Object object, ResultSet rs) {
        for (var entry : classMetaData.getAllColumnsByFields().entrySet()) {
            var field = entry.getValue();
            var columnName = entry.getKey();
            try {
                field.setAccessible(true);
                switch (field.getType().getSimpleName()) {
                    case "int", "Integer" -> field.set(object, rs.getInt(columnName));
                    case "long", "Long" -> field.set(object, rs.getLong(columnName));
                    case "char", "String" -> field.set(object, rs.getString(columnName));
                    default -> throw new RuntimeException("Unknown field type");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
