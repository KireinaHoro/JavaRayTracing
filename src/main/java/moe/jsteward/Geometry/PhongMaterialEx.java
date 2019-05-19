package moe.jsteward.Geometry;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class PhongMaterialEx extends PhongMaterial {
    private Color emissiveColor;

    void setEmissiveColor(Color _emissiveColor) {
        emissiveColor = _emissiveColor;
    }

    Color getEmissiveColor() {
        return emissiveColor;
    }

    public PhongMaterialEx(PhongMaterial material) {
        super();
        this.setBumpMap(material.getBumpMap());
        this.setDiffuseColor(material.getDiffuseColor());
        this.setDiffuseMap(material.getDiffuseMap());
        this.setSpecularColor(material.getSpecularColor());
        this.setSpecularMap(material.getSpecularMap());
        this.setSpecularPower(material.getSpecularPower());
        this.setSelfIlluminationMap(material.getSelfIlluminationMap());
        this.emissiveColor = new Color(0, 0, 0, 1);
    }
}
