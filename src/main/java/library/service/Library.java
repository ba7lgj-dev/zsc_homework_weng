package library.service;

import library.model.Admin;
import library.model.Book;
import library.model.Reader;
import library.repository.AdminRepository;
import library.repository.BookRepository;
import library.repository.ReaderRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class Library {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final AdminRepository adminRepository;

    private List<Book> books;
    private List<Reader> readers;
    private List<Admin> admins;

    public Library() {
        this.bookRepository = new BookRepository();
        this.readerRepository = new ReaderRepository();
        this.adminRepository = new AdminRepository();
        loadData();
    }

    private void loadData() {
        this.books = new ArrayList<>(bookRepository.loadAll());
        this.readers = new ArrayList<>(readerRepository.loadAll());
        this.admins = new ArrayList<>(adminRepository.loadAll());
        if (books.isEmpty() || readers.isEmpty() || admins.isEmpty()) {
            initDefaultData();
            saveAll();
        }
    }

    private void initDefaultData() {
        books = new ArrayList<>();
        books.add(new Book(1, "Java基础", "张三", "清华大学出版社", 4, 4));
        books.add(new Book(2, "C语言程序设计", "谭浩强", "清华大学出版社", 5, 5));
        books.add(new Book(3, "数据结构", "王五", "机械工业出版社", 6, 6));
        books.add(new Book(4, "深入理解Java虚拟机", "周志明", "机械工业出版社", 3, 3));
        books.add(new Book(5, "算法导论", "Thomas H. Cormen", "机械工业出版社", 2, 2));
        books.add(new Book(6, "Python编程从入门到实践", "Eric Matthes", "人民邮电出版社", 4, 4));
        books.add(new Book(7, "计算机网络", "谢希仁", "电子工业出版社", 5, 5));
        books.add(new Book(8, "离散数学", "李四", "高等教育出版社", 3, 3));
        books.add(new Book(9, "操作系统原理", "Peter B. Galvin", "机械工业出版社", 3, 3));
        books.add(new Book(10, "数据库系统概念", "Abraham Silberschatz", "机械工业出版社", 4, 4));

        readers = new ArrayList<>();
        readers.add(new Reader(1, "reader1", "123456", "小明", new ArrayList<>()));
        readers.add(new Reader(2, "reader2", "123456", "小红", new ArrayList<>()));
        readers.add(new Reader(3, "reader3", "123456", "小刚", new ArrayList<>()));
        readers.add(new Reader(4, "reader4", "123456", "小李", new ArrayList<>()));
        readers.add(new Reader(5, "reader5", "123456", "小赵", new ArrayList<>()));

        admins = new ArrayList<>();
        admins.add(new Admin(1, "admin", "admin123", "系统管理员"));
    }

    public Reader loginReader(String username, String password) {
        return readers.stream()
                .filter(reader -> reader.getUsername().equals(username) && reader.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public Admin loginAdmin(String username, String password) {
        return admins.stream()
                .filter(admin -> admin.getUsername().equals(username) && admin.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<Book> listAllBooks() {
        return new ArrayList<>(books);
    }

    public Book findBookById(int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    public List<Book> searchBooksByTitle(String keyword) {
        String lowered = keyword.toLowerCase(Locale.ROOT);
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase(Locale.ROOT).contains(lowered))
                .collect(Collectors.toList());
    }

    public boolean borrowBook(Reader reader, int bookId) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("图书不存在。");
            return false;
        }
        if (book.getAvailableCount() <= 0) {
            System.out.println("图书已借完。");
            return false;
        }
        if (reader.getBorrowedBookIds().contains(bookId)) {
            System.out.println("您已借阅该图书。");
            return false;
        }
        book.setAvailableCount(book.getAvailableCount() - 1);
        reader.getBorrowedBookIds().add(bookId);
        saveAll();
        return true;
    }

    public boolean returnBook(Reader reader, int bookId) {
        if (!reader.getBorrowedBookIds().contains(bookId)) {
            System.out.println("您未借阅该图书。");
            return false;
        }
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("图书记录不存在，无法归还。");
            return false;
        }
        reader.getBorrowedBookIds().remove(Integer.valueOf(bookId));
        book.setAvailableCount(book.getAvailableCount() + 1);
        saveAll();
        return true;
    }

    public List<Book> listBorrowedBooks(Reader reader) {
        return reader.getBorrowedBookIds().stream()
                .map(this::findBookById)
                .filter(book -> book != null)
                .collect(Collectors.toList());
    }

    public boolean addBook(Book book) {
        if (findBookById(book.getId()) != null) {
            return false;
        }
        books.add(book);
        saveAll();
        return true;
    }

    public boolean removeBook(int id) {
        Book book = findBookById(id);
        if (book == null) {
            return false;
        }
        books.remove(book);
        readers.forEach(reader -> reader.getBorrowedBookIds().removeIf(bookId -> bookId == id));
        saveAll();
        return true;
    }

    public boolean updateBook(Book newBook) {
        Optional<Book> optionalBook = books.stream().filter(b -> b.getId() == newBook.getId()).findFirst();
        if (optionalBook.isEmpty()) {
            return false;
        }
        Book existing = optionalBook.get();
        int borrowedCount = existing.getTotalCount() - existing.getAvailableCount();
        existing.setTitle(newBook.getTitle());
        existing.setAuthor(newBook.getAuthor());
        existing.setPublisher(newBook.getPublisher());
        existing.setTotalCount(newBook.getTotalCount());
        int recalculatedAvailable = Math.max(0, newBook.getTotalCount() - borrowedCount);
        existing.setAvailableCount(recalculatedAvailable);
        saveAll();
        return true;
    }

    public List<Book> sortBooksById() {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getId))
                .collect(Collectors.toList());
    }

    public List<Book> sortBooksByTitle() {
        return books.stream()
                .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public void saveAll() {
        bookRepository.saveAll(books);
        readerRepository.saveAll(readers);
        adminRepository.saveAll(admins);
    }
}
