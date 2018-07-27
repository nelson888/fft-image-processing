package com.tambapps.math.array_2d;

import com.tambapps.math.util.AbstractVector;

/**
 * (row, col)
 * @param <T>
 */
public abstract class Array2D<T> {

  private final int M; //nb of rows (= column size)
  private final int N; // nb of columns (= row size)

  //we store the 2D array in a 1D array of size N * M
  private final T[] array;

  public Array2D(int M, int N) {
    this.M = M;
    this.N = N;
    array = initialize(M * N);
  }

  abstract T[] initialize(int size);

  public T get(int row, int col) {
    return array[getIndex(row, col)];
  }

  public T get(int i) {
    return array[i];
  }

  public void set(int row, int col, T value) {
    array[getIndex(row, col)] = value;
  }

  public void set(int i, T value) {
    array[i] = value;
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

    @Override public int getSize() {
      return N;
    }
  }

  @Override public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < N * M; i++) {
      stringBuilder.append("(").append(get(i)).append(")\t");
      if ((i+1) % N == 0) {
        stringBuilder.append("\n");
      }
    }
    return stringBuilder.toString();
  }
}
