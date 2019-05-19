package moe.jsteward.Geometry;

import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.*;
import java.util.stream.Collectors;

public class Geometry {
    /**
     * A 3D geometry.
     */
    private final List<Vector2D> m_textureCoordinates = new LinkedList<>();
    private final List<Triangle> m_triangles = new LinkedList<>();
    private List<Vector3D> m_vertices = new LinkedList<>();

    /**
     * updates all triangles in moe.jsteward.Geometry.Geometry.
     * Should be called after some triangles are changed.
     */
    protected void updateTriangles() {
        for (Triangle triangle : m_triangles) {
            triangle.update();
            // TODO Cloud: does this works?????
        }
    }

    /**
     * gets the vertices.
     */
    public List<Vector3D> getVertices() {
        return m_vertices;
    }

    /**
     * gets the triangles.
     */
    public List<Triangle> getTriangles() {
        return m_triangles;
    }

    /**
     * default constructor.
     */
    public Geometry() {
    }

    /**
     * adds a vertex.
     *
     * @param vertex the added vertex.
     * @return index of the added vertex.
     */
    int addVertex(Vector3D vertex) {
        m_vertices.add(vertex);
        return m_vertices.size() - 1;
    }

    /**
     * adds texture coordinates.
     *
     * @param coord the added coord.
     * @return index of the added coord.
     */
    private int addTextureCoordinates(Vector2D coord) {
        m_textureCoordinates.add(coord);
        return m_textureCoordinates.size() - 1;
    }

    public Geometry(MeshView mesh) {
        TriangleMesh triangleMesh = ((TriangleMesh) mesh.getMesh());
        PhongMaterial material = (PhongMaterial) mesh.getMaterial();
        ObservableFloatArray points = triangleMesh.getPoints();
        ObservableFloatArray normals = triangleMesh.getNormals();
        ObservableFloatArray textureCoordinates = triangleMesh.getTexCoords();
        ObservableFaceArray faces = triangleMesh.getFaces();
        ObservableIntegerArray smoothingGroups = triangleMesh.getFaceSmoothingGroups();
        List<Vector3D> normalsList = new ArrayList<>();

        boolean PT = triangleMesh.getVertexFormat() == VertexFormat.POINT_TEXCOORD;

        // add vertices
        for (int i = 0; i < points.size(); i += triangleMesh.getPointElementSize()) {
            addVertex(new Vector3D(
                    points.get(i),
                    points.get(i + 1),
                    points.get(i + 2)));
        }
        System.err.println("vertexSize:" + m_vertices.size());
        // add texture coordinates
        for (int i = 0; i < textureCoordinates.size(); i += triangleMesh.getTexCoordElementSize()) {
            addTextureCoordinates(new Vector2D(
                    textureCoordinates.get(i),
                    textureCoordinates.get(i + 1)));
        }
        // add normals
        System.err.println("NormalsSize:" + normals.size());
        for (int i = 0; i < normals.size(); i += triangleMesh.getNormalElementSize()) {
            normalsList.add(new Vector3D(
                    points.get(i),
                    points.get(i + 1),
                    points.get(i + 2)));
        }
        // register triangles
        System.err.println("FacesSize:" + faces.size());
        if (PT) {
            for (int i = 0; i < faces.size(); i += triangleMesh.getFaceElementSize()) {
                int p0 = faces.get(i);
                int p1 = faces.get(i + 2);
                int p2 = faces.get(i + 4);
                int t0 = faces.get(i + 1);
                int t1 = faces.get(i + 3);
                int t2 = faces.get(i + 5);
                addTriangle(p0, p1, p2, t0, t1, t2,
                        new PhongMaterialEx(material));
            }
        } else {
            for (int i = 0; i < faces.size(); i += triangleMesh.getFaceElementSize()) {
                int p0 = faces.get(i);
                int p1 = faces.get(i + 3);
                int p2 = faces.get(i + 6);
                int t0 = faces.get(i + 1);
                int t1 = faces.get(i + 4);
                int t2 = faces.get(i + 7);
                int n0 = faces.get(i + 2);
                int n1 = faces.get(i + 5);
                int n2 = faces.get(i + 8);
                Vector3D[] normalsArray = new Vector3D[3];
                normalsArray[0] = normalsList.get(n0);
                normalsArray[1] = normalsList.get(n1);
                normalsArray[2] = normalsList.get(n2);
                addTriangle(p0, p1, p2, t0, t1, t2,
                        new PhongMaterialEx(material),
                        normalsArray);
            }
        }
    }

    /**
     * adds a triangle, normals = null.
     */
    void addTriangle(int i1, int i2, int i3, PhongMaterialEx material) {
        if (m_textureCoordinates.isEmpty()) {
            m_triangles.add(new Triangle(m_vertices.get(i1), m_vertices.get(i2), m_vertices.get(i3),
                    material));
        } else {
            m_triangles.add(new Triangle(m_vertices.get(i1), m_vertices.get(i2), m_vertices.get(i3),
                    m_textureCoordinates.get(i1), m_textureCoordinates.get(i2), m_textureCoordinates.get(i3),
                    material));
        }
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
    private void addTriangle(int i1, int i2, int i3, PhongMaterialEx material, Vector3D[] normals) {
        if (m_textureCoordinates.isEmpty()) {
            m_triangles.add(new Triangle(m_vertices.get(i1), m_vertices.get(i2), m_vertices.get(i3),
                    material, normals));
        } else {
            m_triangles.add(new Triangle(m_vertices.get(i1), m_vertices.get(i2), m_vertices.get(i3),
                    m_textureCoordinates.get(i1), m_textureCoordinates.get(i2), m_textureCoordinates.get(i3),
                    material, normals));
        }
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
    private void addTriangle(int i1, int i2, int i3, int t1, int t2, int t3,
                             PhongMaterialEx material, Vector3D[] normals) {
        m_triangles.add(new Triangle(m_vertices.get(i1), m_vertices.get(i2), m_vertices.get(i3),
                m_textureCoordinates.get(t1), m_textureCoordinates.get(t2), m_textureCoordinates.get(t3),
                material, normals));
    }

    /**
     * adds a triangle, normals = null
     */
    public void addTriangle(int i1, int i2, int i3, int t1, int t2, int t3, PhongMaterialEx material) {
        m_triangles.add(new Triangle(m_vertices.get(i1), m_vertices.get(i2), m_vertices.get(i3),
                m_textureCoordinates.get(t1), m_textureCoordinates.get(t2), m_textureCoordinates.get(t3),
                material));
    }

    /**
     * adds an existing triangle.
     *
     * @param triangle the triangle.
     */
    public void addTriangle(Triangle triangle) {
        int i1 = addVertex(triangle.m_vertex[0]);
        int i2 = addVertex(triangle.m_vertex[1]);
        int i3 = addVertex(triangle.m_vertex[2]);
        addTriangle(i1, i2, i3, triangle.material(), triangle.getVertexNormals());
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
    private void addTriangle(Vector3D p0, Vector3D p1, Vector3D p2, PhongMaterialEx material, Vector3D[] normals) {
        int i1 = addVertex(p0);
        int i2 = addVertex(p1);
        int i3 = addVertex(p2);
        addTriangle(i1, i2, i3, material, normals);
    }

    /**
     * adds a triangle, with normals = null
     */
    void addTriangle(Vector3D p0, Vector3D p1, Vector3D p2, PhongMaterialEx material) {
        int i1 = addVertex(p0);
        int i2 = addVertex(p1);
        int i3 = addVertex(p2);
        addTriangle(i1, i2, i3, material);
    }

    /**
     * merges given moe.jsteward.Geometry.Geometry with this.
     *
     * @param geometry the given moe.jsteward.Geometry.Geometry.
     */
    void merge(Geometry geometry) {
        Map<Vector3D, Integer> vertex2ind = new HashMap<>();
        for (Vector3D vec : geometry.getVertices()) {
            if (!vertex2ind.containsKey(vec)) {
                vertex2ind.put(vec, addVertex(vec));
            }
        }
        Map<Vector2D, Integer> texture2ind = new HashMap<>();
        for (Vector2D texCoord : geometry.m_textureCoordinates) {
            if (!texture2ind.containsKey(texCoord)) {
                texture2ind.put(texCoord, addTextureCoordinates(texCoord));
            }
        }
        for (Triangle triangle : geometry.m_triangles) {
            int i1 = vertex2ind.get(triangle.vertex(0));
            int i2 = vertex2ind.get(triangle.vertex(1));
            int i3 = vertex2ind.get(triangle.vertex(2));
            if (texture2ind.containsKey(triangle.textureCoordinate(0))) {
                addTriangle(i1, i2, i3,
                        texture2ind.get(triangle.textureCoordinate(0)),
                        texture2ind.get(triangle.textureCoordinate(1)),
                        texture2ind.get(triangle.textureCoordinate(2)),
                        triangle.material(), triangle.getVertexNormals());
                // TODO origin not have getVertexNormals()
            } else {
                addTriangle(i1, i2, i3,
                        triangle.material(), triangle.getVertexNormals());
            }
        }
    }

    /*
    //
     * computes if given CastedRay intersects with this.
     *
     * @param ray the given CastedRay
     * @return true if intersects.
     //
    public boolean intersection(CastedRay ray) {
        // TODO should I implement this one?
        return false;
    }
    */

    /**
     * translates this with t.
     *
     * @param t the translation matrix.
     */
    void translate(Vector3D t) {
        m_vertices = m_vertices.stream()
                .map(v -> v.add(t))
                .collect(Collectors.toList());
    }

    /**
     * scales this with v.
     *
     * @param v the scale factor.
     */
    public void scale(double v) {
        m_vertices = m_vertices.stream()
                .map(vec -> vec.scalarMultiply(v))
                .collect(Collectors.toList());
    }

    /**
     * scales this with v on X axis.
     *
     * @param v the scale factor on X axis.
     */
    public void scaleX(double v) {
        m_vertices = m_vertices.stream()
                .map(vec -> new Vector3D(v * vec.getX(), vec.getY(), vec.getZ()))
                .collect(Collectors.toList());
    }

    /**
     * scales this with v on Y axis.
     *
     * @param v the scale factor on Y axis.
     */
    public void scaleY(double v) {
        m_vertices = m_vertices.stream()
                .map(vec -> new Vector3D(vec.getX(), v * vec.getY(), vec.getZ()))
                .collect(Collectors.toList());
    }

    /**
     * scales this with v on Z axis.
     *
     * @param v the scale factor on Z axis.
     */
    public void scaleZ(double v) {
        m_vertices = m_vertices.stream()
                .map(vec -> new Vector3D(vec.getX(), vec.getY(), v * vec.getZ()))
                .collect(Collectors.toList());
    }

    /**
     * rotates this with q.
     *
     * @param q the rotation Quaternion.
     */
    void rotate(Quaternion q) {
        m_vertices = m_vertices.stream()
                .map(vec -> new Vector3D
                        (q.multiply(new Quaternion(vec.toArray())).
                                multiply(q.getConjugate()).getVectorPart())
                )
                .collect(Collectors.toList());
    }

    /**
     * computes the per vertex normals.
     *
     * @param angle the angle limit for smoothing surface.
     */
    public void computeVertexNormals(double angle) {
        double cosAngleLimit = Math.cos(angle);
        Vector<Triangle> triangles = new Vector<>(m_triangles);
        ComputeVertexNormals normalsComputation = new ComputeVertexNormals(triangles);
        normalsComputation.compute(cosAngleLimit);
    }
}
