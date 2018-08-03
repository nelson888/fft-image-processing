package com.tambapps.math.util;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageConverter {

  public static Complex2DArray toArray(BufferedImage image) {
    Complex2DArray array = new Complex2DArray(image.getHeight(), image.getWidth());
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        array.set(y, x, Complex.of(image.getRGB(x, y)));
      }
    }
    return array;
  }

  public static BufferedImage fromArray(Complex2DArray f) {
    return fromArray(f, BufferedImage.TYPE_3BYTE_BGR);
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

  public static BufferedImage copy(BufferedImage bi) {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  }
}
