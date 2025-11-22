package library.repository;

import library.model.Admin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminRepository {
    private final Path filePath;

    public AdminRepository() {
        this(Paths.get("data", "admins.txt"));
    }

    public AdminRepository(Path filePath) {
        this.filePath = filePath;
    }

    public List<Admin> loadAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8)
                    .stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::parseAdmin)
                    .filter(admin -> admin != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("读取管理员数据失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveAll(List<Admin> admins) {
        try {
            Files.createDirectories(filePath.getParent());
            List<String> lines = admins.stream()
                    .map(admin -> String.join(",",
                            String.valueOf(admin.getId()),
                            admin.getAccount(),
                            admin.getPassword(),
                            admin.getName()))
                    .toList();
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("保存管理员数据失败: " + e.getMessage());
        }
    }

    private Admin parseAdmin(String line) {
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
}
