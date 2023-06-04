
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JFrame;

// import java.sql.ResultSet;
import java.sql.SQLException;

class Connect {
    public void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:meal.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            // ResultSet resultSet = statement.executeQuery("select * from auth");
            // while (resultSet.next()) {
            // String value = resultSet.getString("first_name");
            // System.out.println(value);
            // }
            System.out.println("Connection to SQLite has been established.");
            // resultSet.close();
            statement.close();

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
        try {
            // db parameters
            String url = "jdbc:sqlite:meal.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            // ResultSet resultSet = statement.executeQuery("select * from auth");
            // while (resultSet.next()) {
            // String value = resultSet.getString("first_name");
            // System.out.println(value);
            // }
            System.out.println(username);
            statement.executeUpdate("insert into auth(username,first_name,last_name,password) values ('" + username
                    + "','" + firstname + "','" + lastname + "','" + password + "')");
            System.out.println("Connection to SQLite has been established.");
            // resultSet.close();
            statement.close();

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

    public void connect(String username, String password, JFrame parentFrame) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // db parameters
            String url = "jdbc:sqlite:meal.db";
            // create a connection to the database
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
                new Session(username);
                dash.setVisible(true);
                parentFrame.dispose();

            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception e) {
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
            String query = "select * from auth";
            conn = DriverManager.getConnection(url);
            stmt = conn.prepareStatement(query);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                JSONObject json = new JSONObject();
                json.put("first_name", fname);
                json.put("last_name", lname);
                jsonArray.put(json);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (Exception e) {
            }

        }
        return jsonArray;

    }
}