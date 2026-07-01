package core;

public class MatrixOperations {
    
    // Matrix addition
    public static Matrix add(Matrix A, Matrix B) {
        if (A.getRows() != B.getRows() || A.getCols() != B.getCols()) {
            throw new MatrixException("Matrix dimensions must match for addition");
        }
        
        Matrix result = new Matrix(A.getRows(), A.getCols());
        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < A.getCols(); j++) {
                result.set(i, j, A.get(i, j) + B.get(i, j));
            }
        }
        return result;
    }
    
    // Matrix subtraction
    public static Matrix subtract(Matrix A, Matrix B) {
        if (A.getRows() != B.getRows() || A.getCols() != B.getCols()) {
            throw new MatrixException("Matrix dimensions must match for subtraction");
        }
        
        Matrix result = new Matrix(A.getRows(), A.getCols());
        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < A.getCols(); j++) {
                result.set(i, j, A.get(i, j) - B.get(i, j));
            }
        }
        return result;
    }
    
    // Matrix multiplication
    public static Matrix multiply(Matrix A, Matrix B) {
        if (A.getCols() != B.getRows()) {
            throw new MatrixException(
                "Number of columns in A must equal number of rows in B. " +
                "A: " + A.getRows() + "x" + A.getCols() + ", " +
                "B: " + B.getRows() + "x" + B.getCols()
            );
        }
        
        Matrix result = new Matrix(A.getRows(), B.getCols());
        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < B.getCols(); j++) {
                double sum = 0;
                for (int k = 0; k < A.getCols(); k++) {
                    sum += A.get(i, k) * B.get(k, j);
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }
    
    // Scalar multiplication
    public static Matrix scalarMultiply(Matrix A, double scalar) {
        Matrix result = new Matrix(A.getRows(), A.getCols());
        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < A.getCols(); j++) {
                result.set(i, j, A.get(i, j) * scalar);
            }
        }
        return result;
    }
    
    // Matrix transpose
    public static Matrix transpose(Matrix A) {
        Matrix result = new Matrix(A.getCols(), A.getRows());
        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < A.getCols(); j++) {
                result.set(j, i, A.get(i, j));
            }
        }
        return result;
    }
    
    // Dot product (for vectors)
    public static double dotProduct(Matrix A, Matrix B) {
        if (!(A.getRows() == 1 && B.getRows() == 1 && A.getCols() == B.getCols()) &&
            !(A.getCols() == 1 && B.getCols() == 1 && A.getRows() == B.getRows())) {
            throw new MatrixException("Both matrices must be vectors of the same dimension");
        }
        
        double sum = 0;
        int n = Math.max(A.getRows(), A.getCols());
        for (int i = 0; i < n; i++) {
            double aVal = (A.getRows() == 1) ? A.get(0, i) : A.get(i, 0);
            double bVal = (B.getRows() == 1) ? B.get(0, i) : B.get(i, 0);
            sum += aVal * bVal;
        }
        return sum;
    }
    
    // Matrix determinant
    public static double determinant(Matrix A) {
        if (!A.isSquare()) {
            throw new MatrixException("Matrix must be square to calculate determinant");
        }
        
        int n = A.getRows();
        if (n == 1) return A.get(0, 0);
        if (n == 2) return A.get(0, 0) * A.get(1, 1) - A.get(0, 1) * A.get(1, 0);
        
        double det = 0;
        for (int j = 0; j < n; j++) {
            det += Math.pow(-1, j) * A.get(0, j) * determinant(minor(A, 0, j));
        }
        return det;
    }
    
    // Get minor matrix
    private static Matrix minor(Matrix A, int row, int col) {
        Matrix minor = new Matrix(A.getRows() - 1, A.getCols() - 1);
        int mRow = 0;
        for (int i = 0; i < A.getRows(); i++) {
            if (i == row) continue;
            int mCol = 0;
            for (int j = 0; j < A.getCols(); j++) {
                if (j == col) continue;
                minor.set(mRow, mCol, A.get(i, j));
                mCol++;
            }
            mRow++;
        }
        return minor;
    }
    
    // Matrix inverse using Gaussian elimination
    public static Matrix inverse(Matrix A) {
        if (!A.isSquare()) {
            throw new MatrixException("Matrix must be square to calculate inverse");
        }
        
        double det = determinant(A);
        if (Math.abs(det) < 1e-10) {
            throw new MatrixException("Matrix is singular (determinant = 0), cannot compute inverse");
        }
        
        int n = A.getRows();
        Matrix augmented = new Matrix(n, 2 * n);
        
        // Create augmented matrix [A|I]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented.set(i, j, A.get(i, j));
                augmented.set(i, j + n, (i == j) ? 1.0 : 0.0);
            }
        }
        
        // Apply Gaussian elimination
        for (int i = 0; i < n; i++) {
            // Find pivot
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmented.get(k, i)) > Math.abs(augmented.get(maxRow, i))) {
                    maxRow = k;
                }
            }
            
            // Swap rows
            if (maxRow != i) {
                for (int k = 0; k < 2 * n; k++) {
                    double temp = augmented.get(i, k);
                    augmented.set(i, k, augmented.get(maxRow, k));
                    augmented.set(maxRow, k, temp);
                }
            }
            
            // Normalize pivot row
            double pivot = augmented.get(i, i);
            if (Math.abs(pivot) < 1e-10) {
                throw new MatrixException("Matrix is singular");
            }
            
            for (int k = 0; k < 2 * n; k++) {
                augmented.set(i, k, augmented.get(i, k) / pivot);
            }
            
            // Eliminate other rows
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmented.get(k, i);
                    for (int j = 0; j < 2 * n; j++) {
                        augmented.set(k, j, augmented.get(k, j) - factor * augmented.get(i, j));
                    }
                }
            }
        }
        
        // Extract inverse matrix
        Matrix inverse = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse.set(i, j, augmented.get(i, j + n));
            }
        }
        
        return inverse;
    }
    
    // Matrix power
    public static Matrix power(Matrix A, int exponent) {
        if (!A.isSquare()) {
            throw new MatrixException("Matrix must be square for exponentiation");
        }
        if (exponent < 0) {
            return power(inverse(A), -exponent);
        }
        if (exponent == 0) {
            return Matrix.identity(A.getRows());
        }
        if (exponent == 1) {
            return A.copy();
        }
        
        Matrix result = A.copy();
        for (int i = 1; i < exponent; i++) {
            result = multiply(result, A);
        }
        return result;
    }
    
    // Trace of matrix
    public static double trace(Matrix A) {
        if (!A.isSquare()) {
            throw new MatrixException("Matrix must be square to calculate trace");
        }
        
        double trace = 0;
        for (int i = 0; i < A.getRows(); i++) {
            trace += A.get(i, i);
        }
        return trace;
    }
}