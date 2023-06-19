import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.jdesktop.swingx.prompt.PromptSupport;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
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
        return new Dimension(1400, 900);
    }
}



class DashboardPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private JTable table;
    private JLabel totalAmountLabel;

    public DashboardPanel(JFrame parentFrame) {
        tabbedPane = new JTabbedPane();

        JPanel panel1 = new JPanel(false);
        panel1.setPreferredSize(new Dimension(600, 400)); // Set preferred size for Tab 1 content panel
        panel1.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel AmountText = new JLabel("Add Amount to your account");
        panel1.add(AmountText, gbc);

        gbc.gridy = 1;
        JTextField textField = new JTextField(20);
        PromptSupport.setPrompt("Enter amount", textField);
        PromptSupport.setForeground(Color.BLACK, textField);
        panel1.add(textField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton sbbutton = new JButton("Submit");
        panel1.add(sbbutton, gbc);

        sbbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect conn = new Connect();
                conn.upDateAccount(textField.getText());
                System.out.println(textField.getText());

                updateTableData(); // Refresh the table data
            }
        });

        Session ss = new Session();
        System.out.println("Session user");
        System.out.println(ss.getSessionUser());

        tabbedPane.addTab("Account", null, panel1, "Account tooltip");

        JPanel panel2 = new JPanel(false);
        panel2.setPreferredSize(new Dimension(600, 400)); // Set preferred size for Tab 2 content panel
        panel2.setLayout(new GridBagLayout());

        // Add components to panel2
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        JTextField textField1 = new JTextField(20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textField1.setPreferredSize(new Dimension(100, 100));
        PromptSupport.setPrompt("Enter today's cost amount", textField1);
        PromptSupport.setForeground(Color.BLACK, textField1);
        panel2.add(textField1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JButton button1 = new JButton("Submit");
        panel2.add(button1, gbc);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aa = textField1.getText();
                Connect conn = new Connect();
                conn.updateAmount(aa);
                updateTableData();
            }
        });

        // Add components to panel2
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField textField2 = new JTextField(20);
        PromptSupport.setPrompt("Enter personal cost amount", textField2);
        PromptSupport.setForeground(Color.BLACK, textField2);
        panel2.add(textField2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton button2 = new JButton("Submit");
        panel2.add(button2, gbc);

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aa = textField2.getText();
                Connect conn = new Connect();
                conn.personalUpdate(aa);
                updateTableData();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Increase the gridwidth to make panel2 span 2 columns
        gbc.weightx = 1.0; // Set weightx to 1.0 to allow panel2 to expand horizontally
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 10, 10);

        // Create table with columns: Full Name, Total Amount, Today's Cost, Date
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Full Name");
        tableModel.addColumn("Total Amount");
        tableModel.addColumn("Today's Cost");
        tableModel.addColumn("Date");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        panel2.add(scrollPane, gbc);
        float totalAmount = 0;
        
        Connect conn = new Connect();
        JSONArray jsonArray = conn.ShowUser();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String fullName = obj.getString("full_name");
            float amount = obj.getFloat("total_amount");
            float todayCost = obj.getFloat("today_cost");
            String date = obj.getString("date");

            Object[] rowData = { fullName, amount, todayCost, date };
            tableModel.addRow(rowData);

            totalAmount += amount;
        }
        totalAmountLabel = new JLabel("Total Amount: "+totalAmount);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(totalAmountLabel, gbc);

        tabbedPane.addTab("User Details", null, panel2, "User Details tooltip");

        // Set the default selected tab to Tab 2
        tabbedPane.setSelectedIndex(1);

        setLayout(new GridLayout(1, 1));
        add(tabbedPane);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JTable getTable() {
        return table;
    }

    private void updateTableData() {
        Connect conn = new Connect();
        JSONArray jsonArray = conn.ShowUser();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0); // Clear existing rows

        float totalAmount = 0.0f;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String fullName = obj.getString("full_name");
            float amount = obj.getFloat("total_amount");
            float todayCost = obj.getFloat("today_cost");
            String date = obj.getString("date");

            Object[] rowData = { fullName, amount, todayCost, date };
            tableModel.addRow(rowData);

            totalAmount += amount;
        }

        totalAmountLabel.setText("Total Amount: " + totalAmount);
    }
}

class CustomTabbedPaneUI extends BasicTabbedPaneUI {
    private static final int TAB_FONT_SIZE = 20;

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight + TAB_FONT_SIZE);
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

        Connect conn = new Connect();
        System.out.println(conn.getUserData());
        String Wlw = "User: " + conn.getUserData();
        JLabel welUser = new JLabel(Wlw);
        welUser.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0; // Set weighty to 0 to prevent the heading from expanding vertically
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding
        backgroundPanel.add(welUser, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton sbbutton = new JButton("Log Out");
        backgroundPanel.add(sbbutton, gbc);

        sbbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                login.setVisible(true);
                dispose();
            }
        });

        DashboardPanel dashboardPanel = new DashboardPanel(this);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weighty = 1.0; // Set weighty to 1 to allow the DashboardPanel to expand vertically
        backgroundPanel.add(dashboardPanel, gbc);

        // Customize the tab font size
        JTabbedPane tabbedPane = dashboardPanel.getTabbedPane();
        tabbedPane.setUI(new CustomTabbedPaneUI());

        // Get the table
        JTable table = dashboardPanel.getTable();

        // Retrieve data from ShowUser() method
        JSONArray jsonArray = conn.ShowUser();

        Font tableFont = new Font("Arial", Font.PLAIN, 16);
        table.setFont(tableFont);

        // Populate the table with data
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0); // Clear existing rows

        float totalAmount = 0.0f;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String fullName = obj.getString("full_name");
            float amount = obj.getFloat("total_amount");
            float todayCost = obj.getFloat("today_cost");
            String date = obj.getString("date");

            Object[] rowData = { fullName, amount, todayCost, date };
            tableModel.addRow(rowData);

            totalAmount += amount;
        }

        
        System.out.println(totalAmount);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }

}