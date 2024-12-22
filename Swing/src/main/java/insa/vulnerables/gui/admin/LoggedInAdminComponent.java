package insa.vulnerables.gui.admin;

import insa.vulnerables.gui.MainFrame;
import insa.vulnerables.gui.authentication.LoginScreenDialog;
import insa.vulnerables.httpClient.VulnerablesHttpClient;
import insa.vulnerables.user.AppUser;
import insa.vulnerables.user.Request;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoggedInAdminComponent extends JComponent {

    public LoggedInAdminComponent() {
        super();
        setLayout(new BorderLayout());
        List<AppUser> pendingUsers = VulnerablesHttpClient.getInstance().getPendingUsers();
        List<Request> pendingRequests = VulnerablesHttpClient.getInstance().getPendingRequests();

        ApproveDenyUsersAdminComponent approveDenyUsersAdminComponent = new ApproveDenyUsersAdminComponent(pendingUsers);
        approveDenyUsersAdminComponent.setName("Pending users");
        add(approveDenyUsersAdminComponent, BorderLayout.NORTH);

        ApproveDenyRequestsAdminComponent pendingRequestsComponent = new ApproveDenyRequestsAdminComponent(pendingRequests);
        pendingRequestsComponent.setName("Pending requests");
        add(pendingRequestsComponent, BorderLayout.CENTER);

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
        add(logoutButton, BorderLayout.SOUTH);
    }
}
