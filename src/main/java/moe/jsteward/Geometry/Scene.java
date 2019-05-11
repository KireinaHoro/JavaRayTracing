package moe.jsteward.Geometry;

import javafx.geometry.BoundingBox;
import javafx.scene.PointLight;
import moe.jsteward.Math.MyPair;

import java.util.LinkedList;
import java.util.Vector;


/**
 * an instance of geometry scene that can be rendered with ray casting.
 */
public class Scene {
    protected
    Visualizer m_visu;
    LinkedList<MyPair<BoundingBox, Geometry>> m_geometries;
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
        /* TODO */
    }

    /**
     * adds a point light to this Scene.
     */
    public void add(PointLight light) {
        /* TODO */
    }

    /**
     * sets the camera.
     */
    public void setCamera(Camera cam) {
        m_camera = cam;
    }

    /**
     * sends a ray in this Scene, returns the computed RGBColor.
     */
    RGBColor sendRay(Ray ray, int depth, int maxDepth, int diffuseSamples, int specularSamples) {
        return new RGBColor();
        /* TODO */
    }

    /**
     * computes a rendering of this Scene.
     */
    void compute(int maxDepth, int subPixelDivision, int passPrePixel) {
        /* TODO */
    }
}
