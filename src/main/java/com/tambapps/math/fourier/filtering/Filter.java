package com.tambapps.math.fourier.filtering;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.array_2d.Double2DArray;

public interface Filter {
  void apply(Complex2DArray array);

  static Filter matrix(Double2DArray matrix) {
    return new MatrixFilter(matrix);
  }

  static Filter rectangle(int width, int height, boolean inverted) {
    return new RectangleFilter(width, height, inverted);
  }

}
