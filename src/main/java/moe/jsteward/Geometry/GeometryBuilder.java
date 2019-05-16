package moe.jsteward.Geometry;

import javafx.scene.shape.MeshView;

public class GeometryBuilder {
    private Geometry instance = new Geometry();

    public GeometryBuilder add(MeshView mesh) {
        instance.m_triangles.add(new Triangle(mesh.getMesh(), new Material(mesh.getMaterial())));

        return this;
    }

    public Geometry toGeometry() {
        return instance;
    }
}