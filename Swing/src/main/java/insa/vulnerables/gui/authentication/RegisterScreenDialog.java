package insa.vulnerables.gui.authentication;

import insa.vulnerables.httpClient.VulnerablesHttpClient;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class RegisterScreenDialog extends JDialog {

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleComboBox;

    public RegisterScreenDialog(JFrame parent) {
        super(parent);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel firstNamePanel = new JPanel();
        firstNamePanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField(15);
        firstNamePanel.add(firstNameField);

        JPanel lastNamePanel = new JPanel();
        lastNamePanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(15);
        lastNamePanel.add(lastNameField);

        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel();
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordField);

        JPanel rolePanel = new JPanel();
        rolePanel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"VulnerablePerson", "Volunteer"});
        rolePanel.add(roleComboBox);

        inputPanel.add(firstNamePanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(lastNamePanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(usernamePanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(passwordPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(rolePanel);

        JPanel buttonPanel = getjPanel(parent);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel getjPanel(JFrame parent) {
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {

            if (VulnerablesHttpClient.getInstance().createUser(firstNameField.getText(), lastNameField.getText(),
                    usernameField.getText(), new String(passwordField.getPassword()),
                    Objects.requireNonNull(roleComboBox.getSelectedItem()).toString())) {
                dispose();
                LoginScreenDialog loginScreenDialog = new LoginScreenDialog(parent);
                loginScreenDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(RegisterScreenDialog.this,
                        "Invalid parameters!",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backToLoginButton = new JButton("Already have an account?");
        backToLoginButton.addActionListener(e -> {
            dispose();
            LoginScreenDialog loginScreenDialog = new LoginScreenDialog(parent);
            loginScreenDialog.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(backToLoginButton);
        return buttonPanel;
    }
}
