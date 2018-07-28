package com.tambapps.math.fourier.fft_1d;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.ArrayVector;
import com.tambapps.math.util.PowerOfTwo;
import com.tambapps.math.util.Vector;

/**
 * Class that implements multiple 1D FFT algorithms
 */
public class FastFourierTransform {

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
        sum = sum.add(vector.getElement(n).mul(Complex.expI(-2d * Math.PI * ((double) k) * ((double) n) / N)));
      }
      result.setElement(k, sum);
    }
  }

  /**
   * Compute the 1D FFT in the given vector
   * with the iterative Cooley-Tukey algorithm
   * The computation is made in the given vector
   *
   * @param vector the discrete function to compute the DFT
   * @link from https://rosettacode.org/wiki/Fast_Fourier_transform#Java
   */
  public static void iterativeFFT(Vector<Complex> vector) { //FIXME WORKS FOR 1D BUT NOT FOR 2D
    int n = vector.getSize();

    int bits = PowerOfTwo.getExponent(n);
    bitReverseVector(vector, bits);

    for (int m = 2; m <= n; m <<= 1) {
      double dM = (double) m;
      for (int i = 0; i < n; i += m) {
        for (int k = 0; k < m / 2; k++) {
          int evenIndex = i + k;
          int oddIndex = i + k + (m / 2);
          Complex even = vector.getElement(evenIndex);
          Complex odd = vector.getElement(oddIndex);

          Complex wm = Complex.expI(-2d * Math.PI * k / dM).mul(odd);
          vector.setElement(evenIndex, even.add(wm));
          vector.setElement(oddIndex, even.sub(wm));
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
      Complex temp = buffer.getElement(j);
      buffer.setElement(j, buffer.getElement(swapPos));
      buffer.setElement(swapPos, temp);
    }
  }

  public static void inverse(Vector<Complex> vector) {
    for (int i = 0; i < vector.getSize(); i++) {
      vector.setElement(i, vector.getElement(i).conj());
    }

    iterativeFFT(vector);

    double iN = 1d / ((double) vector.getSize());
    for (int i = 0; i < vector.getSize(); i++) {
      vector.setElement(i, vector.getElement(i).conj().scl(iN));
    }
  }

  public static void recursiveFFT(Vector<Complex> vector) {
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
  public static Vector<Complex> recursiveCopyFFT(Vector<Complex> vector) {
    int N = vector.getSize();
    if (N <= 1) {
      return vector;
    }
    Vector<Complex> evens = recursiveCopyFFT(evensCopy(vector));
    Vector<Complex> odds = recursiveCopyFFT(oddsCopy(vector));

    Complex[] T = new Complex[N / 2];
    for (int i = 0; i < N / 2; i++) {
      T[i] = odds.getElement(i).mul(Complex.expI(-2d * Math.PI * ((double) i) / ((double) N)));
    }

    Vector<Complex> result = new ArrayVector<>(N);
    for (int i = 0; i < N; i++) {
      if (i < N / 2) {
        result.setElement(i, evens.getElement(i).add(T[i]));
      } else {
        result.setElement(i, evens
            .getElement(i - N / 2).sub(T[i - N / 2]));
      }
    }
    return result;
  }

  private static Vector<Complex> evensCopy(Vector<Complex> vector) {
    int size = vector.getSize() / 2;
    if (vector.getSize() % 2 != 0) {
      size++;
    }
    Vector<Complex> copy = new ArrayVector<>(size);
    int count = 0;
    for (int i = 0; i < vector.getSize(); i += 2) {
      copy.setElement(count, vector.getElement(i));
      count++;
    }
    return copy;
  }

  private static Vector<Complex> oddsCopy(Vector<Complex> vector) {
    int size = vector.getSize() / 2;
    if (vector.getSize() % 2 != 0) {
      size++;
    }
    Vector<Complex> copy = new ArrayVector<>(size);
    int count = 0;
    for (int i = 1; i < vector.getSize(); i += 2) {
      copy.setElement(count, vector.getElement(i));
      count++;
    }
    return copy;
  }

}
