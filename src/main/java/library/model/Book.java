package library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String category;
    private LocalDate publishDate;
    private int pages;
    private double deposit;
    private int totalCount;
    private int availableCount;
    private List<String> statusHistory;

    public Book() {
        statusHistory = new ArrayList<>();
    }

    public Book(int id, String title, String author, String publisher, int totalCount, int availableCount) {
        this(id, title, author, publisher, "", "", null, 0, 0, totalCount, availableCount, new ArrayList<>());
    }

    public Book(int id, String title, String author, String publisher, String isbn, String category, LocalDate publishDate,
                int pages, double deposit, int totalCount, int availableCount, List<String> statusHistory) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.category = category;
        this.publishDate = publishDate;
        this.pages = pages;
        this.deposit = deposit;
        this.totalCount = totalCount;
        this.availableCount = availableCount;
        this.statusHistory = statusHistory == null ? new ArrayList<>() : new ArrayList<>(statusHistory);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public List<String> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<String> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public void appendStatus(String status) {
        statusHistory.add(status);
    }

    public int borrowBook() {
        if (availableCount <= 0) {
            return -1;
        }
        availableCount -= 1;
        return availableCount;
    }

    public int returnBook() {
        availableCount = Math.min(availableCount + 1, totalCount);
        return availableCount;
    }

    public void showStatus() {
        statusHistory.forEach(System.out::println);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("编号:%d | 书名:%s | 作者:%s | 出版社:%s | ISBN:%s | 分类:%s | 总数量:%d | 可借数量:%d", id, title, author, publisher, isbn, category, totalCount, availableCount);
    }
}
