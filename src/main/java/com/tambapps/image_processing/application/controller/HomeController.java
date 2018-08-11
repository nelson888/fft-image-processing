package com.tambapps.image_processing.application.controller;

import com.tambapps.image_processing.application.model.ColoredFourierImage;
import com.tambapps.image_processing.application.model.FourierImage;
import com.tambapps.image_processing.application.model.GrayFourierImage;
import com.tambapps.image_processing.application.FFTApplication;
import com.tambapps.image_processing.application.ui.view.MyImageView;
import com.tambapps.math.util.ImageConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class HomeController {

  private final ObservableList<FourierImage> fourierImages = FXCollections.observableArrayList();
  private Stage imageTaskStage = new Stage();

  @FXML
  private GridPane gridPane;
  @FXML
  private Button addButton;
  private Stage stage;
  private FileChooser fileChooser;
  private int nbImages;

  @FXML
  private void initialize() {
    fileChooser = new FileChooser();
    nbImages = 0;
    fileChooser.setTitle("Pick an image");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("images files",
        "*.png", "*.jpg", "*.gif", "*.jpeg", "*.bmp"));
    gridPane.widthProperty().addListener((obs, oldVal, newVal) -> {
      for (Node node : gridPane.getChildren()) {
        if (node instanceof MyImageView) {
          ((MyImageView) node).setFitWidth(gridPane.getWidth() / 5d);
        }
      }
    });

    gridPane.heightProperty().addListener((obs, oldVal, newVal) -> {
      for (Node node : gridPane.getChildren()) {
        if (node instanceof MyImageView) {
          ((MyImageView) node).setFitHeight(gridPane.getHeight() / 3d);
        }
      }
    });
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  private void pickImages(ActionEvent event) {
    if (nbImages == 8) {
      maxImagesDialog();
      return;
    }
    File file = fileChooser.showOpenDialog(stage);
    if (file == null) {
      return;
    }

    BufferedImage image;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Couldn't read image");
      alert.setContentText(String
          .format("An error occurred while reading image %s. Please, retry.", file.getName()));
      alert.show();
      return;
    }

    Alert alert = new Alert(Alert.AlertType.NONE);
    alert.setTitle("Type of image");
    ButtonType gray = new ButtonType("gray");
    ButtonType colored = new ButtonType("colored");
    ButtonType alpha = new ButtonType("with transparency");
    ButtonType cancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    alert.getButtonTypes().addAll(gray, colored, alpha, cancel);
    Optional<ButtonType> result = alert.showAndWait();
    if (!result.isPresent() || result.get() == cancel) {
      return;
    }

    gridPane.getChildren().remove(addButton);

    BufferedImage originalImage = ImageConverter.copy(image);
    FourierImage fourierImage;

    if (result.get() == gray) {
      fourierImage = new GrayFourierImage(originalImage);
    } else if (result.get() == colored) {
      fourierImage = new ColoredFourierImage(originalImage, false);
    } else {
      fourierImage = new ColoredFourierImage(originalImage, true);
    }

    fourierImages.add(fourierImage);

    MyImageView imageView = createImageView(image);
    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,
        e -> showImageTaskView(fourierImage, file, image, file.getName() + String
            .format(" (%d x %d)", originalImage.getWidth(), originalImage.getHeight())));
    gridPane.add(imageView, nbImages % 4, nbImages / 4);
    GridPane.setHalignment(imageView, HPos.CENTER);
    GridPane.setValignment(imageView, VPos.CENTER);

    nbImages++;
    if (nbImages < 8) {
      gridPane.add(addButton, nbImages % 4, nbImages / 4);
    }
  }

  private void maxImagesDialog() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Max number of images reached");
    alert.setContentText("You cannot have more than 8 images.");
    alert.show();
  }

  private MyImageView createImageView(BufferedImage image) {
    MyImageView imageView = new MyImageView();
    imageView.setFitWidth(stage.getWidth() / 5d);
    imageView.setFitHeight(stage.getHeight() / 3d);
    imageView.setImage(SwingFXUtils.toFXImage(image, null));

    return imageView;
  }

  private void showImageTaskView(FourierImage fourierImage, File imageFile, BufferedImage image, String title) {
    FXMLLoader loader = new FXMLLoader();
    String path;

    double width = image.getWidth();
    double height = image.getHeight();
    if (width / height > 0.85) {
      path = "/view/image_task_view_vertical.fxml";
    } else {
      path = "/view/image_task_view_horizontal.fxml";
    }
    loader.setLocation(FFTApplication.class.getResource(path));
    if (imageTaskStage == null) {
      imageTaskStage = new Stage();
    } else {
      imageTaskStage.close();
    }
    imageTaskStage.setTitle(title);
    SplitPane root;
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load root", e);
    }

    Scene scene = new Scene(root);
    imageTaskStage.setScene(scene);
    FourierImageController controller = loader.getController();
    controller.setFourierImage(fourierImage);
    controller.setStage(imageTaskStage);
    controller.setImageFile(imageFile);

    imageTaskStage.show();
  }
}
