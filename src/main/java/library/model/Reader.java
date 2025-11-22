package library.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reader {
    private int id;
    private String username;
    private String password;
    private String name;
    private List<Integer> borrowedBookIds;

    public Reader() {
        this.borrowedBookIds = new ArrayList<>();
    }

    public Reader(int id, String username, String password, String name, List<Integer> borrowedBookIds) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.borrowedBookIds = borrowedBookIds == null ? new ArrayList<>() : new ArrayList<>(borrowedBookIds);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getBorrowedBookIds() {
        return borrowedBookIds;
    }

    public void setBorrowedBookIds(List<Integer> borrowedBookIds) {
        this.borrowedBookIds = borrowedBookIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reader reader = (Reader) o;
        return id == reader.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("读者编号:%d | 登录名:%s | 姓名:%s | 已借图书数量:%d", id, username, name, borrowedBookIds.size());
    }
}
