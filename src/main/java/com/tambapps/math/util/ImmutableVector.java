package com.tambapps.math.util;

public class ImmutableVector<T> extends AbstractVector<T> {
  private final T[] values;
  ImmutableVector(T[] values) {
    this.values = values;
  }

  @Override
  public T getElement(int i) {
    checkIndex(i);
    return values[i];
  }

  @Override
  public void setElement(int i, T value) {
    throw new UnsupportedOperationException("Cannot modify immutable vector");
  }

  @Override
  public int getSize() {
    return values.length;
  }


}
