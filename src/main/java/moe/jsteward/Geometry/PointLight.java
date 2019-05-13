package moe.jsteward.Geometry;

/*
   need something import
 */
public class PointLight {
    Vector3f m_position ;
    RGBColor m_color ;
    /*
     *   Constructor
     */
    public PointLight(Vector3f position, RGBColor color ){
        position = Vector3f.makeVector(0.0,0.0,0.0);
        color = RGBColor();
        m_position=position;
        m_color=color;
    }

    /*
     *  get the color
     */
    public RGBColor color() {
        return m_color ;
    }

    /*
     *  get the direction
     */
    public Vector3f position() {
        return m_position ;
    }

    /*
     * intersection a valid intersection between a ray and a triangle
     */
    virtual RGBColor color(RayTriangleIntersection intersection)
    {
        System.out.println("PointLight::color Not implemented [TODO]") ;
        return RGBColor(0,1,0) ;
    }
}
