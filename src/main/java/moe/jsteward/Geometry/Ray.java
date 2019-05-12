package moe.jsteward.Geometry;

/**
 * a Ray with a source and direction.
 */
public class Ray {
    protected
    Vector3f m_source;
    Vector3f m_direction;
    Vector3f m_invDirection;
    int[] m_sign;

    /**
     * Constructor.
     *
     * @param source    source of the ray.
     * @param direction direction of the ray.
     */
    public Ray(Vector3f source, Vector3f direction) {
        m_source = source;
        m_direction = direction / direction.norm();
        m_invDirection = Vector3f.makeVector(1.0f / m_direction[0], 1.0f / m_direction[1], 1.0f / m_direction[2]);
        m_sign = new int[3];
        m_sign[0] = (m_direction[0] < 0.0) ? 1 : 0;
        m_sign[1] = (m_direction[0] < 0.0) ? 1 : 0;
        m_sign[2] = (m_direction[0] < 0.0) ? 1 : 0;
        /* TODO */
    }

    /**
     * gets the source.
     */
    Vector3f source() {
        return m_source;
    }

    /**
     * gets the direction.
     */
    Vector3f direction() {
        return m_direction;
    }

    /**
     * gets the invDirection.
     */
    Vector3f invDirection() {
        return m_invDirection;
    }

    /**
     * projects a point on this ray. point = t*source + delta.
     *
     * @param point the point.
     * @param delta add factor.
     * @return the multi factor t.
     */
    public double project(Vector3f point, Vector3f delta) {
        /* TODO */
        return 1.0;
    }

    /**
     * gets the sign[3].
     */
    public int[] getSign() {
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
