import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.*;

public class GenerateBill extends JFrame implements ActionListener {

    JTextField tfMeter, tfUnits;
    JLabel lblName, lblAmount, lblDate, lblDay;
    JButton btnSearch, btnCalculate, btnGenerate, btnBack;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    double calculatedAmount = 0;

    public GenerateBill() {
        setTitle("Generate Bill");
        setSize(520, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(210, 225, 245));

        JLabel heading = new JLabel("GENERATE BILL");
        heading.setBounds(160, 10, 250, 30);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        add(heading);

        JLabel lblMeter = new JLabel("Meter Number:");
        lblMeter.setBounds(40, 60, 120, 25);
        add(lblMeter);

        tfMeter = new JTextField();
        tfMeter.setBounds(170, 60, 150, 25);
        add(tfMeter);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(340, 60, 120, 25);
        btnSearch.addActionListener(this);
        add(btnSearch);

        lblName = new JLabel("Name: ");
        lblName.setBounds(40, 110, 350, 25);
        add(lblName);

        lblDate = new JLabel("Date: ");
        lblDate.setBounds(40, 150, 350, 25);
        add(lblDate);

        lblDay = new JLabel("Day: ");
        lblDay.setBounds(40, 190, 350, 25);
        add(lblDay);

        JLabel lblUnits = new JLabel("Units Consumed:");
        lblUnits.setBounds(40, 230, 150, 25);
        add(lblUnits);

        tfUnits = new JTextField();
        tfUnits.setBounds(170, 230, 150, 25);
        add(tfUnits);

        btnCalculate = new JButton("Calculate");
        btnCalculate.setBounds(340, 230, 120, 25);
        btnCalculate.addActionListener(this);
        add(btnCalculate);

        lblAmount = new JLabel("Amount: ");
        lblAmount.setBounds(40, 270, 350, 25);
        add(lblAmount);

        btnGenerate = new JButton("Generate Bill");
        btnGenerate.setBounds(120, 320, 130, 30);
        btnGenerate.addActionListener(this);
        add(btnGenerate);

        btnBack = new JButton("Back");
        btnBack.setBounds(280, 320, 90, 30);
        btnBack.addActionListener(this);
        add(btnBack);

        connectDB();
        setVisible(true);
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/electricitybillsystem",
                    "root",
                    "Kavya@0121"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Error: " + e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == btnSearch) {
            searchCustomer();
        }

        else if (ae.getSource() == btnCalculate) {
            calculateAmount();
        }

        else if (ae.getSource() == btnGenerate) {
            generateBill();
        }

        else if (ae.getSource() == btnBack) {
            this.dispose();
        }
    }

    // -------- SEARCH ----------
    private void searchCustomer() {
        String meter = tfMeter.getText().trim();

        if (meter.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Meter Number!");
            return;
        }

        try {
            pst = con.prepareStatement("SELECT name FROM customer WHERE meter_no = ?");
            pst.setString(1, meter);
            rs = pst.executeQuery();

            if (rs.next()) {

                lblName.setText("Name: " + rs.getString("name"));

                LocalDate today = LocalDate.now();
                lblDate.setText("Date: " + today);
                lblDay.setText("Day: " +
                        today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));

            } else {
                JOptionPane.showMessageDialog(this, "Meter number not found!");
                lblName.setText("Name: ");
                lblDate.setText("Date: ");
                lblDay.setText("Day: ");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // -------- CALCULATE ----------
    private void calculateAmount() {
        String unitsStr = tfUnits.getText().trim();

        if (unitsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter units first!");
            return;
        }

        try {
            int units = Integer.parseInt(unitsStr);
            calculatedAmount = units * 10;     // BILL FORMULA
            lblAmount.setText("Amount: " + calculatedAmount);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric units!");
        }
    }

    // -------- GENERATE BILL ----------
    private void generateBill() {

        String meter = tfMeter.getText().trim();
        String unitsStr = tfUnits.getText().trim();

        if (meter.isEmpty() || unitsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter meter number and units!");
            return;
        }

        if (calculatedAmount == 0) {
            JOptionPane.showMessageDialog(this, "Please calculate amount first!");
            return;
        }

        try {
            pst = con.prepareStatement("SELECT * FROM bill WHERE meter_no = ?");
            pst.setString(1, meter);
            rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "Bill already generated for this meter number!");
                return;
            }

            int units = Integer.parseInt(unitsStr);
            LocalDate today = LocalDate.now();

            pst = con.prepareStatement(
                    "INSERT INTO bill(meter_no, units, amount, bill_date, day) VALUES (?, ?, ?, ?, ?)"
            );

            pst.setString(1, meter);
            pst.setInt(2, units);
            pst.setDouble(3, calculatedAmount);
            pst.setDate(4, java.sql.Date.valueOf(today));
            pst.setString(5, today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Bill Generated Successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new GenerateBill();
    }
}
