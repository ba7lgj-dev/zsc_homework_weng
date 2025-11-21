package dzq;

import java.util.Scanner;

public class Demo_2 {

    public static void main(String[] args) {
        // 使用String类方法实现身份证信息提取
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("请输入18位身份证号码：");
            String idCard = sc.nextLine();

            // 验证身份证长度
            if (idCard.length() != 18) {
                System.out.println("输入的身份证号码长度不为18位");
                return;
            }

            // 使用String的substring方法提取出生日期信息
            // 第7-10位是年份，11-12位是月份，13-14位是日期
            String birthYear = idCard.substring(6, 10);
            String birthMonth = idCard.substring(10, 12);
            String birthDay = idCard.substring(12, 14);

            // 提取性别信息（第17位）
            char genderChar = idCard.charAt(16);
            int genderNum = Character.getNumericValue(genderChar);
            String gender = (genderNum % 2 == 1) ? "男" : "女";

            // 格式化输出结果
            String result = birthYear + "年" + birthMonth + "月" + birthDay + "日 " + gender;
            System.out.println(result);

        } catch (Exception e) {
            System.out.println("处理身份证信息时出现错误");
        } finally {
            // 确保资源正确关闭
            sc.close();
        }
    }
}