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
    Complex2DArray transformArray = FFTUtils.paddedCopy(original.getArray(), padding);
    GrayImageHolder transform =
        new GrayImageHolder(transformArray);
    transformer.transform(transformArray);
    transform.computeImage(original.getImage().getType());
    return transform;
  }

  @Override
  GrayImageHolder computeInverse(GrayImageHolder transform,
                                 FastFourierTransformer2D transformer) {
    GrayImageHolder inverse = new GrayImageHolder(transform.getArray().copy());
    transformer.inverse(inverse.getArray());
    return inverse;
  }

  @Override
  void changeCenter(GrayImageHolder transform) {
    FFTUtils.changeCenter(transform.getArray());
  }

  static class GrayImageHolder extends AbstractImageHolder {

    GrayImageHolder(BufferedImage image) {
      super(image, 1);
      channels[0] = ImageConverter.toArrayGrayScale(image);
    }

    GrayImageHolder(Complex2DArray array) {
      super(array);
    }

    @Override
    public void computeImage(int imageType) {
      setImage(ImageConverter.fromArrayGrayScale(getArray(), imageType));
    }

    @Override
    void computeUnpaddedImage(Padding padding, int imageType) {
      setImage(ImageConverter
          .fromArrayGrayScale(FFTUtils.unpaddedCopy(getArray(), padding),
              imageType));
    }

    @Override
    public ImageHolder copy() {
      GrayImageHolder holder = new GrayImageHolder(getArray().copy());
      holder.setImage(getImage());
      return holder;
    }

    Complex2DArray getArray() {
      return channels[0];
    }
  }
}
