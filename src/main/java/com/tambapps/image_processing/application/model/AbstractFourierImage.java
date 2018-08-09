package com.tambapps.image_processing.application.model;

import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.filtering.Filter;

import java.awt.image.BufferedImage;

public abstract class AbstractFourierImage<T extends ImageHolder> implements FourierImage {

  private final T original;
  private final T transform;
  private final T inverse;
  private ChangeListener changeListener;

  AbstractFourierImage(BufferedImage image) {
    int M = image.getHeight();
    int N = image.getWidth();
    this.original = createImage(N, M);
    this.original.setImage(image);
    this.transform = createImage(N, M);
    this.inverse = createImage(N, M);
  }

  @Override
  public void computeTransform(FastFourierTransformer2D transformer) {
    computeTransform(original, transform, transformer);
    if (changeListener != null) {
      changeListener.onTransformChanged(transform.getImage());
    }
  }

  @Override
  public void computeInverse(FastFourierTransformer2D transformer) {
    computeInverse(transform, inverse, transformer);
    if (changeListener != null) {
      changeListener.onInverseChanged(inverse.getImage());
    }
  }

  @Override
  public BufferedImage getTransform() {
    return transform.getImage();
  }

  @Override
  public BufferedImage getInverse() {
    return inverse.getImage();
  }

  @Override
  public void applyFilter(Filter filter) {
    filter.apply(transform.getArray());
  }

  @Override
  public void setChangeListener(ChangeListener changeListener) {
    this.changeListener = changeListener;
  }

  abstract T createImage(int M, int N);
  abstract void computeTransform(T original, T transform, FastFourierTransformer2D transformer);
  abstract void computeInverse(T transform, T inverse, FastFourierTransformer2D transformer);

}
