package Manage;

import DatabaseInformation.Information;

import java.sql.*;
import java.util.Scanner;

public class UserManage {
    Scanner sc = new Scanner(System.in);
    private String url = Information.url;
    private String username = Information.username;
    private String password = Information.password;

    public void showAllUsers() throws SQLException {

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String selectAllQuery = "SELECT * FROM tb_user";
            PreparedStatement selectAllStatement = connection.prepareStatement(selectAllQuery);

            ResultSet resultSet = selectAllStatement.executeQuery();
            if (!isDatabaseEmpty(connection)) {
                System.out.println("以下是所有用户信息：");
                while (resultSet.next()) {
                    showAllU(resultSet);
                }
            } else {
                System.out.println("目前无用户信息！");
            }
        }
    }

    public void deleteUser() {

        System.out.println("请输入您想要删除用户的Id：");
        String targetId = sc.next();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (queryDataById(connection, targetId)) {
                System.out.println("您确定要删除该用户吗？输入‘yes’表示确定，输入其他字符表示取消！");
                String confirmation = sc.next();
                if (confirmation.equals("yes")) {

                    String deleteQuery = "DELETE FROM tb_user WHERE id  = ?"; // 删除数据的SQL语句
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery); // 创建PreparedStatement对象
                    deleteStatement.setString(1, targetId);
                    int rowsDeleted = deleteStatement.executeUpdate(); // 执行删除操作并获取影响的行数
                    System.out.println(rowsDeleted + " 行用户数据删除成功。"); // 输出删除成功的行数
                } else {
                    System.out.println("删除操作已取消！");
                }
            }
        } catch (SQLException e) {
            System.out.println("删除用户数据失败！");
            e.printStackTrace();
        }
    }

    public void queryUser() {
        System.out.println("请输入您想要查询用户的Id或用户名：");
        String targetId = sc.next();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (queryDataById(connection, targetId)) {
                System.out.println("该用户的信息如上所示！");
            }
        } catch (SQLException e) {
            System.out.println("查询用户数据失败！");
        }
    }

    public boolean queryDataById(Connection connection, String targetId) throws SQLException {
        String selectByIdQuery = "SELECT * FROM tb_user WHERE id = ? or name = ?";
        try (PreparedStatement selectByIdStatement = connection.prepareStatement(selectByIdQuery)) {
            selectByIdStatement.setString(1, targetId);
            selectByIdStatement.setString(2, targetId);
            ResultSet resultSet = selectByIdStatement.executeQuery();
            if (resultSet.next()) {
                showAllU(resultSet);
                return true;
            } else {
                System.out.println("未找到ID为 " + targetId + " 的用户！");
                return false;
            }
        }
    }

    public void showAllU(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int level = resultSet.getInt("level");
            String consumerMoney = resultSet.getString("consume_money");
            String phoneNumber = resultSet.getString("phone_number");
            String email = resultSet.getString("email");
            String createTime = resultSet.getString("create_time");

            String newLevel;

            if (level == 1) {
                newLevel = "铜牌用户";
            } else if (level == 2) {
                newLevel = "银牌用户";
            } else {
                newLevel = "金牌用户";
            }

            System.out.println("用户id: " + id + "\t用户名: " + name + "\t\t用户级别: " + newLevel +
                    "\t累计消费总额: " + consumerMoney + "\t\t手机号: " + phoneNumber + "\t\t邮箱: " + email +
                    "\t\t注册时间: " + createTime);
        } catch (SQLException e) {
            System.out.println("数据展示失败！");
        }
    }

    private boolean isDatabaseEmpty(Connection connection) throws SQLException {
        String countTablesQuery = "SELECT COUNT(*) FROM tb_user";
        try (PreparedStatement preparedStatement = connection.prepareStatement(countTablesQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int rowCount = resultSet.getInt(1);
                return rowCount == 0; // 如果行数0，数据库为空
            }
        }
        return false; // 如果未能检索到表的数量或发生错误，默认返回false
    }
}
