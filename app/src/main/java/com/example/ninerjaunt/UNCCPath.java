package com.example.ninerjaunt;

import android.os.Build;

import java.util.Comparator;
import java.util.LinkedList;
import java.io.*; //needed for File and IOException
import java.util.*; //needed for Scanner class


public class UNCCPath {

    final double radius = 0.0002;

    private double[] goal, startLoc;
    private Point start;

    UNCCPath(double startLat, double startLon, double endLat, double endLon) {
        goal = new double[]{endLat, endLon};
        startLoc = new double[]{startLat, startLon};
        start = new Point(null, startLat, startLon, 1, calcH(startLoc, goal)); //starting building
    }

    public double calcH(double[] cord1, double[] cord2) {
        return Math.sqrt(Math.pow(cord2[0] - cord1[0], 2) + Math.pow(cord2[1] - cord1[1], 2));
    }

    private LinkedList<double[]> getChildren(double[] loc) throws IOException {

        LinkedList<double[]> children = new LinkedList<>();
        double distOfKid = 0;

        Scanner input = new Scanner(MainActivity.text);
        StringTokenizer st;
        double[] kid;

        while (input.hasNext()) {
            st = new StringTokenizer(input.nextLine(), ",");
            kid = new double[2];

            kid[0] = Double.parseDouble(st.nextToken());
            kid[1] = Double.parseDouble(st.nextToken());

            distOfKid = calcH(kid, loc);

            if (distOfKid <= radius) {
                children.push(kid);
            }

        }
        input.close();
        return children;
    }

    private boolean listDoesNotContain(LinkedList<Point> l, double[] loc) {
        for (Point p : l)
            if (p.equals(loc)) return false;
        return true;
    }

    private LinkedList<double[]> genPath(Point node, Point start, double[] goal) {
        LinkedList<double[]> path = new LinkedList<>();
        path.addFirst(goal);
        while (!node.equals(start)) {
            path.addFirst(node.getCord());
            //get next node
            node = node.getParent();
        }
        path.addFirst(start.getCord());
        return path;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //														BEGIN A*												    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public LinkedList<double[]> getPath() throws IOException {

        LinkedList<Point> openlist = new LinkedList<>();
        LinkedList<Point> closedList = new LinkedList<>();

        Point kid;
        LinkedList<double[]> family;

        Point node = new Point(start);

        while (!node.equals(goal)) {

            if (!node.equals(goal)) {
                closedList.add(node);

                try {
                    family = getChildren(node.getCord());
                } catch (IOException e) {
                    return null;
                }

                while (family.size() != 0) {
                    double[] c = family.pop();

                    if (listDoesNotContain(openlist, c) && listDoesNotContain(closedList, c)) {
                        kid = new Point(node, c[0], c[1], calcH(c, node.getCord()), calcH(c, goal));
                        openlist.add(kid);
                    }

                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    openlist.sort(new Comparator<Point>() {
                        @Override
                        public int compare(Point o1, Point o2) {

                            return (o1.getF() - o2.getF()) <= 0.0 ? -1 : 1;

                        }
                    });
                }
            }

            //Debugging openlist
            /*for (Point x : openlist){
            	System.out.println(x.toString());
            }
            System.out.println();*/

            try {
                node = openlist.removeFirst();
            } catch (Exception e) {
                break;
            }

        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //														END A*												    //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return genPath(node, start, goal);
    }


}