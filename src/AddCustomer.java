import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AddCustomer extends JFrame implements ActionListener {

    JTextField tfName, tfMeter, tfAddress, tfCity, tfState, tfEmail, tfPhone;
    JButton btnSubmit, btnBack;

    public AddCustomer() {

        setTitle("Add Customer");
        setLayout(null);

        getContentPane().setBackground(new Color(210, 225, 245));

        JPanel panel = new JPanel();
        panel.setBounds(40, 30, 520, 420);
        panel.setBackground(new Color(245, 248, 255));
        panel.setLayout(null);
        add(panel);

        JLabel heading = new JLabel("ADD NEW CUSTOMER");
        heading.setBounds(140, 20, 300, 30);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(new Color(0, 51, 102));
        panel.add(heading);

        addLabel(panel, "Customer Name:", 60);
        tfName = addTextField(panel, 60);

        addLabel(panel, "Meter Number:", 100);
        tfMeter = addTextField(panel, 100);

        addLabel(panel, "Address:", 140);
        tfAddress = addTextField(panel, 140);

        addLabel(panel, "City:", 180);
        tfCity = addTextField(panel, 180);

        addLabel(panel, "State:", 220);
        tfState = addTextField(panel, 220);

        addLabel(panel, "Email:", 260);
        tfEmail = addTextField(panel, 260);

        addLabel(panel, "Mobile No:", 300);
        tfPhone = addTextField(panel, 300);

        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(140, 350, 100, 35);
        styleButton(btnSubmit, new Color(0, 153, 76));
        panel.add(btnSubmit);

        btnBack = new JButton("Back");
        btnBack.setBounds(280, 350, 100, 35);
        styleButton(btnBack, new Color(70, 130, 180));
        panel.add(btnBack);

        btnSubmit.addActionListener(this);
        btnBack.addActionListener(this);

        setSize(620, 520);
        setLocation(450, 120);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addLabel(JPanel panel, String text, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(60, y, 120, 25);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl);
    }

    private JTextField addTextField(JPanel panel, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(200, y, 220, 25);
        panel.add(tf);
        return tf;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == btnSubmit) {

            String name = tfName.getText().trim();
            String meter = tfMeter.getText().trim();
            String phone = tfPhone.getText().trim();

            // REQUIRED FIELD CHECK
            if (name.equals("") || meter.equals("")) {
                JOptionPane.showMessageDialog(null, "Name and Meter Number are required");
                return;
            }

            // MOBILE NUMBER VALIDATION (10 DIGITS ONLY)
            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null, "Enter valid 10-digit mobile number");
                tfPhone.requestFocus();
                return;
            }

            try {
                Conn c = new Conn();

                String query = "INSERT INTO customer VALUES('"
                        + meter + "','"
                        + name + "','"
                        + tfAddress.getText() + "','"
                        + tfCity.getText() + "','"
                        + tfState.getText() + "','"
                        + tfEmail.getText() + "','"
                        + phone + "')";

                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Customer Added Successfully");
                setVisible(false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Meter Number Already Exists");
                e.printStackTrace();
            }

        } else {
            setVisible(false);
        }
    }
}
