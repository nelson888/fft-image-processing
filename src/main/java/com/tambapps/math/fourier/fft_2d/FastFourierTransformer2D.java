package com.tambapps.math.fourier.fft_2d;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.fft_1d.FastFourierTransform;
import com.tambapps.math.util.Vector;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

/**
 * This is the class to apply 2D Fast Fourier Transform
 * we apply the 1D FFT on each row then on each column
 */
public class FastFourierTransformer2D {

  //TODO faire des fonctions qui aggrandissent avec zero padding l'image comme dans le TP (permet une plus grande precision)

  private final double maxThreads;
  private final ExecutorCompletionService<Void> executorService;

  public FastFourierTransformer2D(ExecutorService executor, int maxThreads) {
    executorService = new ExecutorCompletionService<>(executor);
    this.maxThreads = maxThreads;
  }

  public void transform(Complex2DArray f) {
    transform(f, false, true);
    transform(f, false, false);
  }

  public void inverse(Complex2DArray f) {
    transform(f, true, true);
    transform(f, true, false);
  }

  private void transform(Complex2DArray f, final boolean inverse, final boolean row) {
    int treated = 0;
    int max = row ? f.getM() : f.getN();
    int perThread = (int) Math.floor(((double) max) / maxThreads);
    int count = 0;

    while (treated < max) {
      if (inverse) {
        executorService.submit(new InverseTask(f, treated, Math.min(max, treated + perThread), row));
      } else {
        executorService.submit(new TransformTask(f, treated, Math.min(max, treated + perThread), row));
      }

      treated += perThread;
      count++;
    }

    for (int i = 0; i < count; i++) {
      try {
        executorService.take();
      } catch (InterruptedException e) {
        throw new RuntimeException("Couldn't wait longer");
      }
    }
  }

  private abstract class FourierTask implements Callable<Void> {
    private final Complex2DArray data;
    private final int from;
    private final int to;
    private final boolean row;

    FourierTask(Complex2DArray data, int from, int to, boolean row) {
      this.data = data;
      this.from = from;
      this.to = to;
      this.row = row;
    }

    @Override
    public final Void call() {
      if (row) {
        for (int i = from; i < to; i++) {
          computeVector(data.getRow(i));
        }
      } else {
        for (int i = from; i < to; i++) {
          computeVector(data.getColumn(i));
        }
      }
      return null;
    }

    abstract void computeVector(Vector<Complex> vector);
  }

  /**
   * Task that will compute the FFT for many columns/rows
   */
  private class TransformTask extends FourierTask {

    TransformTask(Complex2DArray data, int from, int to, boolean row) {
      super(data, from, to, row);
    }

    @Override
    void computeVector(Vector<Complex> vector) {
      FastFourierTransform.recursiveFFT(vector);
    }

  }

  /**
   * Task that will compute the inverse FFT for many columns/rows
   */
  private class InverseTask extends FourierTask {

    InverseTask(Complex2DArray data, int from, int to, boolean row) {
      super(data, from, to, row);
    }

    @Override
    void computeVector(Vector<Complex> vector) {
      FastFourierTransform.inverse(vector);
    }

  }

}
