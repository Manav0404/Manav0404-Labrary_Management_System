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
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;

public class InventoryForm extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField bookIdField, bookNameField, authorField, quantityField;
    private JButton saveButton, generateReportButton, backToDashboardButton;

    public InventoryForm() {
        setTitle("Inventory Form");
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

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Book ID");
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Author");
        tableModel.addColumn("Quantity");

        table = new JTable(tableModel);
        bookIdField = new JTextField(10);
        bookNameField = new JTextField(10);
        authorField = new JTextField(10);
        quantityField = new JTextField(10);
        saveButton = new JButton("Save");
        generateReportButton = new JButton("Generate Report");
        backToDashboardButton = new JButton("Back to Dashboard");

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.insets = new Insets(10, 10, 10, 10);

        inputConstraints.gridx = 0;
        inputConstraints.gridy = 0;
        inputPanel.add(new JLabel("Book ID:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(bookIdField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 1;
        inputPanel.add(new JLabel("Book Name:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(bookNameField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 2;
        inputPanel.add(new JLabel("Author:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(authorField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 3;
        inputPanel.add(new JLabel("Quantity:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(quantityField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 4;
        inputConstraints.gridwidth = 2;
        inputPanel.add(saveButton, inputConstraints);
        inputConstraints.gridy = 5;
        inputPanel.add(generateReportButton, inputConstraints);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInventoryData();
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateInventoryReport();
            }
        });

        backToDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToDashboard();
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
        add(backToDashboardButton, mainConstraints);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveInventoryData() {
        String databaseURL = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\InventoryFormDb.accdb";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(databaseURL);
            String insertQuery = "INSERT INTO Inventory (BookID, BookName, Author, Quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, bookIdField.getText());
            preparedStatement.setString(2, bookNameField.getText());
            preparedStatement.setString(3, authorField.getText());
            preparedStatement.setInt(4, Integer.parseInt(quantityField.getText()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data saved successfully.");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save the data.");
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

    private void generateInventoryReport() {
        String databaseURL = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\InventoryFormDb.accdb";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(databaseURL);
            String selectQuery = "SELECT BookID, BookName, Author, Quantity FROM Inventory";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Remove existing rows from the table
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }

            // Add retrieved data to the table
            while (resultSet.next()) {
                String bookID = resultSet.getString("BookID");
                String bookName = resultSet.getString("BookName");
                String author = resultSet.getString("Author");
                int quantity = resultSet.getInt("Quantity");

                tableModel.addRow(new Object[]{bookID, bookName, author, quantity});
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

    private void goBackToDashboard() {
        SwingUtilities.invokeLater(() -> {
            LibraryDashboardForm dashboard = new LibraryDashboardForm();
            dashboard.setVisible(true);
            dispose();
        });
    }

    private void clearFields() {
        bookIdField.setText("");
        bookNameField.setText("");
        authorField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InventoryForm();
        });
    }
}
