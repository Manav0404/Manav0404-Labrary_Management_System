import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PurchaseOrderForm extends JFrame {
    private JTextField supplierNameField, supplierIdField, supplierEmailField, bookIdField, bookNameField, authorField, quantityField;
    private JButton orderButton, backButton;

    public PurchaseOrderForm() {
        setTitle("Purchase Order Form");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library3.jpg"));
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (background != null) {
                        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            };
            setContentPane(backgroundPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        supplierNameField = new JTextField(10);
        supplierIdField = new JTextField(10);
        supplierEmailField = new JTextField(20);
        bookIdField = new JTextField(10);
        bookNameField = new JTextField(10);
        authorField = new JTextField(10);
        quantityField = new JTextField(10);
        orderButton = new JButton("Order");
        backButton = new JButton("Back");

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
        inputPanel.add(new JLabel("Supplier Email:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(supplierEmailField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 3;
        inputPanel.add(new JLabel("Book ID:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(bookIdField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 4;
        inputPanel.add(new JLabel("Book Name:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(bookNameField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 5;
        inputPanel.add(new JLabel("Author:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(authorField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 6;
        inputPanel.add(new JLabel("Quantity:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(quantityField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 7;
        inputConstraints.gridwidth = 2;
        inputPanel.add(orderButton, inputConstraints);

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        add(inputPanel, mainConstraints);
        mainConstraints.gridy = 1;
        mainConstraints.insets = new Insets(10, 0, 0, 0); // Add vertical gap
        add(backButton, mainConstraints);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void placeOrder() {
        String databaseURL = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\SupplierDB.accdb"; 
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(databaseURL);
            String insertQuery = "INSERT INTO PurchaseOrder (SupplierName, SupplierID, SupplierEmail, BookID, BookName, Author, Quantity) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, supplierNameField.getText());
            preparedStatement.setString(2, supplierIdField.getText());
            preparedStatement.setString(3, supplierEmailField.getText());
            preparedStatement.setString(4, bookIdField.getText());
            preparedStatement.setString(5, bookNameField.getText());
            preparedStatement.setString(6, authorField.getText());
            preparedStatement.setInt(7, Integer.parseInt(quantityField.getText()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Data inserted successfully
                JOptionPane.showMessageDialog(this, "Order placed successfully.");
                clearFields(); // Clear input fields after successful order
            } else {
                JOptionPane.showMessageDialog(this, "Failed to place the order.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void goBack() {
        SwingUtilities.invokeLater(() -> {
            SupplierForm supplierForm = new SupplierForm();
            supplierForm.setVisible(true);
            dispose();
        });
    }

    private void clearFields() {
        supplierNameField.setText("");
        supplierIdField.setText("");
        supplierEmailField.setText("");
        bookIdField.setText("");
        bookNameField.setText("");
        authorField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PurchaseOrderForm();
        });
    }
}
