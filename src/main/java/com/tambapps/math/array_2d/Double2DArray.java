package com.tambapps.math.array_2d;

public class Double2DArray extends Array2D<Double> {

  public Double2DArray(int M, int N) {
    super(M, N);
  }

  @Override
  Double[] initialize(int size) {
    return new Double[size];
  }
}
