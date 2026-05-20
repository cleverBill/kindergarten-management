import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "123456";
            
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            System.out.println("数据库中的表:");
            while (rs.next()) {
                System.out.println("- " + rs.getString(1));
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
