package insa.vulnerables.gui.admin;

import insa.vulnerables.httpClient.VulnerablesHttpClient;
import insa.vulnerables.user.AppUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;
import java.util.List;

public class ApproveDenyUsersAdminComponent extends JComponent {

    private JTable table;
    private DefaultTableModel tableModel;

    public ApproveDenyUsersAdminComponent(List<AppUser> users) {
        super();

        String[] columnNames = {"Username", "First name", "Last name", "Role", "Approve/Deny"};
        Object[][] data = new Object[users.size()][5];

        for (int i = 0; i < users.size(); i++) {
            AppUser user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getFirstName();
            data[i][2] = user.getLastName();
            data[i][3] = user.getRole().roleName();
            data[i][4] = user; // store the entire object, so it can be retrieved for the button purposes
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // this means only the column with the button is editable
            }
        };

        table = new JTable(tableModel);
        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(this));
        table.setRowHeight(40); // set row height because otherwise the GUI breaks

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
            JButton denyButton = new JButton("Reject");
            approveButton.setPreferredSize(new Dimension(75, 30));
            denyButton.setPreferredSize(new Dimension(75, 30));
            add(approveButton);
            add(denyButton);
            return this;
        }
    }

    // this whole thing is just used, so I can place the button inside the table
    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton approveButton;
        private JButton denyButton;
        private AppUser currentUser;
        private ApproveDenyUsersAdminComponent parentComponent;

        public ButtonEditor(ApproveDenyUsersAdminComponent parentComponent) {
            this.parentComponent = parentComponent;

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            approveButton = new JButton("Approve");
            denyButton = new JButton("Reject");

            approveButton.addActionListener(e -> {
                if (VulnerablesHttpClient.getInstance().changeUserStatus(currentUser.getUserId(), "APPROVED")) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "User approved successfully");
                        parentComponent.removeRow(parentComponent.table.getEditingRow());
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Failed to approve user");
                    });
                }
            });

            denyButton.addActionListener(e -> {
                if (VulnerablesHttpClient.getInstance().changeUserStatus(currentUser.getUserId(), "REJECTED")) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "User rejected successfully");
                        parentComponent.removeRow(parentComponent.table.getEditingRow());
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Failed to reject user");
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
            currentUser = (AppUser) value;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentUser;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
}
