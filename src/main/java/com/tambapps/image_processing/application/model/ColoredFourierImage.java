package com.tambapps.image_processing.application.model;

import com.tambapps.math.carray2d.CArray2D;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.util.FFTUtils;
import com.tambapps.math.fourier.util.Padding;
import com.tambapps.image_processing.application.util.ImageConverter;

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
                                      FastFourierTransformer2D transformer, Padding padding) {
    ColoredImageHolder transform = new ColoredImageHolder(transparencyEnabled);
    for (int i = 0; i < original.channels.length; i++) {
      CArray2D channel = FFTUtils.paddedCopy(original.channels[i], padding);
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
        new ColoredImageHolder(transparencyEnabled);
    for (int i = 0; i < transform.channels.length; i++) {
      CArray2D channel = transform.channels[i].copy();
      transformer.inverse(channel);
      inverse.channels[i] = channel;
    }
    return inverse;
  }

  @Override
  void changeCenter(ColoredImageHolder transform) {
    for (CArray2D channel : transform.channels) {
      FFTUtils.changeCenter(channel);
    }
  }

  static class ColoredImageHolder extends AbstractImageHolder {

    ColoredImageHolder(BufferedImage image, boolean transparencyEnabled) {
      super(image, transparencyEnabled ? 4 : 3);
      ImageConverter.toArray(image, channels, transparencyEnabled);
    }

    ColoredImageHolder(boolean transparencyEnabled) {
      super(transparencyEnabled ? 4 : 3);
    }

    @Override
    public void computeImage(int imageType) {
      setImage(ImageConverter
          .fromColoredChannels(channels, transparancyEnabled()));
    }

    @Override
    void computeUnpaddedImage(Padding padding, int imageType) {
      setImage(ImageConverter
          .fromColoredChannels(channels, transparancyEnabled(), padding));
    }

    @Override
    public ImageHolder copy() {
      ColoredImageHolder holder = new ColoredImageHolder(getImage(), transparancyEnabled());
      for (int i = 0; i < channels.length; i++) {
        holder.channels[i] = channels[i].copy();
      }
      return holder;
    }

    private boolean transparancyEnabled() {
      return channels.length == 4;
    }
  }
}
