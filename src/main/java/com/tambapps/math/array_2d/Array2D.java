package com.tambapps.math.array_2d;

import com.tambapps.math.util.AbstractVector;

import java.util.Objects;

/**
 * class representing a 2D array
 * (row, col)
 *
 * @param <T>
 */
abstract class Array2D<T> {

  private final int M; //nb of rows (= column size)
  private final int N; // nb of columns (= row size)

  //we store the 2D array in a 1D array of size N * M
  private final T[] array;

  Array2D(int M, int N) {
    this.M = M;
    this.N = N;
    array = initialize(M * N);
  }

  Array2D(int M, int N, T[] values) {
    this.M = M;
    this.N = N;
    array = values;
  }

  abstract T[] initialize(int size);

  public T get(int row, int col) {
    checkIndex(row, col);
    return array[getIndex(row, col)];
  }

  public T get(int i) {
    checkIndex(i);
    return array[i];
  }

  public void set(int row, int col, T value) {
    checkIndex(row, col);
    array[getIndex(row, col)] = value;
  }

  public void set(int i, T value) {
    checkIndex(i);
    array[i] = value;
  }

  private void checkIndex(int row, int col) {
    if (row < 0 || row  >= M || col < 0 || col >= N) {
      throw new IndexOutOfBoundsException(String.format("Tried to access index (%d, %d) of array of size (%d, %d)", row, col, M, N));
    }
  }

  private void checkIndex(int i) {
    if (i < 0 || i >= array.length) {
      throw new IndexOutOfBoundsException(String.format("Tried to access index %d of array of size %d", i, M * N));
    }
  }

  private int getIndex(int row, int col) {
    return row * N + col;
  }

  public int getM() {
    return M;
  }

  public int getN() {
    return N;
  }

  public Row getRow(int i) {
    return new Row(i);
  }

  public Column getColumn(int i) {
    return new Column(i);
  }

  private class Column extends AbstractVector<T> {
    final int c;

    Column(int c) {
      this.c = c;
    }

    @Override
    public T getElement(int i) {
      return get(i, c);
    }

    @Override
    public void setElement(int i, T value) {
      set(i, c, value);
    }

    @Override
    public int getSize() {
      return M;
    }
  }

  private class Row extends AbstractVector<T> {
    private int r;

    Row(int r) {
      this.r = r;
    }

    @Override
    public T getElement(int i) {
      return get(r, i);
    }

    @Override
    public void setElement(int i, T value) {
      set(r, i, value);
    }

    @Override
    public int getSize() {
      return N;
    }
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder().append("(")
        .append(M).append(", ").append(N).append(")\n");
    for (int i = 0; i < N * M; i++) {
      stringBuilder.append("(").append(get(i)).append(")\t");
      if ((i + 1) % N == 0) {
        stringBuilder.append("\n");
      }
    }
    return stringBuilder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (! (o instanceof Array2D)) {
      return false;
    }
    Array2D a = (Array2D) o;
    if (a.M != M || a.N != N) {
      return false;
    }

    for (int i = 0; i < M * N; i++) {
      if (!Objects.equals(array[i], a.array[i])) {
        return false;
      }
    }
    return true;
  }

}
