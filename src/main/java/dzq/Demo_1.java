package dzq;

import java.util.Scanner;

public class  Demo_1 {
    public static void main(String[] args) {
        String terms = "立春、雨水、惊蛰、春分、清明、谷雨、"
                + "立夏、小满、芒种、夏至、小暑、大暑、"
                + "立秋、处暑、白露、秋分、寒露、霜降、"
                + "立冬、小雪、大雪、冬至、小寒、大寒";

        // 1. 编写程序将terms中的字符串分割成一个字符数组，存储24个节气，然后输出
        String[] termArray = terms.split("、");
        for (int i = 0; i < termArray.length; i++) {
            System.out.print(termArray[i]+" ");
        }
        //编写程序实现一个循环，当用户输入序号(1~24)，输出相应的节气名
        int index = 0;
        while(index != -1){
            System.out.print("请输入序号(1~24)，输入-1结束：");
            // 用户的输入使用Scanner类
            Scanner sc = new Scanner(System.in);
            index = sc.nextInt();
            if(index >= 1 && index <= 24){
                System.out.println("输出："+termArray[index-1]);
            }
        }




    }
}