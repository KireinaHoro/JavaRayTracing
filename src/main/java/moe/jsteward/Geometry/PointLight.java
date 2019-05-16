package moe.jsteward.Geometry;

/*
   need something import todo
 */
public class PointLight {
    Vector3f m_position ;
    Color m_color ;
    /*
     *   author Louis
     *   Constructor
     */
    public PointLight(Vector3f position, Color color ){
        position = Vector3f.makeVector(0.0,0.0,0.0);
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
    public Vector3f position() {
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
