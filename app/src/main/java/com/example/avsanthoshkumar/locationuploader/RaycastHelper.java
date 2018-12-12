package com.example.avsanthoshkumar.locationuploader;

/**
 * Created by AV SANTHOSH KUMAR on 30-10-2018.
 */

import android.graphics.Point;
import android.util.Log;
import java.util.ArrayList;
public class RaycastHelper {

    public static boolean isPointInside(ArrayList<Point1> points, Point1 point) {
        return isPointInsideEdges(getEdgesFromPoints(points), point);
    }





    private static ArrayList<Edge> getEdgesFromPoints(ArrayList<Point1> points) {
        ArrayList<Edge> edges = new ArrayList<>();
        for(int i = 0; i < points.size(); i++) {
            edges.add(new Edge(points.get(i), i < points.size()-1 ? points.get(i+1) : points.get(0)));
        }

        return edges;
    }

    /**
     * Assuming that the passed edges form a closed polygon, this method
     * tells whether the given point lies inside or outside the polygon
     *
     * @param edges
     * @param point
     * @return
     */
    private static boolean isPointInsideEdges(ArrayList<Edge> edges, Point1 point) {
        Log.d("RAY CAST", "Point: " + point.getX() + "; " + point.getY());
        Log.d("RAY CAST", "Lines ******************");
        for(Edge edge : edges) {
            Log.d("RAY CAST", edge.getStartX() + "," + edge.getStartY() + "; " + edge.getEndX() + "," + edge.getEndY());
        }
        Log.d("RAY CAST", "Lines ******************");
        int intersectionCount = 0;

        for(Edge edge : edges) {
            if(LineUtil.linesIntersect(edge.getStartX(), edge.getStartY(), edge.getEndX(), edge.getEndY(), point.getX(), point.getY(), Double.MAX_VALUE, Double.MAX_VALUE)) {
                intersectionCount++;
            }
        }
        Log.d("RAY CAST", "intersections: " + intersectionCount);

        return (intersectionCount % 2 != 0);
    }
}
