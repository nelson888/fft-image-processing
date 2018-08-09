package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.util.FFTUtils;
import com.tambapps.math.util.ImageConverter;

import java.awt.image.BufferedImage;

public class ColoredFourierImage
    extends AbstractFourierImage<ColoredFourierImage.ColoredImageHolder> {

  private final boolean transparencyEnabled;

  public ColoredFourierImage(BufferedImage original, boolean transparencyEnabled) {
    super(new ColoredImageHolder(original, transparencyEnabled));
    this.transparencyEnabled = transparencyEnabled;
  }

  @Override
  ColoredImageHolder computeTransform(ColoredImageHolder original,
      FastFourierTransformer2D transformer) {
    ColoredImageHolder transform =
        new ColoredImageHolder(original.getArray().getM(), original.getArray().getN(),
            transparencyEnabled);
    for (int i = 0; i < original.channels.length; i++) {
      Complex2DArray channel = Complex2DArray.copy(original.channels[i]);
      transformer.transform(channel);
      transform.channels[i] = channel;
    }
    transform.setImage(ImageConverter.fromColoredChannels(transform.channels, transparencyEnabled));
    return transform;
  }

  @Override
  ColoredImageHolder computeInverse(ColoredImageHolder transform,
      FastFourierTransformer2D transformer) {
    ColoredImageHolder inverse =
        new ColoredImageHolder(transform.getArray().getM(), transform.getArray().getN(),
            transparencyEnabled);
    for (int i = 0; i < transform.channels.length; i++) {
      Complex2DArray channel = Complex2DArray.copy(transform.channels[i]);
      transformer.inverse(channel);
      inverse.channels[i] = channel;
    }
    inverse.setImage(ImageConverter.fromColoredChannels(inverse.channels, transparencyEnabled));
    return inverse;
  }

  @Override
  void changeCenter(ColoredImageHolder transform) {
    FFTUtils.changeCenter(transform.getArray());
    for (Complex2DArray channel : transform.channels) {
      FFTUtils.changeCenter(channel);
    }
  }

  static class ColoredImageHolder extends ImageHolder {

    private final Complex2DArray[] channels;

    ColoredImageHolder(BufferedImage image, boolean transparencyEnabled) {
      super(image);
      channels = new Complex2DArray[transparencyEnabled ? 4 : 3];
      setArray(ImageConverter.toArray(image, channels, transparencyEnabled));
    }

    ColoredImageHolder(int M, int N, boolean transparencyEnabled) {
      super(M, N);
      channels = new Complex2DArray[transparencyEnabled ? 4 : 3];
      for (int i = 0; i < channels.length; i++) {
        channels[i] = new Complex2DArray(M, N);
      }
    }

  }
}
