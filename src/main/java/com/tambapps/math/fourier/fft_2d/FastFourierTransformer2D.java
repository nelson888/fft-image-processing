package com.tambapps.math.fourier.fft_2d;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithm;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithms;
import com.tambapps.math.util.Vector;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the class to apply 2D Fast Fourier Transform
 * we apply the 1D FFT on each row then on each column
 */
public class FastFourierTransformer2D {

  private static final Logger LOGGER =
      Logger.getLogger(FastFourierTransformer2D.class.getSimpleName());
  private static final FFTAlgorithm DEFAULT_ALGORITHM = FFTAlgorithms.BASIC;

  private final double maxThreads;
  private final ExecutorCompletionService<Boolean> executorService;

  public FastFourierTransformer2D(ExecutorService executor, int maxThreads) {
    executorService = new ExecutorCompletionService<>(executor);
    this.maxThreads = maxThreads;
  }

  public boolean transform(Complex2DArray f, FFTAlgorithm algorithm) {
    return compute(f, false, true, algorithm) && compute(f, false, false, algorithm);
  }

  public boolean transform(Complex2DArray f) {
    return transform(f, DEFAULT_ALGORITHM);
  }

  public boolean inverse(Complex2DArray f, FFTAlgorithm algorithm) {
    return compute(f, true, true, algorithm) && compute(f, true, false, algorithm);
  }

  public boolean inverse(Complex2DArray f) {
    return inverse(f, DEFAULT_ALGORITHM);
  }

  private boolean compute(Complex2DArray f, final boolean inverse, final boolean row,
      FFTAlgorithm algorithm) {
    int treated = 0;
    int max = row ? f.getM() : f.getN();
    int perThread = (int) Math.floor(((double) max) / maxThreads);
    int count = 0;

    while (treated < max) {
      if (inverse) {
        executorService.submit(new InverseTask(algorithm, f, treated, Math.min(max, treated + perThread), row));
      } else {
        executorService.submit(new TransformTask(algorithm, f, treated,
            Math.min(max, treated + perThread), row));
      }

      treated += perThread;
      count++;
    }

    boolean success = true;
    for (int i = 0; i < count; i++) {
      try {
        executorService.take().get();
      } catch (InterruptedException e) {
        LOGGER.log(Level.SEVERE, String.format("Couldn't wait longer for thread %d", i), e);
        success = false;
      } catch (ExecutionException e) {
        LOGGER.log(Level.SEVERE, String.format("Thread %d couldn't retrieve success value", i), e);
        success = false;
      }
    }

    return success;
  }

  private abstract class FourierTask implements Callable<Boolean> {

    protected final FFTAlgorithm algorithm;
    private final Complex2DArray data;
    private final int from;
    private final int to;
    private final boolean row;

    FourierTask(FFTAlgorithm algorithm, Complex2DArray data, int from, int to, boolean row) {
      this.algorithm = algorithm;
      this.data = data;
      this.from = from;
      this.to = to;
      this.row = row;
    }

    @Override
    public final Boolean call() {
      if (row) {
        for (int i = from; i < to; i++) {
          computeVector(data.getRow(i));
        }
      } else {
        for (int i = from; i < to; i++) {
          computeVector(data.getColumn(i));
        }
      }
      return true;
    }

    abstract void computeVector(Vector<Complex> vector);
  }

  /**
   * Task that will compute the FFT for many columns/rows
   */
  private class TransformTask extends FourierTask {

    TransformTask(FFTAlgorithm algorithm, Complex2DArray data, int from, int to, boolean row) {
      super(algorithm, data, from, to, row);
    }

    @Override
    void computeVector(Vector<Complex> vector) {
      algorithm.compute(vector);
    }

  }

  /**
   * Task that will compute the inverse FFT for many columns/rows
   */
  private class InverseTask extends FourierTask {

    InverseTask(FFTAlgorithm algorithm, Complex2DArray data, int from, int to, boolean row) {
      super(algorithm, data, from, to, row);
    }

    @Override
    void computeVector(Vector<Complex> vector) {
      FFTAlgorithms.INVERSE.compute(vector, algorithm);
    }

  }

}
