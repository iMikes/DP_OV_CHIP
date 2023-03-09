import java.sql.*;

public class Main {
    private Connection conn;

    public void connect(String url, String user, String password) throws SQLException {
        conn = DriverManager.getConnection(url, user, password);
    }

    public void disconnect() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public void printReizigers() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM reiziger");

        System.out.println("Alle reizigers:");

        int count = 1;
        while (resultSet.next()) {
            String initials = resultSet.getString("voorletters");
            String infix = resultSet.getString("tussenvoegsel");
            String lastName = resultSet.getString("achternaam");
            Date birthdate = resultSet.getDate("geboortedatum");

            String fullName = initials + " " + infix + " " + lastName;

            System.out.printf("#%d: %s (%s)\n", count, fullName, birthdate);
            count++;
        }

        statement.close();
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String user = "postgres";
        String password = "root";

        Main app = new Main();

        try {
            app.connect(url, user, password);
            app.printReizigers();
            app.disconnect();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
