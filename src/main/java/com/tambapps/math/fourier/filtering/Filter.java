package com.tambapps.math.fourier.filtering;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.array_2d.Double2DArray;

public interface Filter {

  void apply(Complex2DArray array);

}
