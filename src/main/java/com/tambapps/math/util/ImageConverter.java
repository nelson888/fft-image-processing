package com.tambapps.math.util;

import com.tambapps.math.complex.Complex;

import java.awt.image.BufferedImage;

public class ImageConverter {

  public static Integer[][] toArray(BufferedImage image) {
    Integer tab[][] = new Integer[image.getHeight()][image.getWidth()];
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        tab[y][x] = image.getRGB(x, y);
      }
    }
    return tab;
  }

  public static Complex[][] toComplexArray(BufferedImage image) {
    Complex tab[][] = new Complex[image.getHeight()][image.getWidth()];

    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        tab[y][x] = Complex.of(image.getRGB(x, y));
      }
    }
    return tab;
  }

  public static BufferedImage fromArray(Complex[][] f, int imageType) {
    BufferedImage image = new BufferedImage(f[0].length, f.length, imageType);
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        image.setRGB(x, y, (int) f[y][x].abs());
      }
    }
    return image;
  }
}
