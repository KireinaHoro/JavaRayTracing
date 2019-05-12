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
        m_vAxis = ( * m_vertex[2])-( * m_vertex[0]);
        m_normal = m_uAxis ^ m_vAxis;
        m_normal = m_normal * (1.0f / m_normal.norm());
        m_vertexNormal[0] = m_normal;
        m_vertexNormal[1] = m_normal;
        m_vertexNormal[2] = m_normal;
    }
}
