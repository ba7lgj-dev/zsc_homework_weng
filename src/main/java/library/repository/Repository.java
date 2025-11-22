package library.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供基于文本文件的通用仓储操作。
 *
 * @param <T> 模型类型
 */
public abstract class Repository<T> {
    private final Path filePath;

    protected Repository(String fileName) {
        this(Paths.get("data", fileName));
    }

    protected Repository(Path filePath) {
        this.filePath = filePath;
    }

    public List<T> loadAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8)
                    .stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::parse)
                    .filter(item -> item != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("读取数据失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveAll(List<T> items) {
        try {
            Files.createDirectories(filePath.getParent());
            List<String> lines = items.stream()
                    .map(this::format)
                    .toList();
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("保存数据失败: " + e.getMessage());
        }
    }

    protected Path getFilePath() {
        return filePath;
    }

    protected abstract T parse(String line);

    protected abstract String format(T item);
}
