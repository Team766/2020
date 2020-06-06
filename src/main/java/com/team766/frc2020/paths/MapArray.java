package com.team766.frc2020.paths;


public class MapArray {
    
    private double[][] imageDoubleArray = new double[707][1384];
    private double[][] imageDoubleArrayLowRes = new double[707/6][1384/6];
    private double[][] imageBoolArray = new double[707][1384];
    
    public MapArray() {};

    // public MapArray(double x, double y) {
    //     this.x = x;
    //     this.y = y;
    //     this.curvature = 0;
    //     this.velocity = 0;
    // };



    public void setArray(int row, int col, double value) {
        imageDoubleArray[row][col] = value;
    }

    public void setLowResArray(int row, int col, double value) {
        imageDoubleArrayLowRes[row][col] = value;
    }
}