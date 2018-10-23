package com.tambapps.image_processing.application.controller;

import com.tambapps.image_processing.application.model.ColoredFourierImage;
import com.tambapps.image_processing.application.model.FourierImage;
import com.tambapps.image_processing.application.model.GrayFourierImage;
import com.tambapps.image_processing.application.FFTApplication;
import com.tambapps.image_processing.application.view.MyImageView;
import com.tambapps.image_processing.application.util.ImageConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HomeController {

  private final ObservableList<FourierImage> fourierImages = FXCollections.observableArrayList();
  private Stage imageTaskStage = new Stage();
  private final Map<FourierImage, MyImageView> imageViewMap = new HashMap<>();

  @FXML
  private GridPane gridPane;
  @FXML
  private Button addButton;
  private Stage stage;
  private FileChooser fileChooser;

  @FXML
  private void initialize() {
    fileChooser = new FileChooser();
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
    stage.setOnHiding(event -> {
      if (imageTaskStage != null) {
        imageTaskStage.close();
      }
    });
  }

  @FXML
  private void pickImages(ActionEvent event) {
    if (fourierImages.size() == 8) {
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

    if (result.get() == gray) {
      ImageConverter.toGrayScale(image);
    }
    BufferedImage originalImage = ImageConverter.copy(image);
    FourierImage fourierImage;

    if (result.get() == gray) {
      fourierImage = new GrayFourierImage(originalImage);
    } else if (result.get() == colored) {
      fourierImage = new ColoredFourierImage(originalImage, false);
    } else {
      fourierImage = new ColoredFourierImage(originalImage, true);
    }

    MyImageView imageView = createImageView(image);
    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,
        e -> showImageTaskView(fourierImage, file, file.getName() + String
            .format(" (%d x %d)", originalImage.getWidth(), originalImage.getHeight())));
    addToGrid(imageView, fourierImages.size());
    GridPane.setHalignment(imageView, HPos.CENTER);
    GridPane.setValignment(imageView, VPos.CENTER);

    imageViewMap.put(fourierImage, imageView);
    fourierImages.add(fourierImage);
    if (fourierImages.size() < 8) {
      addToGrid(addButton, fourierImages.size());
    }
  }

  private void addToGrid(Node node, int i) {
    gridPane.add(node, i % 4, i / 4);
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

  private void showImageTaskView(FourierImage fourierImage, File imageFile, String title) {
    FXMLLoader loader = new FXMLLoader();

    loader.setLocation(FFTApplication.class.getResource("/view/image_task_view.fxml"));
    if (imageTaskStage == null) {
      imageTaskStage = new Stage();
    } else {
      imageTaskStage.close();
    }
    imageTaskStage.setTitle(title);
    Pane root;
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
    controller.setHomeController(this);
    imageTaskStage.show();
  }

  @FXML
  private void about(ActionEvent event) {
    Parent root;
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(FFTApplication.class.getResource("/view/about_view.fxml"));
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load root", e);
    }
    Scene scene = new Scene(root);

    Stage stage = new Stage();
    stage.setScene(scene);
    stage.showAndWait();
  }

  void removeFourierImage(FourierImage fourierImage) {
    fourierImages.remove(fourierImage);
    ObservableList<Node> children = gridPane.getChildren();
    children.remove(imageViewMap.get(fourierImage));
    List<Node> nodes = new ArrayList<>(children.size());

    while (!children.isEmpty()) {
      nodes.add(children.remove(0));
    }
    for (int i = 0; i < nodes.size(); i++) {
      addToGrid(nodes.get(i), i);
    }
  }
}
