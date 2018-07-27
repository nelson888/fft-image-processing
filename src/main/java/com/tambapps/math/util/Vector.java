package com.tambapps.math.util;

public interface Vector<T> {

  T getElement(int i);
  void setElement(int i, T value);
  int getSize();

}
