import java.awt.*;
import javax.swing.*;

public class BackgroundPanel extends JPanel {

    private Image bgImage;

    public BackgroundPanel(String path) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        bgImage = icon.getImage();
        setLayout(new GridBagLayout()); // keeps center panel always centered
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}