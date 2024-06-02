import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LibraryCardRegistrationForm extends JFrame {
    private JTextField fullNameField, studentIdField, studentClassField, emailField;

    public LibraryCardRegistrationForm() {
        setTitle("Library Card Registration");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image
        try {
            Image background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library10.jpg"));
            setContentPane(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (background != null) {
                        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel headingLabel = new JLabel("LIBRARY CARD REGISTRATION");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        contentPanel.add(headingLabel, constraints);

        fullNameField = new JTextField(20);
        studentIdField = new JTextField(20);
        studentClassField = new JTextField(20);
        emailField = new JTextField(20);

        addLabelAndField("Student Full Name:", fullNameField, contentPanel, constraints, 1);
        addLabelAndField("Student ID:", studentIdField, contentPanel, constraints, 2);
        addLabelAndField("Student Class:", studentClassField, contentPanel, constraints, 3);
        addLabelAndField("Email:", emailField, contentPanel, constraints, 4);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(20, 10, 10, 10);
        contentPanel.add(registerButton, constraints);

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 1;
        contentPanel.add(clearButton, constraints);

        JButton backButton = new JButton("Back to Student Page");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        contentPanel.add(backButton, constraints);

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (registerStudent()) {
                    JOptionPane.showMessageDialog(LibraryCardRegistrationForm.this, "Registration successful.");
                    clearFields(); // Clear fields after successful registration
                } else {
                    JOptionPane.showMessageDialog(LibraryCardRegistrationForm.this, "Registration failed.");
                }
            }
        });

        // Clear button action
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToStudentPage();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        add(contentPanel, mainConstraints);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addLabelAndField(String label, JTextField textField, JPanel contentPanel, GridBagConstraints constraints, int gridY) {
        JLabel empLabel = new JLabel(label);
        empLabel.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = gridY;
        constraints.insets = new Insets(10, 30, 10, 30);
        contentPanel.add(empLabel, constraints);

        constraints.gridx = 1;
        constraints.insets = new Insets(10, 200, 10, 200);
        contentPanel.add(textField, constraints);
    }

    private void clearFields() {
        fullNameField.setText("");
        studentIdField.setText("");
        studentClassField.setText("");
        emailField.setText("");
    }

    private boolean registerStudent() {
        String fullName = fullNameField.getText();
        String studentId = studentIdField.getText();
        String studentClass = studentClassField.getText();
        String email = emailField.getText();

        if (fullName.isEmpty() || studentId.isEmpty() || studentClass.isEmpty() || email.isEmpty()) {
            return false;
        }

        Connection connection = setupDatabaseConnection();
        if (connection != null) {
            String sql = "INSERT INTO Register (FullName, StudentID, StudentClass, Email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, studentId);
                preparedStatement.setString(3, studentClass);
                preparedStatement.setString(4, email);

                int rowsAffected = preparedStatement.executeUpdate();
                connection.close();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void goBackToStudentPage() {
        SwingUtilities.invokeLater(() -> {
            StudentForm studentForm = new StudentForm();
            studentForm.setVisible(true);
            dispose();
        });
    }

    private static Connection setupDatabaseConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String dbUrl = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\StudentFormDB.accdb"; 
            Connection connection = DriverManager.getConnection(dbUrl);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryCardRegistrationForm();
        });
    }
}
