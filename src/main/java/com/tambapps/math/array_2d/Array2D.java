package com.tambapps.math.array_2d;

public abstract class Array2D<T> {

  private final int M;
  private final int N;

  //we store the 2D array in a 1D array of size N * M
  private final T[] array;

  public Array2D(int M, int N) {
    this.M = M;
    this.N = N;
    array = initialize(N * M);
  }

  abstract T[] initialize(int size);

  public T get(int m, int n) {
    return array[getIndex(m, n)];
  }

  public T get(int i) {
    return array[i];
  }

  public void set(int m, int n, T value) {
    array[getIndex(m, n)] = value;
  }

  public void set(int i, T value) {
    array[i] = value;
  }


  private int getIndex(int m, int n) {
    return m * M + n;
  }

  public int getM() {
    return M;
  }

  public int getN() {
    return N;
  }
}
