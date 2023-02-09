package com.geopokrovskiy;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class SolutionLong {

    public static long findGCD(long a, long b) {
        if (b == 0) {
            return a;
        }
        return Math.abs(findGCD(b, a % b));
    }

    public static long findLCM(long a, long b){
        return Math.abs(a * b / findGCD(a, b));
    }
    static class Fraction{
        private long a;
        private long b;

        public long getA() {
            return a;
        }

        public long getB() {
            return b;
        }

        public Fraction(){
            this.a = 0;
            this.b = 1;
        }

        public Fraction(long a, long b){
            if(b== 0){
                throw new ArithmeticException("Division by zero!");
            }
            this.a = a;
            this.b = b;
            this.reduce();
        }

        private void reduce() {
            long gcd = findGCD(this.a, this.b);
            if (this.b > 0) {
                this.a /= gcd;
                this.b /= gcd;
            } else {
                this.a /= -gcd;
                this.b /= -gcd;
            }
        }

        private Fraction reciprocal() throws ArithmeticException {
            if (this.toString().equals("0")) {
                throw new ArithmeticException("Division by zero!");
            }
            return new Fraction(this.b, this.a);
        }

        public Fraction add(Fraction other){
            return new Fraction(this.a * other.b + other.a * this.b, this.b * other.b);
        }

        public Fraction subtract(Fraction other){
            return new Fraction(this.a * other.b - other.a * this.b, this.b * other.b);
        }

        public Fraction multiply(Fraction other) {
            return new Fraction(other.a * (this.a), this.b * (other.b));
        }

        public Fraction divide(Fraction other) {
            other = other.reciprocal();
            return this.multiply(other);
        }

        @Override
        public String toString() {
            return a + "/" + b;
        }
    }

    static class Matrix{
        private int m;
        private int n;
        private Fraction[][] matrixFrac;

        public Matrix(){
            this.m = 1;
            this.n = 1;
            this.matrixFrac = new Fraction[1][1];
        }

        public Matrix(int m, int n){
            this.m = m;
            this.n = n;
            this.matrixFrac = new Fraction[m][n];
            for(int i = 0; i < m; i++){
                for(int j = 0; j < n; j++){
                    this.matrixFrac[i][j] = new Fraction(0, 1);
                }
            }
        }


        public Matrix(Fraction[][] matrix){
            this.m = matrix.length;
            this.n = matrix[0].length;
            this.matrixFrac = matrix;
        }
        public Matrix subtract(Matrix anotherMatrix){
            Matrix result = new Matrix(anotherMatrix.m, anotherMatrix.n);
            for(int i = 0; i < anotherMatrix.m; i++){
                for(int j = 0; j < anotherMatrix.n; j++){
                    result.matrixFrac[i][j] = this.matrixFrac[i][j].subtract(anotherMatrix.matrixFrac[i][j]);
                }
            }
            return result;
        }

        public static Matrix identity(int m){
            Matrix result = new Matrix(m, m);
            for(int i = 0; i < m; i++){
                for(int j = 0; j < m; j++){
                    if(i == j){
                        result.matrixFrac[i][i] = new Fraction(1, 1);
                    }
                    else{
                        result.matrixFrac[i][j] = new Fraction(0, 1);
                    }
                }
            }
            return result;
        }

        public static Matrix inverse(Matrix B){

            Matrix A = new Matrix(B.m, B.m);
            for(int i = 0; i < B.m; i++){
                for(int j = 0 ; j < B.m; j++){
                    A.matrixFrac[i][j] = B.matrixFrac[i][j];
                }
            }

            Matrix Id = Matrix.identity(A.m);
            Fraction temp;

            for(int k = 0; k < A.m; k++){
                temp = A.matrixFrac[k][k];

                for(int j = 0; j < A.m; j++){
                    A.matrixFrac[k][j] = A.matrixFrac[k][j].divide(temp);
                    Id.matrixFrac[k][j] = Id.matrixFrac[k][j].divide(temp);
                }

                for(int i = k + 1; i < A.m; i++){
                    temp = A.matrixFrac[i][k];

                    for(int j = 0; j < A.m; j++){
                        A.matrixFrac[i][j] = A.matrixFrac[i][j].subtract(A.matrixFrac[k][j].multiply(temp));
                        Id.matrixFrac[i][j] = Id.matrixFrac[i][j].subtract(Id.matrixFrac[k][j].multiply(temp));
                    }
                }
            }

            for(int k = A.m - 1; k > 0; k--){
                for(int i = k - 1; i >= 0; i--){
                    temp = A.matrixFrac[i][k];

                    for(int j = 0; j < A.m ; j++){
                        A.matrixFrac[i][j] = A.matrixFrac[i][j].subtract(A.matrixFrac[k][j].multiply(temp));
                        Id.matrixFrac[i][j] = Id.matrixFrac[i][j].subtract(Id.matrixFrac[k][j].multiply(temp));
                    }
                }
            }

            for(int i = 0; i < A.m; i++){
                for(int j = 0; j < A.m; j++){
                    A.matrixFrac[i][j] = Id.matrixFrac[i][j];
                }
            }
            return A;
        }
        public static Matrix dot(Matrix A, Matrix B){
            Matrix C = new Matrix(A.m, B.n);
            for(int i = 0; i < A.m; i++){
                for(int j = 0; j < B.n; j++){
                    for(int k = 0; k < B.m; k++){
                        C.matrixFrac[i][j] = C.matrixFrac[i][j].add(A.matrixFrac[i][k].multiply(B.matrixFrac[k][j]));
                    }
                }
            }
            return C;
        }

        @Override
        public String toString(){
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < this.m; i++){
                for(int j = 0; j < this.n; j++){
                    builder.append(this.matrixFrac[i][j] + " ");
                }
                builder.append("\n");
            }
            return builder.toString();
        }

    }

    public static int[] solution(int[][] m) {
        // Your code here
        boolean[] isIntermediate = new boolean[m.length];
        int[] sums = new int[m.length];

        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                if(m[i][j] > 0){
                    isIntermediate[i] = true;
                }
            }
        }

        for(int i = 0; i < m.length; i++){
            if(!isIntermediate[i]){
                m[i][i] = 1;
            }
        }

        int countOfTerminalStates = 0;
        for(int i = 0; i < m.length; i++){
            if(!isIntermediate[i]){
                countOfTerminalStates++;
            }
        }

        if(!isIntermediate[0]){
            int[] answer = new int[countOfTerminalStates + 1];
            answer[0] = 1;
            answer[countOfTerminalStates] = 1;
            for(int i = 1; i < countOfTerminalStates; i++){
                answer[i] = 0;
            }
            return answer;
        }

        for(int i = 0; i < m.length; i++){
            int sum = 0;
            for(int j = 0; j < m[0].length; j++){
                sum += m[i][j];
            }
            sums[i] = sum;
        }


        Fraction[][] pMatrix = new Fraction[m.length][m.length];

        for(int i = 0 ; i < m.length; i++){
            for(int j = 0 ; j < m[0].length; j++){
                pMatrix[i][j] = new Fraction((m[i][j]), (sums[i]));
            }
        }

        Matrix P = new Matrix(pMatrix);

        Fraction[][] Q = new Fraction[m.length - countOfTerminalStates][m.length - countOfTerminalStates];
        int index1 = 0;
        int index2 = 0;
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                if(isIntermediate[j] && isIntermediate[i]){
                    Q[index1][index2] = pMatrix[i][j];
                    index2++;
                }
            }
            if(isIntermediate[i]){
                index1++;
            }
            index2 = 0;
        }

        Fraction[][] R = new Fraction[m.length - countOfTerminalStates][countOfTerminalStates];
        index1 = 0;
        index2 = 0;
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                if(!isIntermediate[j] && isIntermediate[i]){
                    R[index1][index2] = pMatrix[i][j];
                    index2++;
                }
            }
            if(isIntermediate[i]) {
                index1++;
            }
            index2 = 0;
        }

        Matrix QMatrix = new Matrix(Q);
        Matrix RMatrix = new Matrix(R);

        Matrix identity = Matrix.identity(m.length - countOfTerminalStates);
        Matrix IminusQ = identity.subtract(QMatrix);
        System.out.println(Matrix.inverse(IminusQ));
        Matrix B = Matrix.dot(Matrix.inverse(IminusQ), RMatrix);


        Fraction[] firstString = B.matrixFrac[0];
        int[] answer = new int[countOfTerminalStates + 1];
        long lcm = 1;
        for(int i = 0; i < countOfTerminalStates; i++){
            if(firstString[i].getA() == 0){
                answer[i] = 0;
            }
            else lcm = findLCM(lcm, firstString[i].getB());
        }

        for(int i = 0; i < countOfTerminalStates; i++){
            if(firstString[i].getA() != 0){
                answer[i] = (int)(firstString[i].getA() * (lcm /(firstString[i].getB())));
            }
        }
        answer[countOfTerminalStates] = (int)lcm;
        return answer;
    }

    public static void main(String[] args) {
        int[][] example1 = new int[][] {{0, 1, 0 , 0, 0, 1}, {4, 0, 0 , 3, 2, 0}, {0, 0, 0 , 0, 0, 0}, {0, 0, 0 , 0, 0, 0}, {0, 0, 0 , 0, 0, 0}, {0, 0, 0 , 0, 0, 0}};
        int[][] example2 = new int[][] {{4, 0, 0 , 3, 2, 0}, {0, 0, 0 , 0, 0, 0}, {0, 0, 0 , 0, 0, 0}, {0, 1, 0 , 0, 0, 1}, {0, 0, 0 , 0, 0, 0}, {0, 0, 0 , 0, 0, 0}};
        int[][] example3 = new int[][] {{0, 2, 1, 0, 0}, {0, 0, 0, 3, 4}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
        int[][] example4 = new int[][]{{0, 0}, {1, 1}};
        int[][] example5 = new int[][]{{0, 0, 0, 0 ,0}, {0, 0, 0 ,0 ,0}, {2, 2, 2, 2 ,2}, {0, 0, 0 ,0 ,0}, {2, 2, 2, 2 ,2}};
        int[][] example6 = new int[][] {{0}};
        int[][] example7 = new int[][]{{101, 25, 3, 289}, {0, 0, 0, 0}, {1, 1, 458 , 0}, {0, 0, 0, 0}};
        int[][] example8 = new int[][]{{1,2,3,0,0,0},{4,5,6,0,0,0},{7,8,9,1,0,0},{0,0,0,0,1,2},{0,0,0,0,0,0},{0,0,0,0,0,0}};
        int[][] example9 = new int[][]{{1, 2, 3, 0, 1, 1},{4,5,6,0,0,0},{7,8,9,1,0,0},{0,0,0,0,1,2},{0,0,0,0,0,0},{0,0,0,0,0,0}};
        solution(example1);
        System.out.println();
        solution(example2);
        System.out.println();
        solution(example3);
        System.out.println();
        solution(example4);
        System.out.println();
        solution(example5);
        System.out.println();
        solution(example6);
        System.out.println();
        solution(example7);
        System.out.println();
        solution(example9);

        int[][] example10 = new int[][]{
                {5, 13, 0, 0, 7, 21, 1024},
                {1, 2, 3, 0, 8, 6, 0},
                {7, 5, 2, 1, 2048, 0, 9},
                {0, 0, 0, 0, 0, 0 , 0},
                {10, 9, 8, 228, 729, 630, 1},
                {15, 10000, 3, 0, 0, 0, 8},
                {0, 0, 0, 0, 0, 0 , 0}
        };
        solution(example10);
    }
}