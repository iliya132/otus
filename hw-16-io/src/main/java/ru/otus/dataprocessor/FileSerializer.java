package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class FileSerializer implements Serializer {

    private final String fileName;
    private final ObjectMapper objectMapper;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (var outStream = new BufferedOutputStream(new FileOutputStream(fileName))) {
            var sortedData = new TreeMap<>(data);
            var json = objectMapper.writeValueAsString(sortedData);
            outStream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
