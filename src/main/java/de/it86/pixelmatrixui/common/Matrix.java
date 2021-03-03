package de.it86.pixelmatrixui.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Matrix<T> {

    private final int rows;
    private final int columns;
    private final List<T> data;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new ArrayList<>(Collections.nCopies(rows * columns, null));
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public T get(int row, int column) {
        checkIndices(row, column);
        return data.get(toArrayIndex(row, column));
    }

    public void set(int row, int column, T value) {
        checkIndices(row, column);
        data.set(toArrayIndex(row, column), value);
    }

    private void checkIndices(int row, int column) {
        if (row >= rows) {
            throw new IllegalArgumentException(
                    String.format(
                            "The given row index (value: %d) lies outside the number of rows %d!",
                            row, rows));
        }

        if (column >= columns) {
            throw new IllegalArgumentException(
                    String.format(
                            "The given column index (value: %d) lies outside the number of columns %d!",
                            column, columns));
        }
    }

    private int toArrayIndex(int row, int column) {
        return row * columns + column;
    }

}
