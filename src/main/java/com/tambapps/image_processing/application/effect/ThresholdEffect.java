package com.tambapps.image_processing.application.effect;

import com.tambapps.image_processing.application.model.ImageHolder;
import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ThresholdEffect extends AbstractEffect {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThresholdEffect.class);
  private final ExecutorService executorService;
  private double max;
  private Future<Double> futureMax;

  public ThresholdEffect(ExecutorService executorService) {
    this.executorService = executorService;
  }

  @Override
  public void setTransform(ImageHolder transform) {
    super.setTransform(transform);
    max = -1;
    futureMax = executorService.submit(this::computeMax);
  }

  @Override
  Filter getFilter(int M, int N, double value) {
    if (max < 0) {
      try {
        max = futureMax.get();
        futureMax = null;
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    }
    double threshold = value == 0d ? Double.MAX_VALUE : percentageValue(100d - value, max);

    LOGGER.info("Applying {} with max value of {}", this, threshold);
    return Filters.threshold(threshold);
  }

  @Override
  String name() {
    return "threshold";
  }

  @Override
  public String toString() {
    return name() + " effect";
  }

  private double computeMax() {
    double average = 0;
    double ecartType = 0;
    Complex2DArray[] channels = getTransform().getChannels();
    final double nbElements = channels.length * getTransform().getM() * getTransform().getN();
    ArrayList<Double> absValues = new ArrayList<>((int) nbElements);

    for (Complex2DArray array : channels) {
      for (int i = 0; i < array.getM() * array.getN(); i++) {
        Complex c = array.get(i);
        double abs = c.abs();
        absValues.add(abs);
        average += abs;
      }
    }

    average /= nbElements;

    for (int i = 0; i < absValues.size(); i++) {
      ecartType += pow2(absValues.get(i) - average);
    }

    ecartType /= nbElements;
    ecartType = Math.sqrt(ecartType);

    return ecartType;
  }

  private double pow2(double d) {
    return d * d;
  }

  @Override
  public void onDismiss() {
    if (futureMax != null) {
      futureMax.cancel(true);
    }
  }
}
