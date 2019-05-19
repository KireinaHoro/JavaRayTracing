package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/*
   need something import todo
 */
public class PointLight {
    private final Vector3D m_position;
    private final ColorEx m_color;

    /*
     *   author Louis
     *   Constructor
     */
    public PointLight(Vector3D position, ColorEx color) {
        m_position = position;
        m_color = color;
    }

    /*
     *  get the color
     */
    ColorEx color() {
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
    ColorEx color(RayTriangleIntersection intersection) {
        System.out.println("PointLight::color Not implemented [TODO]");
        return new ColorEx(0, 0, 0, 1);
    }
}
