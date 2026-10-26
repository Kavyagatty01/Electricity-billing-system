import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainSystem extends JFrame implements ActionListener {

    JButton addCustomer, deleteCustomer, generateBill, payBill, showDetails, lastBill;

    public MainSystem() {

        setTitle("Electricity Billing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ NORMAL SIZE FIRST (NOT MAXIMIZED)
        setSize(1000, 650);
        setLocationRelativeTo(null); // CENTER ON SCREEN
        setMinimumSize(new Dimension(900, 600));

        // ===== BACKGROUND =====
        BackgroundPanel background = new BackgroundPanel("icons/bg2.jpg");
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        // ===== CENTER PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(520, 420));
        mainPanel.setBackground(new Color(255, 255, 255, 235));
        mainPanel.setLayout(null);

        // ===== TITLE =====
        JLabel title = new JLabel("ELECTRICITY BILLING SYSTEM", SwingConstants.CENTER);
        title.setBounds(0, 25, 520, 35);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(0, 51, 102));
        mainPanel.add(title);

        // ===== BUTTONS =====
        addCustomer = createButton("Add Customer", 90);
        deleteCustomer = createButton("Delete Customer", 135);
        generateBill = createButton("Generate Bill", 180);
        payBill = createButton("Pay Bill", 225);
        showDetails = createButton("Show Details", 270);
        lastBill = createButton("Last Bill", 315);

        mainPanel.add(addCustomer);
        mainPanel.add(deleteCustomer);
        mainPanel.add(generateBill);
        mainPanel.add(payBill);
        mainPanel.add(showDetails);
        mainPanel.add(lastBill);

        background.add(mainPanel); // ALWAYS CENTERED

        setVisible(true);
    }

    private JButton createButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(140, y, 240, 35);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addCustomer) {
            new AddCustomer();

        } else if (e.getSource() == deleteCustomer) {
            new DeleteCustomer();

        } else if (e.getSource() == generateBill) {
            new GenerateBill();

        } else if (e.getSource() == payBill) {
            new PayBill();

        } else if (e.getSource() == showDetails) {
            new ShowDetails();

        } else if (e.getSource() == lastBill) {
            new LastBill();
        }
    }

    public static void main(String[] args) {
        new MainSystem();
    }
}