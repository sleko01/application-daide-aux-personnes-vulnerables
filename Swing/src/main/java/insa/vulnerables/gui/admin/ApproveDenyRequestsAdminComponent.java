package insa.vulnerables.gui.admin;

import insa.vulnerables.httpClient.VulnerablesHttpClient;
import insa.vulnerables.user.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;
import java.util.List;

public class ApproveDenyRequestsAdminComponent extends JComponent {

    private JTable table;
    private DefaultTableModel tableModel;

    public ApproveDenyRequestsAdminComponent(List<Request> requests) {
        super();

        String[] columnNames = {"Username", "Name", "Location", "Message", "Request type", "Approve/Deny"};
        Object[][] data = new Object[requests.size()][6];

        for (int i = 0; i < requests.size(); i++) {
            Request request = requests.get(i);
            data[i][0] = request.getAppUser().getUsername();
            data[i][1] = request.getName();
            data[i][2] = request.getLocation();
            data[i][3] = request.getMessage();
            data[i][4] = request.getRequestType().requestTypeName();
            data[i][5] = request; // store the entire object, so it can be retrieved for the button purposes
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // this means only the column with the button is editable
            }
        };

        table = new JTable(tableModel);
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(this));
        table.setRowHeight(40); // sets row height because otherwise the GUI breaks

        // wrapper in case there are a lot of requests
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void removeRow(int rowIndex) {
        try {
            tableModel.removeRow(rowIndex);
        } catch (ArrayIndexOutOfBoundsException e) {
            tableModel.removeRow(0);
        }
    }

    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            JButton approveButton = new JButton("Approve");
            JButton denyButton = new JButton("Deny");
            approveButton.setPreferredSize(new Dimension(75, 30));
            denyButton.setPreferredSize(new Dimension(75, 30));
            add(approveButton);
            add(denyButton);
            return this;
        }
    }

    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton approveButton;
        private JButton denyButton;
        private Request currentRequest;
        private ApproveDenyRequestsAdminComponent parentComponent;

        public ButtonEditor(ApproveDenyRequestsAdminComponent parentComponent) {
            this.parentComponent = parentComponent;

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            approveButton = new JButton("Approve");
            denyButton = new JButton("Deny");

            approveButton.addActionListener(e -> {
                if (VulnerablesHttpClient.getInstance().changeRequestStatus(currentRequest.getRequestId(), "Validated")) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Request approved successfully");
                        parentComponent.removeRow(parentComponent.table.getEditingRow());
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Failed to approve request, try again");
                    });
                }
            });

            denyButton.addActionListener(e -> {
                if (VulnerablesHttpClient.getInstance().changeRequestStatus(currentRequest.getRequestId(), "Rejected")) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Request denied successfully");
                        parentComponent.removeRow(parentComponent.table.getEditingRow());
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Failed to deny request, try again");
                    });
                }
            });

            approveButton.setPreferredSize(new Dimension(75, 30));
            denyButton.setPreferredSize(new Dimension(75, 30));
            panel.add(approveButton);
            panel.add(denyButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRequest = (Request) value;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentRequest;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
}
