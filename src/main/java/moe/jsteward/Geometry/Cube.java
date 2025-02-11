package moe.jsteward.Geometry;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Cube extends Geometry {

    //protected Square m_squeare[] = new Square[6];
    /*
     *  Constructor for Cube
     */
    public Cube(PhongMaterialEx material) {
        Square sq0 = new Square(material);
        sq0.translate(new Vector3D(0.0, 0.0, 0.5));
        merge(sq0);
        final double PI = 3.1415926535897932384626433832795;

        Square sq1 = new Square(material);
        sq1.translate(new Vector3D(0.0, 0.0, 0.5));
        sq1.rotate(new Quaternion(
                Math.cos(Math.PI / 4.0),
                (new Vector3D(1.0, 0.0, 0.0)).scalarMultiply(Math.sin(Math.PI / 4.0f)).toArray()).normalize()
        );
        merge(sq1);

        Square sq2 = new Square(material);
        sq2.translate(new Vector3D(0.0, 0.0, 0.5));
        sq2.rotate(new Quaternion(
                Math.cos(Math.PI / 2.0),
                (new Vector3D(1.0, 0.0, 0.0)).scalarMultiply(Math.sin(Math.PI / 2.0)).toArray()).normalize()
        );
        merge(sq2);

        Square sq3 = new Square(material);
        sq3.translate(new Vector3D(0.0, 0.0, 0.5));
        sq3.rotate(new Quaternion(
                Math.cos(-Math.PI / 4.0),
                (new Vector3D(1.0, 0.0, 0.0)).scalarMultiply(Math.sin(-Math.PI / 4.0)).toArray()).normalize()
        );
        merge(sq3);

        Square sq4 = new Square(material);
        sq4.translate(new Vector3D(0.0, 0.0, 0.5));
        sq4.rotate(new Quaternion(
                Math.cos(Math.PI / 4.0),
                (new Vector3D(0.0, 1.0, 0.0)).scalarMultiply(Math.sin(Math.PI / 4.0)).toArray()).normalize()
        );
        merge(sq4);

        Square sq5 = new Square(material);
        sq5.translate(new Vector3D(0.0, 0.0, 0.5));
        sq5.rotate(new Quaternion(
                Math.cos(-Math.PI / 4.0),
                (new Vector3D(0.0, 1.0, 0.0)).scalarMultiply(Math.sin(-Math.PI / 4.0)).toArray()).normalize()
        );
        merge(sq5);
    }
}
