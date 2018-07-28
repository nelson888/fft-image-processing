package com.tambapps.math.fourier.fft_1d;

import static org.junit.Assert.assertEquals;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.ArrayVector;
import com.tambapps.math.util.Vector;

import org.junit.Test;

public class FastFourierTransformTest {

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
    FastFourierTransform.basicFFT(input, result);
    assertEquals("Should be equal", expected, result);
  }

  @Test
  public void recursiveTest() {
    Vector<Complex> result = FastFourierTransform.recursiveCopyFFT(input);
    assertEquals("Should be equal", expected, result);
  }

  @Test
  public void iterativeTest() {
    Vector<Complex> result = new ArrayVector<>(input.getSize());
    Vector.copy(input, result);
    FastFourierTransform.iterativeFFT(result);
    assertEquals("Should be equal", expected, result);
  }

}
