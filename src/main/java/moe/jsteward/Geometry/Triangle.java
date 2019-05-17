package moe.jsteward.Geometry;

import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

class Triangle {
    // \brief	Pointers to the three vertices
    final Vector3D[] m_vertex = new Vector3D[3];
    // \brief Pointers to the texture coordinates
    private final Vector2D[] m_textureCoordinates = new Vector2D[3];
    // \brief	The vertex 0 (added to enhance cache consistency)
    private Vector3D m_vertex0;
    // \brief	The u axis.
    private Vector3D m_uAxis;
    // \brief	The v axis.
    private Vector3D m_vAxis;
    // \brief	The normal.
    private Vector3D m_normal;
    // \brief	The associated material.
    private PhongMaterialEx m_material;
    // \brief	Per vertex normal.
    private final Vector3D[] m_vertexNormal = new Vector3D[3];
    // \brief   flag of having m_textureCoordinates
    /*
     *  Sets a vertex normal.
     */
    void setVertexNormal(int index, final Vector3D normal) {
        m_vertexNormal[index] = normal;
    }

    /*
     *  Gets a vertex normal.
     */
    Vector3D getVertexNormal(int index) {
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
    Vector3D[] getVertexNormals() {
        return m_vertexNormal;
    }

    /*
     *  Updates precomputed data. This method should be called if vertices are externally modified.
     */
    void update() {
        m_vertex0 = m_vertex[0];
        m_uAxis = m_vertex[1].subtract(m_vertex[0]);
        m_vAxis = m_vertex[2].subtract(m_vertex[0]);
        m_normal = m_uAxis.crossProduct(m_vAxis);
        // TODO fix detection
        if (m_normal.getNorm() != 0) {
            m_normal = m_normal.normalize();
            m_vertexNormal[0] = m_normal;
            m_vertexNormal[1] = m_normal;
            m_vertexNormal[2] = m_normal;
        }
    }

    /*
     *  Initializes a new instance of the "Triangle" class for TriangleMesh input.
     */
    Triangle(TriangleMesh triangleMesh, PhongMaterialEx material) {
        float[] pointArray = new float[9];
        float[] normalsArray = new float[9];
        float[] coordinatesArray = new float[6];
        triangleMesh.getPoints().toArray(pointArray);
        triangleMesh.getNormals().toArray(normalsArray);
        triangleMesh.getTexCoords().toArray(coordinatesArray);

        Vector3D a = new Vector3D((double) pointArray[0], (double) pointArray[1], (double) pointArray[2]);
        Vector3D b = new Vector3D((double) pointArray[3], (double) pointArray[4], (double) pointArray[5]);
        Vector3D c = new Vector3D((double) pointArray[6], (double) pointArray[7], (double) pointArray[8]);

        Vector2D ta = new Vector2D((double) coordinatesArray[0], (double) coordinatesArray[1]);
        Vector2D tb = new Vector2D((double) coordinatesArray[2], (double) coordinatesArray[3]);
        Vector2D tc = new Vector2D((double) coordinatesArray[4], (double) coordinatesArray[5]);

        Vector3D na = new Vector3D((double) normalsArray[0], (double) normalsArray[1], (double) normalsArray[2]);
        Vector3D nb = new Vector3D((double) normalsArray[3], (double) normalsArray[4], (double) normalsArray[5]);
        Vector3D nc = new Vector3D((double) normalsArray[6], (double) normalsArray[7], (double) normalsArray[8]);

        m_vertex[0] = a;
        m_vertex[1] = b;
        m_vertex[2] = c;
        m_textureCoordinates[0] = ta;
        m_textureCoordinates[1] = tb;
        m_textureCoordinates[2] = tc;
        m_material = material;
        update();
        m_vertexNormal[0] = na;
        m_vertexNormal[1] = nb;
        m_vertexNormal[2] = nc;
    }

    /*
     *  Initializes a new instance of the "Triangle" class.
     *  \param a,b,c :=  A pointer to the first,second,third vertex.
     *  \param ta,tb,tc :=  The texture coordinates of the first,second,third vertex.
     */
    Triangle(Vector3D a, Vector3D b, Vector3D c, Vector2D ta, Vector2D tb, Vector2D tc, PhongMaterialEx material, final Vector3D[] normals) {
        m_vertex[0] = a;
        m_vertex[1] = b;
        m_vertex[2] = c;
        m_textureCoordinates[0] = ta;
        m_textureCoordinates[1] = tb;
        m_textureCoordinates[2] = tc;
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
    public Triangle(Vector3D a, Vector3D b, Vector3D c, Vector2D ta, Vector2D tb, Vector2D tc, PhongMaterialEx material) {
        m_vertex[0] = a;
        m_vertex[1] = b;
        m_vertex[2] = c;
        m_textureCoordinates[0] = ta;
        m_textureCoordinates[1] = tb;
        m_textureCoordinates[2] = tc;
        m_material = material;
        update();
    }

    /*
     *  Initializes a new instance of the "Triangle" class.
     */
    Triangle(Vector3D a, Vector3D b, Vector3D c, PhongMaterialEx material, final Vector3D[] normals) {
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
    public Triangle(Vector3D a, Vector3D b, Vector3D c, PhongMaterialEx material) {
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
    PhongMaterialEx material() {
        return m_material;
    }

    /*
     *  Gets the ith vertex
     */
    Vector3D vertex(int i) {
        assert (i >= 0 && i < 3);
        return (m_vertex[i]);
    }

    /*
     *  Returns the center of this triangle.
     */
    Vector3D center() {
        Vector3D ret = vertex(0).add(vertex(1)).add(vertex(2));
        return new Vector3D(ret.getX() / 3.0, ret.getY() / 3.0, ret.getZ() / 3.0);
    }

    /*
     *  Gets the textures coordinates of a vertex.
     */
    Vector2D textureCoordinate(int i) {
        assert (i >= 0 && i < 3);
        return m_textureCoordinates[i];
    }

    /*
     *  Interpolates the texture coordinate given the u,v coordinates of an intersection.
     */
    private Vector2D interpolateTextureCoordinate(double u, double v) {
        return textureCoordinate(0).scalarMultiply(1 - u - v).add(textureCoordinate(1).scalarMultiply(u)).add(textureCoordinate(2).scalarMultiply(v));
    }

    /*
     *  Samples the texture given the u,v coordinates of an intersection.
     */
    Color sampleTexture(double u, double v) {
        if (StrangeMethods.hasTexture(m_material)
                && m_textureCoordinates[0] != null) {
            return StrangeMethods.getTexture(m_material, interpolateTextureCoordinate(u, v));
        }
        return new Color(1.0, 1.0, 1.0, 0.0);
    }

    /*
     *   Samples the triangle given the u,v coordinates of an intersection
     */
    Vector3D samplePoint(double u, double v) {
        return m_uAxis.scalarMultiply(u).add(m_vAxis.scalarMultiply(v)).add(m_vertex0);
    }

    /*
     *  Samples the normal given the u,v coordinates of an intersection. The normal is oriented toward the 'toward' point.
     */
    Vector3D sampleNormal(double u, double v, final Vector3D toward) {
        Vector3D result = m_vertexNormal[0].scalarMultiply(1 - u - v).add(m_vertexNormal[1].scalarMultiply(u)).add(m_vertexNormal[2].scalarMultiply(v)).normalize();
        if ((toward.subtract(m_vertex0.add(m_uAxis.scalarMultiply(u)).add(m_vAxis.scalarMultiply(v)))).dotProduct(result) < 0.0) {
            return result.scalarMultiply(-1.0).normalize();
        }
        return result.normalize();
    }

    /*
     *  Gets the u axis.
     */
    private Vector3D uAxis() {
        return m_uAxis;
    }

    /*
     *  Gets the v axis.
     */
    private Vector3D vAxis() {
        return m_vAxis;
    }

    /*
     *  Gets the normal.
     */
    Vector3D normal() {
        return m_normal;
    }

    /*
     *  Gets the normal directed toward the half space containing the provided point.
     */
    public Vector3D normal(final Vector3D point) {
        if (point.subtract(m_vertex0).dotProduct(m_normal) < 0.0) {
            return m_normal.scalarMultiply(-1.0);
        }
        return m_normal;
    }

    /*
     *  Returns the direction of a reflected ray, from a surface normal and the direction of the incident ray.
     */
    static Vector3D reflectionDirection(final Vector3D n, final Vector3D dir) {
        return new Vector3D(dir.subtract(n.scalarMultiply(2.0 * dir.dotProduct(n))).toArray());
    }

    /*
     *  Returns the direction of a reflected ray, from the direction of the incident ray.
     */
    Vector3D reflectionDirection(final Vector3D dir) {
        Vector3D n = normal();
        return new Vector3D(dir.subtract(n.scalarMultiply(2.0 * dir.dotProduct(n))).toArray());
    }

    /*
     *  Returns the direction of the reflected ray from a ray description.
     */
    public Vector3D reflectionDirection(final Ray ray) {
        Vector3D n = normal();
        if (n.dotProduct(ray.source().subtract(m_vertex0)) <= 0.0) {
            n = n.scalarMultiply(-1.0);
        }
        return new Vector3D(ray.direction().subtract(n.scalarMultiply(2.0 * ray.direction().dotProduct(n))).toArray());
    }

    /*
     *  Computes the intersection between a ray and this triangle.
     *  tuv[0] = t, tuv[1] = u, tuv[2] = v;
     */
    boolean intersection(final Ray r, double[] tuv) {
        /* find vectors for two edges sharing vert0 */
        final Vector3D edge1 = new Vector3D(uAxis().toArray());
        final Vector3D edge2 = new Vector3D(vAxis().toArray());

        /* begin calculating determinant - also used to calculate U parameter */
        Vector3D pvec = r.direction().crossProduct(edge2);

        /* if determinant is near zero, ray lies in plane of triangle */
        double det = edge1.dotProduct(pvec);

        if (det > -0.000000001 && det < 0.000000001) {
            return false;
        }

        double inv_det = 1.0 / det;

        /* calculate distance from vert0 to ray origin */
        //Math::Vector3 tvec(r.source() - vertex(0));
        Vector3D tvec = new Vector3D(r.source().subtract(m_vertex0).toArray());

        /* calculate U parameter and test bounds */
        tuv[1] = tvec.dotProduct(pvec) * inv_det;

        if (tuv[1] < 0.0 || tuv[1] > 1.0) {
            return false;
        }

        /* prepare to test V parameter */
        Vector3D qvec = new Vector3D(tvec.crossProduct(edge1).toArray());

        /* calculate V parameter and test bounds */
        tuv[2] = (r.direction().dotProduct(qvec)) * inv_det;
        if (tuv[2] < 0.0 || tuv[1] + tuv[2] > 1.0) {
            return false;
        }

        /* calculate t, ray intersects triangle */
        tuv[0] = (edge2.dotProduct(qvec)) * inv_det;

        return tuv[0] >= 0.0001;
    }

    /*
     *  Computes the intersection between the ray and the plane supporting the triangle.
     *  tuv[0] = t, tuv[1] = u, tuv[2] = v;
     */
    public boolean generalIntersection(final Ray r, double[] tuv) {
        /* find vectors for two edges sharing vert0 */
        final Vector3D edge1 = new Vector3D(uAxis().toArray());
        final Vector3D edge2 = new Vector3D(vAxis().toArray());

        /* begin calculating determinant - also used to calculate U parameter */
        Vector3D pvec = r.direction().crossProduct(edge2);

        /* if determinant is near zero, ray lies in plane of triangle */
        double det = edge1.dotProduct(pvec);

        if (det > -0.000001 && det < 0.000001) {
            return false;
        }

        double inv_det = 1.0 / det;

        /* calculate distance from vert0 to ray origin */
        //Math::Vector3 tvec(r.source() - vertex(0));
        Vector3D tvec = new Vector3D(r.source().subtract(m_vertex0).toArray());

        /* calculate U parameter and test bounds */
        tuv[1] = tvec.dotProduct(pvec) * inv_det;

        /* prepare to test V parameter */
        Vector3D qvec = new Vector3D(tvec.crossProduct(edge1).toArray());

        /* calculate V parameter and test bounds */
        tuv[2] = (r.direction().dotProduct(qvec)) * inv_det;

        /* calculate t, ray intersects triangle */
        tuv[0] = (edge2.dotProduct(qvec)) * inv_det;

        return true;
    }

    /*
     *  Returns the surface of the triangle
     */
    double surface() {
        return (m_uAxis.crossProduct(m_vAxis)).getNorm() / 2.0;
    }

    /*
     *  Computes random barycentric coordinates
     */
    Vector3D randomBarycentric() {
        double r = Math.random();
        double s = Math.random();
        double a = 1.0 - Math.sqrt(s);
        double b = ((1.0 - r) * Math.sqrt(s));
        double c = r * Math.sqrt(s);
        return new Vector3D(a, b, c);
    }

    /*
     *  Computes a point on a triangle from the barycentric coordinates
     */
    Vector3D pointFromBraycentric(final Vector3D barycentric) {
        Vector3D tmp = randomBarycentric();
        return (m_vertex[0].scalarMultiply(tmp.getX()).add(m_vertex[1].scalarMultiply(tmp.getY())).add(m_vertex[2].scalarMultiply(tmp.getZ())));
    }

    /*
     *  Samples the texture from the provided barycentric coordinates
     */
    Color sampleTexture(final Vector3D barycentic) {
        if (StrangeMethods.hasTexture(m_material)) {
            Vector2D textureCoord = textureCoordinate(0).scalarMultiply(barycentic.getX()).
                    add(textureCoordinate(1).scalarMultiply(barycentic.getY())).
                    add(textureCoordinate(2).scalarMultiply(barycentic.getZ()));
            return StrangeMethods.getTexture(m_material, textureCoord);
        }
        return new Color(1.0, 1.0, 1.0, 0.0);
    }

    /*
     *  Computes a random point on the triangle
     */
    public Vector3D randomPoint() {
        Vector3D p = randomBarycentric();
        double a = p.getX();
        double b = p.getY();
        double c = p.getZ();
        return (m_vertex[0].scalarMultiply(a).add(m_vertex[1].scalarMultiply(b)).add(m_vertex[2].scalarMultiply(c)));
    }

    /*
     *  Computes the distance between the point and the plane on which the triangle lies.
     */
    public double planeDistance(final Vector3D point) {
        return Math.abs(point.subtract(m_vertex0).dotProduct(m_normal));
    }
}

