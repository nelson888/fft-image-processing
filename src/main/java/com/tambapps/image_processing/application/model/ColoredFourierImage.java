package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.util.FFTUtils;
import com.tambapps.math.fourier.util.Padding;
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
      FastFourierTransformer2D transformer, Padding padding) {
    ColoredImageHolder transform = new ColoredImageHolder(transparencyEnabled);
    for (int i = 0; i < original.channels.length; i++) {
      Complex2DArray channel = FFTUtils.paddedCopy(original.channels[i], padding);
      transformer.transform(channel);
      transform.channels[i] = channel;
    }
    transform.setImage(ImageConverter.fromColoredChannels(transform.channels, transparencyEnabled));
    return transform;
  }

  @Override
  ColoredImageHolder computeInverse(ColoredImageHolder transform,
      FastFourierTransformer2D transformer, Padding padding) {
    ColoredImageHolder inverse =
        new ColoredImageHolder(transparencyEnabled);
    for (int i = 0; i < transform.channels.length; i++) {
      Complex2DArray channel = Complex2DArray.copy(transform.channels[i]);
      transformer.inverse(channel);
      inverse.channels[i] = channel;
    }
    return inverse;
  }

  @Override
  void changeCenter(ColoredImageHolder transform) {
    for (Complex2DArray channel : transform.channels) {
      FFTUtils.changeCenter(channel);
    }
  }

  @Override
  void applyFilter(ColoredImageHolder transform, Filter filter) {
    for (Complex2DArray channel : transform.channels) {
      filter.apply(channel);
    }
  }

  static class ColoredImageHolder extends AbstractImageHolder {

    private final Complex2DArray[] channels;

    ColoredImageHolder(BufferedImage image, boolean transparencyEnabled) {
      super(image);
      channels = new Complex2DArray[transparencyEnabled ? 4 : 3];
      setArray(ImageConverter.toArray(image, channels, transparencyEnabled));
    }

    ColoredImageHolder(boolean transparencyEnabled) {
      super((Complex2DArray) null);
      channels = new Complex2DArray[transparencyEnabled ? 4 : 3];
    }

    @Override
    public void computeImage(int imageType) {
      ImageConverter
              .fromColoredChannels(channels, channels.length == 4);
    }

    @Override
    void computeUnpaddedImage(Padding padding,int imageType) {
      setImage(ImageConverter
              .fromColoredChannels(channels, transparancyEnabled(), padding));
    }
    @Override
    public ImageHolder copy() {
      ColoredImageHolder holder = new ColoredImageHolder(getImage(), transparancyEnabled());
      for (int i = 0; i < channels.length; i++) {
        holder.channels[i] = Complex2DArray.copy(channels[i]);
      }
      return holder;
    }

    private boolean transparancyEnabled() {
      return  channels.length == 4;
    }
    @Override
    public Complex2DArray[] getChannels() {
      return channels;
    }
  }
}
