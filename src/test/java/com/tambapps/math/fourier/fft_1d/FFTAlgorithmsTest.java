package com.tambapps.math.fourier.fft_1d;

import static org.junit.Assert.assertEquals;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.ArrayVector;
import com.tambapps.math.util.Vector;

import org.junit.Test;

public class FFTAlgorithmsTest {

  private final static Complex ONE = Complex.of(1);
  private final Vector<Complex> input = Vector.of(ONE,
      ONE,
      ONE,
      ONE,
      Complex.ZERO,
      Complex.ZERO,
      Complex.ZERO,
      Complex.ZERO);

  private final Vector<Complex> expected = Vector.of(Complex.of(4),
      Complex.of(1d, -2.414214),
      Complex.ZERO,
      Complex.of(1, -0.414214),
      Complex.ZERO,
      Complex.of(1, 0.414214),
      Complex.ZERO,
      Complex.of(1, 2.414214));

  @Test
  public void basicTest() {
    Vector<Complex> result = new ArrayVector<>(input.getSize());
    FFTAlgorithms.basicFFT(input, result);
    assertEquals("Should be equal", expected, result);
  }

  @Test
  public void recursiveTest() {
    Vector<Complex> result = new ArrayVector<>(input.getSize());
    Vector.copy(input, result);
    FFTAlgorithms.CT_RECURSIVE.compute(result);
    assertEquals("Should be equal", expected, result);
  }

  @Test
  public void iterativeTest() {
    Vector<Complex> result = new ArrayVector<>(input.getSize());
    Vector.copy(input, result);
    FFTAlgorithms.CT_ITERATIVE.compute(result);
    assertEquals("Should be equal", expected, result);
  }

  @Test
  public void inverseTest() {
    for (FFTAlgorithm algorithm : new FFTAlgorithm[] {FFTAlgorithms.CT_ITERATIVE,
        FFTAlgorithms.BASIC, FFTAlgorithms.CT_RECURSIVE}) {
      Vector<Complex> result = new ArrayVector<>(expected.getSize());
      Vector.copy(expected, result);
      FFTAlgorithms.INVERSE.compute(result, algorithm);
      assertEquals("Should be equal", input, result);
    }
  }

}