package com.tambapps.image_processing.application.controller;

import static com.tambapps.image_processing.application.FFTApplication.FFT_EXECUTOR_SERVICE;
import static com.tambapps.image_processing.application.FFTApplication.TASK_EXECUTOR_SERVICE;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.image_processing.application.FFTApplication;
import com.tambapps.image_processing.application.model.ImageTask;
import com.tambapps.image_processing.application.ui.view.NumberField;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithm;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithms;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;

import com.tambapps.math.fourier.filtering.Filters;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

  void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  private void initialize() {
    NumberField.addNumberListener(paddingInput);

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
      updateFTImage();

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
    updateFTImage();
  }

  private void updateFTImage() {
    fourierTransform.setImage(toImage(imageTask.getFourierTransform()));
  }

  private Image toImage(Complex2DArray array) {
    return SwingFXUtils.toFXImage(ImageConverter.fromArray(array), null);
  }

  @FXML
  private void applyFilter(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.NONE);
    alert.setTitle("Apply rectangle filter");
    VBox vBox = new VBox();
    CheckBox inverted = new CheckBox("inverted");
    Complex2DArray fourierTransform = imageTask.getFourierTransform();
    HBox widthBox = new HBox();
    Label widthLabel = new Label("Width:");
    NumberField widthField = new NumberField();
    widthField.setText(String.valueOf(fourierTransform.getN() / 4));
    widthBox.getChildren().addAll(widthLabel, widthField);

    HBox heightBox = new HBox();
    Label heightLabel = new Label("Height:");
    NumberField heightField = new NumberField();
    heightField.setText(String.valueOf(fourierTransform.getN() / 4));
    heightBox.getChildren().addAll(heightLabel, heightField);

    vBox.getChildren().addAll(inverted, widthBox, heightBox);
    alert.getDialogPane().setContent(vBox);

    ButtonType apply = new ButtonType("Apply", ButtonBar.ButtonData.APPLY);
    ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    alert.getButtonTypes().addAll(apply, cancel);
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == apply) {
      Complex2DArray ft = imageTask.getFourierTransform();
      Filters.rectangle(widthField.getNumber(), heightField.getNumber(), inverted.isSelected()).apply(ft);
      imageTask.setFourierTransform(ft);
      updateFTImage();
    }
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


    //creating File of image(s) that will be saved
    String[] splitName = imageTask.getImageFile().getName().split("\\.");
    final File ftFile = saveFT.isSelected() ? new File(directory, newName(splitName, "_fourier_transform")) : null;
    final File processedFile = saveProcessed.isSelected() ? new File(directory, newName(splitName, "_processed_image")) : null;

    //confirmation dialog
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Save images");

    String contentText = Stream.of(ftFile, processedFile)
        .filter(Objects::nonNull)
        .map(file -> file.getPath() + file.getName())
        .reduce("", (s1, s2) -> s1 + "\n" + s2);
    contentText = "Theses files will be created\n" + contentText;
    alert.setContentText(contentText);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
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
