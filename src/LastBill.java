import java.awt.*;
import java.awt.print.PrinterException;
import java.io.FileWriter;
import java.sql.*;
import javax.swing.*;

public class LastBill extends JFrame {

    JTextField tfMeter;
    JTextArea taBill;
    JButton btnSearch, btnClose, btnPrint, btnDownload;

    LastBill() {

        setTitle("Last Electricity Bill Details");
        setLayout(null);
        getContentPane().setBackground(new Color(224, 242, 255));

        JLabel heading = new JLabel("Last Electricity Bill Details");
        heading.setBounds(150, 10, 450, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        add(heading);

        JLabel lblMeter = new JLabel("Meter Number:");
        lblMeter.setBounds(120, 60, 120, 25);
        add(lblMeter);

        tfMeter = new JTextField();
        tfMeter.setBounds(250, 60, 150, 25);
        add(tfMeter);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(420, 60, 100, 25);
        add(btnSearch);

        taBill = new JTextArea();
        taBill.setEditable(false);
        taBill.setForeground(Color.BLACK);
        taBill.setBackground(Color.WHITE);
        taBill.setFont(new Font("Monospaced", Font.BOLD, 14));

        JScrollPane sp = new JScrollPane(taBill);
        sp.setBounds(60, 110, 520, 330);
        add(sp);

        // ---------- PRINT BUTTON ----------
        btnPrint = new JButton("Print");
        btnPrint.setBounds(120, 460, 120, 30);
        add(btnPrint);

        // ---------- DOWNLOAD BUTTON ----------
        btnDownload = new JButton("Download");
        btnDownload.setBounds(270, 460, 120, 30);
        add(btnDownload);

        // ---------- CLOSE BUTTON ----------
        btnClose = new JButton("Close");
        btnClose.setBounds(420, 460, 120, 30);
        add(btnClose);

        btnSearch.addActionListener(e -> loadLastBill());
        btnClose.addActionListener(e -> setVisible(false));

        btnPrint.addActionListener(e -> printBill());
        btnDownload.addActionListener(e -> downloadBill());

        setSize(650, 550);
        setLocation(350, 120);
        setVisible(true);
    }

    void loadLastBill() {

        String meter = tfMeter.getText().trim();

        if (meter.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Meter Number");
            return;
        }

        try {
            Conn conn = new Conn();

            String q =
                "SELECT c.name, b.meter_no, b.units, b.amount, b.bill_date, b.day, " +
                "IFNULL(p.status,'Pending') AS status " +
                "FROM bill b " +
                "LEFT JOIN customer c ON b.meter_no = c.meter_no " +
                "LEFT JOIN payment p ON b.meter_no = p.meter_no " +
                "WHERE b.meter_no=? " +
                "ORDER BY b.bill_date DESC LIMIT 1";

            PreparedStatement ps = conn.c.prepareStatement(q);
            ps.setString(1, meter);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                taBill.setText(
                    "        ELECTRICITY BILL RECEIPT\n" +
                    "---------------------------------------------\n\n" +
                    "Meter Number   : " + rs.getString("meter_no") + "\n" +
                    "Customer Name  : " + rs.getString("name") + "\n\n" +
                    "Bill Date      : " + rs.getString("bill_date") + "\n" +
                    "Day            : " + rs.getString("day") + "\n\n" +
                    "Units Used     : " + rs.getString("units") + "\n" +
                    "Total Amount   : ₹ " + rs.getString("amount") + "\n" +
                    "Payment Status : " + rs.getString("status") + "\n" +
                    "---------------------------------------------\n\n" +
                    "Thank you!\n" +
                    "Power Supply Department\n"
                );

            } else {
                JOptionPane.showMessageDialog(this,
                    "No bill found for this meter");
                taBill.setText("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error fetching bill: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------- PRINT FUNCTION ----------
    void printBill() {
        try {
            if (taBill.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No bill to print!");
                return;
            }

            taBill.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this,
                "Print failed: " + e.getMessage());
        }
    }

    // ---------- DOWNLOAD FUNCTION ----------
    void downloadBill() {
        try {
            if (taBill.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No bill to download!");
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("ElectricityBill.txt"));

            int option = chooser.showSaveDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                FileWriter writer =
                    new FileWriter(chooser.getSelectedFile());
                writer.write(taBill.getText());
                writer.close();

                JOptionPane.showMessageDialog(this,
                    "Bill downloaded successfully!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Download failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LastBill();
    }
}
