package com.tambapps.math.fourier.filtering;

import com.tambapps.math.array_2d.Double2DArray;

public final class Filters {

  private Filters() {}

  public static Filter matrix(Double2DArray matrix) {
    return new MatrixFilter(matrix);
  }

  public static Filter rectangle(int width, int height, boolean inverted) {
    return new RectangleFilter(width, height, inverted);
  }

  public static Filter threshold(double max) {
    return new ThresholdFilter(max);
  }
}
