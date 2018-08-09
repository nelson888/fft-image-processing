package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;

import java.awt.image.BufferedImage;

public class ImageHolder {

  private BufferedImage image;
  private Complex2DArray array;

  ImageHolder(int M, int N) {
    array = new Complex2DArray(M, N);
  }

  ImageHolder(BufferedImage image) {
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

}