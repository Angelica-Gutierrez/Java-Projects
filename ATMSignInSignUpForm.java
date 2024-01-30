import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ATMSignInSignUpForm extends JFrame implements ActionListener {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/user";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JTextField cardNumberField;
    private JPasswordField pinField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ATMSignInSignUpForm atmForm = new ATMSignInSignUpForm();
            atmForm.setLocationRelativeTo(null);
            atmForm.setVisible(true);

        });
    }

    public ATMSignInSignUpForm() {
        setTitle("ATM Sign In / Sign Up");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 350));
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Welcome Text
        JLabel welcomeLabel = new JLabel("WELCOME TO ATM");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 15, 0); // Added space after the Welcome text
        add(welcomeLabel, gbc);

        JLabel cardNumberLabel = new JLabel("Card Number:  ");
        cardNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(cardNumberLabel, gbc);

        cardNumberField = new JTextField(20);
        cardNumberField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        
        add(cardNumberField, gbc);

        JLabel pinLabel = new JLabel("PIN:                 ");
        pinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(pinLabel, gbc);

        pinField = new JPasswordField(20);
        pinField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING; // Align with the baseline of the preceding text
        
        add(pinField, gbc);

        JButton signInButton = new JButton("  Sign In  ");
        signInButton.setActionCommand("Sign In");
        signInButton.addActionListener(this);

        JButton clearButton = new JButton("  Clear  ");
        clearButton.setActionCommand("Clear");
        clearButton.addActionListener(this);

        // Create a panel for "Sign In" and "Clear" buttons with FlowLayout
        JPanel signInClearPanel = new JPanel(new FlowLayout());
        signInClearPanel.add(signInButton);
        signInClearPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add horizontal space
        signInClearPanel.add(clearButton);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(signInClearPanel, gbc);

        // Space between "Sign In/Clear" and "Sign Up" buttons
        gbc.gridy++;
        add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical space

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(this);
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(signUpButton, gbc);

        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Sign In")) {
            signIn();
        } else if (e.getActionCommand().equals("Clear")) {
            clearFields();
        } else if (e.getActionCommand().equals("Sign Up")) {
            signUp();
        }
    }

    private void signIn() {
        String cardNumber = cardNumberField.getText();
        String pin = new String(pinField.getPassword());

        if (validateUser(cardNumber, pin)) {
            // Retrieve user information (e.g., balance) from the database
            double balance = getUserBalance(cardNumber);

        // Open ATMGUI with user information
        SwingUtilities.invokeLater(() -> {
            new ATMGUI(cardNumber, balance);
            dispose(); // Close the current sign-in/sign-up form
        });
    } else {
        JOptionPane.showMessageDialog(this, "Invalid card number or PIN. Please try again.", "Sign In", JOptionPane.ERROR_MESSAGE);
        clearFields();
    }
}

    // a method to get the user's balance from the database
    private double getUserBalance(String cardNumber) {
        double balance = 0.0;
    
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT balance FROM user WHERE cardNumber=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Retrieve the balance from the result set
                        balance = resultSet.getDouble("balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return balance;
    }


    private void signUp() {
        // Prompt the user for input
        String name = JOptionPane.showInputDialog(this, "Enter your name:", "Sign Up", JOptionPane.QUESTION_MESSAGE);
        String cardNumber = JOptionPane.showInputDialog(this, "Enter your card number:", "Sign Up", JOptionPane.QUESTION_MESSAGE);
        String pin = JOptionPane.showInputDialog(this, "Enter your PIN:", "Sign Up", JOptionPane.QUESTION_MESSAGE);
    
        // Check for cancel or empty fields
        if (name == null || cardNumber == null || pin == null || name.isEmpty() || cardNumber.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sign up canceled or all fields not provided.", "Sign Up", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // Check if the user already exists
        if (userExists(cardNumber)) {
            JOptionPane.showMessageDialog(this, "Card number already exists. Please choose a different card number.", "Sign Up", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // Save the new user with initial balance
        saveUser(name, cardNumber, pin,0.0);
    
        JOptionPane.showMessageDialog(this, "Sign up successfully!", "Sign Up", JOptionPane.INFORMATION_MESSAGE);
        clearFields();
    }
    
    // Check if the user already exists
    private boolean userExists(String cardNumber) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM user WHERE cardNumber=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Save the new user
    private void saveUser(String name, String cardNumber, String pin, double initialBalance) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO user (name, cardNumber, pin, balance) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, cardNumber);
                preparedStatement.setString(3, pin);
                preparedStatement.setDouble(4, initialBalance);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateUser(String cardNumber, String pin) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM user WHERE cardNumber=? AND pin=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);
                preparedStatement.setString(2, pin);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // Returns true if there is a match, false otherwise
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void clearFields() {
        cardNumberField.setText("");
        pinField.setText("");
    }
  
}
