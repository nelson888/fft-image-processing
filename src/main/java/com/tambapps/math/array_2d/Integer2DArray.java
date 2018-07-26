package com.tambapps.math.array_2d;

public class Integer2DArray extends Array2D<Integer> {

  public Integer2DArray(int M, int N) {
    super(M, N);
  }

  @Override Integer[] initialize(int size) {
    return new Integer[size];
  }
}
