import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class PayBill extends JFrame implements ActionListener {

    JTextField tfMeter;
    JLabel lblName, lblAmount, lblDate, lblDay;
    JButton btnSearch, btnPay, btnCancel;

    String meterNo;
    double amount;
    String customerName;

    // MySQL credentials
    final String URL = "jdbc:mysql://localhost:3306/electricitybillsystem"; // replace with your DB name
    final String USER = "root"; // your username
    final String PASS = "Kavya@0121";     // your password

    public PayBill() {
        setTitle("Pay Bill");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 230, 250));

        JLabel lblMeter = new JLabel("Meter No:");
        lblMeter.setBounds(30, 30, 100, 25);
        add(lblMeter);

        tfMeter = new JTextField();
        tfMeter.setBounds(150, 30, 150, 25);
        add(tfMeter);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(150, 70, 100, 25);
        btnSearch.addActionListener(this);
        add(btnSearch);

        lblName = new JLabel("Name: ");
        lblName.setBounds(30, 110, 300, 25);
        add(lblName);

        lblAmount = new JLabel("Amount: ");
        lblAmount.setBounds(30, 140, 300, 25);
        add(lblAmount);

        lblDate = new JLabel("Date: ");
        lblDate.setBounds(30, 170, 300, 25);
        add(lblDate);

        lblDay = new JLabel("Day: ");
        lblDay.setBounds(30, 200, 300, 25);
        add(lblDay);

        btnPay = new JButton("Pay");
        btnPay.setBounds(50, 250, 100, 25);
        btnPay.addActionListener(this);
        add(btnPay);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(200, 250, 100, 25);
        btnCancel.addActionListener(this);
        add(btnCancel);

        btnPay.setEnabled(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        meterNo = tfMeter.getText().trim();

        if (e.getSource() == btnSearch) {
            searchBill();
        } else if (e.getSource() == btnPay) {
            payBill();
        } else if (e.getSource() == btnCancel) {
            dispose();
        }
    }

    private void searchBill() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);

            // Fetch customer name from "customer" table
            String sql = "SELECT name FROM customer WHERE meter_no=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, meterNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                customerName = rs.getString("name");
                lblName.setText("Name: " + customerName);

                // Fixed or calculated amount
                amount = 500; 
                lblAmount.setText("Amount: " + amount);

                // Show current date and day
                Date today = new Date();
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
                lblDate.setText("Date: " + sdfDate.format(today));
                lblDay.setText("Day: " + sdfDay.format(today));

                // Check if bill already exists today
                sql = "SELECT * FROM payment WHERE meter_no=? AND DATE(bill_date) = CURDATE()";
                ps = con.prepareStatement(sql);
                ps.setString(1, meterNo);
                rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Bill already generated !");
                    btnPay.setEnabled(false);
                } else {
                    btnPay.setEnabled(true);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Meter number not found!");
                lblName.setText("Name: ");
                lblAmount.setText("Amount: ");
                lblDate.setText("Date: ");
                lblDay.setText("Day: ");
                btnPay.setEnabled(false);
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void payBill() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            String sql = "INSERT INTO payment (meter_no, name, amount, bill_date, status) VALUES (?, ?, ?, NOW(), 'Paid')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, meterNo);
            ps.setString(2, customerName);
            ps.setDouble(3, amount);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Payment Successful!");

            // Reset form
            tfMeter.setText("");
            lblName.setText("Name: ");
            lblAmount.setText("Amount: ");
            lblDate.setText("Date: ");
            lblDay.setText("Day: ");
            btnPay.setEnabled(false);

            con.close();
        } catch (SQLIntegrityConstraintViolationException dup) {
            JOptionPane.showMessageDialog(this, "Bill already generated!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new PayBill();
    }
}
