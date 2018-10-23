package com.tambapps.image_processing.application.util;

import com.tambapps.math.carray2d.CArray2D;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.util.Padding;

import java.awt.*;
import java.awt.image.*;

public class ImageConverter {

  public static CArray2D toArray(BufferedImage image, CArray2D[] channels, boolean transparencyEnabled) {
    for (int i = 0; i < channels.length; i++) {
      channels[i] = new CArray2D(image.getHeight(), image.getWidth());
    }
    CArray2D array = new CArray2D(image.getHeight(), image.getWidth());
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

  public static CArray2D toArrayGrayScale(BufferedImage image) {
    CArray2D array = new CArray2D(image.getHeight(), image.getWidth());
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

  public static void toGrayScale(BufferedImage image) {
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        int rgb = image.getRGB(i, j);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);
        int grayLevel = (r + g + b) / 3;
        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
        image.setRGB(i, j, gray);

      }
    }
  }
  public static BufferedImage fromArray(CArray2D f) {
    return fromArray(f, BufferedImage.TYPE_3BYTE_BGR);
  }

  public static BufferedImage fromArray(CArray2D f, int imageType) {
    BufferedImage image = new BufferedImage(f.getN(), f.getM(), imageType);
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        image.setRGB(x, y, (int) f.get(y, x).abs());
      }
    }
    return image;
  }

  private static int getInt(CArray2D array, int i, int j) {
    return (int) array.get(i, j).abs();
  }
  private static int getRgb(CArray2D[] channels, int i, int j, boolean alphaEnabled) {
    int color =  (getInt(channels[0],i,j) & 255) << 16 | (getInt(channels[1],i,j) & 255) << 8 | (getInt(channels[2],i,j) & 255) << 0;
    if (alphaEnabled) {
      color = (getInt(channels[3],i,j) & 255) << 24 | color;
    }
    return color;
  }

  public static BufferedImage fromColoredChannels(CArray2D[] channels, boolean alphaEnabled, Padding padding) {
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

  public static BufferedImage fromColoredChannels(CArray2D[] channels, boolean alphaEnabled) {
    return fromColoredChannels(channels, alphaEnabled, Padding.ZERO);
  }

  public static BufferedImage fromArrayGrayScale(CArray2D f, int imageType) {
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

  public static BufferedImage grayScaleCopy(BufferedImage image) {
    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    Graphics g = result.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return result;
  }
}
