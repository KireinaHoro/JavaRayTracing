package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

class Square extends Geometry {
    /*
     *  Constructor for Square
     */
    Square(PhongMaterialEx material) {
        int p0 = addVertex(new Vector3D(0.5, 0.5, 0.0));
        int p1 = addVertex(new Vector3D(0.5, -0.5, 0.0));
        int p2 = addVertex(new Vector3D(-0.5, 0.5, 0.0));
        int p3 = addVertex(new Vector3D(-0.5, -0.5, 0.0));
        addTriangle(p0, p1, p2, material);
        addTriangle(p1, p2, p3, material);
    }
}
