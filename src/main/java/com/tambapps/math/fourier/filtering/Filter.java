package com.tambapps.math.fourier.filtering;

import com.tambapps.math.array_2d.Complex2DArray;

public interface Filter {

  /**
   * Apply the filter on the given array
   * @param array the array to filter
   */
  void apply(Complex2DArray array);

  /**
   * Apply the filter on the returned array
   * @param array the array to filter
   * @return the filtered array
   */
  Complex2DArray applied(Complex2DArray array);

}
