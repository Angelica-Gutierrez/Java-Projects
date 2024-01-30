import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Calculator extends JFrame {

    private JPanel container = new JPanel();

    // Button labels for digits, operators, and special functions
    String[] buttonLabels = {"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "=", "C", "+", "-", "*", "/"};
    JButton[] buttons = new JButton[buttonLabels.length];
    private JLabel display = new JLabel();
    private Dimension buttonDimension = new Dimension(50, 40);
    private Dimension operatorButtonDimension = new Dimension(50, 31);
    private double operand1;
    private boolean operatorClicked = false, updateDisplay = false;
    private String operator = "";

    public Calculator() {
        this.setSize(350, 300);
        this.setTitle("Calculator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        initializeComponents();
        this.setContentPane(container);
        this.setVisible(true);
    }

    // Method to initialize GUI components
    private void initializeComponents() {

        Font font = new Font("Calibri", Font.BOLD, 15);
        display = new JLabel("0");
        display.setFont(font);
        display.setHorizontalAlignment(JLabel.CENTER);
        display.setPreferredSize(new Dimension(220, 20));

        JPanel operatorPanel = new JPanel();
        operatorPanel.setPreferredSize(new Dimension(55, 225));
        JPanel digitPanel = new JPanel();
        digitPanel.setPreferredSize(new Dimension(165, 225));
        JPanel displayPanel = new JPanel();
        displayPanel.setPreferredSize(new Dimension(220, 30));

        // Loop to create buttons and assign actions based on their labels
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setPreferredSize(buttonDimension);

            switch (i) {
                case 11:
                    buttons[i].setForeground(Color.red);
                    buttons[i].addActionListener(new EqualsListener());
                    digitPanel.add(buttons[i]);
                    break;

                case 12:
                    buttons[i].setForeground(Color.blue);
                    buttons[i].addActionListener(new ResetListener());
                    buttons[i].setPreferredSize(operatorButtonDimension);
                    operatorPanel.add(buttons[i]);
                    break;

                case 13:
                    buttons[i].setForeground(Color.black);
                    buttons[i].addActionListener(new PlusListener());
                    buttons[i].setPreferredSize(operatorButtonDimension);
                    operatorPanel.add(buttons[i]);
                    break;

                case 14:
                    buttons[i].setForeground(Color.black);
                    buttons[i].addActionListener(new MinusListener());
                    buttons[i].setPreferredSize(operatorButtonDimension);
                    operatorPanel.add(buttons[i]);
                    break;

                case 15:
                    buttons[i].setForeground(Color.black);
                    buttons[i].addActionListener(new MultiplyListener());
                    buttons[i].setPreferredSize(operatorButtonDimension);
                    operatorPanel.add(buttons[i]);
                    break;

                case 16:
                    buttons[i].setForeground(Color.black);
                    buttons[i].addActionListener(new DivideListener());
                    buttons[i].setPreferredSize(operatorButtonDimension);
                    operatorPanel.add(buttons[i]);
                    break;

                case 17:
                    buttons[i].setForeground(Color.black);
                    buttons[i].addActionListener(new DivideListener());
                    buttons[i].setPreferredSize(operatorButtonDimension);
                    operatorPanel.add(buttons[i]);
                    break;

                default:
                    buttons[i].addActionListener(new DigitListener());
                    digitPanel.add(buttons[i]);
                    break;
            }
        }

        displayPanel.add(display);
        displayPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        container.add(displayPanel, BorderLayout.NORTH);
        container.add(digitPanel, BorderLayout.CENTER);
        container.add(operatorPanel, BorderLayout.EAST);
    }

    // Method to perform arithmetic calculations
    private void calculate() {
        double denominator = Double.valueOf(display.getText()).doubleValue();
    
        if (denominator == 0 && operator.equals("/")) {
            display.setText("Error: Division by zero");
        } else {
            switch (operator) {
                case "+":
                    operand1 = operand1 + denominator;
                    break;
    
                case "-":
                    operand1 = operand1 - denominator;
                    break;
    
                case "*":
                    operand1 = operand1 * denominator;
                    break;
    
                case "/":
                    operand1 = operand1 / denominator;
                    break;
            }
    
            display.setText(String.valueOf(operand1));
        }
    }

    // ActionListener for digit buttons
    class DigitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String str = ((JButton) e.getSource()).getText();

            if (updateDisplay) {
                updateDisplay = false;
            } else {
                if (!display.getText().equals("0"))
                    str = display.getText() + str;
            }

            display.setText(str);
        }
    }

    // ActionListener for Equals button
    class EqualsListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            if (operatorClicked) {
                calculate();
                updateDisplay = true;
                operatorClicked = false;
            }
            
        }
    }

    // ActionListener for Add button
    class PlusListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            if (operatorClicked) {
                calculate();
                display.setText(String.valueOf(operand1));
            } else {
                operand1 = Double.valueOf(display.getText()).doubleValue();
                operatorClicked = true;
            }
            operator = "+";
            updateDisplay = true;
        }
    }

    // ActionListener for Subtraction button
    class MinusListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            if (operatorClicked) {
                calculate();
                display.setText(String.valueOf(operand1));
            } else {
                operand1 = Double.valueOf(display.getText()).doubleValue();
                operatorClicked = true;
            }
            operator = "-";
            updateDisplay = true;
        }
    }

    // ActionListener for Multiplication button
    class MultiplyListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            if (operatorClicked) {
                calculate();
                display.setText(String.valueOf(operand1));
            } else {
                operand1 = Double.valueOf(display.getText()).doubleValue();
                operatorClicked = true;
            }
            operator = "*";
            updateDisplay = true;
        }
    }

    // ActionListener for Division button
    class DivideListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            if (operatorClicked) {
                calculate();
                display.setText(String.valueOf(operand1));
            } else {
                operand1 = Double.valueOf(display.getText()).doubleValue();
                operatorClicked = true;
            }
            operator = "/";
            updateDisplay = true;
        }
    }

    class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            operatorClicked = false;
            updateDisplay = true;
            operand1 = 0;
            operator = "";
            display.setText("0");
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
