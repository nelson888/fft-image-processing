package com.tambapps.math.array_2d;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.Vector;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Array2DTest {

  private interface Initializer<T> {
    T init(int row, int col);
  }

  @Test
  public void complexRowColumnTest() {
    Initializer<Complex> complexInitializer = Complex::of;
    genericRowColumnTest(new Complex2DArray(3, 2), complexInitializer);
    genericRowColumnTest(new Complex2DArray(10, 10), complexInitializer);
  }

  @Test
  public void realRowColumnTest() {
    Initializer<Double> doubleInitializer = ((row, col) -> (double) (10 * row + col));
    genericRowColumnTest(new Double2DArray(3, 2), doubleInitializer);
    genericRowColumnTest(new Double2DArray(10, 10), doubleInitializer);
  }

  private <T> void genericRowColumnTest(Array2D<T> array, Initializer<T> initializer) {
    int M = array.getM(), N = array.getN();

    for (int j = 0; j < N; j++) {
      for (int i = 0; i < M; i++) {
        array.set(i, j, initializer.init(i, j));
      }
    }

    for (int j = 0; j < N; j++) {
      Vector<T> column = array.getColumn(j);
      for (int i = 0; i < M; i++) {
        assertEquals("Should be equal", initializer.init(i, j), column.getElement(i));
      }
    }

    for (int i = 0; i < M; i++) {
      Vector<T> row = array.getRow(i);
      for (int j = 0; j < N; j++) {
        assertEquals("Should be equal", initializer.init(i, j), row.getElement(j));
      }
    }
  }

  @Test
  public void realRowColumnSetTest() {
    genericRowColumnSetTest(new Double2DArray(4, 7), 2d, 3d);
  }

  @Test
  public void complexRowColumnSetTest() {
    genericRowColumnSetTest(new Complex2DArray(4, 7), Complex.of(2, 4), Complex.of(34, 10));
  }

  public <T> void genericRowColumnSetTest(Array2D<T> array, T valueCol, T valueRow) {
    Vector<T> column = array.getColumn(array.getN() / 2);
    for (int i = 0; i < column.getSize(); i++) {
      column.setElement(i, valueCol);
    }

    for (int j = 0; j < array.getM(); j++) {
      assertEquals("Should be equal", valueCol, array.get(j, array.getN() / 2));
    }

    Vector<T> row = array.getRow(array.getM() / 2);
    for (int i = 0; i < row.getSize(); i++) {
      row.setElement(i, valueRow);
    }

    for (int j = 0; j < array.getN(); j++) {
      assertEquals("Should be equal", valueRow, array.get(array.getM() / 2, j));
    }
  }

}
