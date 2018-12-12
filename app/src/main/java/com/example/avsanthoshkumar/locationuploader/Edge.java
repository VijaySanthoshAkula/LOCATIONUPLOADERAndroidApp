package com.example.avsanthoshkumar.locationuploader;

/**
 * Created by AV SANTHOSH KUMAR on 30-10-2018.
 */



import java.io.Serializable;

/**
 * Created by Infernus on 05/06/15.
 */
public class Edge implements Serializable {
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public Edge(Point1 a, Point1 b) {
        this.startX = a.getX();
        this.startY = a.getY();
        this.endX = b.getX();
        this.endY = b.getY();
    }

    public Edge(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}