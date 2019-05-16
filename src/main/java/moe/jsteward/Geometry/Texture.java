package moe.jsteward.Geometry;

public class Texture {
    private
    int m_width;
    int m_height;
    char[]  m_data;/*Maybe unsigned */

    /*
     * Constructor
     * Author Louis
     */
    public Texture(string filename) {
        m_data = SOIL_load_image(filename.c_str(), &m_width, &m_height, 0, SOIL_LOAD_RGB);
        /* pointer need TODO*/
        if (m_data == NULL)
        {
				System.out.println("Invalid texture file: " + filename);
        }
    }

    /*
     * Dstructor need todo

    public ~Texture() {
        SOIL_free_image_data(m_data);
    }
    */
    /*
     * Verify if m_data is NULL
     */
    public boolean isValid() {
        return m_data != NULL;
    }

    /*
     * Calculate the pixel
     */
    public Color pixel(int x, int y) {
        while (x < 0) { x += m_width; }
        while (y < 0) { y += m_height; }
        x = x%m_width;
        y = y%m_height;
        int offset = y * 3 * m_width + x * 3;
        unsigned char r = m_data[offset];
        unsigned char g = m_data[offset + 1];
        unsigned char b = m_data[offset + 2];
        return Color((double)(r) / 255.0f, (double)(g) / 255.0f, (double)(b) / 255.0f);
    }

    public Color pixel(Vector2f v) {
        return pixel((int)(v[0] * m_width), (int)(v[1] * m_height));
    }
}
