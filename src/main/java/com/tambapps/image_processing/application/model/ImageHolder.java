package com.tambapps.image_processing.application.model;

import com.tambapps.math.carray2d.CArray2D;

import java.awt.image.BufferedImage;

public interface ImageHolder {

  BufferedImage getImage();

  void computeImage(int imageType);

  ImageHolder copy();

  CArray2D[] getChannels();

  int getM();

  int getN();
}