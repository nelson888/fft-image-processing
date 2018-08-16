package com.tambapps.image_processing.application.model;

import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.util.Padding;

import java.awt.image.BufferedImage;

public abstract class AbstractFourierImage<T extends AbstractImageHolder> implements FourierImage {

  private final T original;
  private T transform;
  private T inverse;
  private ImageChangeListener changeListener;
  private Padding padding = Padding.ZERO;

  AbstractFourierImage(T original) {
    this.original = original;
  }

  @Override
  public void computeTransform(FastFourierTransformer2D transformer) {
    transform = computeTransform(original, transformer, padding);
    changeCenter(transform);
    transform.computeImage(original.getImage().getType());
    if (changeListener != null) {
      changeListener.onTransformChanged(transform.getImage());
    }
  }

  @Override
  public void computeInverse(FastFourierTransformer2D transformer) {
    inverse = computeInverse(transform, transformer);
    inverse.computeUnpaddedImage(padding, transform.getImage().getType());
    if (changeListener != null) {
      changeListener.onInverseChanged(inverse.getImage());
    }
  }

  @Override
  public BufferedImage getTransform() {
    return transform == null ? null : transform.getImage();
  }

  @Override
  public BufferedImage getInverse() {
    return inverse == null ? null : inverse.getImage();
  }

  @Override
  public BufferedImage getOriginal() {
    return original.getImage();
  }

  @Override
  public ImageHolder getTransformHolder() {
    return transform;
  }

  @Override
  public void setTransformHolder(ImageHolder holder) {
    transform.set(holder);
  }

  @Override
  public void setChangeListener(ImageChangeListener changeListener) {
    this.changeListener = changeListener;
  }

  abstract T computeTransform(T original, FastFourierTransformer2D transformer, Padding padding);

  abstract T computeInverse(T transform, FastFourierTransformer2D transformer);

  abstract void changeCenter(T transform);


  @Override
  public void setPadding(int left, int right, int top, int end) {
    padding = new Padding(left, right, top, end);
  }

  @Override
  public int getM() {
    return original.getM();
  }

  @Override
  public int getN() {
    return original.getN();
  }

  @Override
  public Padding getPadding() {
    return padding;
  }
}
