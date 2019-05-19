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
        try {
            // TODO what is this???
            R = c.getRed();
            G = c.getGreen();
            B = c.getBlue();
            a = c.getOpacity();
        } catch (NullPointerException e) {
            R = 0.0;
            G = 0.0;
            B = 0.0;
            a = 0.0;
            //System.err.println(e);
        }
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

    double grey() {
        return (R + G + B) / 3.0;
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
