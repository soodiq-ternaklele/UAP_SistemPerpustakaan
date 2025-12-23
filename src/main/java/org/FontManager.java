package org;

import java.awt.*;

public class FontManager {
    public static void loadPoppinsFont() {
        try {
            Font poppinsFont = Font.createFont(Font.TRUETYPE_FONT,
                    Main.class.getResourceAsStream("/Poppins-Regular.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(poppinsFont);
        } catch (Exception e) {
            // Font tidak ditemukan, akan menggunakan font default
            System.out.println("Font Poppins tidak ditemukan: " + e.getMessage());
        }
    }

    public static Font getPoppinsFont(int style, int size) {
        try {
            return new Font("Poppins", style, size);
        } catch (Exception e) {
            return new Font("Dialog", style, size);
        }
    }
}