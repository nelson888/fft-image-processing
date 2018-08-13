package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.util.Padding;

import java.awt.image.BufferedImage;

public abstract class AbstractImageHolder implements ImageHolder {

  //TODO USE AN ARRAY OF COMPLEX2DARRAY INSTEAD OF SIMPLE COMPLEX2DARRAY
  private BufferedImage image;
  private Complex2DArray array;

  AbstractImageHolder(Complex2DArray array) {
    this.array = array;
  }

  AbstractImageHolder(BufferedImage image) {
    this.image = image;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }

  public BufferedImage getImage() {
    return image;
  }

  public void setArray(Complex2DArray array) {
    this.array = array;
  }

  public Complex2DArray getArray() {
    return array;
  }

  void computeImage() {
    computeImage(getImage().getType());
  }

  abstract void computeUnpaddedImage(Padding padding, int imageType);

}