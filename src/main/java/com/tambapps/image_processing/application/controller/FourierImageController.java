package com.tambapps.image_processing.application.controller;

import static com.tambapps.image_processing.application.FFTApplication.FFT_EXECUTOR_SERVICE;
import static com.tambapps.image_processing.application.FFTApplication.TASK_EXECUTOR_SERVICE;

import com.tambapps.image_processing.application.effect.CircEffect;
import com.tambapps.image_processing.application.effect.Effect;
import com.tambapps.image_processing.application.effect.RecEffect;
import com.tambapps.image_processing.application.effect.ThresholdEffect;
import com.tambapps.image_processing.application.model.FourierImage;
import com.tambapps.image_processing.application.FFTApplication;
import com.tambapps.image_processing.application.ui.view.NumberField;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;

import com.tambapps.math.fourier.filtering.Filters;
import com.tambapps.math.fourier.util.Padding;
import com.tambapps.math.util.PowerOfTwo;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

public class FourierImageController implements FourierImage.ImageChangeListener {

  private static final int ORIGINAL = 0;
  private static final int FOURIER_TRANSFORM = 1;
  private static final int PROCESSED_IMAGE = 2;

  private static final String NO_EFFECT = "none";
  private static final String THRESHOLD = "threshold";
  private static final String REC_LOW = "rec\nlow pass";
  private static final String REC_HIGH = "rec\nhigh pass";
  private static final String CIRC_LOW = "circ\nlow pass";
  private static final String CIRC_HIGH = "circ\nhigh pass";


  @FXML
  private ImageView imageView;
  @FXML
  private Button computeButton;
  @FXML
  private Button effectButton;
  @FXML
  private Button inverseButton;
  @FXML
  private Button original;
  @FXML
  private Button transform;
  @FXML
  private Button processed;
  @FXML
  private TextField paddingMInput;
  @FXML
  private TextField paddingNInput;
  @FXML
  private Button saveButton;
  @FXML
  private Button removeButton;
  @FXML
  private CheckBox saveFT;
  @FXML
  private CheckBox saveProcessed;
  @FXML
  private Pane filterControls;
  @FXML
  private Spinner<Effect> filterSpinner;
  @FXML
  private Slider filterSlider;

  private FourierImage fourierImage;
  private Stage stage;
  private FastFourierTransformer2D fastFourierTransformer;
  private File imageFile;
  private boolean transforming = false;
  private boolean filtering = false;
  private boolean inversing = false;

  void setFourierImage(FourierImage fourierImage) {
    this.fourierImage = fourierImage;
    fourierImage.setChangeListener(this);

    BufferedImage transform = fourierImage.getTransform();
    saveFT.setVisible(transform != null);

    BufferedImage inverse = fourierImage.getInverse();
    saveProcessed.setVisible(inverse != null);

    if (inverse != null) {
      setImage(PROCESSED_IMAGE);
    } else if (transform != null) {
      setImage(FOURIER_TRANSFORM);
    } else {
      setImage(ORIGINAL);
    }

    this.transform.setDisable(transform == null);
    this.processed.setDisable(inverse == null);

    if (saveFT.isVisible() || saveProcessed.isVisible()) {
      saveButton.setVisible(true);
    }

    Padding padding = fourierImage.getPadding();
    if (padding.getTop() != 0 || padding.getEnd() != 0) {
      paddingMInput.setText(String.valueOf(padding.getTop() + padding.getEnd()));
    }

    if (padding.getLeft() != 0 || padding.getRight() != 0) {
      paddingNInput.setText(String.valueOf(padding.getLeft() + padding.getRight()));
    }
  }

  void setStage(Stage stage) {
    this.stage = stage;
    stage.setOnHiding(event -> {
      if (fourierImage != null) {
        fourierImage.setChangeListener(null);
      }
    });
  }

  void setImageFile(File imageFile) {
    this.imageFile = imageFile;
  }

  @FXML
  private void initialize() {
    NumberField.addNumberListener(paddingMInput);
    NumberField.addNumberListener(paddingNInput);

    fastFourierTransformer = new FastFourierTransformer2D(FFT_EXECUTOR_SERVICE,
        FFTApplication.MAX_FFT_THREADS - 1);
    setFTButtonsVisibility(false);

    filterSlider.setMin(0);
    filterSlider.setMax(100);
    ObservableList<Effect> effects = FXCollections.observableArrayList(new CircEffect(false, REC_HIGH), new CircEffect(true, REC_LOW), new RecEffect(false, REC_HIGH), new RecEffect(true, REC_LOW), new ThresholdEffect(), Effect.NONE);
    SpinnerValueFactory<Effect> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(effects);
    filterSpinner.setValueFactory(valueFactory);
    valueFactory.setValue(effects.get(effects.size() - 1));
    valueFactory.valueProperty().addListener((observable, oldValue, newValue) -> {
      filterControls.setDisable(newValue == Effect.NONE);

    });
    filterControls.setDisable(true);

    filterSlider.setOnMouseReleased(event -> {
      if (event.getButton() == MouseButton.PRIMARY) {
        Effect effect = filterSpinner.getValue();
        if (effect == Effect.NONE) {
          return;
        }
        effect.apply(filterSlider.getValue());
      }
    });

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
    effectButton.setVisible(visible);
    inverseButton.setVisible(visible);
    saveButton.setVisible(visible);
  }

  private int parsePadding(TextField textField) {
    String text = textField.getText();
    return text == null || text.isEmpty() ? 0 : Integer.parseInt(text);
  }

  private void showTaskAlreadyRunningDialog(String taskTitle) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(taskTitle + " is already computing");
    alert.setContentText("Please wait the end of the computation");
    alert.show();
  }
  @FXML
  private void computeFFT(ActionEvent event) {
    if (transforming) {
      showTaskAlreadyRunningDialog("Fourier Transform");
      return;
    }
    transforming = true;
    int paddingM = parsePadding(paddingMInput);
    int paddingN = parsePadding(paddingNInput);
    fourierImage.setPadding(paddingN / 2, paddingN / 2 + paddingN % 2,
            paddingM / 2, paddingM / 2 + paddingM % 2);

    String title = stage.getTitle();
    stage.setTitle("Computing Fourier Transform...");
    TASK_EXECUTOR_SERVICE.submit(() -> {
      fourierImage.computeTransform(fastFourierTransformer);
      setFTButtonsVisibility(true);
      saveFT.setVisible(true);
      Platform.runLater(() -> stage.setTitle(title));
      transforming = false;
      this.transform.setDisable(false);
    });
  }

  private Image toImage(BufferedImage image) {
    return SwingFXUtils.toFXImage(image, null);
  }

  @Override
  public void onInverseChanged(BufferedImage image) {
    Platform.runLater(() -> imageView.setImage(toImage(image)));
  }

  @Override
  public void onTransformChanged(BufferedImage image) {
    Platform.runLater(() -> imageView.setImage(toImage(image)));
  }

  @FXML
  private void applyEffect(ActionEvent event) {
    if (filtering) {
      showTaskAlreadyRunningDialog("Effect");
      return;
    }
    Alert alert = new Alert(Alert.AlertType.NONE);
    alert.setTitle("Apply rectangle filter");
    VBox vBox = new VBox();
    CheckBox inverted = new CheckBox("inverted");
    BufferedImage fourierTransform = fourierImage.getTransform();

    HBox widthBox = new HBox();
    Label widthLabel = new Label("Width:");
    NumberField widthField = new NumberField();
    widthField.setText(String.valueOf(fourierTransform.getWidth() / 4));
    widthBox.getChildren().addAll(widthLabel, widthField);

    HBox heightBox = new HBox();
    Label heightLabel = new Label("Height:");
    NumberField heightField = new NumberField();
    heightField.setText(String.valueOf(fourierTransform.getHeight() / 4));
    heightBox.getChildren().addAll(heightLabel, heightField);

    vBox.getChildren().addAll(inverted, widthBox, heightBox);
    alert.getDialogPane().setContent(vBox);

    ButtonType apply = new ButtonType("Apply", ButtonBar.ButtonData.APPLY);
    ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    alert.getButtonTypes().addAll(apply, cancel);
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == apply) {
      filtering = true;
      TASK_EXECUTOR_SERVICE.submit(() -> {
        fourierImage.applyFilter(Filters.rectangle(widthField.getNumber(), heightField.getNumber(), inverted.isSelected()));
        filtering = false;
      });
    }
  }


  @FXML
  private void inverse(ActionEvent event) {
    if (inversing) {
      showTaskAlreadyRunningDialog("Fourier inverse");
      return;
    }
    inversing = true;
    String title = stage.getTitle();
    stage.setTitle("Computing Fourier Inverse...");
    TASK_EXECUTOR_SERVICE.submit(() -> {
      fourierImage.computeInverse(fastFourierTransformer);
      saveProcessed.setVisible(true);
      Platform.runLater(() -> stage.setTitle(title)); //UI changes have to happen on JavaFX thread
      inversing = false;
      this.processed.setDisable(false);
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
    directoryChooser.setInitialDirectory(imageFile.getParentFile());
    directoryChooser.setTitle("Choose a directory");
    File directory = directoryChooser.showDialog(stage);
    if (directory == null) {
      return;
    }

    //creating File of image(s) that will be saved
    String[] splitName = imageFile.getName().split("\\.");
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
          saveImage(fourierImage.getTransform(), ftFile);
        }
        if (processedFile != null) {
          saveImage(fourierImage.getInverse(), processedFile);
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

  @FXML
  private void autoPadding(ActionEvent event) {
    int closestM = PowerOfTwo.getClosestSuperior(fourierImage.getM());
    int closestN = PowerOfTwo.getClosestSuperior(fourierImage.getN());
    paddingMInput.setText(String.valueOf(closestM - fourierImage.getM()));
    paddingNInput.setText(String.valueOf(closestN - fourierImage.getN()));

  }


  @FXML
  private void transformImage(ActionEvent event) {
    setImage(FOURIER_TRANSFORM);
  }

  @FXML
  private void originalImage(ActionEvent event) {
    setImage(ORIGINAL);
  }

  @FXML
  private void processedImage(ActionEvent event) {
    setImage(PROCESSED_IMAGE);
  }

  private void setImage(int imageType) {
    BufferedImage image = null;
    switch (imageType) {
      case ORIGINAL:
        image = fourierImage.getOriginal();
        break;
      case FOURIER_TRANSFORM:
        image = fourierImage.getTransform();
        break;
      case PROCESSED_IMAGE:
        image = fourierImage.getInverse();
    }
    imageView.setImage(toImage(image));
  }
}
