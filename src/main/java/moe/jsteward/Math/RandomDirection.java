package moe.jsteward.Math;

import moe.jsteward.Math.MyPair;

import java.math.*;

/**
 * random direction sampling. Biased by a cosine distribution,
 * useful for respecting a BRDF distribution (Diffuse or Specular).
 */
public class RandomDirection {
    protected
    long m_scramble;
    long m_index;

    /**
     * random sampling of spherical coordinates.
     *
     * @param n the shininess (1.0 if diffuse)
     */
    protected MyPair<Double, Double> randomPolar(double n) {
        /* TODO */
        return new MyPair<Double, Double>();
    }

    /**
     * random sampling of spherical coordinates, n = 1.0.
     */
    protected MyPair<Double, Double> randomPolar() {
        /* TODO */
        return new MyPair<Double, Double>();
    }

    /**
     * computes normalized vector with spherical coordinates.
     *
     * @param theta coord one
     * @param phy   coord two
     * @return created vector
     */
    protected Vector3f getVector(double theta, double phy) {
        return Vector3f.makeVector(Math.sin(theta) * Math.cos(phy),
                Math.sin(theta) * Math.sin(phy), Math.cos(theta));
    }

    /**
     * a random value in [0,1)
     *
     * @return a random value in [0,1)
     */
    public static double random() {
        /* TODO original [0, 1] */
        return Math.random();
    }

    /**
     * a random value in [min, max)
     *
     * @return a random value in [min, max)
     */
    public static double random(double min, double max) {
        double value = random();
        return value * (max - min) + min;
    }

    protected
    Vector3f m_direction;
    Vector3f m_directionNormal;
    double m_n;

    /**
     * Constructor.
     *
     * @param direction main direction for random sampling.
     * @param n         shininess of surface.
     */
    public RandomDirection(Vector3f direction, double n) {
        m_direction = new Vector3f(direction.normalized());
        m_n = n;
        m_scramble = 0;
        m_index = (int) (Math.random() * Integer.MAX_VALUE);

        /* TODO */
    }

    /**
     * Constructor, n = 1.0.
     */
    public RandomDirection(Vector3f direction) {
        this(direction, 1.0);
    }


    /**
     * generates a random direction respecting cos^n distribution.
     *
     * @return the random direction.
     */
    Vector3f generate() {
        /* TODO */
        return new Vector3f();
    }


}
