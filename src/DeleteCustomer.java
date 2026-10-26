import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DeleteCustomer extends JFrame implements ActionListener {

    JTextField tfMeter;
    JButton btnDelete, btnBack;

    public DeleteCustomer() {

        setTitle("Delete Customer");

        // FRAME BACKGROUND COLOR
        getContentPane().setBackground(new Color(200, 220, 240));
        setLayout(null);

        // MAIN PANEL
        JPanel panel = new JPanel();
        panel.setBounds(30, 30, 380, 220);
        panel.setBackground(new Color(245, 248, 255));
        panel.setLayout(null);
        add(panel);

        // HEADING
        JLabel heading = new JLabel("DELETE CUSTOMER");
        heading.setBounds(80, 20, 250, 30);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(new Color(0, 51, 102));
        panel.add(heading);

        // LABEL
        JLabel lblMeter = new JLabel("Meter Number:");
        lblMeter.setBounds(50, 80, 120, 25);
        lblMeter.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblMeter);

        // TEXT FIELD
        tfMeter = new JTextField();
        tfMeter.setBounds(180, 80, 150, 25);
        tfMeter.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(tfMeter);

        // DELETE BUTTON
        btnDelete = new JButton("Delete");
        btnDelete.setBounds(70, 150, 100, 35);
        btnDelete.setBackground(new Color(204, 0, 0));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(btnDelete);

        // BACK BUTTON
        btnBack = new JButton("Back");
        btnBack.setBounds(200, 150, 100, 35);
        btnBack.setBackground(new Color(70, 130, 180));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(btnBack);

        btnDelete.addActionListener(this);
        btnBack.addActionListener(this);

        setSize(460, 320);
        setLocation(500, 250);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == btnDelete) {

            if (tfMeter.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter Meter Number");
                return;
            }

            try {
                Conn c = new Conn();
                String query = "DELETE FROM customer WHERE meter_no='" + tfMeter.getText() + "'";
                int rows = c.s.executeUpdate(query);

                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Customer Deleted Successfully");
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Customer Not Found");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            setVisible(false);
        }
    }
}