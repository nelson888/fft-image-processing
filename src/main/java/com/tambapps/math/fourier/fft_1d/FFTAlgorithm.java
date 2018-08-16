package com.tambapps.math.fourier.fft_1d;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.Vector;

public interface FFTAlgorithm {
  void compute(Vector<Complex> vector);

  String getName();

  String getDescription();
}
