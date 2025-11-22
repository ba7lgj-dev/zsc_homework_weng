package library.repository;

import library.model.Reader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderRepository {
    private final Path filePath;

    public ReaderRepository() {
        this(Paths.get("data", "readers.txt"));
    }

    public ReaderRepository(Path filePath) {
        this.filePath = filePath;
    }

    public List<Reader> loadAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8)
                    .stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::parseReader)
                    .filter(reader -> reader != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("读取读者数据失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveAll(List<Reader> readers) {
        try {
            Files.createDirectories(filePath.getParent());
            List<String> lines = readers.stream()
                    .map(reader -> String.join(",",
                            String.valueOf(reader.getId()),
                            reader.getAccount(),
                            reader.getPassword(),
                            reader.getName(),
                            reader.getBorrowedBookIds().stream().map(String::valueOf).collect(Collectors.joining("|")),
                            String.valueOf(reader.getBorrowMax()),
                            String.valueOf(reader.getBalance()),
                            String.valueOf(reader.isVip())))
                    .toList();
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("保存读者数据失败: " + e.getMessage());
        }
    }

    private Reader parseReader(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            return null;
        }
        try {
            List<Integer> borrowedIds = new ArrayList<>();
            if (parts.length >= 5 && !parts[4].isEmpty()) {
                borrowedIds = Arrays.stream(parts[4].split("\\|"))
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
            }
            int borrowMax = parts.length >= 6 ? Integer.parseInt(parts[5]) : 5;
            double balance = parts.length >= 7 ? Double.parseDouble(parts[6]) : 0;
            boolean vip = parts.length >= 8 && Boolean.parseBoolean(parts[7]);
            return new Reader(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], borrowedIds, borrowMax, balance, vip);
        } catch (NumberFormatException e) {
            System.out.println("跳过格式错误的读者记录: " + line);
            return null;
        }
    }
}
