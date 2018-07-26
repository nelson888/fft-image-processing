package com.tambapps.math.fourier.fft_2d;

import com.tambapps.math.array_2d.Array2D;
import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.array_2d.Integer2DArray;
import com.tambapps.math.complex.Complex;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

//TODO TO IMPLEMENT This shows that a 2D FFT can be broken down into a series of 1D Fourier transforms.
// To compute a 2D FFT, 1D Fourier transform is applied to each individual row of the input matrix and then to each column.
//TODO faire des fonctions qui aggrandissent l'image comme dans le TP (permet une plus grande precision)
public class FastFourierTransformer2D {
  private final int maxThreads;
  private final ExecutorCompletionService<Void> executorService;

  public FastFourierTransformer2D(ExecutorService executor, int maxThreads) {
    executorService = new ExecutorCompletionService<>(executor);
    this.maxThreads = maxThreads;
  }

  public Complex2DArray transform(Integer2DArray f) {
    int M = f.getM();
    int N = f.getN();
    int total = N * M;
    int from = 0;
    int frame = total / maxThreads;
    int tasks = 0;

    Complex2DArray fft = new Complex2DArray(M, N);

    while (from < total) {
      executorService.submit(new TransformTask(M, N, f, fft, from, Math.min(from + frame, total)));
      tasks++;
      from += frame;
    }

    for (int i = 0; i < tasks; i++) {
      try {
        executorService.take();
      } catch (InterruptedException e) {
        e.printStackTrace();
        return null;
      }
    }

    return fft;
  }


  /**
   * Task that will do some computation between 'from' and 'to'
   * ('from' and 'to' represent the 2D array indexes in 1D)
   */
  private abstract class FourierTask<T> implements Callable<Void> {
    private final int M;
    private final int N;
    private final Array2D<T> input;
    private final Complex2DArray output;
    private final int from;
    private final int to;

    /**
     * @param input the input of the task
     * @param output the result of the task
     * @param from the starting index in 1D
     * @param to the ending index in 1D (excluded)
     */
    FourierTask(int m, int n, Array2D<T> input, Complex2DArray output, int from, int to) {
      M = m;
      N = n;
      this.input = input;
      this.output = output;
      this.from = from;
      this.to = to;
    }

    @Override
    public final Void call() {
      final double dM = (double)M;
      final double dN = (double)N;

      //transforming the 1D in 2D
      int i = from % M;
      int j = from / M;

      while (j * M + i < to) {
        output.set(i, j, computePoint(dM, dN, input, i, j));
        if (i == M - 1) {
          i = 0;
          j++;
        } else {
          i++;
        }
      }
      return null;
    }

    abstract Complex computePoint(double M, double N, Array2D<T> f, int m, int n);
  }

  /**
   * Task that will compute the FFT  between 'from' and 'to'
   */
  private class TransformTask extends FourierTask<Integer> {

    /**
     * @param fft the result fft
     * @param f the function to transform
     * @param from the starting index in 1D
     * @param to the ending index in 1D (excluded)
     */
    TransformTask(int M, int N, Integer2DArray f, Complex2DArray fft, int from, int to) {
      super(M, N, f, fft, from, to);
    }

    @Override
    Complex computePoint(double M, double N, Array2D<Integer> f, int m, int n) {
      Complex result = Complex.ZERO;
      for (int i = 0; i < f.getM(); i++) {
        for (int j=0; j < f.getN(); j++) {
          result = result.add(Complex.expI(-2 * Math.PI * (i*m/M + j*n/N)).scl(f.get(i,j))); //TODO to simply (to much complex creation)
        }
      }

      return result;
    }
  }

  /**
   * Task that will compute the inverse of the given FFT  between 'from' and 'to'
   */
  private class InverseTask extends FourierTask<Complex> {

    InverseTask(int M, int N, Complex2DArray fft, Complex2DArray f, int from, int to) {
      super(M, N, fft, f, from, to);
    }

    @Override
    Complex computePoint(double M, double N, Array2D<Complex> fft, int m, int n) {
      Complex result = Complex.ZERO;
      for (int i = 0; i < fft.getM(); i++) {
        for (int j=0; j < fft.getN(); j++) {
          result = result.add(Complex.expI(2 * Math.PI * (i*m/M + j*n/N)).mul(fft.get(i, j)));
        }
      }

      return result.scl(1d / (M * N));
    }
  }

}
