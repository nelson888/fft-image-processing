package com.tambapps.math.fourier.fft_2d;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithm;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithms;
import com.tambapps.math.util.PowerOfTwo;
import com.tambapps.math.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

/**
 * This is the class that applies 2D Fast Fourier Transform
 * by applying the 1D FFT independently on each row and then on each column (concurrently)
 */
public class FastFourierTransformer2D {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(FastFourierTransformer2D.class.getSimpleName());

  private final double maxThreads;
  private final ExecutorCompletionService<Boolean> executorService;

  public FastFourierTransformer2D(ExecutorService executor, int maxThreads) {
    executorService = new ExecutorCompletionService<>(executor);
    this.maxThreads = maxThreads;
  }

  public boolean transform(Complex2DArray f, FFTAlgorithm algorithm) {
    LOGGER.info("Computing FT of array of size ({}, {}) with the {} algorithm", f.getM(), f.getN(), algorithm.getName());
    try {
      return compute(f, false, true, algorithm) && compute(f, false, false, algorithm);
    } finally {
      LOGGER.info("Successfully computed FT of array of size ({}, {})", f.getM(), f.getN());

    }

  }

  private boolean isSizePowerOfTwo(Complex2DArray f) {
    return PowerOfTwo.isPowerOfTwo(f.getM()) && PowerOfTwo.isPowerOfTwo(f.getN());
  }

  private FFTAlgorithm getAlgorithm(Complex2DArray f) {
    return isSizePowerOfTwo(f) ? FFTAlgorithms.CT_RECURSIVE : FFTAlgorithms.BASIC;
  }

  public boolean transform(Complex2DArray f) {
    return transform(f, getAlgorithm(f));
  }

  public boolean inverse(Complex2DArray f, FFTAlgorithm algorithm) {
    LOGGER.info("Computing inverse FT of array of size ({}, {}) with the {} algorithm", f.getM(), f.getN(), algorithm.getName());
    try {
      return compute(f, true, true, algorithm) && compute(f, true, false, algorithm);
    } finally {
      LOGGER.info("Successfully inverse computed FT of array of size ({}, {})", f.getM(), f.getN());
    }
  }

  public boolean inverse(Complex2DArray f) {
    return inverse(f, getAlgorithm(f));
  }

  private boolean compute(Complex2DArray f, final boolean inverse, final boolean row,
      FFTAlgorithm algorithm) {
    int treated = 0;
    int max = row ? f.getM() : f.getN();
    int perThread = (int) Math.floor(((double) max) / maxThreads);
    int count = 0;

    while (treated < max) {
      if (inverse) {
        executorService.submit(
            new InverseTask(algorithm, f, treated, Math.min(max, treated + perThread), row));
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
        LOGGER.error("Couldn't wait longer for thread " + i, e);
        success = false;
      } catch (ExecutionException e) {
        LOGGER.error(String.format("Thread %d couldn't retrieve success value", i), e);
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
