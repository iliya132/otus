package ru.otus.model;

import ru.otus.model.interfaces.Copyable;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Copyable<ObjectForMessage> {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage copy() {
        var copy = new ObjectForMessage();
        copy.setData(new ArrayList<>(this.data));
        return copy;
    }
}
