package library.model;

import java.util.ArrayList;
import java.util.List;

public class Reader extends Person {
    private List<Integer> borrowedBookIds;
    private int borrowMax = 5;
    private double balance = 0;
    private boolean vip;

    public Reader() {
        this.borrowedBookIds = new ArrayList<>();
    }

    public Reader(int id, String username, String password, String name, List<Integer> borrowedBookIds) {
        super(id, username, password, name);
        this.borrowedBookIds = borrowedBookIds == null ? new ArrayList<>() : new ArrayList<>(borrowedBookIds);
    }

    public Reader(int id, String username, String password, String name, List<Integer> borrowedBookIds, int borrowMax, double balance, boolean vip) {
        super(id, username, password, name);
        this.borrowedBookIds = borrowedBookIds == null ? new ArrayList<>() : new ArrayList<>(borrowedBookIds);
        this.borrowMax = borrowMax;
        this.balance = balance;
        this.vip = vip;
    }

    public List<Integer> getBorrowedBookIds() {
        return borrowedBookIds;
    }

    public void setBorrowedBookIds(List<Integer> borrowedBookIds) {
        this.borrowedBookIds = borrowedBookIds;
    }

    public int getBorrowMax() {
        return borrowMax;
    }

    public void setBorrowMax(int borrowMax) {
        this.borrowMax = borrowMax;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public int borrowBook(int bookId) {
        if (borrowedBookIds.contains(bookId)) {
            return 1; // 已借阅
        }
        if (borrowedBookIds.size() >= borrowMax) {
            return 2; // 超出借阅上限
        }
        borrowedBookIds.add(bookId);
        return 0;
    }

    public boolean returnBook(int bookId) {
        return borrowedBookIds.remove(Integer.valueOf(bookId));
    }

    public void recharge(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void buyVip(double price) {
        if (!vip && balance >= price) {
            balance -= price;
            vip = true;
            borrowMax += 3;
        }
    }

    @Override
    public String toString() {
        return String.format("读者编号:%d | 登录名:%s | 姓名:%s | VIP:%s | 已借图书数量:%d | 余额:%.2f", getId(), getAccount(), getName(), vip ? "是" : "否", borrowedBookIds.size(), balance);
    }
}
