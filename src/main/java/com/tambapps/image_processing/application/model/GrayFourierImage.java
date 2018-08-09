package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.util.ImageConverter;

import java.awt.image.BufferedImage;

public class GrayFourierImage extends AbstractFourierImage<GrayFourierImage.GrayImageHolder> {

  public GrayFourierImage(BufferedImage original) {
    super(new GrayImageHolder(original));
  }

  @Override
  GrayImageHolder computeTransform(GrayImageHolder original,
      FastFourierTransformer2D transformer) {
    GrayImageHolder transform =
        new GrayImageHolder(original.getArray().getM(), original.getArray().getN());
    transform.setArray(Complex2DArray.copy(original.getArray()));
    transformer.transform(transform.getArray());
    transform.setImage(
        ImageConverter.fromArrayGrayScale(transform.getArray(), original.getImage().getType()));
    return transform;
  }

  @Override
  GrayImageHolder computeInverse(GrayImageHolder transform,
      FastFourierTransformer2D transformer) {
    GrayImageHolder inverse =
        new GrayImageHolder(transform.getArray().getM(), transform.getArray().getN());
    inverse.setArray(Complex2DArray.copy(transform.getArray()));
    transformer.inverse(inverse.getArray());
    inverse.setImage(
        ImageConverter.fromArrayGrayScale(inverse.getArray(), transform.getImage().getType()));
    return inverse;
  }

  static class GrayImageHolder extends ImageHolder {

    GrayImageHolder(BufferedImage image) {
      super(image);
      setArray(ImageConverter.toArrayGrayScale(image));
    }

    GrayImageHolder(int M, int N) {
      super(M, N);
    }

  }
}
