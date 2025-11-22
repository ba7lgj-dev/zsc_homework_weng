package library.repository;

import library.model.Book;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BookRepository extends Repository<Book> {
    public BookRepository() {
        this(Paths.get("data", "books.txt"));
    }

    public BookRepository(Path filePath) {
        super(filePath);
    }

    @Override
    protected Book parse(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            return null;
        }
        try {
            if (parts.length <= 6) {
                return new Book(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3],
                        Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
            }
            return new Book(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3],
                    parts[4], parts[5],
                    parts.length > 6 && !parts[6].isEmpty() ? java.time.LocalDate.parse(parts[6]) : null,
                    parts.length > 7 ? Integer.parseInt(parts[7]) : 0,
                    parts.length > 8 ? Double.parseDouble(parts[8]) : 0,
                    parts.length > 9 ? Integer.parseInt(parts[9]) : 0,
                    parts.length > 10 ? Integer.parseInt(parts[10]) : 0,
                    new ArrayList<>());
        } catch (NumberFormatException e) {
            System.out.println("跳过格式错误的图书记录: " + line);
            return null;
        }
    }

    @Override
    protected String format(Book book) {
        return String.join(",",
                String.valueOf(book.getId()),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getIsbn(),
                book.getCategory(),
                book.getPublishDate() == null ? "" : book.getPublishDate().toString(),
                String.valueOf(book.getPages()),
                String.valueOf(book.getDeposit()),
                String.valueOf(book.getTotalCount()),
                String.valueOf(book.getAvailableCount()));
    }
}
