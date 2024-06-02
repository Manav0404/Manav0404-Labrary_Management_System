import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FacultyForm extends JFrame {
    private JTextField libraryCardIdField, facultyNameField, facultyDepartmentField;
    private JButton requestExtensionButton;
    private JButton saveButton;
    private JButton backButton;
    private RequestExtensionFacultyForm extensionForm; // Reference to the RequestExtensionFacultyForm

    public FacultyForm() {
        setTitle("Faculty Form");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image
        try {
            Image background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library8.jpg"));
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

        JLabel headingLabel = new JLabel("FACULTY FORM");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        contentPanel.add(headingLabel, constraints);

        libraryCardIdField = new JTextField(20);
        addLabelAndField("Faculty ID:", libraryCardIdField, contentPanel, constraints, 1);

        facultyNameField = new JTextField(20);
        addLabelAndField("Faculty Name:", facultyNameField, contentPanel, constraints, 2);

        facultyDepartmentField = new JTextField(20);
        addLabelAndField("Faculty Department:", facultyDepartmentField, contentPanel, constraints, 3);

        requestExtensionButton = addButton("Request Extension", contentPanel, constraints, 4);

        extensionForm = new RequestExtensionFacultyForm(this); // Initialize the RequestExtensionFacultyForm

        saveButton = addButton("Save", contentPanel, constraints, 5);

        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.BLACK);
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        contentPanel.add(backButton, constraints);

        requestExtensionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRequestExtensionFacultyForm();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFacultyData();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLibraryDashboard();
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
        constraints.insets = new Insets(10, 70, 10, 70);
        contentPanel.add(empLabel, constraints);

        constraints.gridx = 1;
        constraints.insets = new Insets(10, 250, 10, 0);
        contentPanel.add(textField, constraints);
    }

    private JButton addButton(String label, JPanel contentPanel, GridBagConstraints constraints, int gridY) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = gridY;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(20, 50, 10, 50);
        contentPanel.add(button, constraints);
        return button;
    }

    private void goBackToLibraryDashboard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LibraryDashboardForm dashboard = new LibraryDashboardForm();
                dashboard.setVisible(true);
                dispose();
            }
        });
    }

    private void openRequestExtensionFacultyForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                extensionForm.setVisible(true);
                dispose();
            }
        });
    }

    private void saveFacultyData() {
        String facultyID = libraryCardIdField.getText();
        String facultyName = facultyNameField.getText();
        String facultyDepartment = facultyDepartmentField.getText();

        String databaseURL = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\FacultyForm1DB.accdb";
        String query = "INSERT INTO Faculty1 (FacultyID, FacultyName, FacultyDepartment) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, facultyID);
            preparedStatement.setString(2, facultyName);
            preparedStatement.setString(3, facultyDepartment);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Faculty data saved successfully.");
                // Optionally, clear input fields here
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save faculty data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FacultyForm();
            }
        });
    }
}
