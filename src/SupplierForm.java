import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.imageio.ImageIO;

public class SupplierForm extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField supplierNameField, supplierIdField, bookIdField, bookNameField, authorField, quantityField;
    private JButton addButton, saveButton, backToDashboardButton, createPurchaseOrderButton;

    public SupplierForm() {
        setTitle("Supplier Form");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            Image background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library3.jpg"));
            setContentPane(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (background != null) {
                        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Supplier Name");
        tableModel.addColumn("Supplier ID");
        tableModel.addColumn("Book ID");
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Author");
        tableModel.addColumn("Quantity");

        table = new JTable(tableModel);
        supplierNameField = new JTextField(10);
        supplierIdField = new JTextField(10);
        bookIdField = new JTextField(10);
        bookNameField = new JTextField(10);
        authorField = new JTextField(10);
        quantityField = new JTextField(10);
        addButton = new JButton("Add Book");
        saveButton = new JButton("Save");
        backToDashboardButton = new JButton("Back to Dashboard");
        createPurchaseOrderButton = new JButton("Create Purchase Order");

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.insets = new Insets(10, 10, 10, 10);

        inputConstraints.gridx = 0;
        inputConstraints.gridy = 0;
        inputPanel.add(new JLabel("Supplier Name:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(supplierNameField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 1;
        inputPanel.add(new JLabel("Supplier ID:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(supplierIdField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 2;
        inputPanel.add(new JLabel("Book ID:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(bookIdField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 3;
        inputPanel.add(new JLabel("Book Name:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(bookNameField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 4;
        inputPanel.add(new JLabel("Author:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(authorField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 5;
        inputPanel.add(new JLabel("Quantity:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(quantityField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 6;
        inputConstraints.gridwidth = 2;
        inputPanel.add(addButton, inputConstraints);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBookDetails();
            }
        });

        backToDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToDashboard();
            }
        });

        createPurchaseOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPurchaseOrderForm();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        add(inputPanel, mainConstraints);
        mainConstraints.gridy = 1;
        add(new JScrollPane(table), mainConstraints);
        mainConstraints.gridy = 2;
        mainConstraints.insets = new Insets(10, 0, 0, 0); // Add vertical gap
        add(saveButton, mainConstraints);
        mainConstraints.gridy = 3;
        mainConstraints.insets = new Insets(10, 0, 0, 0); // Add vertical gap
        add(createPurchaseOrderButton, mainConstraints);
        mainConstraints.gridy = 4;
        mainConstraints.insets = new Insets(10, 0, 10, 0); // Add vertical gap
        add(backToDashboardButton, mainConstraints);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addBook() {
        String supplierName = supplierNameField.getText();
        String supplierId = supplierIdField.getText();
        String bookId = bookIdField.getText();
        String bookName = bookNameField.getText();
        String author = authorField.getText();
        String quantity = quantityField.getText();

        if (!supplierName.isEmpty() && !supplierId.isEmpty() && !bookId.isEmpty() && !bookName.isEmpty() && !author.isEmpty() && !quantity.isEmpty()) {
            if (tableModel.getRowCount() < 5) {
                tableModel.addRow(new Object[]{supplierName, supplierId, bookId, bookName, author, quantity});
                supplierNameField.setText("");
                supplierIdField.setText("");
                bookIdField.setText("");
                bookNameField.setText("");
                authorField.setText("");
                quantityField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Maximum 5 records allowed.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }

    private void saveBookDetails() {
        try {
            // Establish a database connection
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String databaseURL = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\SupplierDB.accdb";
            Connection connection = DriverManager.getConnection(databaseURL);

            // Prepare and execute the SQL insert statement for each row in the table
            String sql = "INSERT INTO Supplier (SupplierName, SupplierID, BookID, BookName, Author, Quantity) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                preparedStatement.setString(1, tableModel.getValueAt(row, 0).toString());
                preparedStatement.setString(2, tableModel.getValueAt(row, 1).toString());
                preparedStatement.setString(3, tableModel.getValueAt(row, 2).toString());
                preparedStatement.setString(4, tableModel.getValueAt(row, 3).toString());
                preparedStatement.setString(5, tableModel.getValueAt(row, 4).toString());
                preparedStatement.setString(6, tableModel.getValueAt(row, 5).toString());
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
            connection.close();

            JOptionPane.showMessageDialog(this, "Book details saved successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage());
        }
    }

    private void goBackToDashboard() {
        SwingUtilities.invokeLater(() -> {
            LibraryDashboardForm dashboard = new LibraryDashboardForm();
            dashboard.setVisible(true);
            dispose();
        });
    }

    private void openPurchaseOrderForm() {
        SwingUtilities.invokeLater(() -> {
            PurchaseOrderForm purchaseOrderForm = new PurchaseOrderForm();
            purchaseOrderForm.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SupplierForm();
        });
    }
}
