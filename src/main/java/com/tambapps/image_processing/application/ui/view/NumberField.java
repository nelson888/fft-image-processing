package com.tambapps.image_processing.application.ui.view;

import javafx.scene.control.TextField;

public class NumberField extends TextField {

  public static void addNumberListener(TextField textField) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.isEmpty()) {
        textField.setText("0");
      }
      else if (!newValue.matches("\\d*")) {
        textField.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });
  }

  public int getNumber() {
    return Integer.parseInt(getText());
  }
}
