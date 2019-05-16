package moe.jsteward;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.MeshView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import moe.jsteward.Geometry.GeometryBuilder;
import moe.jsteward.Geometry.Scene;
import moe.jsteward.Geometry.Utils;

import java.io.File;

public class RenderController {
    public Label greetingLabel;
    public Canvas canvas;
    public TextField widthField;
    public TextField heightField;

    private Scene scene;
    private Timeline timeline;
    private WritableImage image;

    public void chooseModelFile(ActionEvent actionEvent) {
        int width, height;
        try {
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
        } catch (NumberFormatException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Invalid Width and Height");
            a.setHeaderText(null);
            a.setContentText("Please input integer for width and height");
            a.showAndWait();
            return;
        }

        image = new WritableImage(width, height);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Model File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("3DS Model Files", "*.3ds"),
                new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(greetingLabel.getScene().getWindow());

        new Thread(() -> {
            TdsModelImporter importer = new TdsModelImporter();
            importer.read(selectedFile);

            StringBuilder stringBuilder = new StringBuilder();
            Node[] rootNodes = importer.getImport();

            GeometryBuilder geometryBuilder = new GeometryBuilder();
            for (Node n : rootNodes) {
                Utils.inspectNode(n);
                Utils.recursiveProcessNode(n, n1 -> {
                    if (n1 instanceof MeshView) {
                        geometryBuilder.add((MeshView) n1);
                    }
                });
            }

            stringBuilder.append("Successfully loaded 3ds model file.")
                    .append(System.lineSeparator())
                    .append("Number of triangles: ")
                    .append(geometryBuilder.toGeometry().getVertices().size())
                    .append(System.lineSeparator());

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Load Model Results");
            alert.setHeaderText(null);
            alert.setContentText(stringBuilder.toString());
            alert.showAndWait();

            scene = new Scene(image.getPixelWriter(), width, height);
            scene.add(geometryBuilder.toGeometry());
            // TODO: camera, light source, etc.

            scene.compute();
        }).start();

        // update timeline
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(50),
                        (ActionEvent event) -> {
                            // repaint canvas
                            canvas.getGraphicsContext2D().drawImage(image, 0, 0);
                        }
                ));
        timeline.playFromStart();
    }
}
