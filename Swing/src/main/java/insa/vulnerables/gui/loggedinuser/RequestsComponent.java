package insa.vulnerables.gui.loggedinuser;

import insa.vulnerables.user.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RequestsComponent extends JComponent {

    public RequestsComponent(List<Request> requests) {
        super();

        String[] columnNames = {"Status", "Name", "Location", "Message"};

        Object[][] data = new Object[requests.size()][4];

        for (int i = 0; i < requests.size(); i++) {
            Request request = requests.get(i);
            data[i][0] = request.getStatus() != null ? request.getStatus().statusName() : null;
            data[i][1] = request.getName();
            data[i][2] = request.getLocation();
            data[i][3] = request.getMessage();
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
}
