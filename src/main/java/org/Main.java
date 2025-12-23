package org;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set font Poppins jika tersedia
        try {
            FontManager.loadPoppinsFont();
        } catch (Exception e) {
            System.out.println("Font Poppins tidak ditemukan, menggunakan font default");
        }

        // Gunakan SwingUtilities untuk thread safety
        SwingUtilities.invokeLater(() -> {
            LibraryApp app = new LibraryApp();
            app.setVisible(true);
        });
    }
}
