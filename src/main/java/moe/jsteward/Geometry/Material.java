package moe.jsteward.Geometry;

public class Material {
    protected
    Color m_ambientColor ;
    Color m_diffuseColor ;
    Color m_specularColor ;
    double    m_shininess ;
    Color m_emissiveColor ;
    string m_textureFile;
    Texture * m_texture;/* Pointer need todo */



    /*
     *  Constructor
     *  author Louis
     */
    public Material(Color ambientColor, Color diffuseColor, Color specularColor, double shininess, Color emissiveColor, string textureFile){
        ambientColor = Color();
        diffuseColor = Color();
        specularColor = Color();
        shininess = 1.0;
        emissiveColor = Color();
        textureFile = NULL;
        m_ambientColor = ambientColor;
        m_diffuseColor = diffuseColor;
        m_specularColor = specularColor;
        m_shininess = shininess;
        m_emissiveColor = emissiveColor;
        m_textureFile = textureFile;
        m_texture = "";
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
     * need todo
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
     * need todo
     */
    public Texture getTexture() {
        return m_texture;
    }

    /*
     * Tests if a texture is associated with this material
     * need todo
     */
    Boolean hasTexture() {
        return m_texture != "";
    }
}
