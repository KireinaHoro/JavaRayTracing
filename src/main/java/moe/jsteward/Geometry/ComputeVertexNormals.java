package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import javafx.scene.paint.Color;

public class ComputeVertexNormals {
    protected
    pair<Vector3D , Vector3D > makepair<Vector3D , Vector3D >(Vector3D v1, Vector3D  v2) {
        return make_pair(min(v1, v2), max(v1, v2));
    }
    Vector<Triangle> m_triangles;
    map<pair<Vector3D , Vector3D >, Vector<size_t>> m_edgeToTriangle;/* size_t need todo*/
    Vector<Vector<size_t>> m_groups;

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
        computepair<Vector3D , Vector3D >ToTriangle();
        filterpair<Vector3D , Vector3D >s(cosLimit);
        computeGroups();
        computeNormalsForGroups();
        ensure(cosLimit);
    }
    protected
    /*
     * auto need todo
     */
    void computeNormalsForGroups(){
        for (auto it = m_groups.begin(), end = m_groups.end(); it != end; ++it)
        {
            computeNormalsForGroup(*it);
        }
    }
    /*
     * Computes the per vertex normals for a given groups of triangles
     * auto need todo
     */
    void computeNormalsForGroup(Vector<size_t> group) {
        // We create a map accumulating the sum of the normals
        map<Vector3D , Vector3D> m_normals;
        for (auto it = group.begin(), end = group.end(); it != end; ++it) {
            size_t current = it;
            Triangle triangle = m_triangles[current];
            for (int i = 0; i < 3; ++i) {
                m_normals[triangle.vertex(i)] += triangle.normal();
            }
        }
        // We set the normals of the triangles belonging to the group
        for(auto it=group.begin(), end=group.end() ; it!=end ; ++it) {
            Triangle triangle = m_triangles[it];//**it;
            for (size_t i = 0; i < 3; ++i) {
                auto found = m_normals.find(triangle.vertex(i));
                if (found != m_normals.end()) {
                    triangle.setVertexNormal(i, found.second.normalized());
                }
            }
        }
    }
    /*
     * Computes the groups of triangles based on the edge to triangle map
     * Pointer need todo
     */
    void computeGroups() {
        // 1- We compute the triangle adjency graph.
        Vector<Vector<size_t>> graph(m_triangles.size());
        for (auto it = m_edgeToTriangle.begin(), end = m_edgeToTriangle.end(); it != end; ++it)
        {
				Vector<size_t> triangles = it.second;
            graph[triangles[0]].push_back(triangles[1]);
            graph[triangles[1]].push_back(triangles[0]);
        }

        // 2 - We compute the connected components.
			Vector<bool> explored(m_triangles.size(), false);
			Vector<size_t> toExplore;
        for (size_t cpt = 0; cpt < graph.size() ; ++cpt)
        {
            if (explored[cpt]) { continue; }
            m_groups.push_back(Vector<size_t>());
            toExplore.push_back(cpt);
            while (!toExplore.empty())
            {
                size_t current = toExplore.back();
                toExplore.pop_back();
                if (explored[current]) { continue; }
                explored[current] = true;
                m_groups.back().push_back(current);
                copy(graph[current].begin(), graph[current].end(), back_inserter(toExplore));/* copy need todo */
            }
        }
    }

    /*
     * Computes the edge to triangle map
     * need todo
     */
    void computepair<Vector3D , Vector3D >ToTriangle() {
        for (size_t cpt=0 ; cpt<m_triangles.size() ; ++cpt) {
            Triangle triangle = m_triangles[cpt];
            for(size_t i=0; i<3 ; ++i) {
                m_edgeToTriangle[makepair<Vector3D , Vector3D >(triangle.vertex(i), triangle.vertex((i+1)%3))].push_back(cpt);
            }
        }
    }

    /*
     * Filters the edge to triangle map by removing edges shared by triangles for which the dot product
     * of normals is lower than the given threshold
     */
    void filterpair<Vector3D , Vector3D >s(double cosLimit)
    {
        Vector<map<pair<Vector3D , Vector3D >, Vector<size_t>>iterator> toRemove;
        Vector<size_t> trianglesToRemove;
        for (auto it = m_edgeToTriangle.begin(), end = m_edgeToTriangle.end(); it != end; ++it) {
			Vector<size_t> triangles = it.second;
            if (triangles.size() < 2 ) {
                toRemove.push_back(it);
            }
            else if (triangles.size() == 2) {
                if (m_triangles[triangles[0]].normal() * m_triangles[triangles[1]].normal() < cosLimit) {
                    toRemove.push_back(it);
                }
            }
            else {
                toRemove.push_back(it);
				copy(it.second.begin(), it.second.end(), back_inserter(trianglesToRemove));
            }
        }
        for (auto it = toRemove.begin(), end = toRemove.end(); it != end; ++it) {
            m_edgeToTriangle.erase(*it);
        }
        toRemove.clear();
		sort(trianglesToRemove.begin(), trianglesToRemove.end());
        trianglesToRemove.erase(unique(trianglesToRemove.begin(), trianglesToRemove.end()), trianglesToRemove.end());
        for (auto it = m_edgeToTriangle.begin(), end = m_edgeToTriangle.end(); it != end; ++it) {
            if (binary_search(trianglesToRemove.begin(), trianglesToRemove.end(), it.second[0])) {
                toRemove.push_back(it);
            }
				else if (binary_search(trianglesToRemove.begin(), trianglesToRemove.end(), it.second[1])) {
                toRemove.push_back(it);
            }
        }
        for (auto it = toRemove.begin(), end = toRemove.end(); it != end; ++it) {
            m_edgeToTriangle.erase(*it);
        }
    }

    /*
     * Ensures that the cosine of the angle between normals of each triangle is greater than the given threshold
     */
    void ensure(double cosLimit)
    {
        for (auto it = m_triangles.begin(), end = m_triangles.end(); it != end; ++it) {
            Triangle triangle = (**it);
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
