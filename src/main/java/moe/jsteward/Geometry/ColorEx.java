package moe.jsteward.Geometry;

import javafx.scene.paint.Color;

public class ColorEx {
    private double R, G, B, a;

    public ColorEx(double _R, double _G, double _B, double _a) {
        R = _R;
        G = _G;
        B = _B;
        a = _a;
    }

    public ColorEx(Color c) {
        R = c.getRed();
        G = c.getGreen();
        B = c.getBlue();
        a = c.getOpacity();
    }

    double getRed() {
        return R;
    }

    double getGreen() {
        return G;
    }

    double getBlue() {
        return B;
    }

    double getOpacity() {
        return a;
    }

    Color toColor() {
        return new Color(
                getRed() / (getRed() + 1),
                getGreen() / (getGreen() + 1),
                getBlue() / (getBlue() + 1),
                getOpacity());
    }

    public String toString() {
        return new String(
                getRed() + " " +
                        getGreen() + " " +
                        getBlue() + " " +
                        getOpacity()
        );
    }
}
