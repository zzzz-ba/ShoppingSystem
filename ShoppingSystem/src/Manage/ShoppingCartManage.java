package Manage;

import DatabaseInformation.Information;
import Model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ShoppingCartManage {
    Scanner sc = new Scanner(System.in);
    private String url = Information.url;
    private String username = Information.username;
    private String password = Information.password;
    public void insertToShoppingCart(User user) {
        System.out.println("请输入您要购买的商品的Id：");
        int id = sc.nextInt();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // 查询商品表以获取库存信息
            String selectProductQuery = "SELECT number FROM tb_commodity WHERE id = ?";
            PreparedStatement selectProductStatement = connection.prepareStatement(selectProductQuery);
            selectProductStatement.setInt(1, id);
            ResultSet productResult = selectProductStatement.executeQuery();

            if (productResult.next()) {

                System.out.println("请输入您要购买该商品的数量：");
                int quantity = sc.nextInt();

                int availableStock = productResult.getInt("number");
                if (availableStock >= quantity) {
                    // 创建SQL语句，插入购物车记录
                    String insertQuery = "INSERT INTO tb_shoppingcart (user_id, commodity_id, quantity, create_time, update_time) " +
                            "VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setInt(1, user.getUserId());
                    preparedStatement.setInt(2, id);
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.setString(4, String.valueOf(LocalDateTime.now()));
                    preparedStatement.setString(5, String.valueOf(LocalDateTime.now()));

                    // 执行SQL语句
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("商品已成功添加到购物车！");
                    } else {
                        System.out.println("添加商品到购物车失败！");
                    }
                } else {
                    System.out.println("库存不足，无法添加商品到购物车！");
                }
            } else {
                System.out.println("商品不存在！");
            }
        } catch (SQLException e) {
            System.out.println("出错了！");
            e.printStackTrace();
        }
    }

    public void deleteFromShoppingCart(User user) {
        System.out.println("请输入要移除的商品的ID：");
        int id = sc.nextInt();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // 检查购物车中是否存在要移除的商品
            String checkCartItemQuery = "SELECT * FROM tb_shoppingcart WHERE user_id = ? AND commodity_id = ?";
            PreparedStatement checkCartItemStatement = connection.prepareStatement(checkCartItemQuery);
            checkCartItemStatement.setInt(1, user.getUserId());
            checkCartItemStatement.setInt(2, id);
            ResultSet cartItemResult = checkCartItemStatement.executeQuery();

            if (cartItemResult.next()) {
                System.out.println("您确定要移除该商品吗？输入 yes 表示确认：");
                String confirmation = sc.next();

                if (confirmation.equals("yes")) {
                    // 商品存在于购物车中，可以移除
                    String removeItemQuery = "DELETE FROM tb_shoppingcart WHERE user_id = ? AND commodity_id = ?";
                    PreparedStatement removeItemStatement = connection.prepareStatement(removeItemQuery);
                    removeItemStatement.setInt(1, user.getUserId());
                    removeItemStatement.setInt(2, id);

                    // 执行SQL语句
                    int rowsDeleted = removeItemStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("商品已成功从购物车中移除！");
                    } else {
                        System.out.println("移除商品失败！");
                    }
                } else {
                    System.out.println("您已经取消操作！");
                }
            } else {
                System.out.println("购物车中不存在该商品！");
            }
        } catch (SQLException e) {
            System.out.println("出错了！");
            e.printStackTrace();
        }
    }

    public void updateInShoppingCart(User user) {
        System.out.println("请输入要修改购买数量的商品的Id：");
        int commodityId = sc.nextInt();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // 检查购物车中是否存在要修改的商品
            String checkCartItemQuery = "SELECT * FROM tb_shoppingcart WHERE user_id = ? AND commodity_id = ?";
            PreparedStatement checkCartItemStatement = connection.prepareStatement(checkCartItemQuery);
            checkCartItemStatement.setInt(1, user.getUserId());
            checkCartItemStatement.setInt(2, commodityId);
            ResultSet cartItemResult = checkCartItemStatement.executeQuery();

            if (cartItemResult.next()) {
                // 商品存在于购物车中，可以修改购买数量
                System.out.println("请输入新的购买数量：");
                int newQuantity = sc.nextInt();

                // 查询商品表以获取库存信息
                String selectProductQuery = "SELECT number FROM tb_commodity WHERE id = ?";
                PreparedStatement selectProductStatement = connection.prepareStatement(selectProductQuery);
                selectProductStatement.setInt(1, commodityId);
                ResultSet productResult = selectProductStatement.executeQuery();

                if (newQuantity <= 0) {
                    String removeItemQuery = "DELETE FROM tb_shoppingcart WHERE user_id = ? AND commodity_id = ?";
                    PreparedStatement removeItemStatement = connection.prepareStatement(removeItemQuery);
                    removeItemStatement.setInt(1, user.getUserId());
                    removeItemStatement.setInt(2, commodityId);

                    // 执行SQL语句
                    int rowsDeleted = removeItemStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("商品已成功从购物车中移除！");
                    } else {
                        System.out.println("移除商品失败！");
                    }
                } else if (productResult.next()) {
                    int availableStock = productResult.getInt("number");

                    if (availableStock >= newQuantity) {
                        String updateQuantityQuery = "UPDATE tb_shoppingcart SET quantity = ?, update_time = ? WHERE user_id = ?";
                        PreparedStatement updateQuantityStatement = connection.prepareStatement(updateQuantityQuery);
                        updateQuantityStatement.setInt(1, newQuantity);
                        updateQuantityStatement.setString(2, String.valueOf(LocalDateTime.now()));
                        updateQuantityStatement.setInt(3, user.getUserId());

                        // 执行SQL语句
                        int rowsUpdated = updateQuantityStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("购买数量已成功修改！");
                        } else {
                            System.out.println("修改购买数量失败！");
                        }
                    } else {
                        System.out.println("目前商城里只有" + availableStock + "件该商品，请重新输入！");
                    }
                }
            } else {
                System.out.println("购物车中不存在该商品！");
            }
        } catch (SQLException e) {
            System.out.println("出错了！");
            e.printStackTrace();
        }
    }

    public void checkoutShoppingCart(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.setAutoCommit(false); // 关闭自动提交事务

            // 查询用户的购物车内容
            String selectCartItemsQuery = "SELECT sc.commodity_id, sc.quantity, c.selling_price FROM tb_shoppingcart sc " +
                    "JOIN tb_commodity c ON sc.commodity_id = c.id WHERE user_id = ?";
            PreparedStatement selectCartItemsStatement = connection.prepareStatement(selectCartItemsQuery);
            selectCartItemsStatement.setInt(1, user.getUserId());
            ResultSet cartItemsResult = selectCartItemsStatement.executeQuery();

            double totalAmount = 0;

            while (cartItemsResult.next()) {
                int commodityId = cartItemsResult.getInt("commodity_id");
                int quantity = cartItemsResult.getInt("quantity");
                double price = cartItemsResult.getDouble("selling_price");

                // 计算每个商品的总价
                double itemTotal = price * quantity;

                System.out.println("商品ID: " + commodityId);
                System.out.println("购买数量: " + quantity);
                System.out.println("商品总价: " + itemTotal);

                System.out.println("是否支付该商品？请输入 'yes' 表示支付，其他字符表示取消支付：");
                String checkout = sc.next();

                // 更新商品库存
                if (checkout.equals("yes")) {
                    System.out.println("恭喜您支付成功！");

                    totalAmount += itemTotal;

                    // 更新商城记录
                    String updateStockQuery = "UPDATE tb_commodity SET number = number - ? WHERE id = ?";
                    PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery);
                    updateStockStatement.setInt(1, quantity);
                    updateStockStatement.setInt(2, commodityId);
                    updateStockStatement.executeUpdate();

                    // 从购物车中删除该商品记录
                    String deleteCartItemQuery = "DELETE FROM tb_shoppingcart WHERE user_id = ? AND commodity_id = ?";
                    PreparedStatement deleteCartItemStatement = connection.prepareStatement(deleteCartItemQuery);
                    deleteCartItemStatement.setInt(1, user.getUserId());
                    deleteCartItemStatement.setInt(2, commodityId);
                    deleteCartItemStatement.executeUpdate();

                    // 记录购买记录
                    String insertQuery = "INSERT INTO tb_shoppinghistory (user_id, commodity_id, quantity, create_time) " +
                            "VALUES (?, ?, ?, ?)"; // 插入数据的SQL语句

                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery); // 创建PreparedStatement对象

                    insertStatement.setInt(1, user.getUserId());
                    insertStatement.setInt(2, commodityId);
                    insertStatement.setInt(3, quantity);
                    insertStatement.setString(4, String.valueOf(LocalDateTime.now()));

                    insertStatement.executeUpdate();

                } else {
                    System.out.println("商品支付已取消！");
                }
            }

            // 更新用户累计消费金额
            String updateConsumerMoneyQuery = "UPDATE tb_user SET consume_money = consume_money + ? WHERE id = ?";
            PreparedStatement updateConsumerMoneyStatement = connection.prepareStatement(updateConsumerMoneyQuery);
            updateConsumerMoneyStatement.setDouble(1, totalAmount);
            updateConsumerMoneyStatement.setInt(2, user.getUserId());
            updateConsumerMoneyStatement.executeUpdate();
            user.setConsumeMoney(totalAmount + user.getConsumeMoney());

            System.out.println("您当前的累计消费金额为：" + (user.getConsumeMoney()) + "元！");

            // 更新用户等级
            String updateLevelQuery = "UPDATE tb_user SET level = ? WHERE id = ?";
            PreparedStatement updateLevelStatement = connection.prepareStatement(updateLevelQuery);

            if ((user.getConsumeMoney()) >= 500 && (user.getConsumeMoney()) < 1000) {
                updateLevelStatement.setInt(1, 2);
                updateLevelStatement.setInt(2, user.getUserId());
                updateLevelStatement.executeUpdate();
                System.out.println("您目前的用户级别为银牌用户！");
            } else if ((user.getConsumeMoney()) >= 1000) {
                updateLevelStatement.setInt(1, 3);
                updateLevelStatement.setInt(2, user.getUserId());
                updateLevelStatement.executeUpdate();
                System.out.println("您目前的用户级别为金牌用户！");
            } else {
                System.out.println("您目前的用户级别为铜牌用户！");
            }

            connection.commit(); // 提交事务
        } catch (SQLException e) {
            System.out.println("出错了！");
            e.printStackTrace();
        }
    }

    public void showShoppingHistory(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // 查询用户的购物历史记录
            String selectHistoryQuery = "SELECT sh.create_time, c.name AS commodity_name, sh.quantity " +
                    "FROM tb_shoppinghistory sh " +
                    "JOIN tb_commodity c ON sh.commodity_id = c.id " +
                    "WHERE sh.user_id = ?";
            PreparedStatement selectHistoryStatement = connection.prepareStatement(selectHistoryQuery);
            selectHistoryStatement.setInt(1, user.getUserId());
            ResultSet historyResult = selectHistoryStatement.executeQuery();

            if (!historyResult.isBeforeFirst()) {
                System.out.println("您目前还没有购物历史记录！");
            } else {
                System.out.println("以下是您的购物历史记录：");
                System.out.println("---------------------------------------");
                System.out.println("购买时间\t\t\t\t商品名称\t购买数量");
                System.out.println("---------------------------------------");

                while (historyResult.next()) {
                    String createTime = historyResult.getString("create_time");
                    String commodityName = historyResult.getString("commodity_name");
                    int quantity = historyResult.getInt("quantity");

                    System.out.println(createTime + "\t" + commodityName + "\t" + quantity);
                }
            }
        } catch (SQLException e) {
            System.out.println("出错了！");
            e.printStackTrace();
        }
    }

    public void showAllCommoditiesToUser() throws SQLException {

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String selectAllQuery = "SELECT * FROM tb_commodity";
            PreparedStatement selectAllStatement = connection.prepareStatement(selectAllQuery);

            ResultSet resultSet = selectAllStatement.executeQuery();
                System.out.println("以下是所有商品信息，欢迎您尽情选购！");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String manufacturer = resultSet.getString("manufacturer");
                    String date_of_manufacturer = resultSet.getString("date_of_manufacturer");
                    String type = resultSet.getString("type");
                    String selling_price = resultSet.getString("selling_price");
                    String number = resultSet.getString("number");

                    System.out.println("商品id: " + id + "\t商品名称: " + name + "\t生产厂家: " + manufacturer + "\t\t生产日期: "
                            + date_of_manufacturer + "\t商品类型: " + type + "\t零售价: " + selling_price + "\t商品数量: " + number );
                }
        }
    }
}
