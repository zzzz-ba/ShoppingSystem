package Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    // 判断密码是否规范（大于8位，包含大小写字母数字和标点符号）
    public static boolean isValidPassword(String password) {
        // 正则表达式定义，满足以上要求
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!-])(?=\\S+$).{8,}$";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 创建Matcher对象
        Matcher matcher = pattern.matcher(password);

        // 执行匹配
        return matcher.matches();
    }

    // 判断手机号码是否规范（1开头且为11位号码）
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^1\\d{10}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(phoneNumber);

        // 执行匹配
        return matcher.matches();
    }

    // 判断邮箱是否规范()
    public static boolean isValidEmail(String email) {
        String regex = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-z]{2,})$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        // 执行匹配
        return matcher.matches();
    }
}
