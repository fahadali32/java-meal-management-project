import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.json.JSONArray;
import org.json.JSONObject;

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

class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1200, 700));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, gbc);

        gbc.gridy = 1;
        JLabel welcomeLabel = new JLabel("Welcome to the Meal Management System!");
        add(welcomeLabel, gbc);

        gbc.gridy = 2;
        // String data[][] = { { "101", "Amit", "670000" },
        //         { "102", "Jai", "780000" },
        //         { "101", "Sachin", "700000" } };
        // String column[] = { "ID", "NAME", "MONEY" };
        // JTable jt = new JTable(data, column);
        // jt.setBounds(50, 1, 1200, 700);
        // JScrollPane sp = new JScrollPane(jt);
        // add(sp, gbc);

        

        Connect connect = new Connect();
        JSONArray jsonArray = connect.ShowUser();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String username = json.getString("first_name");
            System.out.println(username);
            
            
        }

    }
}

class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Meal Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.CENTER;

        JLabel headingLabel = new JLabel("Meal Management System - Dashboard");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0; // Set weighty to 0 to prevent the heading from expanding vertically
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding
        backgroundPanel.add(headingLabel, gbc);

        DashboardPanel dashboardPanel = new DashboardPanel();
        gbc.gridy = 1;
        gbc.weighty = 1.0; // Set weighty to 1 to allow the DashboardPanel to expand vertically
        backgroundPanel.add(dashboardPanel, gbc);

        pack();
        setVisible(true);
    }
}
