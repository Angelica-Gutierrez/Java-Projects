import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ATMGUI extends JFrame {
    //my database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/user";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JButton quickWithdrawalButton;
    private JButton balanceInquiryButton;
    private JButton otherServicesButton;

    private JButton cashTransactionButton;
    private JButton balanceStatementButton;
    private JButton paymentsButton;
    private JButton onlineBankingButton;

    private String cardNumber;
    private double depositedAmount = 0.0; // Variable to keep track of deposited amount

    public ATMGUI(String cardNumber, double initialBalance) 
    {
        this.cardNumber = cardNumber;
        this.depositedAmount = initialBalance;
        

        // Set up the main menu
        JPanel mainMenuPanel = new JPanel(new GridLayout(3, 1));
        quickWithdrawalButton = new JButton("Quick Withdrawal");
        balanceInquiryButton = new JButton("Balance Inquiry");
        otherServicesButton = new JButton("Other Services");
        
        Font buttonFont = new Font("Arial", Font.BOLD, 16); 
        quickWithdrawalButton.setFont(buttonFont);
        balanceInquiryButton.setFont(buttonFont);
        otherServicesButton.setFont(buttonFont);
        
        mainMenuPanel.add(quickWithdrawalButton);
        mainMenuPanel.add(balanceInquiryButton);
        mainMenuPanel.add(otherServicesButton);

        // Set up the transaction menu
        JPanel transactionMenuPanel = new JPanel(new GridLayout(4, 1));
        cashTransactionButton = new JButton("Cash Transaction");
        balanceStatementButton = new JButton("Balance/Account Statement");
        paymentsButton = new JButton("Payments");
        onlineBankingButton = new JButton("Online Banking");

        cashTransactionButton.setFont(buttonFont);
        balanceStatementButton.setFont(buttonFont);
        paymentsButton.setFont(buttonFont);
        onlineBankingButton.setFont(buttonFont);

        transactionMenuPanel.add(cashTransactionButton);
        transactionMenuPanel.add(balanceStatementButton);
        transactionMenuPanel.add(paymentsButton);
        transactionMenuPanel.add(onlineBankingButton);

        // Set up the main frame with BorderLayout
        setLayout(new BorderLayout());

        // path of the logo image
        String logoImagePath = "C:\\Users\\angelica\\OneDrive\\Desktop\\atmIcon.png";

        // Load the image using ImageIcon
        ImageIcon originalIcon = new ImageIcon(logoImagePath);

        // Resize the icon to a specific width and height
        int width = 80; //  width of the icon
        int height = 80; //  height of the icon

        // Create a scaled version of the image
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Add the ATM logo and transaction label to the main frame
        JLabel atmLogoLabel = new JLabel(scaledIcon);
        atmLogoLabel.setHorizontalAlignment(JLabel.RIGHT);

        // Add an empty border to move the icon a little to the right
        int emptyBorderSize = 100; // Adjust the size as needed
        atmLogoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, emptyBorderSize));

        JLabel chooseTransactionLabel = new JLabel("Choose a transaction");
        chooseTransactionLabel.setHorizontalAlignment(JLabel.LEFT);

        // Set the font color for the label
        chooseTransactionLabel.setForeground(Color.WHITE);
        // Create a panel for the labels
        JPanel labelsPanel = new JPanel(new BorderLayout());
        labelsPanel.add(atmLogoLabel, BorderLayout.WEST);
        labelsPanel.add(chooseTransactionLabel, BorderLayout.CENTER);
        labelsPanel.setBackground(new Color(0, 0, 102));
        // Add the labels panel above the buttons
        add(labelsPanel, BorderLayout.NORTH);
        chooseTransactionLabel.setPreferredSize(new Dimension(700, 100)); // Set preferred size

        // Set the font color for the label
        chooseTransactionLabel.setForeground(Color.WHITE);  // Set to the desired color

        // Set the font for the label
        Font labelFont = new Font("Arial", Font.BOLD, 20); // Replace with your desired font settings
        chooseTransactionLabel.setFont(labelFont);
      
        // Create two panels for buttons, each with GridLayout
        JPanel mainMenuButtonsPanel = new JPanel(new GridLayout(3, 1,0,20));
        mainMenuButtonsPanel.setBackground(new Color(0, 0, 102));  // Dark blue color
        mainMenuButtonsPanel.add(quickWithdrawalButton);
        mainMenuButtonsPanel.add(balanceInquiryButton);
        mainMenuButtonsPanel.add(otherServicesButton);

        JPanel transactionMenuButtonsPanel = new JPanel(new GridLayout(4, 1,0,20));
        transactionMenuButtonsPanel.setBackground(new Color(0, 0, 102));  // Dark blue color
        transactionMenuButtonsPanel.add(cashTransactionButton);
        transactionMenuButtonsPanel.add(balanceStatementButton);
        transactionMenuButtonsPanel.add(paymentsButton);
        transactionMenuButtonsPanel.add(onlineBankingButton);

        // Create a panel for the buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2,20,0));
        // Set the background color for the entire panel
        buttonsPanel.setBackground(new Color(0, 0, 102));  // Dark blue color
        buttonsPanel.add(mainMenuButtonsPanel);
        buttonsPanel.add(transactionMenuButtonsPanel);

        // Add the buttons panel to the main frame
        add(buttonsPanel, BorderLayout.CENTER);

        // Set up action listeners
        quickWithdrawalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show an input dialog to get the withdrawal amount
                String withdrawalAmountString = JOptionPane.showInputDialog("Enter withdrawal amount:");
        
                // Check if the user clicked Cancel or entered an empty string
                if (withdrawalAmountString != null && !withdrawalAmountString.trim().isEmpty()) {
                    try {
                        // Convert the input string to a double
                        double withdrawalAmount = Double.parseDouble(withdrawalAmountString);
        
                        // Check if the withdrawal amount is greater than 0
                if (withdrawalAmount > 0) {
                    // Check if there is enough deposited amount to withdraw
                    if (depositedAmount >= withdrawalAmount) {
                        // Check if the remaining balance after withdrawal will be above the maintaining balance
                        if (depositedAmount - withdrawalAmount >= 100) {
                            depositedAmount -= withdrawalAmount;
                            showMessage("Withdrawing Php" + withdrawalAmount);
                            //update balance in my database
                            updateBalanceInDatabase(depositedAmount);
                        } else {
                            showMessage("Cannot withdraw. Maintaining balance of Php 100 must be maintained.");
                        }
                    } else {
                        showMessage("Insufficient funds. Cannot withdraw.");
                    }
                } else {
                    showMessage("Invalid withdrawal amount. Please enter a positive value.");
                }
            } catch (NumberFormatException ex) {
                showMessage("Invalid input. Please enter a valid number.");
            }
        } else {
            showMessage("Withdrawal canceled.");
        }
    }
});

        balanceInquiryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the user's remaining balance
                showMessage("Your remaining balance is Php " + depositedAmount);
            }
        });

        
        otherServicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creating a new panel for the Other Services menu
                JPanel otherServicesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
                
        
                // Buttons for updating user information
                JButton updateNameButton = new JButton("Update Name");
                JButton updateCardNumberButton = new JButton("Update Card Number");
                JButton updatePINButton = new JButton("Update PIN");
                JButton deleteAccountButton = new JButton("Delete Account");
        
                // Set the font for the buttons
                Font updateButtonFont = new Font("Arial", Font.BOLD, 16);
                updateNameButton.setFont(updateButtonFont);
                updateCardNumberButton.setFont(updateButtonFont);
                updatePINButton.setFont(updateButtonFont);
                deleteAccountButton.setFont(updateButtonFont);
        
                // Add action listeners for each button
                updateNameButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Implement the logic to update the user's name
                        // You can use JOptionPane.showInputDialog to get the new name
                        String newName = JOptionPane.showInputDialog("Enter new name:");
                        // Update the name in the database
                        updateNameInDatabase(newName);
                    }
                });
        
                updateCardNumberButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Implement the logic to update the user's card number
                        // You can use JOptionPane.showInputDialog to get the new card number
                        String newCardNumber = JOptionPane.showInputDialog("Enter new card number:");
                        // Update the card number in the database
                        updateCardNumberInDatabase(newCardNumber);
                    }
                });
        
                updatePINButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Implement the logic to update the user's PIN
                        // You can use JOptionPane.showInputDialog to get the new PIN
                        String newPIN = JOptionPane.showInputDialog("Enter new PIN:");
                        // Update the PIN in the database
                        updatePINInDatabase(newPIN);
                    }
                });
        
                deleteAccountButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Implement the logic to delete the user's account
                        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            // Delete the account from the database
                            deleteAccountFromDatabase();
                        }
                    }
                });
        
                // Add buttons to the panel
                otherServicesPanel.add(updateNameButton);
                otherServicesPanel.add(updateCardNumberButton);
                otherServicesPanel.add(updatePINButton);
                otherServicesPanel.add(deleteAccountButton);
        
                // Show the Other Services panel
                int result = JOptionPane.showOptionDialog(null, otherServicesPanel, "Other Services",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        
                if (result == JOptionPane.OK_OPTION) {
                    showMessage("Other Services completed.");
                }
            }
        });

        cashTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show an input dialog to get the transaction amount
                String transactionAmountString = JOptionPane.showInputDialog("Enter amount to deposit:");
    
                // Check if the user clicked Cancel or entered an empty string
                if (transactionAmountString != null && !transactionAmountString.trim().isEmpty()) {
                    try {
                        // Convert the input string to a double
                        double transactionAmount = Double.parseDouble(transactionAmountString);
    
                        // Perform the transaction (deposit or withdraw)
                        if (transactionAmount >= 0) {
                            depositedAmount += transactionAmount;
                            showMessage("Amount deposited successfully. Current balance: Php " + depositedAmount);
                            // Update the balance in the database after the transaction
                            updateBalanceInDatabase(depositedAmount);
                        }
                    } catch (NumberFormatException ex) {
                        showMessage("Invalid input. Please enter a valid number.");
                    }
                } else {
                    showMessage("Transaction canceled.");
                }
            }
        });

        balanceStatementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage("Generating Balance/Account Statement...");
            }
        });

        paymentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show a dialog to choose the type of payment
                Object[] paymentOptions = {"Transfer Money", "Pay Bills"};
                String selectedOption = (String) JOptionPane.showInputDialog(
                        ATMGUI.this,
                        "Choose the type of payment:",
                        "Payment Options",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        paymentOptions,
                        paymentOptions[0]);
        
                // Perform the selected payment action
                if (selectedOption != null) {
                    switch (selectedOption) {
                        case "Transfer Money":
                            initiateMoneyTransfer();
                            break;
                        case "Pay Bills":
                            initiateBillPayment();
                            break;
                        default:
                            showMessage("Invalid payment option.");
                    }
                }
            }
        });

        onlineBankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage("Accessing Online Banking...");
            }
        });

        // Set up the frame
        setTitle("ATM GUI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Set the background color
        getContentPane().setBackground(new Color(0, 0, 102)); // Dark blue color

        showMessage("Welcome! Your initial balance is Php " + initialBalance);
    }

    // Placeholder method for initiating money transfer
    private void initiateMoneyTransfer() {
        // let's just show a message indicating that a money transfer is being initiated
        showMessage("Initiating Money Transfer...");
    }

    // Placeholder method for initiating bill payment
    private void initiateBillPayment() {
        // Prompt the user for the payment amount
        String amountString = JOptionPane.showInputDialog("Enter the payment amount:");

        // Check if the user clicked Cancel or entered an empty string
        if (amountString != null && !amountString.trim().isEmpty()) {
            try {
                // Convert the input string to a double
                double paymentAmount = Double.parseDouble(amountString);

                // Prompt the user for the type of bill
                String billType = JOptionPane.showInputDialog("Enter the type of bill:");

                // Perform the bill payment
                if (paymentAmount > 0 && billType != null && !billType.trim().isEmpty()) {
                    // Check if there is enough deposited amount to make the payment
                    if (paymentAmount <= depositedAmount && paymentAmount <= 100) {
                        // Deduct the payment amount from the deposited amount
                        depositedAmount -= paymentAmount;

                        

                        // Show a message with the payment details
                        showMessage("Bill payment of Php " + paymentAmount + " for " + billType + " initiated.");

                        // Update the balance in the database after the transaction
                        updateBalanceInDatabase(depositedAmount);
                    } else {
                        showMessage("Cannot make the payment. Amount exceeds maintaining balance limit.");
                    }
                } else {
                    showMessage("Invalid payment details. Please enter valid information.");
                }
            } catch (NumberFormatException ex) {
                showMessage("Invalid input. Please enter a valid number for the payment amount.");
            }
        } else {
            showMessage("Bill payment canceled.");
        }
    }


    // Add a new method to update the balance in the database
    private void updateBalanceInDatabase(double newBalance) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE user SET balance=? WHERE cardNumber=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, newBalance);
                preparedStatement.setString(2, cardNumber);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update the user's name in the database
    private void updateNameInDatabase(String newName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE user SET name=? WHERE cardNumber=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, cardNumber);
                preparedStatement.executeUpdate();
                showMessage("Name updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update the user's card number in the database
    private void updateCardNumberInDatabase(String newCardNumber) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE user SET cardNumber=? WHERE cardNumber=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newCardNumber);
                preparedStatement.setString(2, cardNumber);
                preparedStatement.executeUpdate();
                cardNumber = newCardNumber; // Update the current card number in the class
                showMessage("Card number updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update the user's PIN in the database
    private void updatePINInDatabase(String newPIN) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE user SET pin=? WHERE cardNumber=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newPIN);
                preparedStatement.setString(2, cardNumber);
                preparedStatement.executeUpdate();
                showMessage("PIN updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete the user's account from the database
    private void deleteAccountFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM user WHERE cardNumber=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);
                preparedStatement.executeUpdate();
                showMessage("Account deleted successfully.");
                // Close the GUI after deleting the account
                dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Add a getter method for cardNumber
    public String getCardNumber() {
        return cardNumber;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {    
                
            }
        });
    }
}
