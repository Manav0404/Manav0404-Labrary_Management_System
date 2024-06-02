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

public class EmployeeManagementForm extends JFrame {

    private BufferedImage background;
    private JTextField employeeIdField;
    private JTextField employeeNameField;
    private JTextField basicSalaryField;
    private JTextField attendanceField;
    private JTextField leavesField;

    public EmployeeManagementForm() {
        setTitle("Employee Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen
        setUndecorated(true); // Remove window decorations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library7.jpg")); // Replace with your image file
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

        // Add Employee Management components
        JLabel headingLabel = new JLabel("Employee Management");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE); // Text color is set to white
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(headingLabel, constraints);

        JLabel employeeIdLabel = new JLabel("Employee ID:");
        employeeIdField = new JTextField(20);
        JLabel employeeNameLabel = new JLabel("Employee Name:");
        employeeNameField = new JTextField(20);
        JLabel basicSalaryLabel = new JLabel("Basic Salary:");
        basicSalaryField = new JTextField(20);
        JLabel attendanceLabel = new JLabel("Attendance:");
        attendanceField = new JTextField(20);
        JLabel leavesLabel = new JLabel("Leaves:");
        leavesField = new JTextField(20);

        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        panel.add(employeeIdLabel, constraints);
        employeeIdLabel.setForeground(Color.WHITE); // Text color is set to white
        constraints.gridx = 1;
        panel.add(employeeIdField, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(employeeNameLabel, constraints);
        employeeNameLabel.setForeground(Color.WHITE); // Text color is set to white
        constraints.gridx = 1;
        panel.add(employeeNameField, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(basicSalaryLabel, constraints);
        basicSalaryLabel.setForeground(Color.WHITE); // Text color is set to white
        constraints.gridx = 1;
        panel.add(basicSalaryField, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(attendanceLabel, constraints);
        attendanceLabel.setForeground(Color.WHITE); // Text color is set to white
        constraints.gridx = 1;
        panel.add(attendanceField, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(leavesLabel, constraints);
        leavesLabel.setForeground(Color.WHITE); // Text color is set to white
        constraints.gridx = 1;
        panel.add(leavesField, constraints);

        JButton saveButton = new JButton("Save");
        saveButton.setContentAreaFilled(false);
        saveButton.setBorderPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.setForeground(Color.WHITE); // Text color is set to white

        // Add hover effect to the button
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setForeground(Color.WHITE);
            }
        });

        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        panel.add(saveButton, constraints);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEmployeeData();
            }
        });

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setForeground(Color.WHITE); // Text color is set to white

        // Add hover effect to the button
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(Color.WHITE);
            }
        });

        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        panel.add(backButton, constraints);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToDashboard();
            }
        });

        add(panel);
    }

    private Connection setupDatabaseConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String dbUrl = "jdbc:ucanaccess://C:/Users/Manav/OneDrive/Desktop/PROJECT/projectdatabase/EmployeeManagementDB.accdb"; // Replace with your database path
            Connection connection = DriverManager.getConnection(dbUrl);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveEmployeeData() {
        try (Connection connection = setupDatabaseConnection()) {
            if (connection != null) {
                String employeeId = employeeIdField.getText();
                String employeeName = employeeNameField.getText();
                double basicSalary = Double.parseDouble(basicSalaryField.getText());
                int attendance = Integer.parseInt(attendanceField.getText());
                int leaves = Integer.parseInt(leavesField.getText());

                String sql = "INSERT INTO Employee (EmployeeID, EmployeeName, BasicSalary, Attendance, Leaves) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, employeeId);
                    preparedStatement.setString(2, employeeName);
                    preparedStatement.setDouble(3, basicSalary);
                    preparedStatement.setInt(4, attendance);
                    preparedStatement.setInt(5, leaves);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Employee data saved successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to save employee data.");
                    }
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void returnToDashboard() {
        LibraryDashboardForm libraryDashboard = new LibraryDashboardForm();
        libraryDashboard.setVisible(true);
        this.dispose(); // Close the Employee Management Form
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeManagementForm form = new EmployeeManagementForm();
            form.setVisible(true);
        });
    }
}
