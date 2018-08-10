package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.util.FFTUtils;
import com.tambapps.math.fourier.util.Padding;
import com.tambapps.math.util.ImageConverter;

import java.awt.image.BufferedImage;

public class GrayFourierImage extends AbstractFourierImage<GrayFourierImage.GrayImageHolder> {

  public GrayFourierImage(BufferedImage original) {
    super(new GrayImageHolder(original));
  }

  @Override
  GrayImageHolder computeTransform(GrayImageHolder original,
      FastFourierTransformer2D transformer, Padding padding) {
    GrayImageHolder transform =
        new GrayImageHolder(FFTUtils.paddedCopy(original.getArray(), padding));
    transformer.transform(transform.getArray());
    transform.setImage(
        ImageConverter.fromArrayGrayScale(transform.getArray(),
            original.getImage().getType())); //TODO unpad image??
    return transform;
  }

  @Override
  GrayImageHolder computeInverse(GrayImageHolder transform,
      FastFourierTransformer2D transformer, Padding padding) {
    GrayImageHolder inverse = new GrayImageHolder(Complex2DArray.copy(transform.getArray()));
    transformer.inverse(inverse.getArray());
    inverse.setImage(ImageConverter
        .fromArrayGrayScale(FFTUtils.unpaddedCopy(inverse.getArray(), padding),
            transform.getImage().getType()));
    return inverse;
  }

  @Override
  void changeCenter(GrayImageHolder transform) {
    FFTUtils.changeCenter(transform.getArray());
  }

  static class GrayImageHolder extends ImageHolder {

    GrayImageHolder(BufferedImage image) {
      super(image);
      setArray(ImageConverter.toArrayGrayScale(image));
    }

    GrayImageHolder(Complex2DArray array) {
      super(array);
    }

  }
}
