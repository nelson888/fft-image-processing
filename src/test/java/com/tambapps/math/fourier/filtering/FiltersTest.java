package com.tambapps.math.fourier.filtering;

import static org.junit.Assert.assertEquals;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import org.junit.Test;

public class FiltersTest {

  @Test
  public void retangleFilterTest() {
    int N = 10;
    Complex2DArray array = new Complex2DArray(N, N);
    for (int i = 0; i < array.getN() * array.getM(); i++) {
      array.set(i, Complex.of(i));
    }

    Filters.rectangle(1, 1, false).apply(array);

    Complex2DArray expected = Complex2DArray.copy(array);
    for (int i = 0; i < N; i++) {
      expected.set(i, 0, Complex.ZERO);
      expected.set(0, i, Complex.ZERO);
      expected.set(i, N - 1, Complex.ZERO);
      expected.set(N - 1, i, Complex.ZERO);
    }

    assertEquals("Should be equal", expected, array);
  }

}
