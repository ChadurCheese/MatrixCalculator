package core;

//import java.util.Arrays;
import java.util.Random;

public class Matrix {
    private double[][] data;
    private int rows;
    private int cols;
    
    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new MatrixException("Matrix dimensions must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }
    
    public Matrix(double[][] data) {
        if (data == null || data.length == 0) {
            throw new MatrixException("Invalid matrix data");
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new double[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            if (data[i].length != cols) {
                throw new MatrixException("All rows must have the same number of columns");
            }
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }
    
    // Getters
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public double[][] getData() { return data; }
    public double get(int row, int col) { return data[row][col]; }
    public void set(int row, int col, double value) { data[row][col] = value; }
    
    // Create identity matrix
    public static Matrix identity(int size) {
        Matrix result = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            result.set(i, i, 1.0);
        }
        return result;
    }
    
    // Create zero matrix
    public static Matrix zeros(int rows, int cols) {
        return new Matrix(rows, cols);
    }
    
    // Create random matrix
    public static Matrix random(int rows, int cols, double min, double max) {
        Matrix result = new Matrix(rows, cols);
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.set(i, j, min + (max - min) * rand.nextDouble());
            }
        }
        return result;
    }
    
    // Copy matrix
    public Matrix copy() {
        return new Matrix(this.data);
    }
    
    // Matrix to string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%10.4f", data[i][j]));
                if (j < cols - 1) sb.append(", ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
    
    // Check if matrix is square
    public boolean isSquare() {
        return rows == cols;
    }
    
    // Check if matrix is symmetric
    public boolean isSymmetric() {
        if (!isSquare()) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = i + 1; j < cols; j++) {
                if (Math.abs(data[i][j] - data[j][i]) > 1e-10) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix)) return false;
        Matrix other = (Matrix) obj;
        if (rows != other.rows || cols != other.cols) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.abs(data[i][j] - other.data[i][j]) > 1e-10) {
                    return false;
                }
            }
        }
        return true;
    }
}