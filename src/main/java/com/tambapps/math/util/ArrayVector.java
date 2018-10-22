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
  public T getAt(int i) {
    checkIndex(i);
    return (T) objects[i];
  }

  @Override
  public void setAt(int i, T value) {
    checkIndex(i);
    objects[i] = value;
  }

  @Override
  public int getSize() {
    return objects.length;
  }

}
