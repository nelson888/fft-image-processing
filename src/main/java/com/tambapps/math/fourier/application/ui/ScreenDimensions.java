package com.tambapps.math.fourier.application.ui;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class ScreenDimensions {

  public static final double WIDTH;
  public static final double HEIGHT;

  static {
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    WIDTH = screenBounds.getWidth();
    HEIGHT = screenBounds.getHeight();
  }

}
