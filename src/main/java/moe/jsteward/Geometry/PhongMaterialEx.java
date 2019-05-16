package moe.jsteward.Geometry;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

class PhongMaterialEx extends PhongMaterial {
    private Color emissiveColor;

    void setEmissiveColor(Color _emissiveColor) {
        emissiveColor = _emissiveColor;
    }

    Color getEmissiveColor() {
        return emissiveColor;
    }
}
