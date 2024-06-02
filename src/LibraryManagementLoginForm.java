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
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;

public class LibraryManagementLoginForm extends JFrame {

    private BufferedImage background;

    public LibraryManagementLoginForm() {
        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen
        setUndecorated(true); // Remove window decorations

        try {
            background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library2.jpg")); // Replace with your image file
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

        JLabel headingLabel = new JLabel("LOGIN FORM");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(headingLabel, constraints);

        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        JButton clearButton = new JButton("Clear");
        constraints.gridx = 0;
        constraints.gridy = 3;
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
            }
        });
        panel.add(clearButton, constraints);

        JButton loginButton = new JButton("Login");
        constraints.gridx = 1;
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your login functionality here
                boolean loginSuccess = performLogin(usernameField.getText(), new String(passwordField.getPassword()));
                if (loginSuccess) {
                    openDashboardForm();
                } else {
                    JOptionPane.showMessageDialog(LibraryManagementLoginForm.this, "Login failed. Please check your credentials.");
                }
            }
        });
        panel.add(loginButton, constraints);

        JLabel registrationLabel = new JLabel("New User Registration");
        registrationLabel.setForeground(Color.BLUE);
        registrationLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registrationLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRegistrationForm(); // Handle the registration link action
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registrationLabel.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registrationLabel.setForeground(Color.BLUE);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(registrationLabel, constraints);

        add(panel);
    }

    private boolean performLogin(String username, String password) {
        // Use the UCanAccess JDBC driver to connect to your MS Access database
        String databaseURL = "jdbc:ucanaccess://C:/Users/Manav/OneDrive/Desktop/PROJECT/projectdatabase/LibraryManagementDB.accdb";
        String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Login successful if a matching record is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Login failed due to an exception
        }
    }
    

    private void openRegistrationForm() {
        SwingUtilities.invokeLater(() -> {
            UserRegistrationForm registrationForm = new UserRegistrationForm();
            registrationForm.setVisible(true);
            dispose(); // Close the current form
        });
    }

    private void openDashboardForm() {
        SwingUtilities.invokeLater(() -> {
            LibraryDashboardForm dashboardForm = new LibraryDashboardForm();
            dashboardForm.setVisible(true);
            dispose(); // Close the current form
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementLoginForm frame = new LibraryManagementLoginForm();
            frame.setVisible(true);
        });
    }
}
