package library.model;

public class Admin extends Person {

    public Admin() {
    }

    public Admin(int id, String username, String password, String name) {
        super(id, username, password, name);
    }

    @Override
    public String toString() {
        return String.format("管理员编号:%d | 登录名:%s | 姓名:%s", getId(), getAccount(), getName());
    }
}
