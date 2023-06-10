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

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {

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

class ResizablePanel extends JPanel {
    public ResizablePanel(JFrame parentFrame) {
        Connect con = new Connect();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(300, 300));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel(" Login ");
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
        JTextField password = new JTextField();
        PromptSupport.setPrompt("Enter your password", password);
        PromptSupport.setForeground(Color.BLACK, password);
        add(password, gbc);

        gbc.gridy = 3;
        JButton loginBtn = new JButton("Submit");
        add(loginBtn, gbc);

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String uname = username.getText();
                String passd = password.getText();
                System.out.println(uname.length());
                if (uname.length() != 0 && passd.length() != 0) {
                    con.connect(uname, passd, parentFrame);
                    Session ss = new Session();
                    ss.setSession(uname);
                    System.out.println("ok");
                } else {
                    System.out.println("please at first fill up something");
                }

            }
        });

        gbc.gridy = 4;
        gbc.gridx = 0;
        JButton regiButton = new JButton("Register user");
        regiButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Register Register = new Register();
                parentFrame.dispose(); // Close the current frame
                Register.setVisible(true);
            }

        });
        add(regiButton, gbc);
    }
}

class Login extends JFrame {
    public Login() {
        Connect cn = new Connect();
        cn.connect();

        setTitle("Meal Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.CENTER;

        JLabel headingLabel = new JLabel("Meal Management System -Login");
        headingLabel.setFont(headingLabel.getFont().deriveFont(24.0f));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0; // Set weighty to 0 to prevent the heading from expanding vertically
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding
        backgroundPanel.add(headingLabel, gbc);

        ResizablePanel resizablePanel = new ResizablePanel(this);
        gbc.gridy = 1;
        gbc.weighty = 1.0; // Set weighty to 1 to allow the ResizablePanel to expand vertically
        backgroundPanel.add(resizablePanel, gbc);

        pack();
        setVisible(true);
    }

}