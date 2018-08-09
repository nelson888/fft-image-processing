package com.tambapps.image_processing.application.model;

import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.filtering.Filter;

import java.awt.image.BufferedImage;

public abstract class AbstractFourierImage<T extends ImageHolder> implements FourierImage {

  private final T original;
  private T transform;
  private T inverse;
  private ImageChangeListener changeListener;

  AbstractFourierImage(T original) {
    this.original = original;
  }

  @Override
  public void computeTransform(FastFourierTransformer2D transformer) {
    transform = computeTransform(original, transformer);
    if (changeListener != null) {
      changeListener.onTransformChanged(transform.getImage());
    }
  }

  @Override
  public void computeInverse(FastFourierTransformer2D transformer) {
    inverse = computeInverse(transform, transformer);
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
  public BufferedImage getOriginal() {
    return original.getImage();
  }

  @Override
  public void applyFilter(Filter filter) {
    filter.apply(transform.getArray());
  }

  @Override
  public void changeCenter() {
    changeCenter(transform);
    if (changeListener != null) {
      changeListener.onTransformChanged(transform.getImage());
    }
  }

  @Override
  public void setChangeListener(ImageChangeListener changeListener) {
    this.changeListener = changeListener;
  }

  abstract T computeTransform(T original, FastFourierTransformer2D transformer);

  abstract T computeInverse(T transform, FastFourierTransformer2D transformer);

  abstract void changeCenter(T transform);

}
