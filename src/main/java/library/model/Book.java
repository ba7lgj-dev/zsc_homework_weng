package library.model;

import java.util.Objects;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int totalCount;
    private int availableCount;

    public Book() {
    }

    public Book(int id, String title, String author, String publisher, int totalCount, int availableCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.totalCount = totalCount;
        this.availableCount = availableCount;
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
        return String.format("编号:%d | 书名:%s | 作者:%s | 出版社:%s | 总数量:%d | 可借数量:%d",
                id, title, author, publisher, totalCount, availableCount);
    }
}
