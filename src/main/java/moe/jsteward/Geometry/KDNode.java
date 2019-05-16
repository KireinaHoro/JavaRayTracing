package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.LinkedList;
import java.util.List;

class KDNode {
    private final BoundingBox bbox;
    private KDNode left;
    private KDNode right;

    private List<Triangle> triangles;

    KDNode(List<Triangle> triangles, int depth) {
        bbox = new BoundingBox();

        if (triangles.size() == 0) {
            return;
        }

        Vector3D midPoint = new Vector3D(0, 0, 0);
        double scaleFactor = 1.0 / triangles.size();
        for (Triangle t : triangles) {
            midPoint = midPoint.add(t.center().scalarMultiply(scaleFactor));
            bbox.update(t);
        }
        if (triangles.size() <= 10) {
            this.triangles = triangles;
            return;
        }

        List<Triangle> leftTriangles = new LinkedList<>();
        List<Triangle> rightTriangles = new LinkedList<>();
        int axis = depth % 3;
        for (Triangle t : triangles) {
            if (getOnAxis(midPoint, axis) >= getOnAxis(t.center(), axis)) {
                rightTriangles.add(t);
            } else {
                leftTriangles.add(t);
            }
        }

        left = new KDNode(leftTriangles, depth + 1);
        right = new KDNode(rightTriangles, depth + 1);
    }

    BoundingBox getBbox() {
        return bbox;
    }

    KDNode getLeft() {
        return left;
    }

    KDNode getRight() {
        return right;
    }

    List<Triangle> getTriangles() {
        return triangles;
    }

    private double getOnAxis(Vector3D vector, int axis) {
        switch (axis) {
            case 0:
                return vector.getX();
            case 1:
                return vector.getY();
            case 2:
                return vector.getZ();
        }
        return 0;
    }
}
