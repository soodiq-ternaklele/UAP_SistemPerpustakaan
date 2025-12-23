package org;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class LibraryApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private LibraryManager libraryManager;

    // Komponen untuk pinjam buku
    private JTextField borrowBookIdField;
    private JTextField borrowNameField;
    private JTextField borrowReturnDateField;

    // Komponen untuk kembalikan buku
    private JTextField returnTransactionIdField;
    private JComboBox<String> conditionBox;

    public LibraryApp() {
        libraryManager = new LibraryManager();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Perpustakaan Mini");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tambahkan panel-panel ke mainPanel
        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createDashboardPanel(), "dashboard");
        mainPanel.add(createBookListPanel(), "bookList");
        mainPanel.add(createBorrowBookPanel(), "borrowBook");
        mainPanel.add(createReturnBookPanel(), "returnBook");

        add(mainPanel);
    }

    // ==================== PANEL LOGIN ====================
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Perpustakaan Mini");
        titleLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 28));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Sistem Manajemen Perpustakaan");
        subtitleLabel.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        panel.add(subtitleLabel, gbc);

        // Spacer
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(30), gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        panel.add(userLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        panel.add(passLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;

        JButton loginButton = createRoundedButton("Masuk", new Color(70, 130, 180), Color.WHITE);
        loginButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(150, 45));

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("admin123")) {
                cardLayout.show(mainPanel, "dashboard");
                setTitle("Perpustakaan Mini - Dashboard");
                updateDashboardStatistics();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username atau password salah!\nCoba: admin/admin123",
                        "Login Gagal",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(loginButton, gbc);

        // Info login
        gbc.gridy = 6;
        JLabel infoLabel = new JLabel("Gunakan: admin / admin123");
        infoLabel.setFont(FontManager.getPoppinsFont(Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, gbc);

        return panel;
    }

    // ==================== PANEL DASHBOARD ====================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setName("dashboard"); // Tambahkan nama untuk identifikasi

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Dashboard Perpustakaan");
        titleLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Logout button
        JButton logoutButton = createRoundedButton("Keluar", new Color(220, 20, 60), Color.WHITE);
        logoutButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "login");
            setTitle("Perpustakaan Mini");
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Main Content - Menu Cards
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Menu 1: List Buku
        gbc.gridx = 0;
        gbc.gridy = 0;
        JPanel menu1 = createMenuCard("📚", "Daftar Buku", "Lihat koleksi buku yang tersedia");
        menu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadBooksToTable();
                cardLayout.show(mainPanel, "bookList");
                setTitle("Perpustakaan Mini - Daftar Buku");
            }
        });
        menuPanel.add(menu1, gbc);

        // Menu 2: Pinjam Buku
        gbc.gridx = 1;
        JPanel menu2 = createMenuCard("📖", "Pinjam Buku", "Pinjam buku dari perpustakaan");
        menu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "borrowBook");
                setTitle("Perpustakaan Mini - Pinjam Buku");
            }
        });
        menuPanel.add(menu2, gbc);

        // Menu 3: Kembalikan Buku
        gbc.gridx = 2;
        JPanel menu3 = createMenuCard("↩️", "Kembalikan Buku", "Kembalikan buku yang dipinjam");
        menu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "returnBook");
                setTitle("Perpustakaan Mini - Kembalikan Buku");
            }
        });
        menuPanel.add(menu3, gbc);

        // Menu 4: Statistik
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        int[] stats = libraryManager.getStatistics();
        JPanel menu4 = createMenuCard("📊", "Statistik",
                String.format("Total Buku: %d | Dipinjam: %d | Tersedia: %d",
                        stats[0], stats[1], stats[2]));
        menu4.setBackground(new Color(240, 248, 255));
        menu4.setName("statsCard");
        menuPanel.add(menu4, gbc);

        panel.add(menuPanel, BorderLayout.CENTER);

        return panel;
    }

    // ==================== PANEL DAFTAR BUKU ====================
    private JPanel createBookListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Daftar Buku");
        titleLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Back button
        JButton backButton = createRoundedButton("Kembali", new Color(100, 100, 100), Color.WHITE);
        backButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "dashboard");
            setTitle("Perpustakaan Mini - Dashboard");
            updateDashboardStatistics();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel searchLabel = new JLabel("Cari Buku:");
        searchLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        searchPanel.add(searchLabel);

        JTextField searchField = new JTextField(25);
        searchField.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        searchPanel.add(searchField);

        JButton searchButton = createRoundedButton("Cari", new Color(60, 179, 113), Color.WHITE);
        searchButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        searchButton.addActionListener(e -> {
            String text = searchField.getText();
            if (text.trim().isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });
        searchPanel.add(searchButton);

        // Refresh button
        JButton refreshButton = createRoundedButton("Refresh", new Color(70, 130, 180), Color.WHITE);
        refreshButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        refreshButton.addActionListener(e -> loadBooksToTable());
        searchPanel.add(refreshButton);

        // Panel atas (Header + Search)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columns = {"ID", "Judul", "Penulis", "Tahun", "Status", "Kategori", "Peminjam", "Tgl Pinjam", "Tgl Kembali"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);

        bookTable.setRowHeight(30);
        bookTable.getTableHeader().setFont(FontManager.getPoppinsFont(Font.BOLD, 12));
        bookTable.setFont(FontManager.getPoppinsFont(Font.PLAIN, 12));
        bookTable.getTableHeader().setBackground(new Color(240, 240, 240));
        bookTable.setShowGrid(true);
        bookTable.setGridColor(new Color(220, 220, 220));

        // Set column widths
        int[] columnWidths = {50, 200, 120, 50, 80, 100, 120, 100, 100};
        for (int i = 0; i < columnWidths.length; i++) {
            bookTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ==================== PANEL PINJAM BUKU ====================
    private JPanel createBorrowBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = createHeaderPanel("Pinjam Buku", "borrowBook");
        panel.add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ID Buku
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("ID Buku:");
        idLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        formPanel.add(idLabel, gbc);

        borrowBookIdField = new JTextField();
        borrowBookIdField.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        borrowBookIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 1;
        formPanel.add(borrowBookIdField, gbc);

        // Nama Peminjam
        gbc.gridy = 2;
        JLabel nameLabel = new JLabel("Nama Peminjam:");
        nameLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        formPanel.add(nameLabel, gbc);

        borrowNameField = new JTextField();
        borrowNameField.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        borrowNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 3;
        formPanel.add(borrowNameField, gbc);

        // Tanggal Pinjam (otomatis)
        gbc.gridy = 4;
        JLabel borrowDateLabel = new JLabel("Tanggal Pinjam:");
        borrowDateLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        formPanel.add(borrowDateLabel, gbc);

        JTextField borrowDateField = new JTextField(LocalDate.now().toString());
        borrowDateField.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        borrowDateField.setEditable(false);
        borrowDateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 5;
        formPanel.add(borrowDateField, gbc);

        // Tanggal Pengembalian
        gbc.gridy = 6;
        JLabel returnDateLabel = new JLabel("Tanggal Pengembalian:");
        returnDateLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        formPanel.add(returnDateLabel, gbc);

        borrowReturnDateField = new JTextField(LibraryManager.generateReturnDate());
        borrowReturnDateField.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        borrowReturnDateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 7;
        formPanel.add(borrowReturnDateField, gbc);

        // Info tanggal
        JLabel dateInfoLabel = new JLabel("Format: YYYY-MM-DD (Default: 7 hari dari sekarang)");
        dateInfoLabel.setFont(FontManager.getPoppinsFont(Font.ITALIC, 11));
        dateInfoLabel.setForeground(Color.GRAY);
        gbc.gridy = 8;
        formPanel.add(dateInfoLabel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton submitButton = createRoundedButton("Pinjam Buku", new Color(60, 179, 113), Color.WHITE);
        submitButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.addActionListener(e -> {
            if (validateBorrowForm()) {
                boolean success = libraryManager.borrowBook(
                        borrowBookIdField.getText().trim(),
                        borrowNameField.getText().trim(),
                        borrowReturnDateField.getText().trim()
                );

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Buku berhasil dipinjam!\n" +
                                    "ID Buku: " + borrowBookIdField.getText() + "\n" +
                                    "Peminjam: " + borrowNameField.getText() + "\n" +
                                    "Tanggal Pinjam: " + borrowDateField.getText() + "\n" +
                                    "Tanggal Kembali: " + borrowReturnDateField.getText(),
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    clearBorrowForm();
                    updateDashboardStatistics();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal meminjam buku!\n" +
                                    "Pastikan:\n" +
                                    "1. ID buku benar\n" +
                                    "2. Status buku 'Tersedia'\n" +
                                    "3. Format tanggal YYYY-MM-DD",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = createRoundedButton("Batal", new Color(220, 20, 60), Color.WHITE);
        cancelButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> clearBorrowForm());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(buttonPanel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    // ==================== PANEL KEMBALIKAN BUKU ====================
    private JPanel createReturnBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = createHeaderPanel("Kembalikan Buku", "returnBook");
        panel.add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ID Transaksi / ID Buku
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel transLabel = new JLabel("ID Transaksi atau ID Buku:");
        transLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        formPanel.add(transLabel, gbc);

        returnTransactionIdField = new JTextField();
        returnTransactionIdField.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        returnTransactionIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 1;
        formPanel.add(returnTransactionIdField, gbc);

        // Info label
        JLabel infoLabel = new JLabel("<html>Masukkan ID Transaksi (T...) atau ID Buku (B...)<br>Contoh: T123456789 atau B001</html>");
        infoLabel.setFont(FontManager.getPoppinsFont(Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        formPanel.add(infoLabel, gbc);

        // Button Panel untuk pencarian
        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchButtonPanel.setBackground(Color.WHITE);
        searchButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton findTransactionButton = createRoundedButton("Cari Transaksi",
                new Color(70, 130, 180), Color.WHITE);
        findTransactionButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 12));
        findTransactionButton.setPreferredSize(new Dimension(120, 30));
        findTransactionButton.addActionListener(e -> {
            String searchId = returnTransactionIdField.getText().trim();
            if (!searchId.isEmpty()) {
                showTransactionInfo(searchId);
            } else {
                JOptionPane.showMessageDialog(this, "Masukkan ID Transaksi atau ID Buku terlebih dahulu!",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });
        searchButtonPanel.add(findTransactionButton);

        // Button lihat semua transaksi aktif
        JButton viewAllButton = createRoundedButton("Lihat Semua",
                new Color(100, 149, 237), Color.WHITE);
        viewAllButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 12));
        viewAllButton.setPreferredSize(new Dimension(100, 30));
        viewAllButton.addActionListener(e -> {
            showAllActiveTransactions();
        });
        searchButtonPanel.add(viewAllButton);

        gbc.gridy = 3;
        formPanel.add(searchButtonPanel, gbc);

        // Kondisi Buku
        gbc.gridy = 4;
        JLabel conditionLabel = new JLabel("Kondisi Buku Saat Dikembalikan:");
        conditionLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        formPanel.add(conditionLabel, gbc);

        String[] conditions = {"Baik", "Ringan (ada sedikit kerusakan)",
                "Sedang (ada kerusakan sedang)", "Berat (rusak parah)"};
        conditionBox = new JComboBox<>(conditions);
        conditionBox.setFont(FontManager.getPoppinsFont(Font.PLAIN, 14));
        conditionBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 5;
        formPanel.add(conditionBox, gbc);

        // Button Panel untuk pengembalian
        JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        returnButtonPanel.setBackground(Color.WHITE);
        returnButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton submitButton = createRoundedButton("Kembalikan Buku", new Color(60, 179, 113), Color.WHITE);
        submitButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.addActionListener(e -> {
            String searchId = returnTransactionIdField.getText().trim();
            String condition = (String) conditionBox.getSelectedItem();

            if (!searchId.isEmpty()) {
                // Debug: print informasi transaksi aktif
                libraryManager.printActiveTransactions();
                libraryManager.printBorrowedBooks();

                boolean success = libraryManager.returnBook(searchId, condition);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Buku berhasil dikembalikan!\n" +
                                    "ID yang digunakan: " + searchId + "\n" +
                                    "Kondisi: " + condition + "\n" +
                                    "Tanggal Pengembalian: " + LocalDate.now().toString(),
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);

                    // Reset form
                    returnTransactionIdField.setText("");
                    conditionBox.setSelectedIndex(0);

                    // Refresh data di panel lain jika perlu
                    loadBooksToTable();
                    updateDashboardStatistics();

                } else {
                    JOptionPane.showMessageDialog(this,
                            "<html><b>Gagal mengembalikan buku!</b><br><br>" +
                                    "Kemungkinan penyebab:<br>" +
                                    "1. ID Transaksi/ID Buku tidak ditemukan<br>" +
                                    "2. Buku sudah dikembalikan sebelumnya<br>" +
                                    "3. Tidak ada transaksi aktif untuk ID tersebut<br><br>" +
                                    "Coba gunakan tombol 'Lihat Semua' untuk melihat daftar transaksi aktif</html>",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "ID Transaksi atau ID Buku harus diisi!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButtonPanel.add(submitButton);

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(returnButtonPanel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    // ==================== HELPER METHODS ====================
    private JButton createRoundedButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(fgColor);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border for rounded buttons
            }
        };
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        return button;
    }

    private JPanel createMenuCard(String icon, String title, String description) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, BorderLayout.CENTER);

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(FontManager.getPoppinsFont(Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createHeaderPanel(String title, String panelName) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontManager.getPoppinsFont(Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = createRoundedButton("Kembali", new Color(100, 100, 100), Color.WHITE);
        backButton.setFont(FontManager.getPoppinsFont(Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "dashboard");
            setTitle("Perpustakaan Mini - Dashboard");
            updateDashboardStatistics();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private boolean validateBorrowForm() {
        if (borrowBookIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Buku harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (borrowNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Peminjam harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (borrowReturnDateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal Pengembalian harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearBorrowForm() {
        borrowBookIdField.setText("");
        borrowNameField.setText("");
        borrowReturnDateField.setText(LibraryManager.generateReturnDate());
    }

    private void loadBooksToTable() {
        tableModel.setRowCount(0);
        List<Book> books = libraryManager.getAllBooks();

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getYear(),
                    book.getStatus(),
                    book.getCategory(),
                    book.getBorrower(),
                    book.getBorrowDate(),
                    book.getReturnDate()
            });
        }
    }

    private void updateDashboardStatistics() {
        int[] stats = libraryManager.getStatistics();
        String statText = String.format("Total Buku: %d | Dipinjam: %d | Tersedia: %d",
                stats[0], stats[1], stats[2]);

        // Update label statistik di dashboard
        for (Component comp : mainPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals("dashboard")) {
                JPanel dashboardPanel = (JPanel) comp;
                for (Component child : dashboardPanel.getComponents()) {
                    if (child instanceof JPanel) {
                        JPanel innerPanel = (JPanel) child;
                        for (Component grandChild : innerPanel.getComponents()) {
                            if (grandChild.getName() != null && grandChild.getName().equals("statsCard")) {
                                updateStatLabelInCard((JPanel) grandChild, statText);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateStatLabelInCard(JPanel card, String statText) {
        for (Component comp : card.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().startsWith("<html>")) {
                    label.setText("<html><center>" + statText + "</center></html>");
                    break;
                }
            }
        }
    }

    // Method untuk menampilkan semua transaksi aktif
    private void showAllActiveTransactions() {
        List<BookTransaction> activeTransactions = libraryManager.getActiveTransactions();

        if (activeTransactions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada transaksi peminjaman aktif saat ini.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("<html><b>DAFTAR TRANSAKSI AKTIF:</b><br><br>");
        message.append("<table border='1' cellpadding='5' style='border-collapse: collapse;'>");
        message.append("<tr style='background-color:#f2f2f2'>");
        message.append("<th>No</th>");
        message.append("<th>ID Transaksi</th>");
        message.append("<th>ID Buku</th>");
        message.append("<th>Judul Buku</th>");
        message.append("<th>Peminjam</th>");
        message.append("<th>Tgl Pinjam</th>");
        message.append("<th>Tgl Kembali</th>");
        message.append("</tr>");

        int count = 1;
        for (BookTransaction transaction : activeTransactions) {
            Book book = libraryManager.getBookById(transaction.getBookId());
            String bookTitle = (book != null) ? book.getTitle() : "Buku tidak ditemukan";

            message.append("<tr>");
            message.append("<td align='center'>").append(count).append("</td>");
            message.append("<td><b>").append(transaction.getTransactionId()).append("</b></td>");
            message.append("<td>").append(transaction.getBookId()).append("</td>");
            message.append("<td>").append(bookTitle).append("</td>");
            message.append("<td>").append(transaction.getBorrowerName()).append("</td>");
            message.append("<td>").append(LibraryManager.formatIndonesianDate(transaction.getBorrowDate())).append("</td>");
            message.append("<td>").append(LibraryManager.formatIndonesianDate(transaction.getReturnDate())).append("</td>");
            message.append("</tr>");
            count++;
        }

        message.append("</table><br>");
        message.append("<i>Gunakan ID Transaksi atau ID Buku untuk mengembalikan buku</i></html>");

        // Buat dialog khusus dengan scroll pane
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText(message.toString());
        textPane.setEditable(false);
        textPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(700, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Daftar Transaksi Aktif", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method untuk menampilkan info transaksi
    private void showTransactionInfo(String searchId) {
        BookTransaction transaction = libraryManager.findTransaction(searchId);

        if (transaction != null) {
            Book book = libraryManager.getBookById(transaction.getBookId());
            String bookTitle = (book != null) ? book.getTitle() : "Buku tidak ditemukan";
            String bookAuthor = (book != null) ? book.getAuthor() : "-";

            String message = String.format(
                    "<html><b>DETAIL TRANSAKSI:</b><br><br>" +
                            "<table border='0' cellpadding='3'>" +
                            "<tr><td><b>ID Transaksi:</b></td><td>%s</td></tr>" +
                            "<tr><td><b>ID Buku:</b></td><td>%s</td></tr>" +
                            "<tr><td><b>Judul Buku:</b></td><td>%s</td></tr>" +
                            "<tr><td><b>Penulis:</b></td><td>%s</td></tr>" +
                            "<tr><td><b>Peminjam:</b></td><td>%s</td></tr>" +
                            "<tr><td><b>Tanggal Pinjam:</b></td><td>%s</td></tr>" +
                            "<tr><td><b>Tanggal Kembali:</b></td><td>%s</td></tr>" +
                            "</table><br>" +
                            "<i>Klik 'Kembalikan Buku' untuk melanjutkan</i></html>",
                    transaction.getTransactionId(),
                    transaction.getBookId(),
                    bookTitle,
                    bookAuthor,
                    transaction.getBorrowerName(),
                    LibraryManager.formatIndonesianDate(transaction.getBorrowDate()),
                    LibraryManager.formatIndonesianDate(transaction.getReturnDate())
            );

            JOptionPane.showMessageDialog(this, message, "Info Transaksi", JOptionPane.INFORMATION_MESSAGE);

            // Otomatis isi field ID transaksi dengan ID yang ditemukan
            returnTransactionIdField.setText(transaction.getTransactionId());

        } else {
            // Cek apakah ini ID buku yang sedang dipinjam
            Book book = libraryManager.getBookById(searchId);
            if (book != null && book.getStatus().equals("Dipinjam")) {
                // Cari transaksi berdasarkan ID buku
                BookTransaction bookTransaction = libraryManager.getTransactionByBookId(searchId);
                if (bookTransaction != null) {
                    showTransactionInfo(bookTransaction.getTransactionId());
                    return;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "<html><b>Transaksi tidak ditemukan!</b><br><br>" +
                            "ID: " + searchId + "<br>" +
                            "Kemungkinan:<br>" +
                            "1. Transaksi tidak ada<br>" +
                            "2. Buku sudah dikembalikan<br>" +
                            "3. ID salah<br><br>" +
                            "Coba gunakan tombol 'Lihat Semua'</html>",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}