package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Deque;
import java.util.LinkedList;

public class BoundingBox {
    //  The bounds (min / max vectors).
    private final Vector3D[] m_bounds = new Vector3D[2];

    /*
     *  Initializes the bounding box from the provided geometry.
     *  \param1	geometry :=  The geometry.
     */
    BoundingBox(final Geometry geometry) {
        set(geometry);
    }

    /*
     *  Constructor.
     *  \param1	minVertex :=  The smallest coordinates on X, Y, Z axes.
     *  \param2	maxVertex :=  The highest coordinates on X, Y, Z axes.
     */
    public BoundingBox(final Vector3D minVertex, final Vector3D maxVertex) {
        m_bounds[0] = minVertex;
        m_bounds[1] = maxVertex;
    }

    /*
     *  Constructor.
     *  \param1	minVertex :=  The smallest coordinates on X, Y, Z axes.
     */
    public BoundingBox(final Vector3D minVertex) {
        m_bounds[0] = minVertex;
        m_bounds[1] = new Vector3D(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    /*
     *  Constructor.
     */
    BoundingBox() {
        m_bounds[0] = new Vector3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        m_bounds[1] = new Vector3D(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    /*
     *  Tests wether the box is empty of not.
     */
    public boolean isEmpty() {
        boolean result = false;
        if (m_bounds[0].getX() > m_bounds[1].getX()) result = true;
        if (m_bounds[0].getY() > m_bounds[1].getY()) result = true;
        if (m_bounds[0].getZ() > m_bounds[1].getZ()) result = true;
        return result;
    }

    /*
     *  Sets the bouding box with the given geometry.
     *  \param1	geometry	The geometry.
     */
    private void set(final Geometry geometry) {
        Deque<Vector3D> vertices = new LinkedList<>(geometry.getVertices());
        m_bounds[0] = vertices.getFirst();
        m_bounds[1] = vertices.getFirst();
        update(geometry);
    }

    /*
     *  return min(vector1,vector2)
     */
    private Vector3D vectormin(Vector3D v1, Vector3D v2) {
        return new Vector3D(Math.min(v1.getX(), v2.getX()), Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));
    }

    /*
     *  return max(vector1,vector2)
     */
    private Vector3D vectormax(Vector3D v1, Vector3D v2) {
        return new Vector3D(Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()), Math.max(v1.getZ(), v2.getZ()));
    }

    /*
     *  Updates the bounding box with the given geometry.
     *  \param1	geometry :=  The geometry.
     */
    private void update(final Geometry geometry) {
        Deque<Vector3D> vertices = new LinkedList<>(geometry.getVertices());
        for (Vector3D theNext : vertices) {
            m_bounds[0] = vectormin(m_bounds[0], theNext);
            m_bounds[1] = vectormax(m_bounds[1], theNext);
        }
    }

    /*
     *  Updates the bounding box with the provided triangle.
     *  \param1 triangle
     */
    void update(final Triangle triangle) {
        update(triangle.vertex(0));
        update(triangle.vertex(1));
        update(triangle.vertex(2));
    }

    /*
     *  Updates the bounding box with the provided point.
     *  \param1 Vector3D
     */
    private void update(final Vector3D V) {
        m_bounds[0] = vectormin(m_bounds[0], V);
        m_bounds[1] = vectormax(m_bounds[1], V);
    }

    /*
     *  Updates this bounding box to bound the given boundingBox.
     *  \param1	boundingBox
     */
    void update(final BoundingBox boundingBox) {
        m_bounds[0] = vectormin(m_bounds[0], boundingBox.m_bounds[0]);
        m_bounds[1] = vectormax(m_bounds[1], boundingBox.m_bounds[1]);
    }

    /*
     *  return simdMul(vector1, vector2)
     */
    private Vector3D simdMul(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.getX() * v2.getX(), v1.getY() * v2.getY(), v1.getZ() * v2.getZ());
    }

    /*
     *  Tests if the provided ray intersects this box.
     *  \param1	ray
     *  \param e[0] = entryT , e[1] = exitT
     *  \return	true if an intersection is found, false otherwise.
     */
    boolean intersect(final Ray ray, double t0, double t1, double[] e) {

        int[] sign = ray.getSign();
        Vector3D tmin = new Vector3D(m_bounds[sign[0]].getX(), m_bounds[sign[1]].getY(), m_bounds[sign[2]].getZ());
        tmin = simdMul(tmin.subtract(ray.source()), ray.invDirection());

        Vector3D tmax = new Vector3D(m_bounds[1 - sign[0]].getX(), m_bounds[1 - sign[1]].getY(), m_bounds[1 - sign[2]].getZ());
        tmax = simdMul(tmax.subtract(ray.source()), ray.invDirection());

        if ((tmin.getX() > tmax.getY()) || (tmin.getY() > tmax.getX())) return false;

        if (tmin.getY() > tmin.getX()) tmin = new Vector3D(tmin.getY(), tmin.getY(), tmin.getZ());

        if (tmax.getY() < tmax.getX()) tmax = new Vector3D(tmax.getY(), tmax.getY(), tmax.getZ());

        if ((tmin.getX() > tmax.getZ()) || (tmin.getZ() > tmax.getX())) return false;

        if (tmin.getZ() > tmin.getX()) tmin = new Vector3D(tmin.getZ(), tmin.getY(), tmin.getZ());

        if (tmax.getZ() < tmax.getX()) tmax = new Vector3D(tmax.getZ(), tmax.getY(), tmax.getZ());

        boolean intersectionFound = (tmin.getX() < t1) && (tmax.getX() > t0);
        if (intersectionFound) {
            e[0] = Math.max(tmin.getX(), t0);
            e[1] = Math.min(tmax.getX(), t1);
        }
        return intersectionFound;
    }

    boolean intersect(final Ray ray) {

        int[] sign = ray.getSign();
        Vector3D tmin = new Vector3D(m_bounds[sign[0]].getX(), m_bounds[sign[1]].getY(), m_bounds[sign[2]].getZ());
        tmin = simdMul(tmin.subtract(ray.source()), ray.invDirection());

        Vector3D tmax = new Vector3D(m_bounds[1 - sign[0]].getX(), m_bounds[1 - sign[1]].getY(), m_bounds[1 - sign[2]].getZ());
        tmax = simdMul(tmax.subtract(ray.source()), ray.invDirection());

        if ((tmin.getX() > tmax.getY()) || (tmin.getY() > tmax.getX())) return false;

        if (tmin.getY() > tmin.getX()) tmin = new Vector3D(tmin.getY(), tmin.getY(), tmin.getZ());

        if (tmax.getY() < tmax.getX()) tmax = new Vector3D(tmax.getY(), tmax.getY(), tmax.getZ());

        if ((tmin.getX() > tmax.getZ()) || (tmin.getZ() > tmax.getX())) return false;

        if (tmin.getZ() > tmin.getX()) tmin = new Vector3D(tmin.getZ(), tmin.getY(), tmin.getZ());

        if (tmax.getZ() < tmax.getX()) tmax = new Vector3D(tmax.getZ(), tmax.getY(), tmax.getZ());

        return true;
    }

    public Vector3D min() {
        return m_bounds[0];
    }

    public Vector3D max() {
        return m_bounds[1];
    }
}
