package com.tambapps.math.util;

public class ArrayVector<T> extends AbstractVector<T> {

    private final Object[] objects;

    public ArrayVector(int size) {
        objects = new Object[size];
    }

    ArrayVector(T[] values) {
        this.objects = values;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getElement(int i) {
        return (T) objects[i];
    }

    @Override
    public void setElement(int i, T value) {
        objects[i] = value;
    }

    @Override
    public int getSize() {
        return objects.length;
    }

}
