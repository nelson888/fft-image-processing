package com.tambapps.math.util;

public interface Vector<T> {

  T getAt(int i);

  void setAt(int i, T value);

  int getSize();

  @SafeVarargs
  static <T> Vector<T> of(T... values) {
    return new ImmutableVector<>(values);
  }

  static <T> void copy(Vector<T> src, Vector<T> dst) {
    if (src.getSize() != dst.getSize()) {
      throw new IllegalArgumentException("Both vectors should have the same size");
    }
    for (int i = 0; i < src.getSize(); i++) {
      dst.setAt(i, src.getAt(i));
    }
  }

}
