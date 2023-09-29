package GUI;

import Manage.*;
import Model.Admin;
import Model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class StartMenu {
    private Scanner sc = new Scanner(System.in);
    LoginInManage lim = new LoginInManage();
    public void start() throws SQLException {

        while (true) {
            System.out.println("===欢迎您进入到了ynu购物系统===");
            System.out.println("1、管理员登录");
            System.out.println("2、用户登录");
            System.out.println("3、用户注册");
            System.out.println("4、退出系统");
            System.out.println("请选择：");

            int command = sc.nextInt();

            switch (command) {
                case 1:
                    Admin admin = lim.loginOfAdministrator();
                    if (admin != null) { // 用户登录
                        administratorMenu(admin);
                    }
                    break;
                case 2:
                    User user = lim.loginOfUser();
                    if (user != null) { // 用户登录
                        consumerMenu(user);
                    }
                    break;
                case 3:
                    lim.register(); // 用户注册
                    break;
                case 4:
                    System.out.println("成功退出系统！");
                    return;
                default:
                    System.out.println("没有该操作，请重新选择！");
            }
        }
    }

    public void administratorMenu(Admin admin) throws SQLException {

        while (true) {
            System.out.println("欢迎来到管理员界面，您可以选择以下操作！");
            System.out.println("1、密码管理");
            System.out.println("2、客户管理");
            System.out.println("3、商品管理");
            System.out.println("4、退出系统");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    adPasswordMenu(admin);
                    break;
                case 2:
                    showConsumerMenu();
                    break;
                case 3:
                    showCommodityMenu();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("您当前选择的操作是不存在的，请确认操作！");
            }
        }
    }

    public void adPasswordMenu(Admin admin) {
        PasswordManage pm = new PasswordManage();

        while (true) {
            System.out.println("欢迎来到密码管理系统：");
            System.out.println("1、修改密码");
            System.out.println("2、重置用户密码");
            System.out.println("3、退出系统");
            System.out.println("请选择：");

            int command = sc.nextInt();

            switch (command) {
                case 1:
                    pm.changePassword(admin);
                    break;
                case 2:
                    pm.resetUserPassword();
                    break;
                case 3:
                    System.out.println("成功退出系统！");
                    return;
                default:
                    System.out.println("没有该操作，请重新选择！");
            }
        }
    }

    public void consumerMenu(User user) throws SQLException {

        while (true) {
            System.out.println("欢迎来到用户界面，您可以选择以下操作！");
            System.out.println("1、密码管理");
            System.out.println("2、购物管理");
            System.out.println("3、退出系统");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    cPasswordMenu(user);
                    break;
                case 2:
                    showShoppingMenu(user);
                    break;
                case 3:
                    System.out.println("您已成功退出！");
                    return;
                default:
                    System.out.println("您当前选择的操作是不存在的，请确认操作！");
            }
        }
    }

    public void showShoppingMenu(User user) throws SQLException {
        ShoppingCartManage scm = new ShoppingCartManage();
        CommodityManage cm = new CommodityManage();

        while (true) {
            System.out.println("欢迎来到购物界面，您可以选择以下操作！");
            System.out.println("1、添加商品到购物车");
            System.out.println("2、将商品移除购物车");
            System.out.println("3、修改商品购买数量");
            System.out.println("4、购物车结算");
            System.out.println("5、查看购物历史");
            System.out.println("6、浏览所有商品");
            System.out.println("7、退出");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    scm.insertToShoppingCart(user);
                    break;
                case 2:
                    scm.deleteFromShoppingCart(user);
                    break;
                case 3:
                    scm.updateInShoppingCart(user);
                    break;
                case 4:
                    scm.checkoutShoppingCart(user);
                    break;
                case 5:
                    scm.showShoppingHistory(user);
                    break;
                case 6:
                    scm.showAllCommoditiesToUser();
                    break;
                case 7:
                    System.out.println("您已成功退出！");
                    return;
                default:
                    System.out.println("您当前选择的操作不存在，请确认操作！");
            }
        }
    }

    public void showCommodityMenu() throws SQLException {
        CommodityManage cm = new CommodityManage();

        while (true) {
            System.out.println("欢迎来到商品管理界面，您可以选择以下操作！");
            System.out.println("1、列出所有商品信息");
            System.out.println("2、添加商品信息");
            System.out.println("3、修改商品信息");
            System.out.println("4、删除商品信息");
            System.out.println("5、退出系统");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    cm.showAllCommodities();
                    break;
                case 2:
                    cm.insertCommodity();
                    break;
                case 3:
                    cm.updateCommodity();
                    break;
                case 4:
                    cm.deleteCommodity();
                    break;
                case 5:
                    System.out.println("您已成功退出系统！");
                    return;
                default:
                    System.out.println("您当前选择的操作是不存在的，请确认操作！");
            }
        }
    }


    public void cPasswordMenu(User user) throws SQLException {
        PasswordManage pm = new PasswordManage();

        while (true) {
            System.out.println("欢迎来到密码管理系统：");
            System.out.println("1、修改密码");
            System.out.println("2、忘记密码");
            System.out.println("3、退出系统");
            System.out.println("请选择：");

            int command = sc.nextInt();

            switch (command) {
                case 1:
                    pm.changePassword(user);
                    break;
                case 2:
                    pm.resetPassword(user);
                    break;
                case 3:
                    System.out.println("成功退出系统！");
                    return;
                default:
                    System.out.println("没有该操作，请重新选择！");
            }
        }
    }

    public void showConsumerMenu() throws SQLException {
        UserManage cm = new UserManage();

        while (true) {
            System.out.println("欢迎来到客户管理系统：");
            System.out.println("1、展示所有客户信息");
            System.out.println("2、删除客户信息");
            System.out.println("3、查询客户信息");
            System.out.println("4、退出系统");
            System.out.println("请选择：");

            int command = sc.nextInt();

            switch (command) {
                case 1:
                    cm.showAllUsers();
                    break;
                case 2:
                    cm.deleteUser();
                    break;
                case 3:
                    cm.queryUser();
                    break;
                case 4:
                    System.out.println("成功退出系统！");
                    return;
                default:
                    System.out.println("没有该操作，请重新选择！");
            }
        }
    }
}


