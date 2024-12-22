package insa.vulnerables.gui.loggedinuser;

import insa.vulnerables.httpClient.VulnerablesHttpClient;
import insa.vulnerables.user.Request;

import javax.swing.*;

public class RateRequestDialog extends JDialog {

    public RateRequestDialog(Request request) {
        super();
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("Name: " + request.getName());
        add(nameLabel);

        JLabel locationLabel = new JLabel("Location: " + request.getLocation());
        add(locationLabel);

        JLabel messageLabel = new JLabel("Message: " + request.getMessage());
        add(messageLabel);

        JLabel ratingLabel = new JLabel("Rating: ");
        add(ratingLabel);

        JSlider ratingSlider = new JSlider(0, 5);
        add(ratingSlider);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (VulnerablesHttpClient.getInstance().rateRequest(request.getRequestId(), ratingSlider.getValue())) {
                JOptionPane.showMessageDialog(this, "Request rated successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to rate request, check parameters and try again");
            }
        });
        add(submitButton);
    }
}
