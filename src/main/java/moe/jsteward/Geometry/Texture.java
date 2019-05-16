package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import javafx.scene.paint.Color;

public class Texture {
    private
    int m_width;
    int m_height;
    char[]  m_data;/*Maybe unsigned */

    /*
     * Constructor
     * Author Louis
     */
    public Texture(String filename) {
        m_data = SOIL_load_image(filename.c_str(), &m_width, &m_height, 0, SOIL_LOAD_RGB);
        /* pointer need TODO*/
        if (m_data == null)
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
     * Verify if m_data is null
     */
    public boolean isValid() {
        return m_data != null;
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
        char r = m_data[offset];
        char g = m_data[offset + 1];
        char b = m_data[offset + 2];
        return Color((double)(r) / 255.0f, (double)(g) / 255.0f, (double)(b) / 255.0f);
    }

    public Color pixel(Vector2D v) {
        return pixel((int)(v[0] * m_width), (int)(v[1] * m_height));
    }
}
