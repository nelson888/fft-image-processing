package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.filtering.Filter;

import java.awt.image.BufferedImage;

public class ColoredFourierImage extends AbstractFourierImage<ColoredFourierImage.ColoredImageHolder> {




  public ColoredFourierImage(BufferedImage original) {
    super(original);

  }


  @Override
  ColoredImageHolder createImage(int M, int N) {
    return new ColoredImageHolder(M, N);
  }

  @Override
  void computeTransform(ColoredImageHolder original, ColoredImageHolder transform,
      FastFourierTransformer2D transformer) {

  }

  @Override void computeInverse(ColoredImageHolder transform, ColoredImageHolder inverse,
      FastFourierTransformer2D transformer) {

  }

  static class ColoredImageHolder extends ImageHolder {
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BUE = 2;
    private final Complex2DArray[] channels = new Complex2DArray[3];
    private final Complex2DArray rgb;

    ColoredImageHolder(int M, int N) {
      rgb = new Complex2DArray(M, N);
      for (int i = 0; i < channels.length; i++) {
        channels[i] = new Complex2DArray(M, N);
      }
    }

    @Override
    Complex2DArray getArray() {
      return rgb;
    }
  }
}
