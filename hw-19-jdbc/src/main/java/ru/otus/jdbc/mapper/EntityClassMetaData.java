package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * "Разбирает" объект на составные части
 */
public interface EntityClassMetaData<T> {
    String getName();

    Constructor<T> getConstructor();

    //Поле Id должно определять по наличию аннотации Id
    //Аннотацию @Id надо сделать самостоятельно
    Field getIdField();
    String getIdColumnName();

    List<Field> getAllFields();
    Map<String, Field> getAllColumnsByFields();
    Map<String, Field> getAllColumnsWithoutKeyByFields();

    List<Field> getFieldsWithoutId();
}
