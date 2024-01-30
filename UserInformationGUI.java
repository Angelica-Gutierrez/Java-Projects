import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInformationGUI extends JFrame {
    //Text fields for user input
    private JTextField idField, nameField, ageField, emailField;
    //Table model and table for displaying data
    private DefaultTableModel tableModel;
    private JTable table;
    //file to store the user data
    private static final String DATA_FILE = "user_data.txt";

    //constructor
    public UserInformationGUI() {
        setTitle("Data Record");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        idField = new JTextField(20);
        nameField = new JTextField(20);
        ageField = new JTextField(20);
        emailField = new JTextField(20);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEntry();
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEntry();
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEntry();
            }
        });

        // Table setup
        String[] columns = {"ID", "Name", "Age", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Layout setup
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx++;
        inputPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx++;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx++;
        inputPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx++;
        inputPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(updateButton, gbc);
        gbc.gridx++;
        inputPanel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel(""), gbc); // Empty space for spacing between buttons

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(clearButton, gbc);
        gbc.gridx++;
        inputPanel.add(deleteButton, gbc);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // Load data from file on GUI launch
        loadDataFromFile();

        // Table selection setup
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateFieldsFromSelectedRow();
            }
        });
    }

    //load data from the file into the table using java file handling
    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //save data from the table to the file in the computer named user_data
    private void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.print(tableModel.getValueAt(i, j));
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Update text fields with data from the selected table row
    private void updateFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            idField.setText((String) tableModel.getValueAt(selectedRow, 0));
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            ageField.setText((String) tableModel.getValueAt(selectedRow, 2));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 3));
        }
    }

    //add entry in the table by filling out the fields
    private void addEntry() {
        String id = idField.getText();
        String name = nameField.getText();
        String age = ageField.getText();
        String email = emailField.getText();
    
        if (!id.isEmpty() && !name.isEmpty() && isValidNumber(age) && !email.isEmpty()) {
            String[] rowData = {id, name, age, email};
            tableModel.addRow(rowData);
            clearFields();
            JOptionPane.showMessageDialog(this, "Record added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            saveDataToFile(); // Save data after adding entry
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields or enter a valid age and email", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //update the selected entry in the table
    private void updateEntry() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String age = ageField.getText();
            String email = emailField.getText();
            if (isValidNumber(age) && isValidEmail(email)) {
                tableModel.setValueAt(idField.getText(), selectedRow, 0);
                tableModel.setValueAt(nameField.getText(), selectedRow, 1);
                tableModel.setValueAt(age, selectedRow, 2);
                tableModel.setValueAt(email, selectedRow, 3);
                clearFields();
                JOptionPane.showMessageDialog(this, "Record updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                saveDataToFile(); // Save data after updating entry
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid age or email", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Validate if the input is a valid email address
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    // Validate if the input is a valid number
    private boolean isValidNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
   
    //Clear all textfields
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        table.clearSelection();
    }
    
    //delete the selected entry from the table
    private void deleteEntry() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                clearFields();
                JOptionPane.showMessageDialog(this, "Record deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                saveDataToFile(); // Save data after deleting entry
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 
    //main method to launch the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserInformationGUI gui = new UserInformationGUI();
                gui.setVisible(true);
                
                    }
                });
    }
}