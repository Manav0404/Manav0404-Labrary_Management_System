import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RequestExtensionForm extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField bookIdField, bookNameField, authorField, quantityField, extensionDateField;
    private JButton addButton, saveButton, backToStudentFormButton;
    private Connection connection;

    public RequestExtensionForm(Connection connection) {
        this.connection = connection;

        setTitle("Request Extension Form");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library9.jpg"));
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
        tableModel.addColumn("Extension Date");

        table = new JTable(tableModel);
        bookIdField = new JTextField(10);
        bookNameField = new JTextField(10);
        authorField = new JTextField(10);
        quantityField = new JTextField(10);
        extensionDateField = new JTextField(10);
        addButton = new JButton("Add Book");
        saveButton = new JButton("Save");
        backToStudentFormButton = new JButton("Back to Student Form");

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
        inputPanel.add(new JLabel("Extension Date:"), inputConstraints);
        inputConstraints.gridx = 1;
        inputPanel.add(extensionDateField, inputConstraints);
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 5;
        inputConstraints.gridwidth = 2;
        inputPanel.add(addButton, inputConstraints);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(backToStudentFormButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBookDetails();
            }
        });

        backToStudentFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToStudentForm();
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
        add(buttonPanel, mainConstraints);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addBook() {
        String bookId = bookIdField.getText();
        String bookName = bookNameField.getText();
        String author = authorField.getText();
        String quantity = quantityField.getText();
        String extensionDate = extensionDateField.getText();

        if (!bookId.isEmpty() && !bookName.isEmpty() && !author.isEmpty() && !quantity.isEmpty() && !extensionDate.isEmpty()) {
            if (tableModel.getRowCount() < 5) {
                tableModel.addRow(new Object[]{bookId, bookName, author, quantity, extensionDate});
                bookIdField.setText("");
                bookNameField.setText("");
                authorField.setText("");
                quantityField.setText("");
                extensionDateField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Maximum 5 records allowed.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }

    private void saveBookDetails() {
        if (connection != null) {
            try {
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    String bookId = (String) tableModel.getValueAt(row, 0);
                    String bookName = (String) tableModel.getValueAt(row, 1);
                    String author = (String) tableModel.getValueAt(row, 2);
                    String quantity = (String) tableModel.getValueAt(row, 3);
                    String extensionDate = (String) tableModel.getValueAt(row, 4);

                    String sql = "INSERT INTO Requests (BookID, BookName, Author, Quantity, ExtensionDate) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, bookId);
                        preparedStatement.setString(2, bookName);
                        preparedStatement.setString(3, author);
                        preparedStatement.setString(4, quantity);
                        preparedStatement.setString(5, extensionDate);

                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Book details saved successfully.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to save book details.");
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection is not available.");
        }
    }

    private void goBackToStudentForm() {
        SwingUtilities.invokeLater(() -> {
            StudentForm studentForm = new StudentForm();
            studentForm.setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        Connection connection = setupDatabaseConnection();
        if (connection != null) {
            SwingUtilities.invokeLater(() -> {
                new RequestExtensionForm(connection);
            });
        }
    }

    private static Connection setupDatabaseConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String dbUrl = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\StudentFormDB.accdb"; // Replace with your database path
            Connection connection = DriverManager.getConnection(dbUrl);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
