import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DropDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/?user=root&password=123456";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP DATABASE IF EXISTS student_management");
            System.out.println("Database deleted successfully!");
            stmt.execute("CREATE DATABASE student_management");
            System.out.println("Database created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
