import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Login extends JFrame implements ActionListener {

    JTextField tfUser;
    JPasswordField tfPass;
    JButton btnLogin;

    Login() {

        setTitle("Electricity Billing System - Login");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        BackgroundPanel background = new BackgroundPanel("icons/bg.jpg");
        background.setLayout(new GridBagLayout()); // CENTER EVERYTHING
        setContentPane(background);

        
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(350, 240));
        panel.setBackground(new Color(255, 255, 255, 220));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        background.add(panel); // ALWAYS CENTER

        // ===== COMPONENTS =====
        JLabel title = new JLabel("LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(0, 20, 350, 30);
        panel.add(title);

        JLabel lblUser = new JLabel("Username");
        lblUser.setBounds(40, 70, 100, 25);
        panel.add(lblUser);

        tfUser = new JTextField();
        tfUser.setBounds(140, 70, 160, 25);
        panel.add(tfUser);

        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(40, 110, 100, 25);
        panel.add(lblPass);

        tfPass = new JPasswordField();
        tfPass.setBounds(140, 110, 160, 25);
        panel.add(tfPass);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(115, 165, 120, 35);
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this);
        panel.add(btnLogin);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String user = tfUser.getText();
        String pass = new String(tfPass.getPassword());

        if (user.equals("admin") && pass.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Login Successful");
            new MainSystem();   // YOUR MAIN PAGE
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Username or Password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}