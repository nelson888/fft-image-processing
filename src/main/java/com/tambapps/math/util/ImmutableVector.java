package com.tambapps.math.util;

public class ImmutableVector<T> extends ArrayVector<T> {

  ImmutableVector(T[] values) {
    super(values);
  }

  @Override
  public void setElement(int i, T value) {
    throw new UnsupportedOperationException("Cannot modify immutable vector");
  }

}
