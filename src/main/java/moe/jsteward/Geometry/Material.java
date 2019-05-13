package moe.jsteward.Geometry;

public class Material {
    protected
    RGBColor m_ambientColor ;
    RGBColor m_diffuseColor ;
    RGBColor m_specularColor ;
    double    m_shininess ;
    RGBColor m_emissiveColor ;
    string m_textureFile;
    Texture * m_texture;
    /*
     *  Constructor
     *  author Louis
     */
    public Material(RGBColor ambientColor, RGBColor diffuseColor, RGBColor specularColor, double shininess, RGBColor emissiveColor, string textureFile){
        ambientColor = RGBColor();
        diffuseColor = RGBColor();
        specularColor = RGBColor();
        shininess = 1.0;
        emissiveColor = RGBColor();
        textureFile = NULL;
        m_ambientColor = ambientColor;
        m_diffuseColor = diffuseColor;
        m_specularColor = specularColor;
        m_shininess = shininess;
        m_emissiveColor = emissiveColor;
        m_textureFile = textureFile;
        m_texture = NULL;
    }

    /*
     * Sets the ambient color
     */
    public void setAmbient(RGBColor color) {
        m_ambientColor = color;
    }

    /*
     * Gets the ambient color
     */
    public RGBColor getAmbient(){
        return m_ambientColor ;
    }

    /*
     * Sets the diffuse color
     */
    public void setDiffuse(RGBColor color) {
        m_diffuseColor = color;
    }

    /*
     * Gets the diffuse color
     */
    public RGBColor getDiffuse() {
        return m_diffuseColor ;
    }

    /*
     * Sets the specular color
     */
    public void setSpecular(RGBColor color) {
        m_specularColor = color;
    }

    /*
     * Gets the specular color
     */
    public RGBColor getSpecular() {
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
    void setEmissive(RGBColor color) {
        m_emissiveColor = color;
    }

    /*
     * Gets the emissive color
     */
    public RGBColor getEmissive() {
        return m_emissiveColor ;
    }

    /*
     * Sets the texture file
     * need to correct
     */
    public void setTextureFile(string textureFile)
    {
        m_textureFile = textureFile;
			System.out.println("Loading texture: " + m_textureFile + "...");
        m_texture = new Texture(m_textureFile);
        if (!m_texture->isValid())
        {
            delete m_texture;
            m_texture = NULL;
            System.out.println("discarded");
        }
        else
        {
            System.out.println("OK");
        }
    }

    /*
     * Gets the texture file
     */
    public string getTextureFile(){
        return m_textureFile;
    }

    /*
     * Gets the texture
     * need to correct
     */
    public Texture getTexture() {
        return *m_texture;
    }

    /*
     * Tests if a texture is associated with this material
     * need to correct
     */
    Boolean hasTexture() {
        return m_texture != NULL;
    }
}
