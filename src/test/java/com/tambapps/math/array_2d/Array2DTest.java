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
    genericTest(new Complex2DArray(3, 2), complexInitializer);
    genericTest(new Complex2DArray(10, 10), complexInitializer);
  }

  @Test
  public void realRowColumnTest() {
    Initializer<Double> doubleInitializer = ((row, col) -> (double) (10 * row + col));
    genericTest(new Double2DArray(3, 2), doubleInitializer);
    genericTest(new Double2DArray(10, 10), doubleInitializer);
  }

  private <T> void genericTest(Array2D<T> array, Initializer<T> initializer) {
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

}
