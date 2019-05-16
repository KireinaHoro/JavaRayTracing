package moe.jsteward.Geometry;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class Material_unused {
    private Color m_ambientColor;
    private Color m_diffuseColor;
    private Color m_specularColor;
    private double m_shininess;
    private Color m_emissiveColor;
    private String m_textureFile;
//    private Texture  m_texture;



    /*
     *  Constructor1
     *  author Louis
     */
    public Material_unused(Color ambientColor, Color diffuseColor, Color specularColor, double shininess, Color emissiveColor, String textureFile) {
        ambientColor = new Color(0,0,0,1);
        diffuseColor = new Color(0,0,0,1);
        specularColor = new Color(0,0,0,1);
        shininess = 1.0;
        emissiveColor = new Color(0,0,0,1);
        textureFile = "";
        m_ambientColor = ambientColor;
        m_diffuseColor = diffuseColor;
        m_specularColor = specularColor;
        m_shininess = shininess;
        m_emissiveColor = emissiveColor;
        m_textureFile = textureFile;
        //m_texture = null;
    }

    /*
     * need todo
     */
    public Material_unused(PhongMaterial phongmaterial) {
        /*
        if(phongmaterial.getBumpMap() == null && phongmaterial.getDiffuseMap() == null &&
           phongmaterial.getSpecularMap() == null && phongmaterial.getSelfIlluminationMap() == null) {
            //m_texture = null;
        }
        else m_texture = phongmaterial.getBumpMap()
            phongmaterial.getDiffuseMap()
            phongmaterial.getSpecularMap()
            phongmaterial.getSelfIlluminationMap();
        */
        // TODO how to imply add() here?
        m_ambientColor = new Color(0, 0, 0, 1);
        m_diffuseColor = phongmaterial.getDiffuseColor();
        m_specularColor = phongmaterial.getSpecularColor();
        m_emissiveColor = new Color(0, 0, 0, 1);
        m_shininess = phongmaterial.getSpecularPower();
    }

    /*
     * Sets the ambient color
     */
    public void setAmbient(Color color) {
        m_ambientColor = color;
    }

    /*
     * Gets the ambient color
     */
    public Color getAmbient(){
        return m_ambientColor ;
    }

    /*
     * Sets the diffuse color
     */
    public void setDiffuse(Color color) {
        m_diffuseColor = color;
    }

    /*
     * Gets the diffuse color
     */
    public Color getDiffuse() {
        return m_diffuseColor ;
    }

    /*
     * Sets the specular color
     */
    public void setSpecular(Color color) {
        m_specularColor = color;
    }

    /*
     * Gets the specular color
     */
    public Color getSpecular() {
        return m_specularColor ;
    }

    /*
     * Sets the shininess
     */
    public void setShininess(double s) {
        m_shininess = s;
    }

    /*
     * Gets the shininess
     */
    double getShininess() {
        return m_shininess ;
    }

    /*
     * Sets the emissive color
     */
    void setEmissive(Color color) {
        m_emissiveColor = color;
    }

    /*
     * Gets the emissive color
     */
    public Color getEmissive() {
        return m_emissiveColor ;
    }

    /*
     * Sets the texture file
     *
     */
    public void setTextureFile(String textureFile)
    {
        m_textureFile = textureFile;
        System.out.println("Loading texture: " + m_textureFile + "...");
		/*
		m_texture = new Texture(m_textureFile);
        if (!m_texture.isValid())
        {
            //delete m_texture;
            m_texture = null;
            System.out.println("discarded");
        }
        else
        {
            System.out.println("OK");
        }
        */
    }

    /*
     * Gets the texture file
     */
    public String getTextureFile(){
        return m_textureFile;
    }

    /*
     * Gets the texture
     *

    public Texture getTexture() {
        return
        //return m_texture;
    }
    */

    /*
    /
     * Tests if a texture is associated with this material
     *
     //
    Boolean hasTexture() {
        return m_texture != null;
    }
    */
}
