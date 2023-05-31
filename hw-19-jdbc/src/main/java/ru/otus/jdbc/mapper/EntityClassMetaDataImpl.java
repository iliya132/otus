package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private Constructor<T> constructor;
    private Field idField;
    private List<Field> allFields;
    private List<Field> allButIdFields;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() {
        if (constructor == null) {
            var defaultConstructor = Arrays.stream(clazz.getConstructors())
                    .filter(it -> it.getParameterCount() == 0)
                    .findFirst()
                    .orElseThrow(
                            () -> new EntityClassMetaDataException("No default constructor found for entity " + getName())
                    );
            this.constructor = (Constructor<T>) defaultConstructor;
        }
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        if (this.idField == null) {
            this.idField = Arrays.stream(clazz.getDeclaredFields())
                    .filter(it -> it.isAnnotationPresent(Id.class))
                    .findFirst()
                    .orElseThrow(() -> new EntityClassMetaDataException("No id field found for entity " + getName()));
        }
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (this.allFields == null) {
            this.allFields = Arrays.stream(clazz.getDeclaredFields()).toList();
        }
        return this.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (this.allButIdFields == null) {
            this.allButIdFields = Arrays.stream(this.clazz.getDeclaredFields())
                    .filter(it -> !it.isAnnotationPresent(Id.class))
                    .toList();
        }
        return this.allButIdFields;
    }
}

class EntityClassMetaDataException extends RuntimeException {
    public EntityClassMetaDataException(String message) {
        super(message);
    }

    public EntityClassMetaDataException(Exception e) {
        super(e);
    }
}
