package com.example.ninerjaunt;


class Point {
    private Point parent;
    private double[] cord;
    private double g, h, f;
    private final double radius = 0.0001;

    //Create Point object with self, a parent Point, x,y coordinates, and a heuristic value
    Point(Point p, double lat, double lon, double gcost, double heu) {
        parent = p;
        cord = new double[]{lat, lon};
        g = 1;
        h = heu;
        f = g + h;
    }

    Point(Point p) {
        parent = p.parent;
        cord = new double[]{p.getCord()[0], p.getCord()[1]};
        g = 1;
        h = p.h;
        f = p.f;
    }

    public String toString() {
        return "" + String.valueOf(cord[0]) + "," + String.valueOf(cord[1]) + "";
    }

    public boolean equals(Point p) {
        return ((cord[0] - p.cord[0]) == 0.0 && (cord[1] - p.cord[1]) == 0.0);
    }

    public double calcDist(double[] cord1) {
        return Math.sqrt(Math.pow(cord[0] - cord1[0], 2) + Math.pow(cord[1] - cord1[1], 2));
    }

    public boolean equals(double[] p) {
        return calcDist(p) < radius;
    }

    //Return the parent Point of the calling Point
    Point getParent() {
        return parent;
    }

    //Return (x,y) coordinates of calling Point
    double[] getCord() {
        return cord;
    }

    double getF() {
        return f;
    }
}