package Manage;

import DatabaseInformation.Information;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class CommodityManage {
    Scanner sc = new Scanner(System.in);
    private String url = Information.url;
    private String username = Information.username;
    private String password = Information.password;

    public void showAllCommodities() throws SQLException {

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String selectAllQuery = "SELECT * FROM tb_commodity";
            PreparedStatement selectAllStatement = connection.prepareStatement(selectAllQuery);

            ResultSet resultSet = selectAllStatement.executeQuery();
            if (!isDatabaseEmpty(connection)) {
                System.out.println("以下是所有商品信息：");
                while (resultSet.next()) {
                    showAllC(resultSet);
                }
            } else {
                System.out.println("目前商城里面无商品信息！");
            }
        }
    }

    public void insertCommodity() {
        Scanner sc = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("请输入添加商品的以下信息：");

            String insertQuery = "INSERT INTO tb_commodity (id, name, manufacturer, date_of_manufacturer, " +
                    "type, buying_price, selling_price, number, create_time, update_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // 插入数据的SQL语句
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery); // 创建PreparedStatement对象

            System.out.println("商品Id：");
            int id = sc.nextInt();
            insertStatement.setInt(1, id);

            System.out.println("商品名称：");
            String name = sc.next();
            insertStatement.setString(2, name);

            System.out.println("生产厂家：");
            String manufacturer = sc.next();
            insertStatement.setString(3, manufacturer);

            System.out.println("生产日期（请按照 年 月 日 的方式输入）：");
            LocalDate ld = LocalDate.of(sc.nextInt(), sc.nextInt(), sc.nextInt());
            insertStatement.setString(4, String.valueOf(ld));

            System.out.println("类型：");
            String type = sc.next();
            insertStatement.setString(5, type);

            System.out.println("进货价：");
            double buyingPrice = sc.nextDouble();
            insertStatement.setDouble(6, buyingPrice);

            System.out.println("零售价格：");
            double sellingPrice = sc.nextDouble();
            insertStatement.setDouble(7, sellingPrice);

            System.out.println("数量：");
            int number = sc.nextInt();
            insertStatement.setInt(8, number);

            insertStatement.setString(9, String.valueOf(LocalDateTime.now()));
            insertStatement.setString(10, String.valueOf(LocalDateTime.now()));

            int rowsInserted = insertStatement.executeUpdate(); // 执行插入操作并获取影响的行数
            System.out.println(rowsInserted + " 行商品数据插入成功。"); // 输出插入成功的行数
        } catch (SQLException e) {
            System.out.println("插入商品数据失败！");
        }
    }

    public void deleteCommodity() {

        System.out.println("请输入您想要删除商品的Id：");
        int id = sc.nextInt();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (queryDataById(connection, id)) {
                System.out.println("您确定要删除该商品吗？输入‘yes’表示确定，输入其他字符表示取消！");
                String confirmation = sc.next();
                if (confirmation.equals("yes")) {
                    System.out.println("商品已成功删除！");

                    String deleteQuery = "DELETE FROM tb_commodity WHERE id = ?"; // 删除数据的SQL语句
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery); // 创建PreparedStatement对象
                    deleteStatement.setInt(1, id); // 设置参数的值
                    int rowsDeleted = deleteStatement.executeUpdate(); // 执行删除操作并获取影响的行数
                    System.out.println(rowsDeleted + " 行商品数据删除成功。"); // 输出删除成功的行数
                } else {
                    System.out.println("删除操作已取消！");
                }
            }
        } catch (SQLException e) {
            System.out.println("删除商品数据失败！");
        }
    }

    // 根据Id查找商品
    public boolean queryDataById(Connection connection, int targetId) throws SQLException {
        String selectByIdQuery = "SELECT * FROM tb_commodity WHERE id = ?";
        try (PreparedStatement selectByIdStatement = connection.prepareStatement(selectByIdQuery)) {
            selectByIdStatement.setInt(1, targetId);
            ResultSet resultSet = selectByIdStatement.executeQuery();
            if (resultSet.next()) {
                showAllC(resultSet);
                return true;
            } else {
                System.out.println("未找到ID为 " + targetId + " 的商品！");
                return false;
            }
        }
    }

    // 更新商品数据
    public void updateCommodity() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入您要修改的商品的Id：");
        int id = sc.nextInt();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (!isDatabaseEmpty(connection)) {
                if (queryDataById(connection, id)) {

                    String updateQuery = "UPDATE tb_commodity SET name = ?, manufacturer = ?, date_of_manufacturer = ?, " +
                            "type = ?, buying_price = ?, selling_price = ?, number = ?, update_time = ? " +
                            "WHERE id = ?"; // 更新数据的SQL语句

                    // 创建PreparedStatement对象
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    System.out.println("商品名称：");
                    String name = sc.next();
                    updateStatement.setString(1, name);

                    System.out.println("生产厂家：");
                    String manufacturer = sc.next();
                    updateStatement.setString(2, manufacturer);

                    System.out.println("生产日期（请按照 年 月 日 的方式输入）：");
                    LocalDate ld = LocalDate.of(sc.nextInt(), sc.nextInt(), sc.nextInt());
                    updateStatement.setString(3, String.valueOf(ld));

                    System.out.println("类型：");
                    String type = sc.next();
                    updateStatement.setString(4, type);

                    System.out.println("进货价：");
                    double buyingPrice = sc.nextDouble();
                    updateStatement.setDouble(5, buyingPrice);

                    System.out.println("零售价格：");
                    double sellingPrice = sc.nextDouble();
                    updateStatement.setDouble(6, sellingPrice);

                    System.out.println("数量：");
                    int number = sc.nextInt();
                    updateStatement.setInt(7, number);

                    updateStatement.setString(8, String.valueOf(LocalDateTime.now()));

                    updateStatement.setInt(9, id);

                    int rowsUpdated = updateStatement.executeUpdate(); // 执行更新操作并获取影响的行数
                    System.out.println(rowsUpdated + " 行商品数据更新成功！"); // 输出更新成功的行数

                }
            } else {
                System.out.println("目前无商品数据！");
            }
        } catch (SQLException e) {
            System.out.println("更新数据失败！");
        }
    }


    // 展示所有商品数据
    public void showAllC(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String manufacturer = resultSet.getString("manufacturer");
            String date_of_manufacturer = resultSet.getString("date_of_manufacturer");
            String type = resultSet.getString("type");
            String buying_price = resultSet.getString("buying_price");
            String selling_price = resultSet.getString("selling_price");
            String number = resultSet.getString("number");
            String create_time = resultSet.getString("create_time");
            String update_time = resultSet.getString("update_time");

            System.out.println("商品id: " + id + ", 商品名称: " + name + ", 生产厂家: " + manufacturer +
                    ", 生产日期: " + date_of_manufacturer + ", 商品类型: " + type + ", 进货价: " + buying_price +
                    ", 零售价: " + selling_price + ", 商品数量: " + number + ", 创建时间: " + create_time +
                    "， 更新时间：" + update_time);
        } catch (SQLException e) {
            System.out.println("数据展示失败！");
        }
    }


    // 判断数据库是否为空
    private boolean isDatabaseEmpty(Connection connection) throws SQLException {
        String countTablesQuery = "SELECT COUNT(*) FROM tb_commodity";
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
