package com.tambapps.image_processing.application.model;

import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.filtering.Filter;

import java.awt.image.BufferedImage;

public interface FourierImage {

  void computeTransform(FastFourierTransformer2D transformer);

  void computeInverse(FastFourierTransformer2D transformer);

  BufferedImage getOriginal();

  BufferedImage getTransform();

  BufferedImage getInverse();

  void changeCenter();

  void setPadding(int left, int right, int top, int end);

  void applyFilter(Filter filter);

  void setChangeListener(ImageChangeListener changeListener);

  interface ImageChangeListener {
    void onTransformChanged(BufferedImage image);

    void onInverseChanged(BufferedImage image);
  }

  int getM();

  int getN();
}
