package moe.jsteward.Math;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;


/**
 * random direction sampling. Biased by a cosine distribution,
 * useful for respecting a BRDF distribution (Diffuse or Specular).
 */
class RandomDirection {
    private final long m_scramble;
    private final Vector3D m_direction;
    private final double m_n;

    /**
     * random sampling of spherical coordinates, n = 1.0.
     */
    protected MutablePair<Double, Double> randomPolar() {
        return randomPolar(1.0);
    }

    /**
     * computes normalized vector with spherical coordinates.
     *
     * @param theta coord one
     * @param phy   coord two
     * @return created vector
     */
    protected Vector3D getVector(double theta, double phy) {
        return new Vector3D(Math.sin(theta) * Math.cos(phy),
                Math.sin(theta) * Math.sin(phy), Math.cos(theta));
    }

    private long m_index;

    /**
     * a random value in [min, max)
     *
     * @return a random value in [min, max)
     */
    public static double random(double min, double max) {
        double value = random();
        return value * (max - min) + min;
    }

    private Vector3D m_directionNormal;
    /**
     * Constructor.
     *
     * @param direction main direction for random sampling.
     * @param n         shininess of surface.
     */
    private RandomDirection(Vector3D direction, double n) {
        m_direction = direction.normalize();
        m_n = n;
        m_scramble = 0;
        m_index = (int) (Math.random() * Integer.MAX_VALUE);

        m_directionNormal = new Vector3D(1.0, 0.0, 0.0);
        m_directionNormal = m_directionNormal.subtract(m_direction.scalarMultiply(m_direction.dotProduct(m_directionNormal)));
        if (m_directionNormal.getNorm() < 10.0 * Double.MIN_VALUE) {
            m_directionNormal = new Vector3D(0.0, 1.0, 0.0);
            m_directionNormal = m_directionNormal.subtract(m_direction.scalarMultiply(m_direction.dotProduct(m_directionNormal)));
            if (m_directionNormal.getNorm() < 10.0 * Double.MIN_VALUE) {
                m_directionNormal = new Vector3D(0.0, 0.0, 1.0);
                m_directionNormal = m_directionNormal.subtract(m_direction.scalarMultiply(m_direction.dotProduct(m_directionNormal)));
            }
        }
        m_directionNormal = m_directionNormal.normalize();
    }

    /**
     * a random value in [0,1)
     *
     * @return a random value in [0,1)
     */
    private static double random() {
        /* TODO original [0, 1] */
        return Math.random();
    }

    /**
     * random sampling of spherical coordinates.
     *
     * @param n the shininess (1.0 if diffuse)
     */
    private MutablePair<Double, Double> randomPolar(double n) {
        double rand1 = Sobol.sample(m_index, 0, m_scramble);
        double p = Math.pow(rand1, 1 / (n + 1));
        double theta = Math.acos(p);
        double rand2 = Sobol.sample(m_index, 1, m_scramble);
        double phy = 2 * Math.PI * rand2;
        m_index++;
        return new MutablePair<>(theta, phy);
    }

    /**
     * Constructor, n = 1.0.
     */
    public RandomDirection(Vector3D direction) {
        this(direction, 1.0);
    }


    /**
     * generates a random direction respecting cos^n distribution.
     *
     * @return the random direction.
     */
    Vector3D generate() {
        MutablePair<Double, Double> perturbation = randomPolar(m_n);
        Quaternion q1 = new Quaternion(m_directionNormal.getX(), m_directionNormal.getY(), m_directionNormal.getZ(), perturbation.left);
        Quaternion q2 = new Quaternion(m_direction.getX(), m_direction.getY(), m_direction.getZ(), perturbation.right);
        // rotate : (this.multiply(q)).multiply(this.getConjugate())
        q1 = (q1.multiply(new Quaternion(m_direction.toArray()))).multiply(q1.getConjugate());
        q2 = (q2.multiply(q1)).multiply(q2.getConjugate());
        return new Vector3D(q2.getVectorPart());
    }


}
