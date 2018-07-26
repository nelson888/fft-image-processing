package com.tambapps.math;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FFTApplication extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/layout.fxml"));
    stage.setTitle("Fast Fourier Transform App");
    stage.setScene(new Scene(root, 800, 500));
    stage.show();
  }
}
