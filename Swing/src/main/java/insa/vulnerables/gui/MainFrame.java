package insa.vulnerables.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super();
        setTitle("Vulnerables Application");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    public void showFrame(JComponent component) {
        SwingUtilities.invokeLater(() -> {
            getContentPane().removeAll();
            getContentPane().add(component, BorderLayout.CENTER);
            revalidate();
            repaint();
        });
    }
}
