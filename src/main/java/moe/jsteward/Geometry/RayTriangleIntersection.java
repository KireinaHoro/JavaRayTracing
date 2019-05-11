package moe.jsteward.Geometry;

import java.math.BigInteger;

/**
 * an intersection between a ray and a triangle.
 */
public class RayTriangleIntersection {

    protected
    /* distance between source and intersection **/
            double m_t;
    /* u coord of intersection **/
    double m_u;
    /* v coord of intersection **/
    double m_v;
    /* if intersection is valid **/
    boolean m_valid;
    /* triangle associated to this intersection **/
    Triangle m_triangle;

    /**
     * Constructor.
     *
     * @param triangle associated triangle.
     * @param ray      the ray.
     */
    public RayTriangleIntersection(Triangle triangle, Ray ray) {
        m_triangle = triangle;
        m_valid = triangle -> intersectionValid(ray);
        double[] Params = triangle -> intersectionParams(ray);
        m_t = Params[0];
        m_u = Params[1];
        m_v = Params[2];
        /* TODO intersection -> ~Valid & ~Params */
    }

    /**
     * Constructor.
     * no association, no validation(?)
     */
    public RayTriangleIntersection() {
        m_triangle = null;
        m_valid = false;
    }

    /**
     * validates intersection.
     */
    public boolean valid() {
        return m_valid;
    }

    /**
     * gets distance between source and intersection.
     */
    public double tRayValue() {
        return m_t;
    }

    /**
     * gets u coord of intersection.
     */
    public double uTriangleValue() {
        return m_u;
    }

    /**
     * gets v coord of intersection.
     */
    public double vTriangleValue() {
        return m_v;
    }

    /**
     * gets associated triangle.
     */
    public Triangle triangle() {
        return m_triangle;
    }

    /**
     * returns intersection point.
     */
    public Vector3f intersection() {
        return m_triangle -> samplePoint(m_u, m_v);
    }

    /**
     * compares this intersection with another i.
     *
     * @return -1 if this is lessThan, 1 else.
     */
    public int compareTo(RayTriangleIntersection i) {
        /* TODO this is different from original < operator */
        if ((m_valid & i.m_valid & (m_t < i.m_t)) | (!i.m_valid))
            return -1;
        return 1;
    }
}
