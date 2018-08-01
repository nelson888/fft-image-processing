package com.tambapps.math.fourier.application.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;

public class ImageTaskController {

  @FXML
  private ImageView original;
  @FXML
  private ImageView fourierTransform;
  @FXML
  private ImageView processedImage;
  @FXML
  private Button computeButton;
  @FXML
  private Button centerButton;
  @FXML
  private Button filterButton;
  @FXML
  private Button inverseButton;

  public void setOriginal(BufferedImage image) {
    original.setImage(SwingFXUtils.toFXImage(image, null));
  }
}
