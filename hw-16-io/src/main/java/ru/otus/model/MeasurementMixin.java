package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MeasurementMixin {

    @JsonCreator
    public MeasurementMixin(@JsonProperty("name") String name, @JsonProperty("value") double value) {

    }
}