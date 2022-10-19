package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Cloneable {
    private List<String> data;

    public ObjectForMessage() {
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage clone() {
        try {
            ObjectForMessage clone = (ObjectForMessage) super.clone();
            List<String> cloneData = new ArrayList<>(data);
            clone.setData(cloneData);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
