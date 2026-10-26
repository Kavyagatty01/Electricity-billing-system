import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ShowDetails extends JFrame {

    JTable table;
    DefaultTableModel model;

    JComboBox<String> cbTable;
    JButton btnLoad, btnClose;

    ShowDetails() {

        setTitle("Show Details");
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 255));

        JLabel heading = new JLabel("Show Details");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        heading.setBounds(250, 10, 200, 30);
        add(heading);

        JLabel lblSelect = new JLabel("Select Details:");
        lblSelect.setBounds(30, 60, 120, 25);
        add(lblSelect);

        cbTable = new JComboBox<>(new String[]{
            "Customer Details",
            "Generated Bills",
            "Payment Details"
        });
        cbTable.setBounds(150, 60, 200, 25);
        add(cbTable);

        btnLoad = new JButton("Load");
        btnLoad.setBounds(380, 60, 90, 25);
        add(btnLoad);

        btnClose = new JButton("Close");
        btnClose.setBounds(490, 60, 90, 25);
        add(btnClose);

        model = new DefaultTableModel();
        table = new JTable(model);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 110, 760, 360);
        add(sp);

        btnLoad.addActionListener(e -> loadData());
        btnClose.addActionListener(e -> setVisible(false));

        setSize(820, 540);
        setLocation(250, 120);
        setVisible(true);
    }

    void loadData() {

        model.setRowCount(0);
        model.setColumnCount(0);

        String selected = (String) cbTable.getSelectedItem();

        try {
            Conn conn = new Conn();
            Statement s = conn.c.createStatement();
            ResultSet rs;

            // ---------------- CUSTOMER ----------------
            if (selected.equals("Customer Details")) {

                model.addColumn("Meter No");
                model.addColumn("Name");
                model.addColumn("Address");
                model.addColumn("City");
                model.addColumn("State");
                model.addColumn("Email");
                model.addColumn("Phone");

                rs = s.executeQuery("SELECT * FROM customer");

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("meter_no"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("email"),
                        rs.getString("phone")
                    });
                }
            }

            // ---------------- GENERATED BILL (WITH NAME) ----------------
            else if (selected.equals("Generated Bills")) {

                model.addColumn("Meter No");
                model.addColumn("Name");      // Added
                model.addColumn("Units");
                model.addColumn("Amount");
                model.addColumn("Bill Date");
                model.addColumn("Day");

                // JOIN customer + bill
                String query =
                    "SELECT bill.meter_no, customer.name, bill.units, bill.amount, bill.bill_date, bill.day " +
                    "FROM bill INNER JOIN customer " +
                    "ON bill.meter_no = customer.meter_no";

                rs = s.executeQuery(query);

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("meter_no"),
                        rs.getString("name"),
                        rs.getInt("units"),
                        rs.getDouble("amount"),
                        rs.getString("bill_date"),
                        rs.getString("day")
                    });
                }
            }

            // ---------------- PAYMENT ----------------
            else if (selected.equals("Payment Details")) {

                model.addColumn("Meter No");
                model.addColumn("Name");
                model.addColumn("Amount");
                model.addColumn("Status");
                model.addColumn("Bill Date");

                rs = s.executeQuery("SELECT * FROM payment");

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("meter_no"),
                        rs.getString("name"),
                        rs.getString("amount"),
                        rs.getString("status"),
                        rs.getString("bill_date")
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ShowDetails();
    }
}
