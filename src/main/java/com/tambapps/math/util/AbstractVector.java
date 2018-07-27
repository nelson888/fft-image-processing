package com.tambapps.math.util;

import java.util.Objects;

public abstract class AbstractVector<T> implements Vector<T> {

  @Override
  public boolean equals(Object o) {
    if (o == null || !o.getClass().equals(getClass())) {
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
}
