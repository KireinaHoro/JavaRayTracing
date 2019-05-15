package moe.jsteward.Geometry;

import moe.jsteward.Geometry.BoundingBox;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javafx.scene.paint.Color;
import org.apache.commons.lang3.tuple.MutablePair;
import sun.awt.image.ImageWatched;


/**
 * an instance of geometry scene that can be rendered with ray casting.
 */
public class Scene {
    protected
    Visualizer m_visu;
    List<MutablePair<BoundingBox, Geometry>> m_geometries
            = new LinkedList<MutablePair<BoundingBox, Geometry>>();
    Vector<PointLight> m_lights;
    Camera m_camera;
    BoundingBox m_sceneBoundingBox;
    int m_diffuseSamples;
    int m_specularSamples;
    int m_lightSamples;
    int m_pass;
    LightSampler m_lightSampler;

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
     */
    public void printStats() {
        /* TODO */
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
        m_geometries.addLast(new MutablePair<BoundingBox, Geometry>(box, geometry));
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
        m_lights.addLast(light);
    }

    /**
     * sets the camera.
     */
    public void setCamera(Camera cam) {
        m_camera = cam;
    }

    /**
     * sends a ray in this Scene, returns the computed Color.
     */
    Color sendRay(Ray ray, int depth, int maxDepth, int diffuseSamples, int specularSamples) {
        return new Color(0, 0, 0, 1);
        /* TODO */
    }

    /**
     * computes a rendering of this Scene.
     */
    void compute(int maxDepth, int subPixelDivision, int passPrePixel) {
        /* TODO */
    }
}
