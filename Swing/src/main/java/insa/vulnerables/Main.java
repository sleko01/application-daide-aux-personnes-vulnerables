package insa.vulnerables;

import insa.vulnerables.gui.authentication.LoginScreenDialog;
import insa.vulnerables.gui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            LoginScreenDialog dialog = new LoginScreenDialog(mainFrame);
            mainFrame.setVisible(true);
            dialog.setVisible(true);
        });
    }
}