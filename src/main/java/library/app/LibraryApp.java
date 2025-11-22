package library.app;

import library.app.ui.UserInterface;
import library.model.Admin;
import library.model.Book;
import library.model.Reader;
import library.service.Library;
import library.app.ui.TerminalConsole;

import java.util.List;

public class LibraryApp {
    private final Library library;
    private final UserInterface ui;

    public LibraryApp(UserInterface ui) {
        this.ui = ui;
        library = new Library();
    }

    public static void main(String[] args) {
        TerminalConsole console = new TerminalConsole();
        new LibraryApp(console).run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = ui.readLine("");
            switch (choice) {
                case "1" -> readerLogin();
                case "2" -> adminLogin();
                case "0" -> {
                    library.saveAll();
                    ui.println(success("感谢使用，系统已退出。"));
                    running = false;
                }
                default -> ui.println(warning("无效选择，请重新输入。"));
            }
        }
    }

    private String title(String text) {
        return TerminalConsole.highlight(text);
    }

    private String success(String text) {
        return TerminalConsole.success(text);
    }

    private String warning(String text) {
        return TerminalConsole.warning(text);
    }

    private String danger(String text) {
        return TerminalConsole.danger(text);
    }

    private String info(String text) {
        return TerminalConsole.info(text);
    }

    private String option(String key, String description) {
        return info("[" + key + "]") + " " + description;
    }

    private String divider() {
        return TerminalConsole.colorize("----------------------------------------", TerminalConsole.CYAN);
    }

    private String fit(String text, int length) {
        if (text == null) {
            return "";
        }
        return text.length() <= length ? text : text.substring(0, length - 1) + "…";
    }

    private void printMainMenu() {
        ui.println(title("====== 图书借阅管理系统 ======"));
        ui.println(option("1", "读者登录"));
        ui.println(option("2", "管理员登录"));
        ui.println(option("0", "退出系统"));
        ui.println(divider());
        ui.print(info("请输入你的选择："));
    }

    private void readerLogin() {
        String username = ui.readLine("请输入读者登录名：");
        String password = ui.readLine("请输入密码：");
        Reader reader = library.loginReader(username, password);
        if (reader != null) {
            ui.println(success("欢迎你，" + reader.getName() + "！"));
            readerMenu(reader);
        } else {
            ui.println(danger("用户名或密码错误。"));
        }
    }

    private void adminLogin() {
        String username = ui.readLine("请输入管理员登录名：");
        String password = ui.readLine("请输入密码：");
        Admin admin = library.loginAdmin(username, password);
        if (admin != null) {
            ui.println(success("欢迎你，" + admin.getName() + "！"));
            adminMenu();
        } else {
            ui.println(danger("用户名或密码错误。"));
        }
    }

    private void readerMenu(Reader reader) {
        boolean loggedIn = true;
        while (loggedIn) {
            ui.println(title("====== 读者菜单 ======"));
            ui.println(option("1", "查看所有图书"));
            ui.println(option("2", "检索图书（按书名关键字）"));
            ui.println(option("3", "借书"));
            ui.println(option("4", "还书"));
            ui.println(option("5", "查看已借图书"));
            ui.println(option("6", "充值/购买VIP"));
            ui.println(option("0", "退出登录"));
            ui.println(divider());
            String choice = ui.readLine("请输入你的选择：");
            switch (choice) {
                case "1" -> printBooks(library.listAllBooks());
                case "2" -> searchBooks();
                case "3" -> borrowBook(reader);
                case "4" -> returnBook(reader);
                case "5" -> printBooks(library.listBorrowedBooks(reader));
                case "6" -> readerRecharge(reader);
                case "0" -> loggedIn = false;
                default -> ui.println(warning("无效选择，请重新输入。"));
            }
        }
    }

    private void adminMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            ui.println(title("====== 管理员菜单 ======"));
            ui.println(option("1", "查看所有图书"));
            ui.println(option("2", "新增图书"));
            ui.println(option("3", "删除图书"));
            ui.println(option("4", "修改图书"));
            ui.println(option("5", "检索图书（按书名关键字）"));
            ui.println(option("6", "按编号排序"));
            ui.println(option("7", "按书名排序"));
            ui.println(option("0", "退出登录"));
            ui.println(divider());
            String choice = ui.readLine("请输入你的选择：");
            switch (choice) {
                case "1" -> printBooks(library.listAllBooks());
                case "2" -> addBook();
                case "3" -> removeBook();
                case "4" -> updateBook();
                case "5" -> searchBooks();
                case "6" -> printBooks(library.sortBooksById());
                case "7" -> printBooks(library.sortBooksByTitle());
                case "0" -> loggedIn = false;
                default -> ui.println(warning("无效选择，请重新输入。"));
            }
        }
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            ui.println(warning("没有找到图书。"));
            return;
        }
        String header = String.format("%-6s %-22s %-12s %-10s %-10s %-8s",
                "编号", "书名", "作者", "分类", "出版社", "可借/总");
        ui.println(title(header));
        books.forEach(book -> {
            String availability = book.getAvailableCount() > 0
                    ? success(book.getAvailableCount() + "")
                    : danger(book.getAvailableCount() + "");
            String row = String.format("%-6d %-22s %-12s %-10s %-10s %s/%d",
                    book.getId(),
                    fit(book.getTitle(), 22),
                    fit(book.getAuthor(), 12),
                    fit(book.getCategory(), 10),
                    fit(book.getPublisher(), 10),
                    availability,
                    book.getTotalCount());
            ui.println(row);
        });
    }

    private void searchBooks() {
        String keyword = ui.readLine("请输入书名关键字：");
        printBooks(library.searchBooksByTitle(keyword));
    }

    private void borrowBook(Reader reader) {
        int bookId = ui.readInt("请输入要借阅的图书编号：");
        if (library.borrowBook(reader, bookId)) {
            ui.println(success("借阅成功！"));
        }
    }

    private void returnBook(Reader reader) {
        int bookId = ui.readInt("请输入要归还的图书编号：");
        if (library.returnBook(reader, bookId)) {
            ui.println(success("归还成功！"));
        }
    }

    private void addBook() {
        int id = ui.readInt("请输入图书编号：");
        String title = ui.readLine("请输入书名：");
        String author = ui.readLine("请输入作者：");
        String publisher = ui.readLine("请输入出版社：");
        String isbn = ui.readLine("请输入ISBN：");
        String category = ui.readLine("请输入分类：");
        int total = ui.readInt("请输入总数量：");
        double deposit = ui.readDouble("请输入押金：");
        Book book = new Book(id, title, author, publisher, isbn, category, null, 0, deposit, total, total, new java.util.ArrayList<>());
        if (library.addBook(book)) {
            ui.println(success("新增图书成功。"));
        } else {
            ui.println(danger("编号已存在，新增失败。"));
        }
    }

    private void removeBook() {
        int id = ui.readInt("请输入要删除的图书编号：");
        if (library.removeBook(id)) {
            ui.println(success("删除成功。"));
        } else {
            ui.println(warning("未找到该编号的图书。"));
        }
    }

    private void updateBook() {
        int id = ui.readInt("请输入要修改的图书编号：");
        Book existing = library.findBookById(id);
        if (existing == null) {
            ui.println(warning("未找到该编号的图书。"));
            return;
        }
        ui.println(info("当前信息：" + existing));
        String title = ui.readLine("请输入新的书名（直接回车保持不变）：");
        if (title.isEmpty()) {
            title = existing.getTitle();
        }
        String author = ui.readLine("请输入新的作者（直接回车保持不变）：");
        if (author.isEmpty()) {
            author = existing.getAuthor();
        }
        String publisher = ui.readLine("请输入新的出版社（直接回车保持不变）：");
        if (publisher.isEmpty()) {
            publisher = existing.getPublisher();
        }
        String totalInput = ui.readLine("请输入新的总数量（直接回车保持不变）：");
        int total = existing.getTotalCount();
        if (!totalInput.isEmpty()) {
            try {
                total = Integer.parseInt(totalInput);
            } catch (NumberFormatException e) {
                ui.println(warning("总数量输入无效，保持原值。"));
            }
        }
        Book updated = new Book(id, title, author, publisher, existing.getIsbn(), existing.getCategory(), existing.getPublishDate(), existing.getPages(), existing.getDeposit(), total, existing.getAvailableCount(), existing.getStatusHistory());
        if (library.updateBook(updated)) {
            ui.println(success("修改成功。"));
        } else {
            ui.println(danger("修改失败。"));
        }
    }

    private void readerRecharge(Reader reader) {
        double amount = ui.readDouble("请输入充值金额：");
        reader.recharge(amount);
        String buyVip = ui.readLine("是否花费50元购买VIP并提升借阅额度(Y/N)：");
        if (buyVip.equalsIgnoreCase("Y")) {
            reader.buyVip(50);
        }
        library.saveAll();
        ui.println(success("当前余额：" + reader.getBalance() + "，VIP状态：" + (reader.isVip() ? "是" : "否")));
    }
}
