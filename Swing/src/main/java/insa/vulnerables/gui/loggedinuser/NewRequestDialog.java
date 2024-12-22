package insa.vulnerables.gui.loggedinuser;

import insa.vulnerables.httpClient.VulnerablesHttpClient;
import insa.vulnerables.user.AppUser;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class NewRequestDialog extends JDialog {
    private JTextField nameField;
    private JTextField locationField;
    private JTextArea messageArea;

    public NewRequestDialog(JFrame parent, LoggedInUserComponent loggedInUserComponent) {
        super(parent, "New Request/Offer", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Location:"));
        locationField = new JTextField();
        add(locationField);

        add(new JLabel("Message:"));
        messageArea = new JTextArea();
        add(new JScrollPane(messageArea));

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String type = Objects.equals(AppUser.getLoggedInUser().getRole().roleName(), "VulnerablePerson") ? "request" : "offer";
            if (VulnerablesHttpClient.getInstance().addRequest(type, nameField.getText(), locationField.getText(), messageArea.getText())) {
                JOptionPane.showMessageDialog(this, "Request submitted successfully");
                dispose();
                loggedInUserComponent.refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit request, check parameters and try again");
                dispose();
            }
        });
        add(submitButton);
    }
}
