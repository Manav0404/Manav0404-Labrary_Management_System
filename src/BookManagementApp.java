import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
import javax.imageio.ImageIO;

public class BookManagementApp extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField bookIdField, bookNameField, authorField, quantityField;
    private JButton addButton, saveButton, bookIssueButton, backToDashboardButton;
    private Connection conn;

    public BookManagementApp() {
        setTitle("Book Management");
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
        addButton = new JButton("Add Book");
        saveButton = new JButton("Save");
        bookIssueButton = new JButton("Book Issue");
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
        inputPanel.add(addButton, inputConstraints);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(bookIssueButton);
        buttonPanel.add(backToDashboardButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBookDetails();
            }
        });

        bookIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openBookIssueForm();
            }
        });

        backToDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToDashboard();
            }
        });

        saveButton.setBackground(new Color(0, 0, 0, 0));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));

        bookIssueButton.setBackground(new Color(0, 0, 0, 0));
        bookIssueButton.setForeground(Color.BLACK);
        bookIssueButton.setFont(new Font("Arial", Font.BOLD, 14));

        backToDashboardButton.setBackground(new Color(10, 20, 10, 20));
        backToDashboardButton.setForeground(Color.BLACK);
        backToDashboardButton.setFont(new Font("Arial", Font.BOLD, 14));

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

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String dbURL = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\BookManagement.accdb";
            conn = DriverManager.getConnection(dbURL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
        }
    }

    private void addBook() {
        // Get data from input fields
        String bookId = bookIdField.getText();
        String bookName = bookNameField.getText();
        String author = authorField.getText();
        String quantity = quantityField.getText();

        try {
            // Create a PreparedStatement to insert data into the "Addbook" table
            String sql = "INSERT INTO Addbook (BookID, BookName, Author, Quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, bookId);
            preparedStatement.setString(2, bookName);
            preparedStatement.setString(3, author);
            preparedStatement.setString(4, quantity);

            // Execute the insert statement
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Clear input fields
                bookIdField.setText("");
                bookNameField.setText("");
                authorField.setText("");
                quantityField.setText("");
                JOptionPane.showMessageDialog(this, "Book added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add the book: " + e.getMessage());
        }
    }

    private void saveBookDetails() {
        // Implement the logic to save book details to a file or database here.
        JOptionPane.showMessageDialog(this, "Book details saved successfully.");
    }

    private void openBookIssueForm() {
        BookIssueForm bookIssueForm = new BookIssueForm();
        bookIssueForm.setVisible(true);
    }

    private void goBackToDashboard() {
        LibraryDashboardForm dashboard = new LibraryDashboardForm();
        dashboard.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookManagementApp();
        });
    }
}
