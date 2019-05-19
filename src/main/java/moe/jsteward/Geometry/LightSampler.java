package moe.jsteward.Geometry;

import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Arrays;
import java.util.Vector;

class LightSampler {
    private Vector<Double> surfaceSum;
    private Vector<Triangle> allTriangles;
    private double currentSum;

    public
    LightSampler() {
        currentSum = 0.0;
    }


    /*
     * Adds a triangle to the light sampler
     */
    private void add(Triangle triangle) {
        if (!StrangeMethods.isBlack(triangle.material().getEmissiveColor())) {
            currentSum += triangle.surface();
            surfaceSum.add(currentSum);
            allTriangles.add(triangle);
        }
    }

    /*
     * Adds a geometry to the light sampler
     */
    void add(Geometry geometry) {
        for (Triangle triangle : geometry.getTriangles()) {
            add(triangle);
        }
    }

    /**
     * Genates a point light by sampling the kept triangles.
     */
    private PointLight generate() {
        double random = Math.random();
        random = random * currentSum;
        // TODO complexity may be wrong...
        int index = Arrays.binarySearch(surfaceSum.toArray(), random);
        //auto found = std::lower_bound(surfaceSum.begin(), surfaceSum.end(), random);
        //size_t index = found - surfaceSum.begin();
        Vector3D barycentric = allTriangles.elementAt(index).randomBarycentric();
        Vector3D point = allTriangles.elementAt(index).pointFromBraycentric(barycentric);
        Color color = allTriangles.elementAt(index).sampleTexture(barycentric);


        return new PointLight(point,
                new ColorEx(
                        StrangeMethods.multiply(
                                allTriangles.elementAt(index).material().getEmissiveColor(),
                                color)
                ));
    }

    /*
     * Generates nb point lights by sampling the kept triangles
     */
    public Vector<PointLight> generate(Vector<PointLight> output, int nb) {
        for (int cpt = 0; cpt < nb; ++cpt) {
            output.setElementAt(generate(), cpt);
        }
            return output;
    }

    /*
    //
    // Generates end-begin point lights by sampling the kept triangles
    //
        public<T> T Tgenerate(T begin, T end) {
            for (; begin != end; ++begin) {
                (*begin) = generate();
                ++begin;
            }
            return begin;
        }
    */
    /*
         * Returns true if the light sampler can sample lights
         */
    boolean hasLights() {
        return allTriangles.size() > 0;
    }


}
