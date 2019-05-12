package moe.jsteward.Geometry;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Camera {
    //  The camera position.
    protected Vector3D m_position;

    //  The aim of the camera.
    protected Vector3D m_target;

    //  Distance of the focal plane.
    protected double m_planeDistance;

    //  Width of the projection rectangle.
    protected double m_planeWidth;

    //  Height of the projection rectangle.
    protected double m_planeHeight;

    //  The front vector of the camera.
    protected Vector3D m_front;

    //  The right vector.
    protected Vector3D m_right;

    //  The down vector.
    protected Vector3D m_down;

    //  The width vector of the projection rectangle.
    protected Vector3D m_widthVector;

    //  The height vector of the projection rectangle.
    protected Vector3D m_heightVector;

    //  The upper left point oft he projection rectangle.
    protected Vector3D m_upLeftPoint;

    /*
     *  rotate a Quaternion by another Quaternion
     */
    public Vector3D rotate(Quaternion m1, Quaternion m2) {
        return new Vector3D(m1.multiply(m2).multiply(m1.getInverse()).getVectorPart());
    }

    /*
     *  Calculates the camera parameters.
     */
    protected void computeParameters() {
        m_front = m_target.subtract(m_position);
        m_front = m_front.normalize();
        m_right = rotate(new Quaternion(-3.14159265f / 2.0, 0.0, 0.0, 1.0), new Quaternion(m_front.toArray()));
        m_right = new Vector3D(m_right.getX(), m_right.getY(), 0.0);
        m_right = m_right.normalize();
        m_down = m_front.crossProduct(m_right);
        m_down = m_down.normalize();
        m_widthVector = m_right.scalarMultiply(m_planeWidth);
        m_heightVector = m_down.scalarMultiply(m_planeHeight);
        m_upLeftPoint = m_position.add(m_front.scalarMultiply(m_planeDistance))
                .subtract(m_widthVector.scalarMultiply(0.5))
                .subtract(m_heightVector.scalarMultiply(0.5));

    }

    /*
     *  Constructor for Camera
     */
    public Camera(Vector3D position, Vector3D target, double planeDistance, double planeWidth, double planeHeight) {
        m_position = new Vector3D(position.toArray());
        m_target = new Vector3D(target.toArray());
        m_planeDistance = planeDistance;
        m_planeWidth = planeWidth;
        m_planeHeight = planeHeight;
        computeParameters();
    }

    /*
     *  Translates the camera in local coordinates (X = right, Y = front, Z=up).
     *  \param1 translation := The translation vector.
     */
    public void translateLocal(Vector3D translation) {
        Vector3D trans = m_right.scalarMultiply(translation.getX()).add(m_front.scalarMultiply(translation.getY())).subtract(m_down.scalarMultiply(translation.getZ()));
        m_position = m_position.add(trans);
        m_target = m_target.add(trans);
        computeParameters();
    }

    /*
     *  Sets the camera position position.
     *  \param1	position :=  The new camera position.
     */
    public void setPosition(Vector3D position) {
        m_position = position;
        computeParameters();
    }

    /*
     *  Sets the target.
     *  \param1	target :=  The new target.
     */
    void setTarget(Vector3D target) {
        m_target = target;
        computeParameters();
    }

    /*
     *  Get a primary ray from screen coordinates (coordX, coordY)
     *  \param1	coordX :=  X coordinate in the projection rectangle.
     *  \param2	coordY :=  Y coordinate in the projection rectangle.
     */
    Ray getRay(double coordX, double coordY) {
        return new Ray(m_position, m_widthVector.scalarMultiply(coordX).add(m_upLeftPoint).add(m_heightVector.scalarMultiply(coordY)).subtract(m_position));
    }
}
