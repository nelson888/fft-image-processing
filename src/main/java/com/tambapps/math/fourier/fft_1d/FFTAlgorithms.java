package com.tambapps.math.fourier.fft_1d;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.ArrayVector;
import com.tambapps.math.util.PowerOfTwo;
import com.tambapps.math.util.Vector;

/**
 * Class that implements multiple 1D FFT algorithms
 */
public final class FFTAlgorithms {

  public static final FFTAlgorithm BASIC = new FFTAlgorithm() {
    @Override
    public void compute(Vector<Complex> vector) {
      basicFFT(vector);
    }

    @Override
    public String getName() {
      return "Basic";
    }

    @Override
    public String getDescription() {
      return "Compute the sums like in the basic FFT formula";
    }
  };

  //Cooley-Tukey algorithms (image sizes must be a power of two)
  public static final FFTAlgorithm CT_RECURSIVE = new FFTAlgorithm() {
    @Override
    public void compute(Vector<Complex> vector) {
      recursiveFFT(vector);
    }

    @Override
    public String getName() {
      return "Cooley-Tukey recursive";
    }

    @Override
    public String getDescription() {
      return "Cooley-Tukey algorithm implemented recursively (input array must have power of 2 sizes)";
    }
  };
  public static final FFTAlgorithm CT_ITERATIVE = new FFTAlgorithm() {
    @Override
    public void compute(Vector<Complex> vector) {
      iterativeFFT(vector);
    }

    @Override
    public String getName() {
      return "Cooley-Tukey iterative";
    }

    @Override
    public String getDescription() {
      return "Cooley-Tukey algorithm implemented iteratively (input array must have power of 2 sizes)";
    }
  };

  public static final FFTInverseAlgorithm INVERSE = FFTAlgorithms::inverse;

  private FFTAlgorithms() {}

  /**
   * The basic algorithm for the FFT
   *
   * @param vector the vector to compute
   * @param result the vector in which the computation will be made
   */
  public static void basicFFT(Vector<Complex> vector, Vector<Complex> result) {
    double N = vector.getSize();
    for (int k = 0; k < vector.getSize(); k++) {
      Complex sum = Complex.ZERO;
      for (int n = 0; n < vector.getSize(); n++) {
        sum = sum.plus(vector.getAt(n).multiply(Complex.expI(-2d * Math.PI * ((double) k) * ((double) n) / N)));
      }
      result.setAt(k, sum);
    }
  }

  private static void basicFFT(Vector<Complex> vector) {
    Vector<Complex> result = new ArrayVector<>(vector.getSize());
    basicFFT(vector, result);
    Vector.copy(result, vector);
  }

    /**
     * Compute the 1D FFT in the given vector
     * with the iterative Cooley-Tukey algorithm
     * The computation is made in the given vector
     * (not very precise for large 2D arrays)
     * @param vector the discrete function to compute the DFT
     * @link from https://rosettacode.org/wiki/Fast_Fourier_transform#Java
     */
  private static void iterativeFFT(Vector<Complex> vector) {
    int n = vector.getSize();

    int bits = PowerOfTwo.getExponent(n);
    bitReverseVector(vector, bits);

    for (int m = 2; m <= n; m <<= 1) {
      double dM = (double) m;
      for (int i = 0; i < n; i += m) {
        for (int k = 0; k < m / 2; k++) {
          int evenIndex = i + k;
          int oddIndex = i + k + (m / 2);
          Complex even = vector.getAt(evenIndex);
          Complex odd = vector.getAt(oddIndex);

          Complex wm = Complex.expI(-2d * Math.PI * k / dM).multiply(odd);
          vector.setAt(evenIndex, even.plus(wm));
          vector.setAt(oddIndex, even.minus(wm));
        }
      }
    }
  }

  private static int bitReversedIndex(int n, int bits) {
    int reversedN = n;
    int count = bits - 1;

    n >>= 1;
    while (n > 0) {
      reversedN = (reversedN << 1) | (n & 1);
      count--;
      n >>= 1;
    }

    return ((reversedN << count) & ((1 << bits) - 1));
  }

  private static void bitReverseVector(Vector<Complex> buffer, int bits) {
    for (int j = 1; j < buffer.getSize() / 2; j++) {
      int swapPos = bitReversedIndex(j, bits);
      Complex temp = buffer.getAt(j);
      buffer.setAt(j, buffer.getAt(swapPos));
      buffer.setAt(swapPos, temp);
    }
  }

  private static void recursiveFFT(Vector<Complex> vector) {
    Vector.copy(recursiveCopyFFT(vector), vector);
  }

  /**
   * Compute the FFT in the given vector
   * with the recursive Cooley-Tukey algorithm
   *
   * @param vector the discrete function to compute the DFT
   * @return the result FFT of the given vector
   * @link from https://rosettacode.org/wiki/Fast_Fourier_transform
   */
  private static Vector<Complex> recursiveCopyFFT(Vector<Complex> vector) {
    int N = vector.getSize();
    if (N <= 1) {
      return vector;
    }
    Vector<Complex> evens = recursiveCopyFFT(evensCopy(vector));
    Vector<Complex> odds = recursiveCopyFFT(oddsCopy(vector));

    Complex[] T = new Complex[N / 2];
    for (int i = 0; i < N / 2; i++) {
      T[i] = odds.getAt(i).multiply(Complex.expI(-2d * Math.PI * ((double) i) / ((double) N)));
    }

    Vector<Complex> result = new ArrayVector<>(N);
    for (int i = 0; i < N / 2; i++) {
      result.setAt(i, evens.getAt(i).plus(T[i]));
      result.setAt(i + N / 2, evens.getAt(i).minus(T[i]));
    }
    return result;
  }

  private static Vector<Complex> evensCopy(Vector<Complex> vector) {
    int size = (vector.getSize() + 1) / 2;

    Vector<Complex> copy = new ArrayVector<>(size);
    int count = 0;
    for (int i = 0; i < vector.getSize(); i += 2) {
      copy.setAt(count, vector.getAt(i));
      count++;
    }
    return copy;
  }

  private static Vector<Complex> oddsCopy(Vector<Complex> vector) {
    int size = vector.getSize() / 2;

    Vector<Complex> copy = new ArrayVector<>(size);
    int count = 0;
    for (int i = 1; i < vector.getSize(); i += 2) {
      copy.setAt(count, vector.getAt(i));
      count++;
    }
    return copy;
  }

  /**
   * Inverse a Fourier Transformed vector with a given FFT algorithm
   * @param vector the vector to inverse
   * @param algorithm the FFT algorithm used in the inverse
   */
  private static void inverse(Vector<Complex> vector, FFTAlgorithm algorithm) {
    for (int i = 0; i < vector.getSize(); i++) {
      vector.setAt(i, vector.getAt(i).conj());
    }

    algorithm.compute(vector);

    double iN = 1d / ((double) vector.getSize());
    for (int i = 0; i < vector.getSize(); i++) {
      vector.setAt(i, vector.getAt(i).conj().multiply(iN));
    }
  }
}
