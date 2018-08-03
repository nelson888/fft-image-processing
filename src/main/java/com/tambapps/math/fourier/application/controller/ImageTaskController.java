package com.tambapps.math.fourier.application.controller;

import static com.tambapps.math.fourier.application.FFTApplication.FFT_EXECUTOR_SERVICE;
import static com.tambapps.math.fourier.application.FFTApplication.TASK_EXECUTOR_SERVICE;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.application.FFTApplication;
import com.tambapps.math.fourier.application.model.ImageTask;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithm;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithms;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;

import com.tambapps.math.fourier.util.FFTUtils;
import com.tambapps.math.util.ImageConverter;
import com.tambapps.math.util.PowerOfTwo;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
  private Button saveButton;
  @FXML
  private Button removeButton;
  @FXML
  private CheckBox saveFT;
  @FXML
  private CheckBox saveProcessed;

  private ImageTask imageTask;
  private Stage stage;
  private FastFourierTransformer2D fastFourierTransformer;
  private int currentPadding;

  void setImageTask(ImageTask imageTask) {
    this.imageTask = imageTask;
    original.setImage(SwingFXUtils.toFXImage(ImageConverter.fromArray(imageTask.getImage()), null));
    if (imageTask.getFourierTransform() != null) {
      fourierTransform.setImage(SwingFXUtils.toFXImage(ImageConverter.fromArray(imageTask.getFourierTransform()), null));
    }
    if (imageTask.getProcessedImage() != null) {
      processedImage.setImage(SwingFXUtils.toFXImage(ImageConverter.fromArray(imageTask.getProcessedImage()), null));
    }
    saveFT.setVisible(imageTask.getFourierTransform() != null);
    saveProcessed.setVisible(imageTask.getProcessedImage() != null);
  }

  public void setStage(Stage stage) {
    this.stage = stage;
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
    setFTButtonsVisibility(false);

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

  private void setFTButtonsVisibility(boolean visible) {
    centerButton.setVisible(visible);
    filterButton.setVisible(visible);
    inverseButton.setVisible(visible);
    saveButton.setVisible(visible);
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
      if (!fastFourierTransformer.transform(array, getAlgorithm(array))) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("An error occurred while computing Fourier Transform.");
        alert.show();
        return;
      }
      imageTask.setFourierTransform(Complex2DArray.copy(array));
      updateFTImage(array);

      setFTButtonsVisibility(true);
      saveFT.setVisible(true);
    });
  }

  private FFTAlgorithm getAlgorithm(Complex2DArray array) {
    return PowerOfTwo.isPowerOfTwo(array.getM()) && PowerOfTwo.isPowerOfTwo(array.getN()) ?
        FFTAlgorithms.CT_RECURSIVE : FFTAlgorithms.BASIC;
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

      fastFourierTransformer.inverse(inverse, getAlgorithm(ft));
      if (currentPadding > 0) {
        inverse = FFTUtils.unpaddedCopy(inverse, currentPadding, currentPadding);
      }

      BufferedImage bufferedImage = ImageConverter.fromArray(inverse);
      processedImage.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
      imageTask.setProcessedImage(inverse);
      saveProcessed.setVisible(true);
    });
  }

  @FXML
  private void save(ActionEvent event) {
    if (!saveFT.isSelected() && !saveProcessed.isSelected()) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("There is nothing to save");
      alert.setContentText("You didn't select anything to save");
      alert.show();
      return;
    }

    //ask the user to pick a directory
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setInitialDirectory(imageTask.getImageFile().getParentFile());
    directoryChooser.setTitle("Choose a directory");
    File directory = directoryChooser.showDialog(stage);
    if (directory == null) {
      return;
    }

    System.err.println(imageTask.getImageFile().getName());
    //creating File of image(s) that will be saved
    String[] splitName = imageTask.getImageFile().getName().split("\\.");
    final File ftFile = saveFT.isSelected() ? new File(directory, newName(splitName, "_fourier_transform")) : null;
    final File processedFile = saveProcessed.isSelected() ? new File(directory, newName(splitName, "_processed_image")) : null;

    //confirmation dialog
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Do you want to save these files?");

    String contentText = Stream.of(ftFile, processedFile)
        .filter(Objects::nonNull)
        .map(file -> file.getPath() + file.getName())
        .reduce("", (s1, s2) -> s1 + "\n" + s2);
    alert.setContentText(contentText);
    ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    ButtonType yes = new ButtonType("YES", ButtonBar.ButtonData.YES);
    alert.getButtonTypes().addAll(cancel, yes);
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == yes) {
      //saving image(s)
      TASK_EXECUTOR_SERVICE.submit(() -> {
        if (ftFile != null) {
          saveImage(ImageConverter.fromArray(imageTask.getFourierTransform()), ftFile);
        }
        if (processedFile != null) {
          saveImage(ImageConverter.fromArray(imageTask.getProcessedImage()), processedFile);
        }
      });
    }
  }

  private void saveImage(BufferedImage image, File file) {
    try {
      ImageIO.write(image, "jpg", file);
    } catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Couldn't save " + file.getName());
      alert.setContentText("An error occurred while saving " + file.getName() + ":\n" + e.getLocalizedMessage());
      alert.show();
    }
  }

  private String newName(String[] splitName, String nameExtension) {
    if (splitName.length == 1) {
      return splitName[0] + nameExtension;
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < splitName.length - 1; i++) {
      builder.append(splitName[i]);
    }
    return builder.append(nameExtension)
        .append(".")
        .toString();
  }
  @FXML
  private void remove(ActionEvent event) {
    stage.close();
  }

}
