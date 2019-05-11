package moe.jsteward.Geometry;

import java.util.Deque;

public class Geometry {
    /**
     * A 3D geometry.
     *
     * @author CloudLiu
     */
    protected
    Deque<Vector3f> m_vertices;
    Deque<Vector2f> m_textureCoordinates;
    Deque<Triangle> m_triangles;

    /**
     * updates all triangles in moe.jsteward.Geometry.Geometry.
     * Should be called after some triangles are changed.
     */
    protected void updateTriangles() {
        /* TODO */
    }

    /**
     * gets the vertices.
     */
    public Deque<Vector3f> getVertices() {
        return m_vertices;
    }

    /**
     * gets the triangles.
     */
    public Deque<Triangle> getTriangles() {
        return m_triangles;
    }

    /**
     * default constructor.
     */
    Geometry() {
    }

    /**
     * copy constructor.
     */
    Geometry(Geometry geometry) {
        merge(geometry);
    }

    /**
     * adds a vertex.
     *
     * @param vertex the added vertex.
     * @return index of the added vertex.
     */
    public int addVertex(Vector3f vertex) {
        m_vertices.addLast(vertex);
        return m_vertices.size() - 1;
    }

    /**
     * adds texture coordinates.
     *
     * @param coord the added coord.
     * @return index of the added coord.
     */
    public int addTextureCoordinates(Vector2f coord) {
        m_textureCoordinates.addLast(coord);
        return m_textureCoordinates.size() - 1;
    }

    /**
     * adds a triangle.
     *
     * @param i1       index of first vertex.
     * @param i2       index of second vertex.
     * @param i3       index of third vertex.
     * @param material the material.
     * @param normals  the normals.
     */
    public void addTriangles(int i1, int i2, int i3, Material material, Vector3f normals) {
        /* TODO */
    }

    /**
     * adds a triangle, normals = null.
     */
    public void addTriangles(int i1, int i2, int i3, Material material) {
        addTriangles(i1, i2, i3, material, null);
    }

    /**
     * adds a triangle.
     *
     * @param i1       index of first vertex.
     * @param i2       index of second vertex.
     * @param i3       index of third vertex.
     * @param t1       index of first texture.
     * @param t2       index of second texture.
     * @param t3       index of third texture.
     * @param material the material.
     * @param normals  the normals.
     */
    public void addTriangle(int i1, int i2, int i3, int t1, int t2, int t3, Material material, Vector3f normals) {
        /* TODO */
    }

    /**
     * adds a triangle, normals = null
     */
    public void addTriangle(int i1, int i2, int i3, int t1, int t2, int t3, Material material) {
        addTriangle(i1, i2, i3, t1, t2, t3, material, null);
    }

    /**
     * adds an existing triangle.
     *
     * @param triangle the triangle.
     */
    public void addTriangle(Triangle triangle) {
        /* TODO */
    }

    /**
     * adds a triangle.
     *
     * @param p0       first vertex.
     * @param p1       second vertex.
     * @param p2       third vertex.
     * @param material the material.
     * @param normals  the normals.
     */
    public void addTriangle(Vertor3f p0, Vertor3f p1, Vertor3f p2, Material material, Vector3f normals) {
        /* TODO */
    }

    /**
     * adds a triangle, with normals = null
     */
    public void addTriangle(Vertor3f p0, Vertor3f p1, Vertor3f p2, Material material) {
        addTriangle(p0, p1, p2, material, null);
    }

    /**
     * merges given moe.jsteward.Geometry.Geometry with this.
     *
     * @param geometry the given moe.jsteward.Geometry.Geometry.
     */
    public void merge(Geometry geometry) {
        /* TODO */
    }

    /**
     * computes if given CastedRay intersects with this.
     *
     * @param ray the given CastedRay
     * @return true if intersects.
     */
    public boolean intersection(CastedRay ray) {
        /* TODO */
        return false;
    }

    /**
     * translates this with t.
     *
     * @param t the translation matrix.
     */
    public void translate(Vector3f t) {
        /* TODO */
    }

    /**
     * scales this with v.
     *
     * @param v the scale factor.
     */
    public void scale(double v) {
        /* TODO */
    }

    /**
     * scales this with v on X axis.
     *
     * @param v the scale factor on X axis.
     */
    public void scaleX(double v) {
        /* TODO */
    }

    /**
     * scales this with v on Y axis.
     *
     * @param v the scale factor on Y axis.
     */
    public void scaleY(double v) {
        /* TODO */
    }

    /**
     * scales this with v on Z axis.
     *
     * @param v the scale factor on Z axis.
     */
    public void scaleZ(double v) {
        /* TODO */
    }

    /**
     * rotates this with q.
     *
     * @param q the rotation Quaternion.
     */
    public void rotate(Quaternion q) {
        /* TODO */
    }

    /**
     * computes the per vertex normals.
     *
     * @param angle the angle limit for smoothing surface.
     */
    public void computeVertexNormals(double angle) {
        /* TODO */
    }
}
