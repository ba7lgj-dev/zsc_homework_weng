

public class Account {
    private String accName;
    private String accNo;
    private double balance;

    public Account() {
        this.accName = "000";
        this.accNo = "";
        this.balance = 0;
    }

    public Account(String accNo, String accName) {
        this.accNo = accNo;
        this.accName = accName;
        this.balance = 0;
    }

    public Account(String accNo, String accName, double balance) {
        this(accNo, accName);
        this.balance = balance;
    }

    public void printInfo() {
        System.out.println(accNo + "  " + accName + " " + balance);
    }

    public void deposit(double amount) {
        this.balance += amount;
        System.out.println("成功存入"+amount+"元");
        this.printInfo();
    }

    public void withdraw(double amount) {
        if (this.balance > amount) {
            this.balance -= amount;
            System.out.println("账户："+this.accNo+";户主："+this.accName+";成功取出:"+amount+"元，当前余额为："+this.balance+"元");
            this.printInfo();
        } else {
            System.out.println("账户："+this.accNo+";户主："+this.accName+";取款:"+amount+"元失败，余额不足，当前余额为："+this.balance+"元");
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

    public static void main(String[] args) {
        //对象数组
        Account[] accounts =new Account[]{
                new Account("001","张三",10000),
                new Account("002","李四",2000),
                new Account("003","王五",200000)
        };

        for (Account account : accounts) {
            account.printInfo();
        }

        for (Account account : accounts) {
            account.deposit(10);
            account.withdraw(10000);

        }

        for (Account account : accounts) {
            account.printInfo();
        }
    }
}
