package moe.jsteward.Geometry;

import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/*
   need something import todo
 */
public class PointLight {
    private final Vector3D m_position;
    private final Color m_color;

    /*
     *   author Louis
     *   Constructor
     */
    public PointLight(Vector3D position, Color color) {
        m_position = position;
        m_color = color;
    }

    /*
     *  get the color
     */
    Color color() {
        return m_color;
    }

    /*
     *  get the direction
     */
    Vector3D position() {
        return m_position;
    }

    /*
     * intersection a valid intersection between a ray and a triangle
     */
    Color color(RayTriangleIntersection intersection) {
        System.out.println("PointLight::color Not implemented [TODO]");
        return new Color(0, 0, 0, 1);
    }
}
