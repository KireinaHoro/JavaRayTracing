package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Triangle {
    // \brief	Pointers to the three vertices
    protected Vector3D m_vertex[] = new Vector3D[3];
    // \brief Pointers to the texture coordinates
    protected Vector2D m_textureCoordinates[] = new Vector2D[3];
    // \brief	The vertex 0 (added to enhance cache consistency)
    protected Vector3D m_vertex0;
    // \brief	The u axis.
    protected Vector3D m_uAxis;
    // \brief	The v axis.
    protected Vector3D m_vAxis;
    // \brief	The normal.
    protected Vector3D m_normal;
    // \brief	The associated material.
    Material m_material;
    // \brief	Per vertex normal.
    protected Vector3D m_vertexNormal[] = new Vector3D[3];
    // \brief   flag of having m_textureCoordinates
    protected boolean flagtexture = false;
    /*
     *  Sets a vertex normal.
     */
    public void setVertexNormal(int index, final Vector3D normal) {
        m_vertexNormal[index] = normal;
    }

    /*
     *  Gets a vertex normal.
     */
    public final Vector3D getVertexNormal(int index) {
        return m_vertexNormal[index];
    }

    /*
     *  Gets the vertex normal oriented toward a given point.
     */
    public Vector3D getVertexNormal(int index, final Vector3D toward) {
        Vector3D normal = m_vertexNormal[index];
        if (toward.subtract(vertex(index)).dotProduct(normal) < 0.0) {
            return normal.negate();
        }
        return normal;
    }

    /*
     *  Gets a pointer to the vertex normals array (size = 3, one normal per vertex).
     */
    public final Vector3D[] getVertexNormals() {
        return m_vertexNormal;
    }

    /*
     *  Updates precomputed data. This method should be called if vertices are externally modified.
     */
    public void update() {
        m_vertex0 = m_vertex[0];
        m_uAxis = m_vertex[1].subtract(m_vertex[0]);
        m_vAxis = m_vertex[2].subtract(m_vertex[0]);
        m_normal = m_uAxis.crossProduct(m_vAxis);
        m_normal = m_normal.normalize();
        m_vertexNormal[0] = m_normal;
        m_vertexNormal[1] = m_normal;
        m_vertexNormal[2] = m_normal;
    }

    /*
     *  Initializes a new instance of the "Triangle" class.
     *  \param a,b,c :=  A pointer to the first,second,third vertex.
     *  \param ta,tb,tc :=  The texture coordinates of the first,second,third vertex.
     */
    public Triangle(Vector3D a, Vector3D b, Vector3D c, Vector2D ta, Vector2D tb, Vector2D tc, Material material, final Vector3D normals[]) {
        m_vertex[0] = a;
        m_vertex[1] = b;
        m_vertex[2] = c;
        m_textureCoordinates[0] = ta;
        m_textureCoordinates[1] = tb;
        m_textureCoordinates[2] = tc;
        flagtexture = true;
        m_material = material;
        update();
        m_vertexNormal[0] = normals[0];
        m_vertexNormal[1] = normals[1];
        m_vertexNormal[2] = normals[2];
    }

    /*
     *  Initializes a new instance of the "Triangle" class.
     *  \param a,b,c :=  A pointer to the first,second,third vertex.
     *  \param ta,tb,tc :=  The texture coordinates of the first,second,third vertex.
     */
    public Triangle(Vector3D a, Vector3D b, Vector3D c, Vector2D ta, Vector2D tb, Vector2D tc, Material material) {
        m_vertex[0] = a;
        m_vertex[1] = b;
        m_vertex[2] = c;
        m_textureCoordinates[0] = ta;
        m_textureCoordinates[1] = tb;
        m_textureCoordinates[2] = tc;
        flagtexture = true;
        m_material = material;
        update();
    }

    /*
     *  Initializes a new instance of the "Triangle" class.
     */
    public Triangle(Vector3D a, Vector3D b, Vector3D c, Material material, final Vector3D normals[]) {
        m_vertex[0] = a;
        m_vertex[1] = b;
        m_vertex[2] = c;
        m_material = material;
        // std::fill(m_textureCoordinates, m_textureCoordinates + 3, nullptr);
        update();
        m_vertexNormal[0] = normals[0];
        m_vertexNormal[1] = normals[1];
        m_vertexNormal[2] = normals[2];
    }

    /*
     *  Initializes a new instance of the "Triangle" class.
     */
    public Triangle(Vector3D a, Vector3D b, Vector3D c, Material material) {
        m_vertex[0] = a;
        m_vertex[1] = b;
        m_vertex[2] = c;
        m_material = material;
        // std::fill(m_textureCoordinates, m_textureCoordinates + 3, nullptr);
        update();
    }

    /*
     *  Initializes a new instance of the "Triangle" class.
     *  m_vertex and m_textureCoordinates are NULL
     */
    public Triangle() {
    }

    /*
     *  Gets the material.
     */
    public Material material() {
        return m_material;
    }

    /*
     *  Gets the ith vertex
     */
    public final Vector3D vertex(int i) {
        assert (i >= 0 && i < 3);
        return (m_vertex[i]);
    }

    /*
     *  Returns the center of this triangle.
     */
    public Vector3D center() {
        Vector3D ret = vertex(0).add(vertex(1)).add(vertex(2));
        return new Vector3D(ret.getX() / 3.0, ret.getY() / 3.0, ret.getZ() / 3.0);
    }

    /*
     *  Gets the textures coordinates of a vertex.
     */
    public final Vector2D textureCoordinate(int i) {
        assert (i >= 0 && i < 3);
        return m_textureCoordinates[i];
    }

    /*
     *  Interpolates the texture coordinate given the u,v coordinates of an intersection.
     */
    public Vector2D interpolateTextureCoordinate(double u, double v) {
        return textureCoordinate(0).scalarMultiply(1 - u - v).add(textureCoordinate(1).scalarMultiply(u)).add(textureCoordinate(2).scalarMultiply(v));
    }

    /*
     *  Samples the texture given the u,v coordinates of an intersection.
     */
    RGBColor sampleTexture(double u, double v) {
        if (m_material.hasTexture() && flagtexture) {
            RGBColor texel = m_material.getTexture().pixel(interpolateTextureCoordinate(u, v));
            return texel;
        }
        return new RGBColor(1.0, 1.0, 1.0);
    }

    /*
     *   Samples the triangle given the u,v coordinates of an intersection
     */
    public Vector3D samplePoint(double u, double v) {
        return m_uAxis.scalarMultiply(u).add(m_vAxis.scalarMultiply(v)).add(m_vertex0);
    }

    /*
     *  Samples the normal given the u,v coordinates of an intersection. The normal is oriented toward the 'toward' point.
     */
    public Vector3D sampleNormal(double u, double v, final Vector3D toward) {

    }

    /*
     *  Gets the u axis.
     */
    public final Vector3D uAxis() {
        return m_uAxis;
    }

    /*
     *  Gets the v axis.
     */
    public final Vector3D vAxis() {
        return m_vAxis;
    }

    /*
     *  Gets the normal.
     */
    public final Vector3D normal() {
        return m_normal;
    }

    /*
     *  Gets the normal directed toward the half space containing the provided point.
     */
    public Vector3D normal(final Vector3D point) {

    }

    /*
     *  Returns the direction of a reflected ray, from a surface normal and the direction of the incident ray.
     */
    public static Vector3D reflectionDirection(final Vector3D n, final Vector3D dir) {


    }

    /*
     *  Returns the direction of a reflected ray, from the direction of the incident ray.
     */
    public Vector3D reflectionDirection(final Vector3D dir) {

    }

    /*
     *  Returns the direction of the reflected ray from a ray description.
     */
    public Vector3D reflectionDirection(final Ray ray) {

    }

    /*
     *  Computes the intersection between a ray and this triangle.
     *  tuv[0] = t, tuv[1] = u, tuv[2] = v;
     */
    public boolean intersection(final Ray r, double tuv[]) {

    }

    /*
     *  Computes the intersection between the ray and the plane supporting the triangle.
     *  tuv[0] = t, tuv[1] = u, tuv[2] = v;
     */
    public boolean generalIntersection(final Ray r, double tuv[]) {

    }

    /*
     *  Returns the surface of the triangle
     */
    public double surface() {

    }

    /*
     *  Computes random barycentric coordinates
     */
    public Vector3D randomBarycentric() {

    }

    /*
     *  Computes a point on a triangle from the barycentric coordinates
     */
    public Vector3D pointFromBraycentric(final Vector3D barycentric) {

    }

    /*
     *  Samples the texture from the provided barycentric coordinates
     */
    public RGBColor sampleTexture(final Vector3D barycentic) {

    }

    /*
     *  Computes a random point on the triangle
     */
    public Vector3D randomPoint() {

    }

    /*
     *  Computes the distance between the point and the plane on which the triangle lies.
     */
    public double planeDistance(final Vector3D point) {

    }
}

