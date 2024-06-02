import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LibraryManagementSystemForm extends JFrame {

    private BufferedImage background;

    public LibraryManagementSystemForm() {
        setTitle("Library Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen
        setUndecorated(true); // Remove window decorations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image
        try {
            background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library1.jpg")); // Replace with your image file
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a custom JPanel to paint the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        backgroundPanel.setLayout(new BorderLayout());

        // Create a label for the heading in the middle
        JLabel headingLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 40));
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(headingLabel, BorderLayout.CENTER);

        // Create a "Skip" link in the bottom-right corner
        JLabel skipLabel = new JLabel("Skip");
        skipLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        skipLabel.setForeground(Color.GREEN);
        skipLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        skipLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set hand cursor

        // Add hover effect to "Skip" link
        skipLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                skipLabel.setForeground(Color.YELLOW); // Change text color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                skipLabel.setForeground(Color.GREEN); // Restore text color on exit
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                openLoginForm(); // Handle the skip action to open the login form
            }
        });

        backgroundPanel.add(skipLabel, BorderLayout.SOUTH);

        add(backgroundPanel);
    }

    private void openLoginForm() {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementLoginForm loginForm = new LibraryManagementLoginForm();
            loginForm.setVisible(true);
            dispose(); // Close the current form
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementSystemForm frame = new LibraryManagementSystemForm();
            frame.setVisible(true);
        });
    }
}
