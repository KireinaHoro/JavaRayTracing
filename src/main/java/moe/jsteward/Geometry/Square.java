package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Square extends Geometry {
    /*
     *  Constructor for Square
     */
    public Square(PhongMaterialEx material) {
        super();
        int p0 = addVertex(new Vector3D(0.5, 0.5, 0.0));
        int p1 = addVertex(new Vector3D(0.5, -0.5, 0.0));
        int p2 = addVertex(new Vector3D(-0.5, 0.5, 0.0));
        int p3 = addVertex(new Vector3D(-0.5, -0.5, 0.0));
        System.err.println("p0:" + p0 + " p1:" + p1 + " p2:" + p2 + " p3:" + p3);
        addTriangle(p0, p1, p2, material);
        addTriangle(p1, p2, p3, material);
    }
}
