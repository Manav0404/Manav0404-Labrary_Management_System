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

public class StudentForm extends JFrame {
    private JButton requestExtensionButton;
    private JButton libraryCardButton;
    private JButton saveButton;
    private JButton backButton;
    private JTextField libraryCardIdField;
    private JTextField studentNameField;
    private JTextField studentClassField;
    private Connection connection;

    public StudentForm() {
        setTitle("Student Form");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        connection = setupDatabaseConnection();

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        libraryCardIdField = addLabelAndField("Library Card ID:", contentPanel, constraints, 1);
        studentNameField = addLabelAndField("Student Name:", contentPanel, constraints, 2);
        studentClassField = addLabelAndField("Student Class:", contentPanel, constraints, 3);

        saveButton = addButton("Save", contentPanel, constraints, 4);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudentData();
            }
        });

        backButton = addButton("Back to Dashboard", contentPanel, constraints, 5);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLibraryDashboard();
            }
        });

        requestExtensionButton = addButton("Request Extension", contentPanel, constraints, 6);
        requestExtensionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRequestExtensionForm();
            }
        });

        libraryCardButton = addButton("Library Card Registration", contentPanel, constraints, 7);
        libraryCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLibraryCardRegistrationForm();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        add(contentPanel, mainConstraints);

        setVisible(true);
    }

    private JTextField addLabelAndField(String label, JPanel contentPanel, GridBagConstraints constraints, int gridY) {
        JLabel empLabel = new JLabel(label);
        constraints.gridx = 0;
        constraints.gridy = gridY;
        constraints.insets = new Insets(10, 10, 10, 10);
        contentPanel.add(empLabel, constraints);

        JTextField empField = new JTextField(20);
        constraints.gridx = 1;
        constraints.insets = new Insets(10, 200, 10, 200);
        contentPanel.add(empField, constraints);

        return empField;
    }

    private JButton addButton(String label, JPanel contentPanel, GridBagConstraints constraints, int gridY) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = gridY;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(20, 10, 10, 10);
        contentPanel.add(button, constraints);
        return button;
    }

    private Connection setupDatabaseConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String dbUrl = "jdbc:ucanaccess://C:/Users/Manav/OneDrive/Desktop/PROJECT/projectdatabase/StudentFormDB.accdb"; // Update with your database path
            Connection connection = DriverManager.getConnection(dbUrl);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveStudentData() {
        String libraryCardId = libraryCardIdField.getText();
        String studentName = studentNameField.getText();
        String studentClass = studentClassField.getText();

        if (!libraryCardId.isEmpty() && !studentName.isEmpty() && !studentClass.isEmpty()) {
            try {
                String sql = "INSERT INTO Students (LibraryCardID, StudentName, StudentClass) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, libraryCardId);
                preparedStatement.setString(2, studentName);
                preparedStatement.setString(3, studentClass);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Student data saved successfully.");
                    libraryCardIdField.setText("");
                    studentNameField.setText("");
                    studentClassField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save student data.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }

    private void goBackToLibraryDashboard() {
        // Implement logic to return to the library dashboard (replace with your code)
        LibraryDashboardForm dashboard = new LibraryDashboardForm();
        dashboard.setVisible(true);
        dispose();
    }

    private void openRequestExtensionForm() {
        // Implement logic to open the Request Extension form (replace with your code)
        RequestExtensionForm requestExtensionForm = new RequestExtensionForm(connection);
        requestExtensionForm.setVisible(true);
        dispose();
    }

    private void openLibraryCardRegistrationForm() {
        // Implement logic to open the Library Card Registration form (replace with your code)
        LibraryCardRegistrationForm libraryCardRegistrationForm = new LibraryCardRegistrationForm();
        libraryCardRegistrationForm.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentForm();
        });
    }
}