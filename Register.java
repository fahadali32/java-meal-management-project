import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.prompt.PromptSupport;

class RegisterBackgroundPanel extends JPanel {
    private Image backgroundImage;

    public RegisterBackgroundPanel() {
        try {
            // Load the background image
            backgroundImage = ImageIO.read(getClass().getResource("food.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Scale the background image to fit the panel
        Image scaledImage = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);

        // Draw the scaled background image
        g.drawImage(scaledImage, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1500, 900);
    }
}

class RegisterPanel extends JPanel {

    public RegisterPanel(JFrame parentFrame) {

        Connect con = new Connect();
        Login login = new Login();

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(300, 300));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel(" Register ");
        titleLabel.setFont(titleLabel.getFont().deriveFont(20.0f));
        add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.insets.top = 10;
        gbc.insets.bottom = 10;
        JTextField username = new JTextField();
        PromptSupport.setPrompt("Enter your username", username);
        PromptSupport.setForeground(Color.BLACK, username);
        add(username, gbc);

        gbc.gridy = 2;
        JTextField first_name = new JTextField();
        PromptSupport.setPrompt("Enter your firstname", first_name);
        PromptSupport.setForeground(Color.BLACK, first_name);
        add(first_name, gbc);

        gbc.gridy = 3;
        JTextField last_name = new JTextField();
        PromptSupport.setPrompt("Enter your lastname", last_name);
        PromptSupport.setForeground(Color.BLACK, last_name);
        add(last_name, gbc);

        gbc.gridy = 4;
        JTextField password = new JTextField();
        PromptSupport.setPrompt("Enter your password", password);
        PromptSupport.setForeground(Color.BLACK, password);
        add(password, gbc);

        gbc.gridy = 5;
        JButton registerBtn = new JButton("Register");
        add(registerBtn, gbc);
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // con.connect("INSERT INTO auth (username,first_name,last_name,password )
                // VALUES ( %s,%s,%s,%s
                // )",username.getText(),first_name.getText(),last_name.getText(),password.getText());
                // System.out.println();
                // System.out.println(username.getText());
                String uname = username.getText();
                String ftName = first_name.getText();
                String ltName = last_name.getText();
                String passd = password.getText();

                // String query = "INSERT INTO auth (username,first_name,last_name,password ) VALUES ( ?, ?, ?, ?)";
                con.connect(uname, ftName, ltName, passd);
            }
        });

        gbc.gridy = 6;
        gbc.gridx = 0;
        JButton backBtn = new JButton("Back to Login");
        backBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                login.setVisible(true);
                parentFrame.dispose();

            }

        });
        add(backBtn, gbc);
    }
}

public class Register extends JFrame {
    public Register() {
        setTitle("Meal Management System - Register");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        RegisterBackgroundPanel backgroundPanel = new RegisterBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.CENTER;

        JLabel headingLabel = new JLabel("Meal Management System - Register");
        headingLabel.setFont(headingLabel.getFont().deriveFont(24.0f));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0; // Set weighty to 0 to prevent the heading from expanding vertically
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding
        backgroundPanel.add(headingLabel, gbc);

        RegisterPanel registerPanel = new RegisterPanel(this);
        gbc.gridy = 1;
        gbc.weighty = 1.0; // Set weighty to 1 to allow the RegisterPanel to expand vertically
        backgroundPanel.add(registerPanel, gbc);

        pack();
        setVisible(true);
    }

}
