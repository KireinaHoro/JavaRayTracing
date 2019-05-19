package moe.jsteward.Geometry;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * an instance of geometry scene that can be rendered with ray casting.
 */
public class Scene {
    private PixelWriter pw;
    private int imgYRange, imgXRange;

    private final List<MutablePair<BoundingBox, Geometry>> m_geometries
            = new LinkedList<>();
    private List<PointLight> m_lights = new LinkedList<>();
    private Camera m_camera;
    private BoundingBox m_sceneBoundingBox;
    private int m_diffuseSamples;
    private int m_specularSamples;
    private int m_lightSamples;
    private LightSampler m_lightSampler;
    private KDNode kdTree;

    private Map<MutablePair<Integer, Integer>, MutablePair<Integer, ColorEx>>
            accumulateBuffer = new HashMap<>();

    /**
     * Constructor.
     */
    public Scene() {
        m_diffuseSamples = 30;
        m_specularSamples = 30;
        m_lightSamples = 0;
    }

    /**
     * Constructor.
     */
    public Scene(final PixelWriter _pw, final int _imgXRange, final int _imgYRange) {
        pw = _pw;
        imgXRange = _imgXRange;
        imgYRange = _imgYRange;
        m_diffuseSamples = 30;
        m_specularSamples = 30;
        m_lightSamples = 0;
    }

    /**
     * prints stats about the geometries associated with this Scene.
     * In this, number of triangles in Scene is counted.
     */
    public void printStats() {
        int numberTriangle = 0;
        for (MutablePair<BoundingBox, Geometry> pair : m_geometries) {
            numberTriangle += pair.right.getTriangles().size();
        }
        System.out.println("Scene Stats : " + numberTriangle + "triangles...");
    }

    /**
     * gets this Scene's BoundingBox.
     */
    public BoundingBox getBoundingBox() {
        return m_sceneBoundingBox;
    }

    /**
     * sets number of diffuse samples.
     */
    public void setDiffuseSamples(final int num) {
        m_diffuseSamples = num;
    }

    /**
     * sets number of specular samples.
     */
    public void setSpecularSamples(final int num) {
        m_specularSamples = num;
    }

    /**
     * sets number of light samples.
     */
    public void setLightSamples(final int num) {
        m_lightSamples = num;
    }

    /**
     * adds a geometry to this Scene.
     */
    public void add(final Geometry geometry) {
        if (geometry.getVertices().isEmpty()) return;
        BoundingBox box = new BoundingBox(geometry);
        m_geometries.add(new MutablePair<>(box, geometry));
        m_geometries.get(m_geometries.size() - 1).right.computeVertexNormals(Math.PI / 8.0);
        if (m_geometries.size() == 1) {
            m_sceneBoundingBox = box;
        } else {
            m_sceneBoundingBox.update(box);
        }
    }

    /**
     * adds a point light to this Scene.
     */
    public void add(PointLight light) {
        if (light == null)
            System.err.println("WTF is it REALLY NULL????");
        m_lights.add(light);
    }

    /**
     * sets the camera.
     */
    public void setCamera(final Camera cam) {
        m_camera = cam;
    }

    /**
     * computes ray intersection with kdTree.
     *
     * @param ray  given ray.
     * @param node current KDNode finding on. null implies the whole kdTree.
     * @return the RayTriangleIntersection needed.
     */
    private RayTriangleIntersection getIntersection(final Ray ray, KDNode node) {
        RayTriangleIntersection result = new RayTriangleIntersection();
        assert (kdTree != null);
        if (node == null) node = kdTree;
        if (node.getBbox().intersect(ray)) {
            // the ray intersects with current bbox
            if (node.getLeft() != null || node.getRight() != null) {
                // internal node -- left/right child exists.
                // NOTE -- one side exists implies another exists.
                RayTriangleIntersection iL = getIntersection(ray, node.getLeft());
                RayTriangleIntersection iR = getIntersection(ray, node.getRight());
                if (iL.valid() && iR.valid()) {
                    if (iL.tRayValue() > iR.tRayValue())
                        return iR;
                    else
                        return iL;
                } else if (iR.valid()) {
                    return iR;
                } else if (iL.valid()) {
                    return iL;
                }
                return result;
            } else {
                // left node. make a direct lookup.
                for (Triangle triangle : node.getTriangles()) {
                    RayTriangleIntersection intersection =
                            new RayTriangleIntersection(triangle, ray);
                    if (intersection.valid() &&
                            (!result.valid() ||
                                    result.tRayValue() > intersection.tRayValue())) {
                        result = intersection;
                    }
                }
            }
        }
        return result;
    }


    /**
     * sends a ray in this Scene, returns the computed ColorEx.
     */
    private ColorEx sendRay(final Ray ray, int depth, final int maxDepth,
                            final int diffuseSamples, final int specularSamples) {
        ColorEx result = new ColorEx(0, 0, 0, 1);
        // calculate intersection of current ray.
        RayTriangleIntersection intersection = getIntersection(ray, null);
        if (!intersection.valid()) {
            // background color.
            return result;
        }

        //return new ColorEx(1, 1, 1, 1);


        //System.err.println("Hey Intersects!!!"+depth);
        Vector3D intersectionPoint = intersection.intersection();
        Triangle triangle = intersection.triangle();
        PhongMaterialEx material = triangle.material();
        ColorEx diffuse = new ColorEx(material.getDiffuseColor());
        ColorEx specular = new ColorEx(material.getSpecularColor());
        //System.err.println("di"+diffuse.toString()+", sp"+specular.toString());

        double shininess = material.getSpecularPower();
        Vector3D normal = triangle.sampleNormal(intersection.uTriangleValue(),
                intersection.vTriangleValue(), ray.source());
        Vector3D reflection = Triangle.reflectionDirection(normal, ray.direction()).normalize();
        // for each light source
        for (PointLight light : m_lights) {
            //System.err.println("lighting:: " + light.color().toString());
            Vector3D lightRay = light.position().subtract(intersectionPoint);
            double distance = lightRay.getNorm();
            lightRay = lightRay.normalize();

            // calculate shadow
            RayTriangleIntersection blockIntersection = getIntersection(new Ray(intersectionPoint, lightRay), null);
            if (blockIntersection.valid()) {
                double blockDistance =
                        (blockIntersection.intersection().subtract(intersectionPoint)).getNorm();
                if (distance > blockDistance)
                    continue;
            }
            // ambient and emissive lighting -- phong part 1
            // TODO those are default set to ColorEx(0, 0, 0, 1)
            result = StrangeMethods.add(
                    StrangeMethods.add(result,
                            new ColorEx(0, 0, 0, 1)),
                    new ColorEx(material.getEmissiveColor()));

            //System.err.println(
            //        "distance:: " + distance + " " +
            //                "normal dot lightRay:: " + normal.dotProduct(lightRay) + " " +
            //            "diffuse:: " + diffuse + " " +
            //            "specular:: " + specular);
            // diffuse and specular lighting -- phong part 2
            // diffuse : result =
            //result + diffuse * (normal * lightRay) * light.color() / distance;
            if (normal.dotProduct(lightRay) > 0)
                result = StrangeMethods.add(
                        result,
                        StrangeMethods.multiply(
                                light.color(),
                                StrangeMethods.divide(
                                        StrangeMethods.multiply(
                                                diffuse, normal.dotProduct(lightRay)),
                                        distance)));
            // specular : result =
            //result + specular * pow(reflection * lightRay, shininess) * light.color() / distance;
            if (reflection.dotProduct(lightRay) > 0)
                result = StrangeMethods.add(
                        result,
                        StrangeMethods.divide(
                                StrangeMethods.multiply(
                                        StrangeMethods.multiply(
                                                specular,
                                                Math.pow(reflection.dotProduct(lightRay), shininess)),
                                        light.color()),
                                distance)
                );
        }

        if (depth < maxDepth && !StrangeMethods.isBlack(specular)) {
            // ray bouncing : send new ray
            result = StrangeMethods.add(result,
                    StrangeMethods.multiply(specular,
                            sendRay(
                                    new Ray(intersectionPoint, reflection),
                                    depth + 1, maxDepth, diffuseSamples, specularSamples
                            )
                    )
            );
        }

        // texture for intersection point
        if (StrangeMethods.hasTexture(material)) {
            result = StrangeMethods.multiply(result,
                    new ColorEx(triangle.sampleTexture(intersection.uTriangleValue(), intersection.vTriangleValue()))
            );
        }
        //

        //if(!StrangeMethods.isBlack(result)) {
        //    System.err.println(result.toString());
        //}
        return result;


    }

    /**
     * computes a rendering of this Scene.
     */
    public void compute(int maxDepth, int subPixelDivision, int passPerPixel) {
        m_lightSampler = new LightSampler();
        // build the kdTree
        List<Triangle> listTriangle = new LinkedList<>();
        for (MutablePair<BoundingBox, Geometry> pair : m_geometries) {
            listTriangle.addAll(pair.right.getTriangles());
        }
        kdTree = new KDNode(listTriangle, 0);
        // prepare lightSampler (stores triangles with a non null emissive component)
        for (MutablePair<BoundingBox, Geometry> pair : m_geometries) {
            m_lightSampler.add(pair.right);
        }

        for (int x = 0; x < imgXRange; ++x)
            for (int y = 0; y < imgYRange; ++y) {
                accumulateBuffer.put(new MutablePair<>(x, y),
                        new MutablePair<>(0,
                                new ColorEx(0, 0, 0, 1)));
            }
        // step on x and y for subpixel sampling
        double step = 1.0 / subPixelDivision;
        List<Double> stepList = new LinkedList<>();
        for (double xp = -0.5; xp < 0.5; xp += step) stepList.add(xp);

        // 1 - Rendering time
        long t1, t2;           // timeStamps
        double elapsedTime;

        // start timer
        t1 = System.currentTimeMillis();
        // Rendering pass number
        int m_pass = 0;
        // Rendering
        for (int passPerPixelCounter = 0; passPerPixelCounter < passPerPixel; ++passPerPixelCounter) {
            for (final double yp : stepList) {
                for (final double xp : stepList) {
                    System.out.println("Pass" + m_pass + "/" + (passPerPixel * subPixelDivision * subPixelDivision));
                    ++m_pass;
                    // Sends primary rays for each pixel (uncomment the pragma to parallelize rendering)
                    IntStream.range(0, imgXRange).forEach(x -> IntStream.range(0, imgYRange).parallel()
                            .forEach(y -> {
                                ColorEx result = sendRay(m_camera.getRay(((double) x + xp) / imgXRange,
                                        ((double) y + yp) / imgYRange), 0,
                                        maxDepth, m_diffuseSamples, m_specularSamples);
                                MutablePair<Integer, Integer> coord = new MutablePair<>(x, y);
                                MutablePair<Integer, ColorEx> curr = accumulateBuffer.get(coord);
                                curr.left = curr.left + 1;
                                curr.right = StrangeMethods.add(curr.right, result);
                                pw.setColor(x, y,
                                        StrangeMethods.multiply(
                                                StrangeMethods.divide(result, curr.left),
                                                10)
                                                .toColor()
                                );
                                accumulateBuffer.replace(coord, curr);
                            }));
                    t2 = System.currentTimeMillis();
                    elapsedTime = (double) (t2 - t1);
                    double remainingTime = (elapsedTime / m_pass) * (passPerPixel * subPixelDivision * subPixelDivision - m_pass);
                    System.out.println("time:" + elapsedTime + "ms. ,remaining time:" + remainingTime + "ms." + " total time:" + (elapsedTime + remainingTime));
                }
            }
        }
        for (int x = 0; x < imgXRange; ++x)
            for (int y = 0; y < imgYRange; ++y) {
                MutablePair<Integer, ColorEx> curr = accumulateBuffer.get(new MutablePair<>(x, y));
                System.err.println("(x:" + x + ",y:" + y + ") - " + curr.left + "," + curr.right);
            }
        // stop timer
        t2 = System.currentTimeMillis();
        elapsedTime = (double) (t2 - t1);
        System.out.println("elapsed time:" + elapsedTime + "ms.");
    }
}
