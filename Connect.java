import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JFrame;
import java.time.LocalDate;
import javax.swing.*;
import java.sql.SQLException;

class Connect {
    private final Object lock = new Object(); // Lock object for synchronization

    public void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:meal.db";
            // create a connection to the database
            synchronized (lock) {
                conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement();
                System.out.println("Connection to SQLite has been established.");
                statement.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void connect(String username, String firstname, String lastname, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        JFrame f = new JFrame();
        try {
            // db parameters
            String url = "jdbc:sqlite:meal.db";
            // create a connection to the database
            synchronized (lock) {
                conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement();

                LocalDate dateTime = LocalDate.now();

                String query = "select * from auth where username=?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Exists Data");
                    JOptionPane.showMessageDialog(f, "Please use a different username");
                } else {
                    statement.executeUpdate("insert into auth(username,first_name,last_name,password) values ('" + username
                            + "','" + firstname + "','" + lastname + "','" + password + "')");
                    statement.executeUpdate(
                            "insert into user_details(full_name,total_amount,today_cost,date,username) values ('"
                                    + firstname + " " + lastname
                                    + "','" + 0 + "','" + 0 + "','" + dateTime + "','" + username + "')");
                    JOptionPane.showMessageDialog(f, "Register successful. Please click the back button to log in.");
                    System.out.println("Connection to SQLite has been established.");
                }

                statement.close();
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void connect(String username, String password, JFrame parentFrame) {
        Connection conn = null;
        PreparedStatement stmt = null;
        Session ss = new Session();
        ss.setSession(username);
        try {
            // db parameters
            String url = "jdbc:sqlite:meal.db";
            // create a connection to the database
            synchronized (lock) {
                String query = "select * from auth where username=? AND password=?";
                conn = DriverManager.getConnection(url);
                stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Login Successful if match is found
                    System.out.println("success");
                    Dashboard dash = new Dashboard();
                    dash.setVisible(true);
                    parentFrame.dispose();
                } else {
                    System.out.println("bad credential");
                }
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray ShowUser() {
        Connection conn = null;
        PreparedStatement stmt = null;
        JSONArray jsonArray = new JSONArray();
        try {
            // db parameters
            String url = "jdbc:sqlite:meal.db";
            // create a connection to the database
            // synchronized (lock) this block can make one thread at a time so that multiple thread or sqlite busy is not happening
            synchronized (lock) {
                String query = "select * from user_details";
                conn = DriverManager.getConnection(url);
                stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String fname = rs.getString("full_name");
                    String ta = rs.getString("total_amount");
                    Float total_amount = Float.parseFloat(ta);
                    String tc = rs.getString("today_cost");
                    Float today_cost = Float.parseFloat(tc);
                    String date = rs.getString("date");
                    JSONObject json = new JSONObject();
                    json.put("full_name", fname);
                    json.put("total_amount", total_amount);
                    json.put("today_cost", today_cost);
                    json.put("date", date);
                    jsonArray.put(json);
                }

                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public String getUserData() {
        Connection conn = null;
        PreparedStatement stmt = null;
        Session ss = new Session();
        ss.getSessionUser();
        try {
            String url = "jdbc:sqlite:meal.db";
            conn = DriverManager.getConnection(url);
            String selectQuery = "SELECT * FROM auth WHERE username=?";
            stmt = conn.prepareStatement(selectQuery);
            stmt.setString(1, ss.getSessionUser());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullname = rs.getString("first_name") + " " + rs.getString("last_name");
                return fullname;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void upDateAccount(String amountString) {
        float amount = Float.parseFloat(amountString);
        Connection conn = null;
        PreparedStatement stmt = null;
        Session ss = new Session();
        ss.getSessionUser();
        JFrame f = new JFrame();
        try {
            String url = "jdbc:sqlite:meal.db";
            conn = DriverManager.getConnection(url);
            String query = "UPDATE user_details SET total_amount=? WHERE username=?";
            stmt = conn.prepareStatement(query);
            stmt.setFloat(1, amount);
            stmt.setString(2, ss.getSessionUser());
            int result = stmt.executeUpdate();
            stmt.close();
            conn.close();
            System.out.println(result);
            if (result == 1) {
                JOptionPane.showMessageDialog(f, "Update data successfully");
            } else {
                JOptionPane.showMessageDialog(f, "Can't update the data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void personalUpdate(String amountString) {
        float amount = Float.parseFloat(amountString);
        Connection conn = null;
        PreparedStatement stmt = null;
        Session ss = new Session();
        ss.getSessionUser();
        JFrame f = new JFrame();
        try {
            String url = "jdbc:sqlite:meal.db";
            conn = DriverManager.getConnection(url);
            String updateQuery = "UPDATE user_details SET total_amount=total_amount+? WHERE username=?";
            stmt = conn.prepareStatement(updateQuery);
            stmt.setFloat(1, amount);
            stmt.setString(2, ss.getSessionUser());
            int result = stmt.executeUpdate();
            stmt.close();
            conn.close();
            System.out.println(result);
            if (result == 1) {
                JOptionPane.showMessageDialog(f, "Update data successfully");
            } else {
                JOptionPane.showMessageDialog(f, "Can't update the data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAmount(String amountString) {
        float amount = Float.parseFloat(amountString);
        Connection conn = null;
        PreparedStatement stmt = null;
        Session ss = new Session();
        ss.getSessionUser();
        JFrame f = new JFrame();
        try {
            String url = "jdbc:sqlite:meal.db";
            conn = DriverManager.getConnection(url);
            String countQuery = "SELECT COUNT(*) AS total_users FROM user_details";
            stmt = conn.prepareStatement(countQuery);
            ResultSet countRs = stmt.executeQuery();
            int totalUsers = countRs.getInt("total_users");
            countRs.close();
            stmt.close();
            float amountPerUser = amount / totalUsers;
            String updateQuery = "UPDATE user_details SET total_amount = total_amount - ?";
            stmt = conn.prepareStatement(updateQuery);
            stmt.setFloat(1, amountPerUser);
            int result = stmt.executeUpdate();
            stmt.close();
            conn.close();
            System.out.println(result + " rows updated.");
            JOptionPane.showMessageDialog(f, "Update data successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
