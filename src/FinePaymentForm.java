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
import java.sql.Timestamp;
import java.util.Date;
import javax.imageio.ImageIO;

public class FinePaymentForm extends JFrame {

    private BufferedImage background;
    private Connection connection;

    public FinePaymentForm() {
        setTitle("Fine Payment");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            }
        });

        // Initialize the database connection
        String databaseURL = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\FinePayDB.accdb";
        try {
            connection = DriverManager.getConnection(databaseURL);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database: " + e.getMessage());
        }

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel headingLabel = new JLabel("FINE PAYMENT");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(headingLabel, constraints);

        addFormField("Full Name:", 1);
        JTextField fullNameField = new JTextField(20);
        addFormField(fullNameField, 2);

        addFormField("Library/Faculty ID:", 3);
        JTextField idField = new JTextField(20);
        addFormField(idField, 4);

        addFormField("Book Name:", 5);
        JTextField bookNameField = new JTextField(20);
        addFormField(bookNameField, 6);

        addFormField("Fine Amount:", 7);
        JTextField fineAmountField = new JTextField(20);
        addFormField(fineAmountField, 8);

        addFormField("Status:", 9);
        JTextField statusField = new JTextField(20);
        addFormField(statusField, 10);

        JButton payFineButton = new JButton("Pay Fine");
        payFineButton.setFont(new Font("Arial", Font.BOLD, 16));
        payFineButton.setForeground(Color.BLACK);
        constraints.gridx = 0;
        constraints.gridy = 11;
        constraints.gridwidth = 2;
        add(payFineButton, constraints);

        payFineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = fullNameField.getText();
                String id = idField.getText();
                String bookName = bookNameField.getText();
                String fineAmount = fineAmountField.getText();
                String status = statusField.getText();

                String summary = "Payment Summary:\n" +
                        "Full Name: " + fullName + "\n" +
                        "Library/Faculty ID: " + id + "\n" +
                        "Book Name: " + bookName + "\n" +
                        "Fine Amount: $" + fineAmount + "\n" +
                        "Status: " + status;

                JOptionPane.showMessageDialog(null, summary, "Payment Summary", JOptionPane.INFORMATION_MESSAGE);

                // Save the payment details to the database
                savePaymentDetails(fullName, id, bookName, fineAmount, status);
            }
        });

        JButton backToDashboardButton = new JButton("Back to Dashboard");
        backToDashboardButton.setFont(new Font("Arial", Font.BOLD, 16));
        backToDashboardButton.setForeground(Color.BLACK);
        constraints.gridx = 0;
        constraints.gridy = 12;
        constraints.gridwidth = 2;
        add(backToDashboardButton, constraints);

        backToDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToDashboard();
            }
        });
    }

    private void savePaymentDetails(String fullName, String id, String bookName, String fineAmount, String status) {
        if (connection != null) {
            String insertQuery = "INSERT INTO PaymentHistory (FullName, ID, BookName, FineAmount, Status, PaymentDate) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, id);
                preparedStatement.setString(3, bookName);
                preparedStatement.setDouble(4, Double.parseDouble(fineAmount));
                preparedStatement.setString(5, status);
                preparedStatement.setTimestamp(6, new Timestamp(new Date().getTime()));

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Payment details saved to the database.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save payment details.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while saving payment details: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection is not available.");
        }
    }

    private void goBackToDashboard() {
        SwingUtilities.invokeLater(() -> {
            LibraryDashboardForm dashboard = new LibraryDashboardForm();
            dashboard.setVisible(true);
            dispose();
        });
    }

    private void addFormField(String label, int gridY) {
        JLabel formLabel = new JLabel(label);
        formLabel.setFont(new Font("Arial", Font.BOLD, 16));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = gridY;
        add(formLabel, constraints);
    }

    private void addFormField(JTextField field, int gridY) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = gridY;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(field, constraints);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FinePaymentForm().setVisible(true);
        });
    }
}
