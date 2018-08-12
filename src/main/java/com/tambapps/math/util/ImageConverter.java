package com.tambapps.math.util;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.util.Padding;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageConverter {

  public static Complex2DArray toArray(BufferedImage image, Complex2DArray[] channels, boolean transparencyEnabled) {
    for (int i = 0; i < channels.length; i++) {
      channels[i] = new Complex2DArray(image.getHeight(), image.getWidth());
    }
    Complex2DArray array = new Complex2DArray(image.getHeight(), image.getWidth());
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        int rgb = image.getRGB(x, y);
        array.set(y, x, Complex.of(rgb));

        double r = (rgb >> 16) & 0xFF;
        double g = (rgb >> 8) & 0xFF;
        double b = (rgb & 0xFF);
        if (transparencyEnabled) {
          double a = (rgb >>24) & 0xFF;
          channels[3].set(y, x, Complex.of(a));
        }
        channels[2].set(y, x, Complex.of(b));
        channels[1].set(y, x, Complex.of(g));
        channels[0].set(y, x, Complex.of(r));

      }
    }
    return array;
  }

  public static Complex2DArray toArrayGrayScale(BufferedImage image) {
    Complex2DArray array = new Complex2DArray(image.getHeight(), image.getWidth());
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        int rgb = image.getRGB(x, y);
        double r = (rgb >> 16) & 0xFF;
        double g = (rgb >> 8) & 0xFF;
        double b = (rgb & 0xFF);
        array.set(y, x, Complex.of((r + g + b) / 3d));
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

  private static int getInt(Complex2DArray array, int i, int j) {
    return (int) array.get(i, j).abs();
  }
  private static int getRgb(Complex2DArray[] channels, int i, int j, boolean alphaEnabled) {
    int color =  (getInt(channels[0],i,j) & 255) << 16 | (getInt(channels[1],i,j) & 255) << 8 | (getInt(channels[2],i,j) & 255) << 0;
    if (alphaEnabled) {
      color = (getInt(channels[3],i,j) & 255) << 24 | color;
    }
    return color;
  }

  public static BufferedImage fromColoredChannels(Complex2DArray[] channels, boolean alphaEnabled, Padding padding) {
    if ((!alphaEnabled && channels.length != 3) || (alphaEnabled && channels.length != 4)) {
      throw new IllegalArgumentException("There should be " + (alphaEnabled ? 4 : 3) + " channels");
    }
    BufferedImage image = new BufferedImage(channels[0].getN() - padding.getLeft() - padding.getRight(), channels[0].getM() - padding.getTop() - padding.getEnd(), alphaEnabled ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR);
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        image.setRGB(x, y, getRgb(channels, y + padding.getEnd(), x + padding.getLeft(), alphaEnabled));
      }
    }
    return image;
  }

  public static BufferedImage fromColoredChannels(Complex2DArray[] channels, boolean alphaEnabled) {
    return fromColoredChannels(channels, alphaEnabled, Padding.ZERO);
  }

  public static BufferedImage fromArrayGrayScale(Complex2DArray f, int imageType) {
    BufferedImage image = new BufferedImage(f.getN(), f.getM(), imageType);
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        int grayLevel = (int) f.get(y, x).abs();
        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
        image.setRGB(x, y, gray);
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
