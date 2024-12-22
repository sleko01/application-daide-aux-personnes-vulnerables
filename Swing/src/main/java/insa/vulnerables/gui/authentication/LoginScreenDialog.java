package insa.vulnerables.gui.authentication;

import insa.vulnerables.gui.admin.LoggedInAdminComponent;
import insa.vulnerables.gui.loggedinuser.LoggedInUserComponent;
import insa.vulnerables.gui.MainFrame;
import insa.vulnerables.httpClient.VulnerablesHttpClient;
import insa.vulnerables.user.AppUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginScreenDialog extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreenDialog(JFrame parent) {
        super(parent);
        setLayout(new GridLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel usernamePanel = new JPanel(new BorderLayout(5, 5));
        usernamePanel.add(new JLabel("Username:"), BorderLayout.WEST);
        usernameField = new JTextField(20);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        JPanel passwordPanel = new JPanel(new BorderLayout(5, 5));
        passwordPanel.add(new JLabel("Password:"), BorderLayout.WEST);
        passwordField = new JPasswordField(20);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        inputPanel.add(usernamePanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(passwordPanel);

        JPanel buttonPanel = getjPanel(parent);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel getjPanel(JFrame parent) {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (VulnerablesHttpClient.getInstance().loginUser(username, password)) {
                // the user has successfully logged in
                if (AppUser.getLoggedInUser().getUserStatus().name().equals("PENDING")) {
                    JOptionPane.showMessageDialog(LoginScreenDialog.this,
                            "Before use, your account needs to be validated by administrator!",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                    VulnerablesHttpClient.getInstance().logout(); // immediately logout user
                    return;
                } else if (AppUser.getLoggedInUser().getUserStatus().name().equals("REJECTED")) {
                    JOptionPane.showMessageDialog(LoginScreenDialog.this,
                            "Your registration has been rejected by administrator!",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                    VulnerablesHttpClient.getInstance().logout(); //immediately logout user
                    return;
                }
                int roleId = AppUser.getLoggedInUser().getRole().roleId();
                if (roleId == 1 || roleId == 2) {
                    // corresponds to the User role so the app should display options for users
                    MainFrame frame = (MainFrame) parent;
                    frame.showFrame(new LoggedInUserComponent());
                    dispose();
                } else if (AppUser.getLoggedInUser().getRole().roleId() == 3) {
                    // corresponds to the Admin role so the app should display options for admins
                    MainFrame frame = (MainFrame) parent;
                    frame.showFrame(new LoggedInAdminComponent());
                    dispose();
                } else {
                    throw new RuntimeException("The role with id " + AppUser.getLoggedInUser().getRole().roleId() + " does not exist!");
                }
            } else {
                JOptionPane.showMessageDialog(LoginScreenDialog.this,
                        "Invalid username or password!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // register button to open the register dialog
        JButton registerButton = new JButton("Create new account");
        registerButton.addActionListener(e -> {
            dispose();  // disposes the current dialog
            RegisterScreenDialog registerDialog = new RegisterScreenDialog(parent);
            registerDialog.setVisible(true);  // show the dialog for registration
        });

        // a panel which holds the two buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        return buttonPanel;
    }
}
