import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LibraryDashboardForm extends JFrame {

    private BufferedImage background;

    public LibraryDashboardForm() {
        setTitle("Library Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen
        setUndecorated(true); // Remove window decorations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library5.jpg")); // Replace with your image file
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

        JLabel headingLabel = new JLabel("LIBRARY DASHBOARD");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE); // Set text color to white
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(headingLabel, constraints);

        String[] buttonLabels = {
            "BOOK MANAGEMENT", "EMPLOYEE", "STUDENT", "FACULTY", "SUPPLIER", "INVENTORY", "FINE PAYMENT", "LOGOUT"
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setForeground(Color.WHITE);

            // Add hover effect to buttons
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setForeground(Color.BLACK);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setForeground(Color.WHITE);
                }
            });

            constraints.gridx = 0;
            constraints.gridy = 1 + i;
            constraints.gridwidth = 2;
            panel.add(button, constraints);

            // Link the buttons to corresponding forms
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String buttonText = button.getText();
                    openFormBasedOnButton(buttonText);
                }
            });
        }

        add(panel);
    }

    private void openFormBasedOnButton(String buttonText) {
        if ("BOOK MANAGEMENT".equals(buttonText)) {
            openForm(new BookManagementApp());
        } else if ("EMPLOYEE".equals(buttonText)) {
            openForm(new EmployeeManagementForm());
        } else if ("STUDENT".equals(buttonText)) {
            openForm(new StudentForm());
        } else if ("FACULTY".equals(buttonText)) {
            openForm(new FacultyForm());
        } else if ("SUPPLIER".equals(buttonText)) {
            openForm(new SupplierForm());
        } else if ("INVENTORY".equals(buttonText)) {
            openForm(new InventoryForm());
        } else if ("FINE PAYMENT".equals(buttonText)) {
            openForm(new FinePaymentForm());
        }
        // You can add more forms and conditions here

        // For "LOGOUT" button, you can handle the logout action here
        if ("LOGOUT".equals(buttonText)) {
            openLoginForm();
        }
    }

    private void openForm(JFrame form) {
        SwingUtilities.invokeLater(() -> {
            form.setVisible(true);
            dispose();
        });
    }

    // Method to open the login form
    private void openLoginForm() {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementLoginForm loginForm = new LibraryManagementLoginForm();
            loginForm.setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryDashboardForm frame = new LibraryDashboardForm();
            frame.setVisible(true);
        });
    }
}
