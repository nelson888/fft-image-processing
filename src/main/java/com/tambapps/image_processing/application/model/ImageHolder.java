package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;

import java.awt.image.BufferedImage;

public abstract class ImageHolder {
  private BufferedImage image;

  public void setImage(BufferedImage image) {
    this.image = image;
  }

  public BufferedImage getImage() {
    return image;
  }

  abstract Complex2DArray getArray();
}