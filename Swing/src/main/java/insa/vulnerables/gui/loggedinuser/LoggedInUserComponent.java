package insa.vulnerables.gui.loggedinuser;

import insa.vulnerables.gui.MainFrame;
import insa.vulnerables.gui.authentication.LoginScreenDialog;
import insa.vulnerables.httpClient.VulnerablesHttpClient;

import javax.swing.*;
import java.awt.*;

public class LoggedInUserComponent extends JComponent {

    private RequestsComponent myRequestsComponent;
    private OtherUsersRequestsComponent otherUsersRequestsComponent;

    public LoggedInUserComponent() {
        super();
        setLayout(new BorderLayout());

        myRequestsComponent = new RequestsComponent(VulnerablesHttpClient.getInstance().getUserRequests());
        myRequestsComponent.setName("My requests or offers");
        add(myRequestsComponent, BorderLayout.NORTH);

        otherUsersRequestsComponent = new OtherUsersRequestsComponent(
                VulnerablesHttpClient.getInstance().getAllOtherOffers(),
                VulnerablesHttpClient.getInstance().getAllMyAcceptedRequests(),
                this
        );
        otherUsersRequestsComponent.setName("Other user requests");
        add(otherUsersRequestsComponent, BorderLayout.CENTER);

        JButton newRequestButton = new JButton("New Request/Offer");
        newRequestButton.addActionListener(e -> {
            NewRequestDialog newRequestDialog = new NewRequestDialog((JFrame) SwingUtilities.getWindowAncestor(this), this);
            newRequestDialog.setVisible(true);
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            if (VulnerablesHttpClient.getInstance().logout()) {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                LoginScreenDialog dialog = new LoginScreenDialog(mainFrame);
                dialog.setVisible(true);
                SwingUtilities.getWindowAncestor(this).dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(newRequestButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // refreshes the entire component, re-fetches the data from the server
    public void refreshData() {
        remove(myRequestsComponent);
        remove(otherUsersRequestsComponent);

        myRequestsComponent = new RequestsComponent(VulnerablesHttpClient.getInstance().getUserRequests());
        myRequestsComponent.setName("My requests or offers");
        add(myRequestsComponent, BorderLayout.NORTH);

        otherUsersRequestsComponent = new OtherUsersRequestsComponent(
                VulnerablesHttpClient.getInstance().getAllOtherOffers(),
                VulnerablesHttpClient.getInstance().getAllMyAcceptedRequests(),
                this
        );
        otherUsersRequestsComponent.setName("Other user requests");
        add(otherUsersRequestsComponent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
