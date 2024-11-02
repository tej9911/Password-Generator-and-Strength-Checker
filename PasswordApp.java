import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.regex.Pattern;

public class PasswordApp extends JFrame {
    private JTextField passwordField;
    private JSpinner lengthSpinner;
    private JCheckBox includeLowercase;
    private JCheckBox includeUppercase;
    private JCheckBox includeNumbers;
    private JCheckBox includeSymbols;
    private JCheckBox includeAboveAll;
    private JTextField passwordStrengthField;
    private JLabel strengthResult;  // Declaration
    
    public PasswordApp() {
        setTitle("Password Generator & Strength Checker");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set a modern dark theme
        getContentPane().setBackground(new Color(45, 45, 45));
        setLayout(new BorderLayout());

        // Create tabbed pane with improved layout
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Password Generator", createPasswordGeneratorPanel());
        tabbedPane.addTab("Password Strength Checker", createPasswordStrengthPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // Create the Password Generator Tab
    private JPanel createPasswordGeneratorPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lengthLabel = createLabel("Password Length:", Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lengthLabel, gbc);

        lengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(lengthSpinner, gbc);

        includeLowercase = createCheckBox("Include Lowercase Letters");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(includeLowercase, gbc);

        includeUppercase = createCheckBox("Include Uppercase Letters");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(includeUppercase, gbc);

        includeNumbers = createCheckBox("Include Numbers");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(includeNumbers, gbc);

        includeSymbols = createCheckBox("Include Symbols");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(includeSymbols, gbc);

        includeAboveAll = createCheckBox("Include Above All");
        includeAboveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = includeAboveAll.isSelected();
                includeLowercase.setSelected(isSelected);
                includeUppercase.setSelected(isSelected);
                includeNumbers.setSelected(isSelected);
                includeSymbols.setSelected(isSelected);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(includeAboveAll, gbc);

        JButton generateButton = createButton("Generate Password");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(generateButton, gbc);

        passwordField = new JTextField();
        passwordField.setEditable(false);
        passwordField.setBackground(Color.BLACK);
        passwordField.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(passwordField, gbc);

        return panel;
    }

    // Create the Password Strength Checker Tab
    private JPanel createPasswordStrengthPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel strengthLabel = createLabel("Enter Password:", Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(strengthLabel, gbc);

        passwordStrengthField = new JTextField();
        passwordStrengthField.setBackground(Color.BLACK);
        passwordStrengthField.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(passwordStrengthField, gbc);

        JButton checkStrengthButton = createButton("Check Password Strength");
        checkStrengthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordStrengthField.getText();
                String strength = checkPasswordStrength(password);
                strengthResult.setText("Strength: " + strength);
                updateStrengthColor(strength);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(checkStrengthButton, gbc);

        strengthResult = createLabel("", Color.WHITE); // Initialize strengthResult
        strengthResult.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(strengthResult, gbc);

        return panel;
    }

    // Generate Password Method
    private void generatePassword() {
        int length = (int) lengthSpinner.getValue();
        boolean lower = includeLowercase.isSelected();
        boolean upper = includeUppercase.isSelected();
        boolean numbers = includeNumbers.isSelected();
        boolean symbols = includeSymbols.isSelected();

        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numberChars = "0123456789";
        String symbolChars = "!@#$%^&*()_+~`|}{[]:;?><,./-=\\'\"";

        StringBuilder allChars = new StringBuilder();

        if (lower) allChars.append(lowercaseChars);
        if (upper) allChars.append(uppercaseChars);
        if (numbers) allChars.append(numberChars);
        if (symbols) allChars.append(symbolChars);

        if (allChars.length() == 0 || length == 0) {
            passwordField.setText("");
            return;
        }

        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allChars.length());
            password.append(allChars.charAt(randomIndex));
        }

        passwordField.setText(password.toString());
    }

    // Password Strength Checker Method
    private String checkPasswordStrength(String password) {
        if (password.length() < 6) {
            return "Very Weak";
        }

        int strengthScore = 0;

        if (Pattern.compile("[a-z]").matcher(password).find()) strengthScore++;
        if (Pattern.compile("[A-Z]").matcher(password).find()) strengthScore++;
        if (Pattern.compile("[0-9]").matcher(password).find()) strengthScore++;
        if (Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) strengthScore++;

        switch (strengthScore) {
            case 4:
                return "Very Strong";
            case 3:
                return "Strong";
            case 2:
                return "Weak";
            default:
                return "Very Weak";
        }
    }

    // Update strength label color based on result
    private void updateStrengthColor(String strength) {
        switch (strength) {
            case "Very Strong":
                strengthResult.setForeground(Color.GREEN);
                break;
            case "Strong":
                strengthResult.setForeground(Color.ORANGE);
                break;
            case "Weak":
                strengthResult.setForeground(Color.YELLOW);
                break;
            case "Very Weak":
            default:
                strengthResult.setForeground(Color.RED);
                break;
        }
    }

    // Helper methods to create styled components
    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private JCheckBox createCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setForeground(Color.WHITE);
        checkBox.setBackground(new Color(45, 45, 45));
        return checkBox;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180)); // SlateBlue color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PasswordApp().setVisible(true);
            }
        });
    }
}
