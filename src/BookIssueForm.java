import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookIssueForm extends JFrame {
    private Connection conn;

    public BookIssueForm() {
        setTitle("Book Issued Information");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage background = ImageIO.read(new File("C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\library6.jpg"));
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (background != null) {
                        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            };
            setContentPane(backgroundPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;

        JLabel headingLabel = new JLabel("BOOK ISSUED INFORMATION");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        contentPanel.add(headingLabel, constraints);

        JLabel bookNameLabel = new JLabel("Book Name:");
        constraints.gridy = 1;
        contentPanel.add(bookNameLabel, constraints);

        JTextField bookNameField = new JTextField(20);
        constraints.gridy = 2;
        contentPanel.add(bookNameField, constraints);

        JLabel issuedDateLabel = new JLabel("Issued Date:");
        constraints.gridy = 3;
        contentPanel.add(issuedDateLabel, constraints);

        JTextField issuedDateField = new JTextField(20);
        constraints.gridy = 4;
        contentPanel.add(issuedDateField, constraints);

        JLabel returnDateLabel = new JLabel("Return Date:");
        constraints.gridy = 5;
        contentPanel.add(returnDateLabel, constraints);

        JTextField returnDateField = new JTextField(20);
        constraints.gridy = 6;
        contentPanel.add(returnDateField, constraints);

        JLabel dueDaysLabel = new JLabel("Due Days:");
        constraints.gridy = 7;
        contentPanel.add(dueDaysLabel, constraints);

        JTextField dueDaysField = new JTextField(20);
        constraints.gridy = 8;
        contentPanel.add(dueDaysField, constraints);

        JLabel fineAmountLabel = new JLabel("Fine Amount:");
        constraints.gridy = 9;
        contentPanel.add(fineAmountLabel, constraints);

        JTextField fineAmountField = new JTextField(20);
        constraints.gridy = 10;
        contentPanel.add(fineAmountField, constraints);

        JButton addButton = new JButton("ADD");
        constraints.gridx = 0;
        constraints.gridy = 11;
        constraints.gridwidth = 2;
        contentPanel.add(addButton, constraints);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = bookNameField.getText();
                String issuedDate = issuedDateField.getText();
                String returnDate = returnDateField.getText();
                String dueDays = dueDaysField.getText();
                String fineAmount = fineAmountField.getText();

                try {
                    String sql = "INSERT INTO Bookissues (BookName, IssuedDate, ReturnDate, DueDays, FineAmount) " +
                                 "VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, bookName);
                    preparedStatement.setString(2, issuedDate);
                    preparedStatement.setString(3, returnDate);
                    preparedStatement.setString(4, dueDays);
                    preparedStatement.setString(5, fineAmount);

                    int rowsInserted = preparedStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(BookIssueForm.this, "Book issue information added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(BookIssueForm.this, "Failed to add book issue information.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(BookIssueForm.this, "Error: " + ex.getMessage());
                }
            }
        });

        JButton backButton = new JButton("Back");
        constraints.gridy = 12;
        contentPanel.add(backButton, constraints);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToBookManagement();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        add(contentPanel, mainConstraints);

        setLocationRelativeTo(null);
        setVisible(true);

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String dbURL = "jdbc:ucanaccess://C:\\Users\\Manav\\OneDrive\\Desktop\\PROJECT\\projectdatabase\\BookManagement.accdb";
            conn = DriverManager.getConnection(dbURL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
        }
    }

    private void goBackToBookManagement() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            new BookManagementApp().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookIssueForm();
        });
    }
}
