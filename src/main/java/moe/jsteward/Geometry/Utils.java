package moe.jsteward.Geometry;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.TriangleMesh;

import java.io.OutputStream;
import java.io.PrintStream;

public class Utils {
    public interface NodeProcessor {
        void process(Node n);
    }

    public static void recursiveProcessNode(Node n, NodeProcessor np) {
        np.process(n);
        if (n instanceof Group) {
            for (Node nn : ((Group) n).getChildren()) {
                recursiveProcessNode(nn, np);
            }
        }
    }

    public static void inspectNode(Node n) {
        PrintStream ps;
        if (Boolean.parseBoolean(System.getProperty("debug"))) {
            ps = System.out;
        } else {
            ps = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {
                }
            });
        }
        inspectNode(ps, n, 0);
    }

    private static void inspectNode(PrintStream writer, Node n, int indentLevel) {
        String indent = new String(new char[indentLevel]).replace("\0", " ");
        if (n instanceof Group) {
            writer.println(indent + "-- Group with " + ((Group) n).getChildren().size() + " children --");
            for (Node sn : ((Group) n).getChildren()) {
                inspectNode(writer, sn, indentLevel + 1);
            }
        } else if (n instanceof Shape3D) {
            if (n instanceof MeshView) {
                TriangleMesh mesh = (TriangleMesh) ((MeshView) n).getMesh();
                long numPoints = mesh.getPoints().size() / mesh.getPointElementSize();
                writer.println(indent + "== TriangleMesh with " + numPoints + " points ==");
            } else {
                writer.println(indent + "== " + n.getClass().getName() + " ==");
            }
            writer.println(indent + "> " + ((Shape3D) n).getMaterial());
        } else {
            throw new RuntimeException("Unexpected node type: " + n.getClass().getName());
        }
    }

}
