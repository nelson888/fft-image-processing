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
      if (!Objects.equals(getElement(i), v.getElement(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder().append("[ ");
    for (int i = 0; i < getSize(); i++) {
      stringBuilder.append(getElement(i));
      if (i < getSize() - 1) {
        stringBuilder.append(", ");
      }
    }
    stringBuilder.append(" ]");
    return stringBuilder.toString();
  }
}
