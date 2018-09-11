package com.californiadreamshostel.officetv.Weather.model;

public class Initializer<T> {
    WeatherDataProxy.DATA_BLOCK type;
    String fieldName;
    T value; //The value to assign to Field.
    long timestamp;//millis
    public Initializer() {
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public WeatherDataProxy.DATA_BLOCK getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public T getValue() {
        return value;
    }

    public void setType(WeatherDataProxy.DATA_BLOCK type) {
        this.type = type;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
