package moe.jsteward.Geometry;

import javafx.scene.paint.Color;

import java.util.Vector;

public class LightSampler {
    protected
    Vector<Double> surfaceSum;
    Vector<Triangle> allTriangles;
    double currentSum ;

    public
    LightSampler() {
        currentSum = 0.0;
    }

    /*
     * Adds a triangle to the light sampler
     */
    void add(Triangle triangle) {
        if (!triangle.material().getEmissive().isBlack()) {
            currentSum += triangle.surface()
            surfaceSum.push_back(currentSum);
            allTriangles.push_back(triangle);
        }
    }

    /*
     * Adds a geometry to the light sampler
     */
    void add(Geometry geometry) {
        auto triangles = geometry.getTriangles();
        add(triangles.begin(), triangles.end());
    }

    /*
     * Generates nb point lights by sampling the kept triangles
     * template need todo
     */

    public<T>  T generate(T output, int nb) {
            for (int cpt = 0; cpt < nb; ++cpt) {
                ( * output) =generate();
                output++;
            }
            return output;
        }
    }

    /*
     * Generates end-begin point lights by sampling the kept triangles
     */
        public<T> T Tgenerate(T begin, T end) {
            for (; begin != end; ++begin) {
                (*begin) = generate();
                ++begin;
            }
            return begin;
        }

        /*
         * Returns true if the light sampler can sample lights
         */
    boolean hasLights() {
        return allTriangles.size() > 0;
    }


}
