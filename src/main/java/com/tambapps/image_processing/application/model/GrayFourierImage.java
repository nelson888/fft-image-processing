package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.util.ImageConverter;

import java.awt.image.BufferedImage;

public class GrayFourierImage extends AbstractFourierImage<GrayFourierImage.GrayImageHolder> {

  public GrayFourierImage(BufferedImage original) {
    super(original);
  }

  @Override
  GrayImageHolder createImage(int M, int N) {
    return new GrayImageHolder(M, N);
  }

  @Override
  void computeTransform(GrayImageHolder original, GrayImageHolder transform,
      FastFourierTransformer2D transformer) {
    transform.array = Complex2DArray.copy(original.getArray());
    transformer.transform(transform.array);
    transform.setImage(ImageConverter.fromArrayGrayScale(transform.array, original.getImage().getType()));
  }

  @Override
  void computeInverse(GrayImageHolder transform, GrayImageHolder inverse,
      FastFourierTransformer2D transformer) {
    inverse.array = Complex2DArray.copy(transform.getArray());
    transformer.inverse(inverse.array);
    inverse.setImage(ImageConverter.fromArrayGrayScale(inverse.array, transform.getImage().getType()));
  }

  static class GrayImageHolder extends ImageHolder {
    private Complex2DArray array;
    GrayImageHolder(int M, int N) {
      array = new Complex2DArray(M, N);
    }
    @Override
    Complex2DArray getArray() {
      return array;
    }
  }
}
