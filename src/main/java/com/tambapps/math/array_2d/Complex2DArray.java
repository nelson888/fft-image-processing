package com.tambapps.math.array_2d;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.util.AbstractVector;
import com.tambapps.math.util.Vector;

import java.util.Arrays;

public class Complex2DArray  {

  private final int M; //nb of rows (= column size)
  private final int N; // nb of columns (= row size)
  private final Column[] columns;
  private final Row[] rows;

  //we store the 2D array in a 1D array of size N * M
  private final Complex[] array;

  public Complex2DArray(int M, int N) {
    this(M, N, new Complex[M * N]);
  }

  public Complex2DArray(int M, int N, Complex[] values) {
    this.M = M;
    this.N = N;
    array = values;
    columns = new Column[N];
    rows = new Row[M];
    for (int i = 0; i < columns.length; i++) {
      columns[i] = new Column(i);
    }
    for (int i = 0; i < rows.length; i++) {
      rows[i] = new Row(i);
    }
  }

  public Complex get(int row, int col) {
    checkIndex(row, col);
    return array[getIndex(row, col)];
  }

  Vector<Complex> getAt(int i) {
    return rows[i];
  }

  public Complex get(int i) {
    checkIndex(i);
    return array[i];
  }

  public void set(int row, int col, Complex value) {
    checkIndex(row, col);
    array[getIndex(row, col)] = value;
  }

  public void set(int i, Complex value) {
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
    return rows[i];
  }

  public Column getColumn(int i) {
    return columns[i];
  }

  private class Column extends AbstractVector<Complex> {
    final int c;

    Column(int c) {
      this.c = c;
    }

    @Override
    public Complex getAt(int i) {
      return get(i, c);
    }

    @Override
    public void setAt(int i, Complex value) {
      set(i, c, value);
    }

    @Override
    public int getSize() {
      return M;
    }
  }

  private class Row extends AbstractVector<Complex> {
    private int r;

    Row(int r) {
      this.r = r;
    }

    @Override
    public Complex getAt(int i) {
      return get(r, i);
    }

    @Override
    public void setAt(int i, Complex value) {
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
    if (! (o instanceof Complex2DArray)) {
      return false;
    }
    Complex2DArray a = (Complex2DArray) o;
    if (a.M != M || a.N != N) {
      return false;
    }

    return Arrays.equals(array, a.array);
  }

  public Complex2DArray copy() {
    int size = M * N;
    Complex[] copyArray = new Complex[size];
    System.arraycopy(array, 0, copyArray, 0, size);
    return new Complex2DArray(M, N, copyArray);
  }

  public Complex2DArray immutableCopy() {
    int size = M * N;
    Complex[] copyArray = new Complex[size];
    System.arraycopy(array, 0, copyArray, 0, size);
    return new ImmutableComplexArray2D(M, N, copyArray);
  }

}
