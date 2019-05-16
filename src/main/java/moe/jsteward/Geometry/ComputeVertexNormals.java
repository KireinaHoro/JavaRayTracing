package moe.jsteward.Geometry;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.*;

class ComputeVertexNormals {

    private Vector3D min(Vector3D a, Vector3D b) {
        if ((a.getX() < b.getX())
                || (a.getX() == b.getX() && a.getY() < b.getY())
                || (a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() < b.getZ()))
            return a;
        else
            return b;
    }

    private Vector3D max(Vector3D a, Vector3D b) {
        if ((a.getX() > b.getX())
                || (a.getX() == b.getX() && a.getY() > b.getY())
                || (a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() > b.getZ()))
            return a;
        else
            return b;
    }

    private MutablePair<Vector3D, Vector3D> makeEdge(Vector3D v1, Vector3D v2) {
        return new MutablePair<Vector3D, Vector3D>(min(v1, v2), max(v1, v2));
    }

    private Vector<Triangle> m_triangles;
    private Map<MutablePair<Vector3D, Vector3D>, Vector<Integer>> m_edgeToTriangle;/* Integer need todo*/
    private Vector<Vector<Integer>> m_groups;

    /*
     * Constructor
     * Author Louis
     */
    ComputeVertexNormals(Vector<Triangle> triangles) {
        m_triangles = triangles;
    }
    /*
     * Compute something?
     *
     */
    void compute(double cosLimit) {
        computeEdgeToTriangle();
        filterEdges(cosLimit);
        computeGroups();
        computeNormalsForGroups();
        ensure(cosLimit);
    }

    /*
     * auto need todo
     */
    private void computeNormalsForGroups() {
        for (Vector<Integer> it: m_groups) {
            computeNormalsForGroup(it);
        }
    }
    /*
     * Computes the per vertex normals for a given groups of triangles
     * auto need todo
     */
    private void computeNormalsForGroup(Vector<Integer> group) {
        // We create a Map accumulating the sum of the normals
        Map<Vector3D, Vector3D> m_normals = new HashMap<>();
        for (Integer it: group) {
            Triangle triangle = m_triangles.elementAt(it);
            for (int i = 0; i < 3; ++i) {
                m_normals.replace(triangle.vertex(i),
                        m_normals.get(triangle.vertex(i)).add(triangle.normal()));
            }
        }
        // We set the normals of the triangles belonging to the group
        for(Integer it: group) {
            Triangle triangle = m_triangles.elementAt(it);//**it;
            for (int i = 0; i < 3; ++i) {
                Vector3D found = m_normals.get(triangle.vertex(i));
                if (found != null) {
                    //Map<Integer,Integer> a=new Map<Integer,Integer>;
                    triangle.setVertexNormal(i, found.normalize());
                }
            }
        }
    }
    /*
     * Computes the groups of triangles based on the edge to triangle Map
     * Pointer need todo
     */
    private void computeGroups() {
        // 1- We compute the triangle adjency graph.
        Vector<Vector<Integer>> graph = new Vector<Vector<Integer>>(m_triangles.size());
        for (Vector<Integer> it : m_edgeToTriangle.values())
        {
            graph.elementAt(it.elementAt(1)).add(it.elementAt(1));
            graph.elementAt(it.elementAt(0)).add(it.elementAt(0));
        }

        // 2 - We compute the connected components.
        Vector<Boolean> explored = new Vector<Boolean>(m_triangles.size());/* maybe need init to false */
        Vector<Integer> toExplore = new Vector<>();
        for (int cpt = 0; cpt < graph.size(); ++cpt)
        {
            if (explored.elementAt(cpt)) {
                continue;
            }
            m_groups.add(new Vector<Integer>());
            toExplore.add(cpt);
            while (!toExplore.isEmpty()) {
                int current = toExplore.lastElement();
                toExplore.remove(toExplore.lastElement());/*pop_back() */
                if (explored.elementAt(current)) { continue; }
                explored.setElementAt(true,current);
                m_groups.lastElement().add(current);
                toExplore.addAll(graph.elementAt(current));
            }
        }
    }

    /*
     * Computes the edge to triangle map
     */
    private void computeEdgeToTriangle() {
        for (int cpt = 0; cpt < m_triangles.size(); ++cpt)
        {
            Triangle triangle = m_triangles.elementAt(cpt);
            for (int i = 0; i < 3; ++i)
            {
                m_edgeToTriangle.get(makeEdge(triangle.vertex(i), triangle.vertex((i+1)%3))).add(cpt);
            }
        }
    }

    /*
     * Filters the edge to triangle Map by removing edges shared by triangles for which the dot product
     * of normals is lower than the given threshold
     */

    private void filterEdges(double cosLimit)
    {
        Vector<Map.Entry<MutablePair<Vector3D, Vector3D>, Vector<Integer>>> toRemove = new Vector<>();
        Vector<Integer> trianglesToRemove = new Vector<>();
        Iterator<Map.Entry<MutablePair<Vector3D , Vector3D >, Vector<Integer>>> it = m_edgeToTriangle.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<MutablePair<Vector3D , Vector3D >, Vector<Integer>> entry = it.next();
			Vector<Integer> triangles = entry.getValue();
            if (triangles.size() < 2 ) {
                toRemove.add(entry);
            }
            else if (triangles.size() == 2) {
                if (m_triangles.elementAt(triangles.elementAt(0)).normal().dotProduct(m_triangles.elementAt(triangles.elementAt(1)).normal()) < cosLimit) {
                    toRemove.add(entry);
                }
            }
            else {
                toRemove.add(entry);
                for (Vector<Integer> vec : m_edgeToTriangle.values())
                    trianglesToRemove.addAll(vec);
            }
        }
        for (Map.Entry<MutablePair<Vector3D , Vector3D >, Vector<Integer>> it1 : toRemove) {
            // TODO Cloud : i saw the warning... confusing...
            m_edgeToTriangle.remove(it1);
        }
        toRemove.clear();
        Collections.sort(trianglesToRemove);
        Set<Integer> tempset = new HashSet<Integer>(trianglesToRemove);
        trianglesToRemove = new Vector<Integer>(tempset);
        it = m_edgeToTriangle.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<MutablePair<Vector3D , Vector3D >, Vector<Integer>> entry = it.next();
            if (Arrays.binarySearch(trianglesToRemove.toArray(),entry.getValue().elementAt(0) )!= -1){
                toRemove.add(entry);
            }
				else if (Arrays.binarySearch(trianglesToRemove.toArray(), entry.getValue().elementAt(1)) != -1) {
                toRemove.add(entry);
            }
        }
        for (Map.Entry<MutablePair<Vector3D , Vector3D >, Vector<Integer>> it1 : toRemove) {
            m_edgeToTriangle.remove(it1);
        }
    }

    /*
     * Ensures that the cosine of the angle between normals of each triangle is greater than the given threshold
     */
    private void ensure(double cosLimit)
    {
        for (Triangle triangle : m_triangles) {
            // If the angle between normals is greater that the threshold, we reset the per vertex normals to the triangle normal
            if (triangle.getVertexNormal(0).dotProduct(triangle.getVertexNormal(1)) < cosLimit ||
                    triangle.getVertexNormal(1).dotProduct(triangle.getVertexNormal(2)) < cosLimit ||
                    triangle.getVertexNormal(0).dotProduct(triangle.getVertexNormal(2)) < cosLimit)
            {
                triangle.setVertexNormal(0, triangle.normal());
                triangle.setVertexNormal(1, triangle.normal());
                triangle.setVertexNormal(2, triangle.normal());
            }
            // If the norm of a normal is 0 (it should not be...), we reset it to the triangle normal.
            if (triangle.getVertexNormal(0).getNorm() == 0.0) {
                triangle.setVertexNormal(0, triangle.normal());
            }
            if (triangle.getVertexNormal(1).getNorm() == 0.0) {
                triangle.setVertexNormal(1, triangle.normal());
            }
            if (triangle.getVertexNormal(2).getNorm() == 0.0) {
                triangle.setVertexNormal(2, triangle.normal());
            }
        }
    }


}
