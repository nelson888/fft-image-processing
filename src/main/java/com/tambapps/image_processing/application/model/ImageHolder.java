package com.tambapps.image_processing.application.model;

import com.tambapps.math.array_2d.Complex2DArray;

import java.awt.image.BufferedImage;

public interface ImageHolder {

  BufferedImage getImage();

  void computeImage(int imageType);

  ImageHolder copy();

  Complex2DArray[] getChannels();

  int getM();

  int getN();
}