import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.imageio.ImageIO;

public class RequestExtensionFacultyForm extends JFrame {
    private FacultyForm parentForm;

    public RequestExtensionFacultyForm(FacultyForm parent) {
        this.parentForm = parent;
        setTitle("Request Extension Faculty Form");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image
        try {
            Image background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library9.jpg"));
            setContentPane(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (background != null) {
                        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        mainPanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridLayout(6, 0));
        formPanel.setOpaque(false);

        JTextField bookIDField = new JTextField();
        JTextField bookNameField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField extensionDateField = new JTextField();

        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(bookIDField);
        formPanel.add(new JLabel("Book Name:"));
        formPanel.add(bookNameField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Extension Date:"));
        formPanel.add(extensionDateField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back to Faculty Form");

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookID = bookIDField.getText();
                String bookName = bookNameField.getText();
                String author = authorField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                String extensionDate = extensionDateField.getText();

                try {
                    // Establish a database connection
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    String url = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\FacultyExDB.accdb";
                    Connection connection = DriverManager.getConnection(url);

                    // Prepare and execute the SQL insert statement
                    String sql = "INSERT INTO Extension1 (BookID, BookName, Author, Quantity, ExtensionDate) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, bookID);
                    preparedStatement.setString(2, bookName);
                    preparedStatement.setString(3, author);
                    preparedStatement.setInt(4, quantity);
                    preparedStatement.setString(5, extensionDate);
                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                    connection.close();
                    JOptionPane.showMessageDialog(RequestExtensionFacultyForm.this, "Data saved successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(RequestExtensionFacultyForm.this, "Error saving data: " + ex.getMessage());
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToFacultyForm();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(330, 0, 0, 0);
        mainPanel.add(formPanel, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(50, 0, 0, 0);
        mainPanel.add(buttonPanel, constraints);

        add(mainPanel);

        setVisible(true);
    }

    private void goBackToFacultyForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                parentForm.setVisible(true);
                dispose();
            }
        });
    }

    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public void setParentForm(FacultyForm parent) {
        this.parentForm = parent;
    }
}
