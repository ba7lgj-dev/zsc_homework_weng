package library.app;

import library.model.Admin;
import library.model.Book;
import library.model.Reader;
import library.service.Library;

import java.util.List;
import java.util.Scanner;

public class LibraryApp {
    private final Library library;
    private final Scanner scanner;

    public LibraryApp() {
        library = new Library();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new LibraryApp().run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> readerLogin();
                case "2" -> adminLogin();
                case "0" -> {
                    library.saveAll();
                    System.out.println("感谢使用，系统已退出。");
                    running = false;
                }
                default -> System.out.println("无效选择，请重新输入。");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("====== 图书借阅管理系统 ======");
        System.out.println("1. 读者登录");
        System.out.println("2. 管理员登录");
        System.out.println("0. 退出系统");
        System.out.print("请输入你的选择：");
    }

    private void readerLogin() {
        System.out.print("请输入读者登录名：");
        String username = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();
        Reader reader = library.loginReader(username, password);
        if (reader != null) {
            System.out.println("欢迎你，" + reader.getName() + "！");
            readerMenu(reader);
        } else {
            System.out.println("用户名或密码错误。");
        }
    }

    private void adminLogin() {
        System.out.print("请输入管理员登录名：");
        String username = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();
        Admin admin = library.loginAdmin(username, password);
        if (admin != null) {
            System.out.println("欢迎你，" + admin.getName() + "！");
            adminMenu();
        } else {
            System.out.println("用户名或密码错误。");
        }
    }

    private void readerMenu(Reader reader) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("====== 读者菜单 ======");
            System.out.println("1. 查看所有图书");
            System.out.println("2. 检索图书（按书名关键字）");
            System.out.println("3. 借书");
            System.out.println("4. 还书");
            System.out.println("5. 查看已借图书");
            System.out.println("0. 退出登录");
            System.out.print("请输入你的选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> printBooks(library.listAllBooks());
                case "2" -> searchBooks();
                case "3" -> borrowBook(reader);
                case "4" -> returnBook(reader);
                case "5" -> printBooks(library.listBorrowedBooks(reader));
                case "0" -> loggedIn = false;
                default -> System.out.println("无效选择，请重新输入。");
            }
        }
    }

    private void adminMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("====== 管理员菜单 ======");
            System.out.println("1. 查看所有图书");
            System.out.println("2. 新增图书");
            System.out.println("3. 删除图书");
            System.out.println("4. 修改图书");
            System.out.println("5. 检索图书（按书名关键字）");
            System.out.println("6. 按编号排序");
            System.out.println("7. 按书名排序");
            System.out.println("0. 退出登录");
            System.out.print("请输入你的选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> printBooks(library.listAllBooks());
                case "2" -> addBook();
                case "3" -> removeBook();
                case "4" -> updateBook();
                case "5" -> searchBooks();
                case "6" -> printBooks(library.sortBooksById());
                case "7" -> printBooks(library.sortBooksByTitle());
                case "0" -> loggedIn = false;
                default -> System.out.println("无效选择，请重新输入。");
            }
        }
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("没有找到图书。");
            return;
        }
        books.forEach(System.out::println);
    }

    private void searchBooks() {
        System.out.print("请输入书名关键字：");
        String keyword = scanner.nextLine();
        printBooks(library.searchBooksByTitle(keyword));
    }

    private void borrowBook(Reader reader) {
        int bookId = readInt("请输入要借阅的图书编号：");
        if (library.borrowBook(reader, bookId)) {
            System.out.println("借阅成功！");
        }
    }

    private void returnBook(Reader reader) {
        int bookId = readInt("请输入要归还的图书编号：");
        if (library.returnBook(reader, bookId)) {
            System.out.println("归还成功！");
        }
    }

    private void addBook() {
        int id = readInt("请输入图书编号：");
        System.out.print("请输入书名：");
        String title = scanner.nextLine();
        System.out.print("请输入作者：");
        String author = scanner.nextLine();
        System.out.print("请输入出版社：");
        String publisher = scanner.nextLine();
        int total = readInt("请输入总数量：");
        Book book = new Book(id, title, author, publisher, total, total);
        if (library.addBook(book)) {
            System.out.println("新增图书成功。");
        } else {
            System.out.println("编号已存在，新增失败。");
        }
    }

    private void removeBook() {
        int id = readInt("请输入要删除的图书编号：");
        if (library.removeBook(id)) {
            System.out.println("删除成功。");
        } else {
            System.out.println("未找到该编号的图书。");
        }
    }

    private void updateBook() {
        int id = readInt("请输入要修改的图书编号：");
        Book existing = library.findBookById(id);
        if (existing == null) {
            System.out.println("未找到该编号的图书。");
            return;
        }
        System.out.println("当前信息：" + existing);
        System.out.print("请输入新的书名（直接回车保持不变）：");
        String title = scanner.nextLine();
        if (title.isEmpty()) {
            title = existing.getTitle();
        }
        System.out.print("请输入新的作者（直接回车保持不变）：");
        String author = scanner.nextLine();
        if (author.isEmpty()) {
            author = existing.getAuthor();
        }
        System.out.print("请输入新的出版社（直接回车保持不变）：");
        String publisher = scanner.nextLine();
        if (publisher.isEmpty()) {
            publisher = existing.getPublisher();
        }
        System.out.print("请输入新的总数量（直接回车保持不变）：");
        String totalInput = scanner.nextLine();
        int total = existing.getTotalCount();
        if (!totalInput.isEmpty()) {
            try {
                total = Integer.parseInt(totalInput);
            } catch (NumberFormatException e) {
                System.out.println("总数量输入无效，保持原值。");
            }
        }
        Book updated = new Book(id, title, author, publisher, total, existing.getAvailableCount());
        if (library.updateBook(updated)) {
            System.out.println("修改成功。");
        } else {
            System.out.println("修改失败。");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入数字。");
            }
        }
    }
}
