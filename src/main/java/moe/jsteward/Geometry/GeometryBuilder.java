package moe.jsteward.Geometry;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class GeometryBuilder {
    private final Geometry instance = new Geometry();

    public void add(MeshView mesh) {
        instance.getTriangles().add(new Triangle((TriangleMesh) mesh.getMesh(), (PhongMaterialEx) mesh.getMaterial()));

    }

    public Geometry toGeometry() {
        return instance;
    }
}