package com.tambapps.math.fourier.application.model;

import com.tambapps.math.array_2d.Complex2DArray;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ImageTask {
  private ObjectProperty<Complex2DArray> image;
  private ObjectProperty<Complex2DArray> fourierTransform;
  private ObjectProperty<Complex2DArray> processedImage;

  public ImageTask() {
    this.image = new SimpleObjectProperty<>(null);
    this.fourierTransform = new SimpleObjectProperty<>(null);
    this.processedImage = new SimpleObjectProperty<>(null);
  }

  public Complex2DArray getImage() {
    return image.get();
  }

  public ObjectProperty<Complex2DArray> imageProperty() {
    return image;
  }

  public void setImage(Complex2DArray image) {
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

  public Complex2DArray getProcessedImage() {
    return processedImage.get();
  }

  public ObjectProperty<Complex2DArray> processedImageProperty() {
    return processedImage;
  }

  public void setProcessedImage(Complex2DArray processedImage) {
    this.processedImage.set(processedImage);
  }
}
