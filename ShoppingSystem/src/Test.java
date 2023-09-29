import GUI.StartMenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {

    // 数据库连接信息
    public static String url = "jdbc:mysql://localhost:3306/shopping_system"; // 数据库的URL
    public static String username = "root"; // 数据库用户名
    public static String password = "z1751374435"; // 数据库密码

    public static void main(String[] args) {

        try {
            // 建立数据库连接
            Connection connection = DriverManager.getConnection(url, username, password); // 创建数据库连接

            // 进入商城页面
            while (true) {
                try {
                    StartMenu sm = new StartMenu();
                    sm.start();
                    break;
                } catch (Exception e) {
                    System.out.println("请您输入合法的操作！系统已退出至初始界面！");
                }
            }

            connection.close(); // 关闭数据库连接
        } catch (SQLException e) {
            System.out.println("连接出错！");
            e.printStackTrace(); // 处理SQL异常
        }
    }
}


