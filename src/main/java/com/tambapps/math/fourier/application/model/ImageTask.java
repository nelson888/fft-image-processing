package com.tambapps.math.fourier.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.awt.image.BufferedImage;

public class ImageTask {
  private ObjectProperty<BufferedImage> image;
  private ObjectProperty<Complex2DArray> fourierTransform;
  private ObjectProperty<BufferedImage> processedImage;

  public ImageTask(BufferedImage original) {
    this.image = new SimpleObjectProperty<>(original);
    this.fourierTransform = new SimpleObjectProperty<>(null);
    this.processedImage = new SimpleObjectProperty<>(null);
  }

  public BufferedImage getImage() {
    return image.get();
  }

  public ObjectProperty<BufferedImage> imageProperty() {
    return image;
  }

  public void setImage(BufferedImage image) {
    this.image.set(image);
  }

  public Complex2DArray getFourierTransform() {
    return fourierTransform.get();
  }

  public ObjectProperty<Complex2DArray> fourierTransformProperty() {
    return fourierTransform;
  }

  public void setFourierTransform(Complex2DArray fourierTransform) {
    this.fourierTransform.set(fourierTransform);
  }

  public BufferedImage getProcessedImage() {
    return processedImage.get();
  }

  public ObjectProperty<BufferedImage> processedImageProperty() {
    return processedImage;
  }

  public void setProcessedImage(BufferedImage processedImage) {
    this.processedImage.set(processedImage);
  }
}
