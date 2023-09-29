package Manage;

import DatabaseInformation.Information;
import Model.Admin;
import Model.User;
import Regex.Regex;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;


public class LoginInManage {
    Scanner sc = new Scanner(System.in);
    // StartMenu sm = new StartMenu();
    private String url = Information.url;
    private String username = Information.username;
    private String password = Information.password;

    public void register() {

        System.out.println("==用户注册界面==");
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String insertQuery = "INSERT INTO tb_user (name, password, phone_number, email, create_time, update_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?)"; // 插入用户的SQL查询
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);


            System.out.println("请输入您的用户名：");
            String name = sc.next();
            if (name.length() < 5) {
                System.out.println("您输入的用户名不符合规范，请重新输入！");
            } else {
                System.out.println("请输入您的密码：");
                String password = sc.next();

                System.out.println("请确认您的密码：");
                String confirmationPassword = sc.next();
                if (confirmationPassword.equals(password) && Regex.isValidPassword(password)) {

                    System.out.println("请输入您的手机号：");
                    String phoneNumber = sc.next();

                    System.out.println("请输入您的邮箱：");
                    String email = sc.next();

                    if (Regex.isValidEmail(email) && Regex.isValidPhoneNumber(phoneNumber)) {
                        preparedStatement.setString(1, name); // 设置用户名
                        preparedStatement.setString(2, password); // 设置密码
                        preparedStatement.setString(3, phoneNumber); // 设置手机号
                        preparedStatement.setString(4, email); // 设置邮箱
                        preparedStatement.setString(5, String.valueOf(LocalDateTime.now())); // 设置密码
                        preparedStatement.setString(6, String.valueOf(LocalDateTime.now())); // 设置密码

                        preparedStatement.executeUpdate(); // 执行插入操作
                        System.out.println("恭喜您，你的账户" + name + "注册成功！");
                    } else {
                        System.out.println("您的某一部分信息输入不规范，请重新输入！");
                    }
                } else {
                    System.out.println("您两次输入的密码不一致或密码不符合规范，请重新操作！");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("注册失败！");
        }
    }


    public Admin loginOfAdministrator() {
        System.out.println("==管理员系统登录==");
        Admin admin = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String selectQuery = "SELECT * FROM tb_admin WHERE name = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            int count = 1;

            while (true) {
                System.out.println("请输入用户名：");
                String name = sc.next();
                preparedStatement.setString(1, name);

                System.out.println("请输入密码：");
                String password = sc.next();
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    admin = new Admin();
                    admin.setUserName(resultSet.getString("name"));
                    admin.setPassword(resultSet.getString("password"));
                    // 设置其他用户信息字段

                    System.out.println("恭喜您成功登录系统！");
                    break;

                } else {
                    if (count == 5) {
                        System.out.println("您登录失败的次数过多，登录自动退出！");
                        break;
                    }
                    System.out.println("您输入的账号数据有误，请重新输入！");
                    count++;
                }
            }

        } catch (SQLException e) {
            System.out.println("出错了！");
            e.printStackTrace();
        }

        return admin;
    }


    public User loginOfUser() {
        System.out.println("==用户系统登录==");
        User user = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String selectQuery = "SELECT * FROM tb_user WHERE name = ? AND password = ? AND block = 0";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            int count = 1;

            while (true) {
                System.out.println("请输入用户名：");
                String name = sc.next();
                preparedStatement.setString(1, name);

                System.out.println("请输入密码：");
                String password = sc.next();
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    user = new User();
                    user.setUserId(resultSet.getInt("id"));
                    user.setUserName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                    user.setConsumeMoney(resultSet.getDouble("consume_money"));
                    // 设置其他用户信息字段

                    System.out.println("恭喜您成功登录系统！");
                    break;

                } else {
                    if (count == 5) {
                        // 如果登录失败五次，则锁定用户账户
                        try {
                            String updateBlockQuery = "UPDATE tb_user SET block = 1 WHERE name = ?";
                            PreparedStatement updateBlockStatement = connection.prepareStatement(updateBlockQuery);
                            updateBlockStatement.setString(1, name);
                            updateBlockStatement.executeUpdate();
                            System.out.println("您登录失败的次数过多，登录自动退出并锁定账户！");
                        } catch (SQLException e) {
                            System.out.println("锁定用户账户失败！");
                            e.printStackTrace();
                        }
                        break;
                    }
                    System.out.println("您输入的账号数据有误或账号已被锁定，请重新输入！");
                    count++;
                }
            }

        } catch (SQLException e) {
            System.out.println("出错了！");
            e.printStackTrace();
        }

        return user;
    }
}

