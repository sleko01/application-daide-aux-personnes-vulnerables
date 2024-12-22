package insa.vulnerables.gui.loggedinuser;

import insa.vulnerables.httpClient.VulnerablesHttpClient;
import insa.vulnerables.user.Request;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;
import java.util.List;

public class OtherUsersRequestsComponent extends JComponent {

    private JTable activeRequestsTable;
    private JTable myAcceptedRequestsTable;
    private DefaultTableModel activeRequestsTableModel;
    private DefaultTableModel myAcceptedRequestsTableModel;
    private LoggedInUserComponent parentComponent;

    public OtherUsersRequestsComponent(List<Request> activeRequests, List<Request> myAcceptedRequests, LoggedInUserComponent parentComponent) {
        super();
        this.parentComponent = parentComponent;

        setLayout(new BorderLayout());

        activeRequestsTableModel = createTableModel(activeRequests, "Accept");
        activeRequestsTable = createTable(activeRequestsTableModel, "Accept", true);

        myAcceptedRequestsTableModel = createTableModel(myAcceptedRequests, "Mark as completed");
        myAcceptedRequestsTable = createTable(myAcceptedRequestsTableModel, "Mark as completed", false);

        JScrollPane activeRequestsScrollPane = new JScrollPane(activeRequestsTable);
        JScrollPane myAcceptedRequestsScrollPane = new JScrollPane(myAcceptedRequestsTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("My Accepted Requests"));
        topPanel.add(myAcceptedRequestsScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Active Requests"));
        bottomPanel.add(activeRequestsScrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.EAST);
    }

    private DefaultTableModel createTableModel(List<Request> requests, String buttonLabel) {
        String[] columnNames = {"Username", "Name", "Location", "Message", buttonLabel};
        Object[][] data = new Object[requests.size()][5];

        for (int i = 0; i < requests.size(); i++) {
            Request request = requests.get(i);
            data[i][0] = request.getAppUser().getUsername();
            data[i][1] = request.getName();
            data[i][2] = request.getLocation();
            data[i][3] = request.getMessage();
            data[i][4] = request; // store the entire object so it can be used for the button
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // only the button column is editable
            }
        };
    }

    private JTable createTable(DefaultTableModel tableModel, String buttonLabel, boolean isActiveRequestsTable) {
        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(buttonLabel));
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(this, isActiveRequestsTable));
        table.setRowHeight(40);
        return table;
    }

    public void removeRow(DefaultTableModel tableModel, int rowIndex) {
        try {
            tableModel.removeRow(rowIndex);
        } catch (ArrayIndexOutOfBoundsException e) {
            tableModel.removeRow(0);
        }
    }

    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private String buttonLabel;

        public ButtonRenderer(String buttonLabel) {
            this.buttonLabel = buttonLabel;
            setLayout(new FlowLayout(FlowLayout.CENTER));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            JButton button = new JButton(buttonLabel);
            button.setPreferredSize(new Dimension(150, 30));
            add(button);
            return this;
        }
    }

    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton button;
        private Request currentRequest;
        private OtherUsersRequestsComponent parentComponent;
        private boolean isActiveRequestsTable;

        public ButtonEditor(OtherUsersRequestsComponent parentComponent, boolean isActiveRequestsTable) {
            this.parentComponent = parentComponent;
            this.isActiveRequestsTable = isActiveRequestsTable;

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            button = new JButton();

            button.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    if (isActiveRequestsTable) {
                        if (!VulnerablesHttpClient.getInstance().acceptRequest(currentRequest.getRequestId())) {
                            JOptionPane.showMessageDialog(null, "Failed to accept request, try again");
                            return;
                        }
                    } else {
                        if (VulnerablesHttpClient.getInstance().changeRequestStatus(currentRequest.getRequestId(), "Completed")) {
                            SwingUtilities.invokeLater(() -> {
                                RateRequestDialog rateRequestDialog = new RateRequestDialog(currentRequest);
                                rateRequestDialog.setVisible(true);
                            });
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to mark request as completed, try again");
                            return;
                        }
                    }
                    String message = isActiveRequestsTable ? "Request accepted successfully" : "Request marked as completed successfully";
                    JOptionPane.showMessageDialog(null, message);

                    JTable table = isActiveRequestsTable ? parentComponent.activeRequestsTable : parentComponent.myAcceptedRequestsTable;
                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                    parentComponent.removeRow(tableModel, getEditingRow());
                    parentComponent.parentComponent.refreshData();
                });
            });

            button.setPreferredSize(new Dimension(150, 30));
            panel.add(button);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRequest = (Request) value; // Retrieve the request object
            button.setText(isActiveRequestsTable ? "Accept" : "Mark as completed");
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

        private int getEditingRow() {
            return parentComponent.activeRequestsTable.getEditingRow();
        }
    }
}
