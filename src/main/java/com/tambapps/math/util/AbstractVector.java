package com.tambapps.math.util;

import java.util.Objects;

public abstract class AbstractVector<T> implements Vector<T> {

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Vector)) {
      return false;
    }
    Vector v = (Vector) o;
    if (v.getSize() != getSize()) {
      return false;
    }

    for (int i = 0; i < getSize(); i++) {
      if (!Objects.equals(getAt(i), v.getAt(i))) {
        return false;
      }
    }
    return true;
  }

  void checkIndex(int i) {
    if (i < 0 || i >= getSize()) {
      throw new IndexOutOfBoundsException(String.format("Tried to access index %d of vector with size %d", i, getSize()));
    }
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder().append("[ ");
    for (int i = 0; i < getSize(); i++) {
      stringBuilder.append(getAt(i));
      if (i < getSize() - 1) {
        stringBuilder.append(", ");
      }
    }
    stringBuilder.append(" ]");
    return stringBuilder.toString();
  }

}
