package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * a Ray with a source and direction.
 */
class Ray {
    private final Vector3D m_source;
    private final Vector3D m_direction;
    private final Vector3D m_invDirection;
    private final int[] m_sign;

    /**
     * Constructor.
     *
     * @param source    source of the ray.
     * @param direction direction of the ray.
     */
    Ray(Vector3D source, Vector3D direction) {
        m_source = source;
        m_direction = direction.normalize();
        m_invDirection = new Vector3D(1.0f / m_direction.getX(), 1.0f / m_direction.getY(), 1.0f / m_direction.getZ());
        m_sign = new int[3];
        m_sign[0] = (m_direction.getX() < 0.0) ? 1 : 0;
        m_sign[1] = (m_direction.getY() < 0.0) ? 1 : 0;
        m_sign[2] = (m_direction.getZ() < 0.0) ? 1 : 0;
    }

    /**
     * gets the source.
     */
    Vector3D source() {
        return m_source;
    }

    /**
     * gets the direction.
     */
    Vector3D direction() {
        return m_direction;
    }

    /**
     * gets the invDirection.
     */
    Vector3D invDirection() {
        return m_invDirection;
    }

    class projectResult {
        public final double t;
        public final Vector3D delta;

        public projectResult(double _t, Vector3D _delta) {
            t = _t;
            delta = _delta;
        }
    }
    /**
     * projects a point on this ray. point = t*source + delta.
     *
     * @param point the point.
     * @return the multi factor t.
     */
    public projectResult project(Vector3D point) {
        /* TODO return type to be informed*/
        double t = (point.subtract(source())).dotProduct(direction());
        Vector3D delta = (point.subtract(source())).subtract(direction().scalarMultiply(t));
        return new projectResult(t, delta);
    }

    /**
     * gets the sign[3].
     */
    int[] getSign() {
        return m_sign;
    }

    /**
     * gets the formatted String of Ray.
     *
     * @return (source, direction).
     */
    public String toString() {
        return "(" + source().toString() + "," + direction().toString() + ")";
    }
}
