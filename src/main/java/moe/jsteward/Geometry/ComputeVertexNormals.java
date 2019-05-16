package moe.jsteward.Geometry;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.*;

public class ComputeVertexNormals {
    protected
    MutablePair<Vector3D , Vector3D > makeEdge(Vector3D v1, Vector3D v2) {
        MutablePair<Vector3D , Vector3D > temp = new MutablePair<Vector3D , Vector3D >(min(v1, v2), max(v1, v2));
            return temp;
    }
    Vector<Triangle> m_triangles;
    Map<MutablePair<Vector3D , Vector3D >, Vector<Integer>> m_edgeToTriangle;/* Integer need todo*/
    Vector<Vector<Integer>> m_groups;

    /*
     * Constructor
     * Author Louis
     */
    public ComputeVertexNormals(Vector<Triangle> triangles) {
        m_triangles = triangles;
    }
    /*
     * Compute something?
     *
     */
    public void compute(double cosLimit) {
        computeEdgeToTriangle();
        filterEdges(cosLimit);
        computeGroups();
        computeNormalsForGroups();
        ensure(cosLimit);
    }
    protected
    /*
     * auto need todo
     */
    void computeNormalsForGroups(){
        for (Vector<Integer> it: m_groups) {
            computeNormalsForGroup(it);
        }
    }
    /*
     * Computes the per vertex normals for a given groups of triangles
     * auto need todo
     */
    void computeNormalsForGroup(Vector<Integer> group) {
        // We create a Map accumulating the sum of the normals
        Map<Vector3D , Vector3D> m_normals;
        for (Integer it: group) {
            Integer current = it;
            Triangle triangle = m_triangles.elementAt(current);
            for (int i = 0; i < 3; ++i) {
                m_normals[triangle.vertex(i)] += triangle.normal();
            }
        }
        // We set the normals of the triangles belonging to the group
        for(Integer it: group) {
            Triangle triangle = m_triangles.elementAt(it);//**it;
            for (Integer i = 0; i < 3; ++i) {
                Vector3D found = m_normals.get(triangle.vertex(i));
                if (found != null) {
                    //Map<Integer,Integer> a=new Map<Integer,Integer>;
                    triangle.setVertexNormal(i, found.normalized());
                }
            }
        }
    }
    /*
     * Computes the groups of triangles based on the edge to triangle Map
     * Pointer need todo
     */
    void computeGroups() {
        // 1- We compute the triangle adjency graph.
        Vector<Vector<Integer>> graph = new Vector<Vector<Integer>>(m_triangles.size());
        for (Vector<Integer> it : m_edgeToTriangle.values())
        {
            Vector<Integer> triangles = (it);
            graph.elementAt(triangles.elementAt(1)).add(triangles.elementAt(1));
            graph.elementAt(triangles.elementAt(0)).add(triangles.elementAt(0));
        }

        // 2 - We compute the connected components.
        Vector<Boolean> explored = new Vector<Boolean>(m_triangles.size());/* maybe need init to false */
		Vector<Integer> toExplore;
        for (Integer cpt = 0; cpt < graph.size() ; ++cpt)
        {
            if (explored.elementAt(cpt) == true) { continue; }
            m_groups.add(new Vector<Integer>());
            toExplore.add(cpt);
            while (!toExplore.isEmpty()) {
                Integer current = toExplore.lastElement();
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
    void computeEdgeToTriangle() {
        for (Integer cpt=0 ; cpt<m_triangles.size() ; ++cpt)
        {
            Triangle triangle = m_triangles.elementAt(cpt);
            for(Integer i=0; i<3 ; ++i)
            {
                m_edgeToTriangle.get(makeEdge(triangle.vertex(i), triangle.vertex((i+1)%3))).add(cpt);
            }
        }
    }

    /*
     * Filters the edge to triangle Map by removing edges shared by triangles for which the dot product
     * of normals is lower than the given threshold
     */

    void filterEdges(double cosLimit)
    {
        Vector<Map<MutablePair<Vector3D , Vector3D >, Vector<Integer>>> toRemove;
        Vector<Integer> trianglesToRemove;
        Iterator<Map.Entry<MutablePair<Vector3D , Vector3D >, Vector<Integer>>> it = m_edgeToTriangle.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<MutablePair<Vector3D , Vector3D >, Vector<Integer>> entry = it.next();
			Vector<Integer> triangles = entry.getValue();
            if (triangles.size() < 2 ) {
                toRemove.add(entry.getKey(),entry.getValue());
            }
            else if (triangles.size() == 2) {
                if (m_triangles.elementAt(triangles.elementAt(0)).normal() * m_triangles.elementAt(triangles.elementAt(1)).normal() < cosLimit) {
                    toRemove.add(entry.getKey(),entry.getValue());
                }
            }
            else {
                toRemove.add(it);
                trianglesToRemove.addAll(m_edgeToTriangle);
            }
        }
        for (Map<MutablePair<Vector3D , Vector3D >, Vector<Integer>> it : toRemove) {
            m_edgeToTriangle.remove(it);
        }
        toRemove.clear();
        Collections.sort(trianglesToRemove);
        Set<Integer> tempset = new HashSet<Integer>(trianglesToRemove);
        trianglesToRemove = new Vector<Integer>(tempset);
        for (Map<MutablePair<Vector3D , Vector3D >, Vector<Integer>> it : m_edgeToTriangle) {
            if (Arrays.binarySearch(trianglesToRemove.toArray(), m_edgeToTriangle.get(it).elementAt(0)) != -1) {
                toRemove.add(it);
            }
				else if (Arrays.binarySearch(trianglesToRemove.toArray(), m_edgeToTriangle.get(it).elementAt(1)) != -1) {
                toRemove.add(it);
            }
        }
        for (Map<MutablePair<Vector3D , Vector3D >, Vector<Integer>> it : toRemove) {
            m_edgeToTriangle.remove(it);
        }
    }

    /*
     * Ensures that the cosine of the angle between normals of each triangle is greater than the given threshold
     */
    void ensure(double cosLimit)
    {
        for (Triangle it : m_triangles) {
            Triangle triangle = (it);
            // If the angle between normals is greater that the threshold, we reset the per vertex normals to the triangle normal
            if (triangle.getVertexNormal(0)*triangle.getVertexNormal(1) < cosLimit ||
                    triangle.getVertexNormal(1)*triangle.getVertexNormal(2) < cosLimit ||
                    triangle.getVertexNormal(0)*triangle.getVertexNormal(2) < cosLimit)
            {
                triangle.setVertexNormal(0, triangle.normal());
                triangle.setVertexNormal(1, triangle.normal());
                triangle.setVertexNormal(2, triangle.normal());
            }
            // If the norm of a normal is 0 (it should not be...), we reset it to the triangle normal.
            if (triangle.getVertexNormal(0).norm() == 0.0) { triangle.setVertexNormal(0, triangle.normal()); }
            if (triangle.getVertexNormal(1).norm() == 0.0) { triangle.setVertexNormal(1, triangle.normal()); }
            if (triangle.getVertexNormal(2).norm() == 0.0) { triangle.setVertexNormal(2, triangle.normal()); }
        }
    }


}
