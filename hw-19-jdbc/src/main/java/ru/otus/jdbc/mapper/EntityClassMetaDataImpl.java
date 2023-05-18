package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private Constructor<T> constructorCache;
    private Field idField;
    private List<Field> allFields;
    private List<Field> allFieldsWithoutId;
    private final Map<String, Field> columnsByFields = new HashMap<>();
    private final Map<String, Field> columnsByFieldsWithoutKey = new HashMap<>();
    private String idColumnName;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        initializeMetaData();
    }

    private void initializeMetaData() {
        allFields = Arrays.stream(clazz.getDeclaredFields()).toList();

        idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow();

        idColumnName = idField.getAnnotation(Column.class).value();

        allFields.forEach(it -> {
            var columnAnnotation = it.getAnnotation(Column.class);
            if (columnAnnotation == null) {
                columnsByFields.put(it.getName(), it);
                if(!it.isAnnotationPresent(Id.class)){
                    columnsByFieldsWithoutKey.put(it.getName(), it);
                }
            } else {
                columnsByFields.put(columnAnnotation.value(), it);
                if(!it.isAnnotationPresent(Id.class)){
                    columnsByFieldsWithoutKey.put(columnAnnotation.value(), it);
                }
            }
        });

        allFieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> !it.isAnnotationPresent(Id.class))
                .toList();

        constructorCache = getDefaultConstructorOrThrow();
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructorCache;
    }

    @SuppressWarnings("unchecked")
    private Constructor<T> getDefaultConstructorOrThrow() {
        return (Constructor<T>) Arrays.stream(clazz.getDeclaredConstructors())
                .filter(it -> it.getParameterCount() == 0)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public String getIdColumnName() {
        return idColumnName;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public Map<String, Field> getAllColumnsByFields() {
        return columnsByFields;
    }

    public Map<String, Field> getAllColumnsWithoutKeyByFields() {
        return columnsByFieldsWithoutKey;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return allFieldsWithoutId;
    }
}
