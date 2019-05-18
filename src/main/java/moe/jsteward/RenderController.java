package moe.jsteward;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import moe.jsteward.Geometry.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

public class RenderController {
    public Label greetingLabel;
    public Canvas canvas;
    public TextField widthField;
    public TextField heightField;

    private Scene scene;
    private WritableImage image;

    // max depth for ray bounces
    private static final int maxDepth = 5;
    // multi-sample anti-aliasing
    private static final int subPixelDivision = 4;
    // monte-carlo passes for smooth shadow
    private static final int passPerPixel = 1;

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

        // start render worker
        Platform.runLater(() -> {
            // TODO set options: GENERATE_NORMALS
            TdsModelImporter importer = new TdsModelImporter();
            importer.read(selectedFile);

            scene = new Scene(image.getPixelWriter(), width, height);
            Node[] rootNodes = importer.getImport();

            AtomicInteger numberOfTriangles = new AtomicInteger();
            for (Node n : rootNodes) {
                Utils.inspectNode(n);
                Utils.recursiveProcessNode(n, n1 -> {
                    if (n1 instanceof MeshView) {
                        Geometry geometry = new Geometry((MeshView) n1);
                        numberOfTriangles.addAndGet(geometry.getTriangles().size());
                        scene.add(geometry);
                    }
                });
            }
            // TODO: adjust camera, light source, etc. according to scene
            BoundingBox sb = scene.getBoundingBox();
            Vector3D position = sb.max();
            Vector3D reflected = new Vector3D(position.getX(), -position.getY(), position.getZ());

            scene.add(new PointLight(position.add(new Vector3D(0, 0, 70)),
                    new Color(1.0, 1.0, 1.0, 1.0)));
            scene.add(new PointLight(reflected.add(new Vector3D(0, 0, 200)),
                    new Color(1.0, 1.0, 1.0, 1.0)));
            Camera camera = new Camera(new Vector3D(-500, -1000, 1000).scalarMultiply(1.05),
                    new Vector3D(500, 0, 0), 0.6, 1, 1);
            camera.translateLocal(new Vector3D(100, -100, -200));
            scene.setCamera(camera);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Load Model Results");
            alert.setHeaderText(null);
            String stringBuilder = "Successfully loaded 3ds model file." +
                    System.lineSeparator() +
                    "Number of triangles: " +
                    numberOfTriangles +
                    System.lineSeparator();
            alert.setContentText(stringBuilder);
            alert.showAndWait();

            // fire up the computation
            scene.compute(maxDepth, subPixelDivision, passPerPixel);

            // finished, try until successful save
            while (true) {
                FileChooser saveFileChooser = new FileChooser();
                saveFileChooser.setTitle("Save Rendering Output");
                saveFileChooser.getExtensionFilters().add(
                        new ExtensionFilter("PNG Image", "*.png")
                );
                File file = saveFileChooser.showSaveDialog(greetingLabel.getScene().getWindow());
                if (file != null) {
                    for (int x = 0; x < image.getWidth(); ++x) {
                        for (int y = 0; y < image.getHeight(); ++y) {
                            System.err.println("(x:" + x + ",y:" + y + "):" +
                                    image.getPixelReader().getColor(x, y)
                                            .toString());
                        }
                    }
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null),
                                "png", file);
                        break;
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    /*
                    // write image png
                    PixelReader reader = image.getPixelReader();
                    int size = width * height * 4;
                    byte[] buffer = new byte[size];
                    //WritablePixelFormat<ByteBuffer> format =
                    //        (WritablePixelFormat<ByteBuffer>) PixelFormat.getByteRgbInstance();
                    //reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
                    reader.getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), buffer, 0, width * 4);
                    try {
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                        for (int count = 0; count < buffer.length; count += 4) {
                            out.write(buffer[count + 2]);
                            out.write(buffer[count + 1]);
                            out.write(buffer[count]);
                            out.write(buffer[count + 3]);
                        }
                        out.flush();
                        out.close();
                        break;
                    } catch (IOException e) {
                        Alert a = new Alert(AlertType.ERROR);
                        a.setTitle("Failed to output file");
                        a.setHeaderText(null);
                        a.setContentText(ExceptionUtils.getStackTrace(e));
                        a.showAndWait();
                    }
                    */
                }
            }

            Alert aa = new Alert(AlertType.INFORMATION);
            aa.setTitle("Rendering Complete");
            aa.setHeaderText(null);
            aa.setContentText("The rendering has completed successfully.  The program will now exit.");
            aa.showAndWait();
            System.exit(0);
        });

        // update timeline
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(50),
                        (ActionEvent event) -> {
                            // repaint canvas
                            canvas.getGraphicsContext2D().drawImage(image, 0, 0,
                                    image.getWidth(), image.getHeight());
                        }
                ));
        timeline.playFromStart();
    }
}
