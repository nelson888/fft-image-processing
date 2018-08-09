package com.tambapps.image_processing.application.model;

import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.filtering.Filter;

import java.awt.image.BufferedImage;

public interface FourierImage {

  void computeTransform(FastFourierTransformer2D transformer);

  void computeInverse(FastFourierTransformer2D transformer);

  BufferedImage getTransform();

  BufferedImage getInverse();

  void applyFilter(Filter filter);

  void setChangeListener(ChangeListener changeListener);

  interface ChangeListener {
    void onTransformChanged(BufferedImage image);
    void onInverseChanged(BufferedImage image);
  }

}
