import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.imageio.ImageIO;

public class UserRegistrationForm extends JFrame {

    private BufferedImage background;

    public UserRegistrationForm() {
        setTitle("User Registration Form");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen
        setUndecorated(true); // Remove window decorations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library4.jpg")); // Replace with your image file
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel headingLabel = new JLabel("USER REGISTRATION FORM");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(headingLabel, constraints);

        JLabel usernameLabel = new JLabel("Create Username:");
        usernameLabel.setForeground(Color.WHITE);
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Create Password:");
        passwordLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(confirmPasswordLabel, constraints);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        constraints.gridx = 1;
        panel.add(confirmPasswordField, constraints);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(emailLabel, constraints);

        JTextField emailField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(emailField, constraints);

        JButton clearButton = new JButton("Clear");
        constraints.gridx = 0;
        constraints.gridy = 5;
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                emailField.setText("");
            }
        });
        panel.add(clearButton, constraints);

        JButton registerButton = new JButton("Register");
        constraints.gridx = 1;
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your registration functionality here
                boolean registrationSuccess = performRegistration(usernameField.getText(), new String(passwordField.getPassword()), emailField.getText());
                if (registrationSuccess) {
                    openLoginForm();
                } else {
                    JOptionPane.showMessageDialog(UserRegistrationForm.this, "Registration failed. Please check your input.");
                }
            }
        });
        panel.add(registerButton, constraints);

        JLabel backToLoginLabel = new JLabel("Back to Login");
        backToLoginLabel.setForeground(Color.BLUE);
        backToLoginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backToLoginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openLoginForm();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backToLoginLabel.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backToLoginLabel.setForeground(Color.BLUE);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        panel.add(backToLoginLabel, constraints);

        add(panel);
    }

    private boolean performRegistration(String username, String password, String email) {
        String databaseURL = "jdbc:ucanaccess://C:/Users/Manav/OneDrive/Desktop/PROJECT/projectdatabase/LibraryManagementDB.accdb";
        String query = "INSERT INTO Users (Username, Password, Email) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Registration successful if rows are affected
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed due to an exception
        }
    }

    private void openLoginForm() {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementLoginForm loginForm = new LibraryManagementLoginForm();
            loginForm.setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserRegistrationForm frame = new UserRegistrationForm();
            frame.setVisible(true);
        });
    }
}
