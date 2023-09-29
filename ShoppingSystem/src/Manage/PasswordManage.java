package Manage;

import DatabaseInformation.Information;
import Model.Admin;
import Model.User;
import Regex.Regex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PasswordManage {
    Scanner sc = new Scanner(System.in);
    private String url = Information.url;
    private String username = Information.username;
    private String password = Information.password;

    public void changePassword(Admin admin) {
        System.out.println("==密码修改操作==");

        System.out.println("请您输入当前账户的密码：");
        String oldPassword = sc.next();

        if (oldPassword.equals(admin.getPassword())) {
            System.out.println("请输入您的新密码：");
            String newPassword = sc.next();

            System.out.println("请确认您的新密码：");
            String confirmation = sc.next();

            if (newPassword.equals(confirmation)) {
                try (Connection connection = DriverManager.getConnection(url, username, password)) {

                    String updateQuery = "UPDATE tb_admin SET password = ? WHERE name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                    preparedStatement.setString(1, newPassword);
                    preparedStatement.setString(2, admin.getUserName());
                    admin.setPassword(newPassword);

                    preparedStatement.executeUpdate(); // 执行更新操作并获取影响的行数

                    System.out.println("您的密码修改成功！"); // 输出更新成功的行数
                } catch (SQLException e) {
                    System.out.println("密码修改失败！");
                }
            } else {
                System.out.println("您两次输入的密码不一致，请重新操作！");
            }
        } else {
            System.out.println("您的密码输入错误！");
        }
    }

    public void changePassword(User user) throws SQLException {
        System.out.println("==密码修改操作==");

        System.out.println("请您输入当前账户的密码：");
        String oldPassword = sc.next();

        if (oldPassword.equals(user.getPassword())) {
            System.out.println("请输入您的新密码：");
            String newPassword = sc.next();

            System.out.println("请确认您的新密码：");
            String confirmation = sc.next();

            if (newPassword.equals(confirmation) && Regex.isValidPassword(newPassword)) {
                try (Connection connection = DriverManager.getConnection(url, username, password)) {

                    String updateQuery = "UPDATE tb_user SET password = ? WHERE name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                    preparedStatement.setString(1, newPassword);
                    preparedStatement.setString(2, user.getUserName());
                    user.setPassword(newPassword);

                    preparedStatement.executeUpdate(); // 执行更新操作并获取影响的行数

                    System.out.println("您的密码修改成功！"); // 输出更新成功的行数
                } catch (SQLException e) {
                    System.out.println("密码修改失败！");
                }
            } else {
                System.out.println("您两次输入的密码不一致或密码不符合规范，请重新操作！");
            }
        } else {
            System.out.println("您的密码输入错误！");
        }
    }


    public void resetPassword(User user) {
        System.out.println("==密码重置操作==");

        System.out.println("您确定要重置密码吗？输入 yes 表示确定！");
        String confirmation = sc.next();
        if (confirmation.equals("yes")) {

            try (Connection connection = DriverManager.getConnection(url, username, password)) {

                String updateQuery = "UPDATE tb_user SET password = ? WHERE name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                preparedStatement.setString(1, "resetPassword@3");
                preparedStatement.setString(2, user.getUserName());
                user.setPassword("resetPassword@3");

                preparedStatement.executeUpdate(); // 执行更新操作并获取影响的行数

                System.out.println("您的密码重置成功！重置后的密码为 resetPassword@3"); // 输出更新成功的行数
            } catch (SQLException e) {
                System.out.println("密码重置失败！");
            }
        } else {
            System.out.println("您已取消操作！");
        }

    }

    public void resetUserPassword() {
        System.out.println("==密码重置操作==");

        System.out.println("请输入您要重置密码的用户Id：");
        int id = sc.nextInt();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String updateQuery = "UPDATE tb_user SET password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, "123456789@Aa");
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("您已成功重置该用户的密码！"); // 输出更新成功的行数
            } else {
                System.out.println("重置密码失败，请输入正确的用户Id！");
            }
        } catch (SQLException e) {
            System.out.println("重置密码失败！");
        }
    }
}


