package com.tambapps.math.fourier.application.controller;

import static com.tambapps.math.fourier.application.FFTApplication.FFT_EXECUTOR_SERVICE;
import static com.tambapps.math.fourier.application.FFTApplication.TASK_EXECUTOR_SERVICE;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.application.FFTApplication;
import com.tambapps.math.fourier.application.model.ImageTask;
import com.tambapps.math.fourier.application.ui.view.MyImageView;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithms;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;

import com.tambapps.math.fourier.util.FFTUtils;
import com.tambapps.math.util.ImageConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
  @FXML
  private TextField paddingInput;
  @FXML
  private AnchorPane imagesContainer;

  private ImageTask imageTask;
  private FastFourierTransformer2D fastFourierTransformer;
  private int currentPadding;

  public void setImageTask(ImageTask imageTask) {
    this.imageTask = imageTask;
    original.setImage(SwingFXUtils.toFXImage(ImageConverter.fromArray(imageTask.getImage()), null));
    if (imageTask.getFourierTransform() != null) {
      fourierTransform.setImage(SwingFXUtils.toFXImage(ImageConverter.fromArray(imageTask.getFourierTransform()), null));
    }
    if (imageTask.getProcessedImage() != null) {
      processedImage.setImage(SwingFXUtils.toFXImage(ImageConverter.fromArray(imageTask.getProcessedImage()), null));
    }
  }

  @FXML
  private void initialize() {
    paddingInput.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        paddingInput.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });
    fastFourierTransformer = new FastFourierTransformer2D(FFT_EXECUTOR_SERVICE,
        FFTApplication.MAX_FFT_THREADS - 1);
    centerButton.setVisible(false);
    filterButton.setVisible(false);
    inverseButton.setVisible(false);

    /*
    imagesContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
      original.setFitWidth(newVal.doubleValue());
      fourierTransform.setFitWidth(newVal.doubleValue());
      processedImage.setFitWidth(newVal.doubleValue());
    });
    imagesContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
      original.setFitHeight(newVal.doubleValue());
      fourierTransform.setFitHeight(newVal.doubleValue());
      processedImage.setFitHeight(newVal.doubleValue());
    });
    */
  }

  @FXML
  private void computeFFT(ActionEvent event) {
    final Complex2DArray array;
    int padding = Integer.parseInt(paddingInput.getText());
    if (padding > 0) {
      currentPadding = padding;
      array = FFTUtils.paddedCopy(imageTask.getImage(), padding, padding);
    } else {
      array = Complex2DArray.copy(imageTask.getImage());
    }
    TASK_EXECUTOR_SERVICE.submit(() -> {
      if (!fastFourierTransformer.transform(array)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("An error occurred while computing Fourier Transform.");
        alert.show();
        return;
      }
      imageTask.setFourierTransform(Complex2DArray.copy(array));
      updateFTImage(array);

      centerButton.setVisible(true);
      filterButton.setVisible(true);
      inverseButton.setVisible(true);
    });
  }

  @FXML
  private void changeCenter(ActionEvent event) {
    Complex2DArray ft = imageTask.getFourierTransform();
    FFTUtils.changeCenter(ft);
    updateFTImage(ft);
  }

  private void updateFTImage(Complex2DArray ft) {
    fourierTransform.setImage(toImage(ft));
  }

  private Image toImage(Complex2DArray array) {
    return SwingFXUtils.toFXImage(ImageConverter.fromArray(array), null);
  }

  @FXML
  private void applyFilter(ActionEvent event) {

  }

  @FXML
  private void inverse(ActionEvent event) {
    Complex2DArray ft = imageTask.getFourierTransform();
    TASK_EXECUTOR_SERVICE.submit(() -> {
      Complex2DArray inverse = Complex2DArray.copy(ft);

      fastFourierTransformer.inverse(inverse);
      if (currentPadding > 0) {
        inverse = FFTUtils.unpaddedCopy(inverse, currentPadding, currentPadding);
      }

      BufferedImage bufferedImage = ImageConverter.fromArray(inverse);
      processedImage.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
      imageTask.setProcessedImage(inverse);
    });
  }

  @FXML
  private void save(ActionEvent event) {

  }

  @FXML
  private void remove(ActionEvent event) {

  }

}
