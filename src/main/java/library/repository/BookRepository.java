package library.repository;

import library.model.Book;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepository {
    private final Path filePath;

    public BookRepository() {
        this(Paths.get("data", "books.txt"));
    }

    public BookRepository(Path filePath) {
        this.filePath = filePath;
    }

    public List<Book> loadAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8)
                    .stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::parseBook)
                    .filter(book -> book != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("读取图书数据失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveAll(List<Book> books) {
        try {
            Files.createDirectories(filePath.getParent());
            List<String> lines = books.stream()
                    .map(book -> String.join(",",
                            String.valueOf(book.getId()),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getPublisher(),
                            String.valueOf(book.getTotalCount()),
                            String.valueOf(book.getAvailableCount())))
                    .toList();
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("保存图书数据失败: " + e.getMessage());
        }
    }

    private Book parseBook(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            return null;
        }
        try {
            return new Book(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3],
                    Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
        } catch (NumberFormatException e) {
            System.out.println("跳过格式错误的图书记录: " + line);
            return null;
        }
    }
}
