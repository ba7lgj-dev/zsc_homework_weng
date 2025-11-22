package library.repository;

import library.model.Admin;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AdminRepository extends Repository<Admin> {
    public AdminRepository() {
        this(Paths.get("data", "admins.txt"));
    }

    public AdminRepository(Path filePath) {
        super(filePath);
    }

    @Override
    protected Admin parse(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            return null;
        }
        try {
            return new Admin(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]);
        } catch (NumberFormatException e) {
            System.out.println("跳过格式错误的管理员记录: " + line);
            return null;
        }
    }

    @Override
    protected String format(Admin admin) {
        return String.join(",",
                String.valueOf(admin.getId()),
                admin.getAccount(),
                admin.getPassword(),
                admin.getName());
    }
}
