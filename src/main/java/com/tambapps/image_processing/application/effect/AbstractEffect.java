package com.tambapps.image_processing.application.effect;

import com.tambapps.image_processing.application.model.ImageHolder;
import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.filtering.Filter;

public abstract class AbstractEffect implements Effect {

  private ImageHolder transform;
  private ImageHolder result;

  @Override
  public void setTransform(ImageHolder transform) {
    this.transform = transform;
    this.result = transform.copy();
  }

  @Override
  public final void apply(double value) {
    Filter filter = getFilter(transform.getM(), transform.getN(), value);
    for (int i = 0; i < result.getChannels().length; i++) {
      result.getChannels()[i] = Complex2DArray.copy(transform.getChannels()[i]);
      filter.apply(result.getChannels()[i]);
    }

    result.computeImage(transform.getImage().getType());
  }

  @Override
  public String toString() {
    return name() + " effect";
  }

  @Override
  public ImageHolder getResult() {
    return result;
  }

  int percentageValue(double percentage, int max) {
    return (int) percentageValue(percentage, (double) max);
  }


  double percentageValue(double percentage, double max) {
    return percentage * max / 100d;
  }

  ImageHolder getTransform() {
    return transform;
  }

  abstract Filter getFilter(int M, int N, double value);

  abstract String name();

  @Override
  public double getMaxValue() {
    return 100;
  }

  @Override
  public double getMinValue() {
    return 0;
  }

  @Override
  public void onDismiss() {

  }
}
