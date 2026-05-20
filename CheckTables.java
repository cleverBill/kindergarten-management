import java.sql.*;

public class CheckTables {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "123456";
            
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            
            System.out.println("=== 数据库中的表 ===");
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            while (rs.next()) {
                System.out.println("- " + rs.getString(1));
            }
            
            System.out.println("\n=== students表结构 ===");
            try {
                ResultSet rs2 = stmt.executeQuery("DESCRIBE students");
                while (rs2.next()) {
                    System.out.println(rs2.getString(1) + " - " + rs2.getString(2));
                }
            } catch (Exception e) {
                System.out.println("students表不存在或查询失败: " + e.getMessage());
            }
            
            System.out.println("\n=== students表数据 ===");
            try {
                ResultSet rs3 = stmt.executeQuery("SELECT * FROM students");
                ResultSetMetaData meta = rs3.getMetaData();
                int columnCount = meta.getColumnCount();
                
                // 打印列名
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(meta.getColumnName(i) + "\t");
                }
                System.out.println("\n" + "=".repeat(80));
                
                // 打印数据
                while (rs3.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(rs3.getString(i) + "\t");
                    }
                    System.out.println();
                }
            } catch (Exception e) {
                System.out.println("查询students表数据失败: " + e.getMessage());
            }
            
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
