package moe.jsteward.Geometry;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

class StrangeMethods {
    static boolean hasTexture(PhongMaterialEx material) {
        return material.getBumpMap() != null
                || material.getDiffuseMap() != null
                || material.getSpecularMap() != null;
    }

    static Color getTexture(PhongMaterialEx material, Vector2D coord) {
        PixelReader bmp = material.getBumpMap().getPixelReader();
        PixelReader diffuse = material.getDiffuseMap().getPixelReader();
        PixelReader specular = material.getSpecularMap().getPixelReader();
        //int X = (int)material.getBumpMap().getWidth();
        //int Y = (int)material.getBumpMap().getWidth();
        int x = (int) coord.getX();
        int y = (int) coord.getY();
        return add(
                bmp.getColor(x, y),
                add(
                        diffuse.getColor(x, y),
                        specular.getColor(x, y)
                )
        );
    }

    static Color add(final Color a, final Color b) {
        return new Color(a.getRed() + b.getRed(), a.getGreen() + b.getGreen(), a.getBlue() + b.getBlue(),
                a.getOpacity());
    }

    static Color multiply(final Color a, final Color b) {
        return new Color(a.getRed() * b.getRed(), a.getGreen() * b.getGreen(), a.getBlue() * b.getBlue(),
                a.getOpacity());
    }

    static Color multiply(final Color a, final double b) {
        return new Color(a.getRed() * b, a.getGreen() * b, a.getBlue() * b,
                a.getOpacity());
    }

    static Color divide(final Color a, final double b) {
        return new Color(a.getRed() / b, a.getGreen() / b, a.getBlue() / b,
                a.getOpacity());
    }

    static boolean isBlack(final Color a) {
        return a.getRed() == 0.0 && a.getGreen() == 0.0 && a.getBlue() == 0.0;
    }
}
