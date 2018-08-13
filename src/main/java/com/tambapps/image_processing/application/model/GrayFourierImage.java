package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.filtering.Filter;
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
    transform.computeImage(original.getImage().getType());
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

  @Override
  void applyFilter(GrayImageHolder transform, Filter filter) {
    filter.apply(transform.getArray());
  }

  static class GrayImageHolder extends AbstractImageHolder {

    GrayImageHolder(BufferedImage image) {
      super(image);
      setArray(ImageConverter.toArrayGrayScale(image));
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
      GrayImageHolder holder = new GrayImageHolder(Complex2DArray.copy(getArray()));
      holder.setImage(getImage());
      return holder;
    }

    @Override
    public Complex2DArray[] getChannels() {
      return new Complex2DArray[] {getArray()};
    }
  }
}
