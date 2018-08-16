package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.util.Padding;

import java.awt.image.BufferedImage;

public abstract class AbstractImageHolder implements ImageHolder {

  private BufferedImage image;
  final Complex2DArray[] channels;

  AbstractImageHolder(Complex2DArray array) {
    this.channels = new Complex2DArray[]{array};
  }

  AbstractImageHolder(BufferedImage image, int nbChannels) {
    this(nbChannels);
    this.image = image;
  }

  AbstractImageHolder(int nbChannels) {
    channels = new Complex2DArray[nbChannels];
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }

  public BufferedImage getImage() {
    return image;
  }

  @Override
  public Complex2DArray[] getChannels() {
    return channels;
  }

  @Override
  public int getM() {
    return channels[0].getM();
  }

  @Override
  public int getN() {
    return channels[0].getN();
  }

  void set(ImageHolder imageHolder) {
    if (imageHolder.getChannels().length != channels.length) {
      throw new IllegalArgumentException("Cannot set imageholder with different channels");
    }
    for (int i = 0; i < channels.length; i++) {
      channels[i] = imageHolder.getChannels()[i];
    }
    image = imageHolder.getImage();

  }

  abstract void computeUnpaddedImage(Padding padding, int imageType);

}