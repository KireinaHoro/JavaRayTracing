package moe.jsteward.Geometry;

import javafx.scene.paint.Color;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


/**
 * an instance of geometry scene that can be rendered with ray casting.
 */
public class Scene {
    protected
    Visualizer m_visu;
    List<MutablePair<BoundingBox, Geometry>> m_geometries
            = new LinkedList<MutablePair<BoundingBox, Geometry>>();
    List<PointLight> m_lights;
    Camera m_camera;
    BoundingBox m_sceneBoundingBox;
    int m_diffuseSamples;
    int m_specularSamples;
    int m_lightSamples;
    int m_pass;
    LightSampler m_lightSampler;
    KDNode kdTree;

    /**
     * Constructor.
     */
    public Scene(Visualizer visu) {
        m_visu = visu;
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
            numberTriangle += pair.right.m_triangles.size();
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
    public void setDiffuseSamples(int num) {
        m_diffuseSamples = num;
    }

    /**
     * sets number of specular samples.
     */
    public void setSpecularSamples(int num) {
        m_specularSamples = num;
    }

    /**
     * sets number of light samples.
     */
    public void setLightSamples(int num) {
        m_lightSamples = num;
    }

    /**
     * adds a geometry to this Scene.
     */
    public void add(Geometry geometry) {
        if (geometry.getVertices().isEmpty()) return;
        BoundingBox box = new BoundingBox(geometry);
        m_geometries.add(new MutablePair<BoundingBox, Geometry>(box, geometry));
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
        m_lights.add(light);
    }

    /**
     * sets the camera.
     */
    public void setCamera(Camera cam) {
        m_camera = cam;
    }

    /**
     * computes ray intersection with kdTree.
     *
     * @param ray  given ray.
     * @param node current KDNode finding on. null implies the whole kdTree.
     * @return the RayTriangleIntersection needed.
     */
    private RayTriangleIntersection getIntersection(Ray ray, KDNode node) {
        RayTriangleIntersection result = new RayTriangleIntersection();
        assert (kdTree != null);
        if (node == null) node = kdTree;
        if (node.bbox.intersect(ray)) {
            // the ray intersects with current bbox
            if (node.left || node.right) {
                // internal node -- left/right child exists.
                // NOTE -- one side exists implies another exists.
                RayTriangleIntersection iL = getIntersection(ray, node.left);
                RayTriangleIntersection iR = getIntersection(ray, node.right);
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
                for (Triangle triangle : node.triangles) {
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

    private Color add(Color a, Color b) {
        return new Color(a.getRed() + b.getRed(), a.getGreen() + b.getGreen(), a.getBlue() + b.getBlue(),
                a.getOpacity());
    }

    private Color multiply(Color a, Color b) {
        return new Color(a.getRed() * b.getRed(), a.getGreen() * b.getGreen(), a.getBlue() * b.getBlue(),
                a.getOpacity());
    }

    private Color multiply(Color a, double b) {
        return new Color(a.getRed() * b, a.getGreen() * b, a.getBlue() * b,
                a.getOpacity());
    }

    private Color divide(Color a, double b) {
        return new Color(a.getRed() / b, a.getGreen() / b, a.getBlue() / b,
                a.getOpacity());
    }

    private boolean isBlack(Color a) {
        return a.getRed() == 0.0 && a.getGreen() == 0.0 && a.getBlue() == 0.0;
    }



    /**
     * sends a ray in this Scene, returns the computed Color.
     */
    public Color sendRay(Ray ray, int depth, int maxDepth, int diffuseSamples, int specularSamples) {
        Color result = new Color(0, 0, 0, 1);
        // calculate intersection of current ray.
        RayTriangleIntersection intersection = getIntersection(ray, null);
        if (!intersection.valid()) {
            // background color.
            return result;
        }

        Vector3D intersectionPoint = intersection.intersection();
        Triangle triangle = intersection.triangle();
        Material material = triangle.material();
        // TODO types
        Color diffuse = material.getDiffuse();
        Color specular = material.getSpecular();
        double shininess = material.getShininess();
        Vector3D normal = triangle.sampleNormal(intersection.uTriangleValue(),
                intersection.vTriangleValue(), ray.source());
        Vector3D reflection = Triangle.reflectionDirection(normal, ray.direction()).normalize();
        // for each light source
        for (PointLight light : m_lights) {
            Vector3D lightRay = light.position().subtract(intersectionPoint);
            double distance = lightRay.getNormSq();
            lightRay = lightRay.normalize();
            // calculate shadow
            RayTriangleIntersection blockIntersection = getIntersection(new Ray(intersectionPoint, lightRay), null);
            if (blockIntersection.valid()) {
                double blockDistance =
                        (blockIntersection.intersection().subtract(intersectionPoint)).getNormSq();
                if (distance > blockDistance)
                    continue;
            }
            // ambient and emissive lighting -- phong part 1
            result = add(add(result, material.getAmbient()), material.getEmissive());
            // diffuse and specular lighting -- phong part 2
            // diffuse : result =
            //result + diffuse * (normal * lightRay) * light.color() / distance;
            if (normal.dotProduct(lightRay) > 0)
                result = add(result,
                        multiply(light.color(),
                                divide(multiply(diffuse, normal.dotProduct(lightRay)), distance)));
            // specular : result =
            //result + specular * pow(reflection * lightRay, shininess) * light.color() / distance;
            if (reflection.dotProduct(lightRay) > 0)
                result = add(result,
                        divide(multiply(multiply(specular, Math.pow(reflection.dotProduct(lightRay), shininess)),
                                light.color()
                        ), distance)
                );
        }

        if (depth < maxDepth && !isBlack(specular)) {
            // ray bouncing : send new ray
            result = add(result,
                    multiply(specular,
                            sendRay(
                                    new Ray(intersectionPoint, reflection),
                                    depth + 1, maxDepth, diffuseSamples, specularSamples
                            )
                    )
            );
        }
        // texture for intersection point
        if (material.hasTexture()) {
            result = multiply(result,
                    triangle.sampleTexture(intersection.uTriangleValue(), intersection.vTriangleValue())
            );
        }

        return result;
    }

    /**
     * computes a rendering of this Scene.
     */
    void compute(int maxDepth, int subPixelDivision, int passPerPixel) {
        // build the kdTree
        List<Triangle> listTriangle = new LinkedList<Triangle>();
        for (MutablePair<BoundingBox, Geometry> pair : m_geometries) {
            listTriangle.addAll(pair.right.getTriangles());
        }
        kdTree = new KDNode(listTriangle, 0);
        // prepare lightSampler (stores triangles with a non null emissive component)
        for (MutablePair<BoundingBox, Geometry> pair : m_geometries) {
            m_lightSampler.add(pair.right);
        }

        // step on x and y for subpixel sampling
        double step = 1.0 / subPixelDivision;
        // Table accumulating values computed per pixel (enable rendering of each pass)
        // TODO somehow strange...
        Vector<Vector<MutablePair<Integer, Color>>> pixelTable =
                new Vector<Vector<MutablePair<Integer, Color>>>
                        (m_visu.width(), new Vector<MutablePair<Integer, Color>>(m_visu.width(), new MutablePair(0, new Color(0, 0, 0, 1))));

        // 1 - Rendering time
        long t1, t2;           // timeStamps
        double elapsedTime;
        // get ticks per second

        // start timer
        t1 = System.currentTimeMillis();
        // Rendering pass number
        m_pass = 0;
        // Rendering
        for (int passPerPixelCounter = 0; passPerPixelCounter < passPerPixel; ++passPerPixelCounter) {
            for (double xp = -0.5; xp < 0.5; xp += step) {
                for (double yp = -0.5; yp < 0.5; yp += step) {
                    System.out.println("Pass" + m_pass + "/" + (passPerPixel * subPixelDivision * subPixelDivision));
                    ++m_pass;
                    // Sends primary rays for each pixel (uncomment the pragma to parallelize rendering)
                    // TODO Stream to be implemented...
#pragma omp parallel for schedule(dynamic)//, 10)//guided)//dynamic)
                    for (int y = 0; y < m_visu.height(); y++) {
                        for (int x = 0; x < m_visu.width(); x++) {
#pragma omp critical(visu)
                            m_visu.plot(x, y, new Color(1000.0, 0.0, 0.0, 1));
                            //TODO buffer Array to be implemented.
// Ray casting
                            Color result = sendRay(m_camera.getRay(((double) x + xp) / m_visu.width(), ((double) y + yp) / m_visu.height()), 0, maxDepth, m_diffuseSamples, m_specularSamples);
                            // Accumulation of ray casting result in the associated pixel
                            MutablePair<Integer, Color> currentPixel = pixelTable.elementAt(x).elementAt(y);
                            currentPixel.left++;
                            currentPixel.right = add(result, currentPixel.right);
                            // Pixel rendering (with simple tone mapping)
#pragma omp critical(visu)
                            m_visu.plot(x, y, divide(pixelTable.elementAt(x).elementAt(y).right,
                                    (double) (pixelTable.elementAt(x).elementAt(y).left) * 10));
                            //TODO buffer Array to be implemented.
                            // Updates the rendering context (per pixel) - warning per pixel update can be costly...
//#pragma omp critical (visu)
                            //m_visu.update();
                        }
                        // Updates the rendering context (per line)
#pragma omp critical(visu)
                        m_visu.update();
                        //TODO buffer Array to be implemented.
                    }
                    // Updates the rendering context (per pass)
                    //m_visu.update();
                    // We print time for each pass
                    t2 = System.currentTimeMillis();
                    elapsedTime = (double) (t2 - t1);
                    double remainingTime = (elapsedTime / m_pass) * (passPerPixel * subPixelDivision * subPixelDivision - m_pass);
                    System.out.println("time:" + elapsedTime + "ms. ,remaining time:" + remainingTime + "ms." + " total time:" + (elapsedTime + remainingTime));
                }
            }
        }
        // stop timer
        t2 = System.currentTimeMillis();
        elapsedTime = (double) (t2 - t1);
        System.out.println("elapsed time:" + elapsedTime + "ms.");
    }
}
