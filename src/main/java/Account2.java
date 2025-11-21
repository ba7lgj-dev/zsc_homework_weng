 

import java.util.Base64;

public class Account2 {
    private String accName;
    private String accNo;
    private double balance;
    private static String bank = "中国银行";
    private static int count;

    public Account2() {
        count++;
    }


    public Account2(String accName) {
        count++;
        this.accName = accName;
        this.accNo = String.format("SVIP%03d", count);
    }

    public Account2(String accName, double balance) {
        count++;
        this.accName = accName;
//        this.accNo = accNo;
        this.accNo = String.format("SVIP%03d", count);
        this.balance = balance;
    }


    public void printInfo() {
        System.out.println(bank + ":" + accNo + "  " + accName + " " + balance);
    }

    public void deposit(double amount) {
        this.balance += amount;
        System.out.println( "当前账户"+ accNo +"成功存入" + amount + "元");
        this.printInfo();
    }

    public void withdraw(double amount) {
        if (this.balance > amount) {
            this.balance -= amount;
            System.out.println("账户："+this.accNo+";户主："+this.accName+";成功取出:"+amount+"元");

            this.printInfo();
        } else {

            System.out.println("账户："+this.accNo+";户主："+this.accName+";取款:"+amount+"元失败，余额不足，当前余额为："+this.balance+"元");
            this.printInfo();
        }
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static String getBank() {
        return bank;
    }

    public static int getCount() {
        return count;
    }

    public static void main(String[] args) {
        Account2 acc1 = new Account2();
        Account2 acc2 = new Account2("张三");
        Account2 acc3 = new Account2("李四", 0.0);

        acc2.deposit(3000);
        acc2.withdraw(1000);
        acc2.withdraw(5000);

        System.out.println("当前有：" + Account2.getCount() + "个账户");

        //对象数组
        Account2[] Account2s = new Account2[]{
                new Account2("张三", 100000),
                new Account2("李四", 20000),
                new Account2("王五", 200000)
        };

        Account2s[0].printInfo();
        Account2s[1].printInfo();
        Account2s[2].printInfo();
        for (Account2 Account2 : Account2s) {
            Account2.printInfo();
        }
        System.out.println(Account2.getBank());
        System.out.println("当前有：" + Account2.getCount() + "个账户");
    }
}
