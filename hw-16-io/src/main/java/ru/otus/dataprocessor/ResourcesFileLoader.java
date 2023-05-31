package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementMixin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper objectMapper;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.addMixIn(Measurement.class, MeasurementMixin.class);
    }

    @Override
    public List<Measurement> load() {
        try {
            File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
            return objectMapper.readerForListOf(Measurement.class).readValue(file);
        } catch (IOException | NullPointerException e) {
            throw new FileProcessException(e);
        }
    }
}
