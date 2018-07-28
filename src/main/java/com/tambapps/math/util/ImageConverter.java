package com.tambapps.math.util;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;

import java.awt.image.BufferedImage;

public class ImageConverter {

  public static Integer[][] toArray2(BufferedImage image) {
    Integer tab[][] = new Integer[image.getHeight()][image.getWidth()];
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        tab[y][x] = image.getRGB(x, y);
      }
    }
    return tab;
  }

  public static Complex[][] toComplexArray2(BufferedImage image) {
    Complex tab[][] = new Complex[image.getHeight()][image.getWidth()];

    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        tab[y][x] = Complex.of(image.getRGB(x, y));
      }
    }
    return tab;
  }

  public static BufferedImage fromArray2(Complex[][] f, int imageType) {
    BufferedImage image = new BufferedImage(f[0].length, f.length, imageType);
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        image.setRGB(x, y, (int) f[y][x].abs());
      }
    }
    return image;
  }

  public static Complex2DArray toArray(BufferedImage image) {
    Complex2DArray array = new Complex2DArray(image.getHeight(), image.getWidth());
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        array.set(y, x, Complex.of(image.getRGB(x, y)));
      }
    }
    return array;
  }

  public static BufferedImage fromArray(Complex2DArray f, int imageType) {
    BufferedImage image = new BufferedImage(f.getN(), f.getM(), imageType);
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        image.setRGB(x, y, (int) f.get(y, x).abs());
      }
    }
    return image;
  }
}
