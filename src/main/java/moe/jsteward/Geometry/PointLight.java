package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import javafx.scene.paint.Color;

/*
   need something import todo
 */
public class PointLight {
    Vector3D m_position ;
    Color m_color ;
    /*
     *   author Louis
     *   Constructor
     */
    public PointLight(Vector3D position, Color color ){
        position = new Vector3D(0.0,0.0,0.0);
        color = Color();
        m_position=position;
        m_color=color;
    }

    /*
     *  get the color
     */
    public Color color() {
        return m_color ;
    }

    /*
     *  get the direction
     */
    public Vector3D position() {
        return m_position ;
    }

    /*
     * intersection a valid intersection between a ray and a triangle
     */
    virtual Color color(RayTriangleIntersection intersection)
    {
        System.out.println("PointLight::color Not implemented [TODO]") ;
        return Color(0,1,0) ;
    }
}
