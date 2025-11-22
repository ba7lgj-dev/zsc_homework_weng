package library.app;

import library.app.ui.ConsoleInterface;
import library.app.ui.UserInterface;
import library.model.Admin;
import library.model.Book;
import library.model.Reader;
import library.service.Library;

import java.util.List;

public class LibraryApp {
    private final Library library;
    private final UserInterface ui;

    public LibraryApp(UserInterface ui) {
        this.ui = ui;
        library = new Library();
    }

    public static void main(String[] args) {
        UserInterface console = new ConsoleInterface();
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
                    System.out.println("感谢使用，系统已退出。");
                    running = false;
                }
                default -> System.out.println("无效选择，请重新输入。");
            }
        }
    }

    private void printMainMenu() {
        ui.println("====== 图书借阅管理系统 ======");
        ui.println("1. 读者登录");
        ui.println("2. 管理员登录");
        ui.println("0. 退出系统");
        ui.print("请输入你的选择：");
    }

    private void readerLogin() {
        String username = ui.readLine("请输入读者登录名：");
        String password = ui.readLine("请输入密码：");
        Reader reader = library.loginReader(username, password);
        if (reader != null) {
            printReaderWelcome(reader);
            readerMenu(reader);
        } else {
            ui.println("用户名或密码错误。");
        }
    }

    private void adminLogin() {
        String username = ui.readLine("请输入管理员登录名：");
        String password = ui.readLine("请输入密码：");
        Admin admin = library.loginAdmin(username, password);
        if (admin != null) {
            ui.println("欢迎你，" + admin.getName() + "！");
            adminMenu();
        } else {
            ui.println("用户名或密码错误。");
        }
    }

    private void printReaderWelcome(Reader reader) {
        if (reader.isVip()) {
            ui.println("==============================");
            ui.println(" 尊贵的会员 " + reader.getName() + "，欢迎回来！");
            ui.println(" 您的专属借阅额度：" + reader.getBorrowMax());
            ui.println(" 当前余额：" + reader.getBalance());
            ui.println("==============================");
        } else {
            ui.println("欢迎你，" + reader.getName() + "！");
            ui.println("升级为VIP可提升借阅额度并获得更多权益哦~");
        }
    }

    private void readerMenu(Reader reader) {
        boolean loggedIn = true;
        while (loggedIn) {
            ui.println("====== 读者菜单 ======");
            ui.println("1. 查看所有图书");
            ui.println("2. 检索图书（按书名关键字）");
            ui.println("3. 借书");
            ui.println("4. 还书");
            ui.println("5. 查看已借图书");
            ui.println("6. 充值/购买VIP");
            ui.println("0. 退出登录");
            String choice = ui.readLine("请输入你的选择：");
            switch (choice) {
                case "1" -> printBooks(library.listAllBooks());
                case "2" -> searchBooks();
                case "3" -> borrowBook(reader);
                case "4" -> returnBook(reader);
                case "5" -> printBooks(library.listBorrowedBooks(reader));
                case "6" -> readerRecharge(reader);
                case "0" -> loggedIn = false;
                default -> ui.println("无效选择，请重新输入。");
            }
        }
    }

    private void adminMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            ui.println("====== 管理员菜单 ======");
            ui.println("1. 查看所有图书");
            ui.println("2. 新增图书");
            ui.println("3. 删除图书");
            ui.println("4. 修改图书");
            ui.println("5. 检索图书（按书名关键字）");
            ui.println("6. 按编号排序");
            ui.println("7. 按书名排序");
            ui.println("0. 退出登录");
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
                default -> ui.println("无效选择，请重新输入。");
            }
        }
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            ui.println("没有找到图书。");
            return;
        }
        books.forEach(book -> ui.println(book.toString()));
    }

    private void searchBooks() {
        String keyword = ui.readLine("请输入书名关键字：");
        printBooks(library.searchBooksByTitle(keyword));
    }

    private void borrowBook(Reader reader) {
        int bookId = ui.readInt("请输入要借阅的图书编号：");
        if (library.borrowBook(reader, bookId)) {
            ui.println("借阅成功！");
        }
    }

    private void returnBook(Reader reader) {
        int bookId = ui.readInt("请输入要归还的图书编号：");
        if (library.returnBook(reader, bookId)) {
            ui.println("归还成功！");
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
        double deposit = Double.parseDouble(ui.readLine("请输入押金："));
        Book book = new Book(id, title, author, publisher, isbn, category, null, 0, deposit, total, total, new java.util.ArrayList<>());
        if (library.addBook(book)) {
            ui.println("新增图书成功。");
        } else {
            ui.println("编号已存在，新增失败。");
        }
    }

    private void removeBook() {
        int id = ui.readInt("请输入要删除的图书编号：");
        if (library.removeBook(id)) {
            ui.println("删除成功。");
        } else {
            ui.println("未找到该编号的图书。");
        }
    }

    private void updateBook() {
        int id = ui.readInt("请输入要修改的图书编号：");
        Book existing = library.findBookById(id);
        if (existing == null) {
            ui.println("未找到该编号的图书。");
            return;
        }
        ui.println("当前信息：" + existing);
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
                ui.println("总数量输入无效，保持原值。");
            }
        }
        Book updated = new Book(id, title, author, publisher, existing.getIsbn(), existing.getCategory(), existing.getPublishDate(), existing.getPages(), existing.getDeposit(), total, existing.getAvailableCount(), existing.getStatusHistory());
        if (library.updateBook(updated)) {
            ui.println("修改成功。");
        } else {
            ui.println("修改失败。");
        }
    }

    private void readerRecharge(Reader reader) {
        String amountStr = ui.readLine("请输入充值金额：");
        try {
            double amount = Double.parseDouble(amountStr);
            reader.recharge(amount);
            String buyVip = ui.readLine("是否花费50元购买VIP并提升借阅额度(Y/N)：");
            if (buyVip.equalsIgnoreCase("Y")) {
                reader.buyVip(50);
            }
            library.saveAll();
            ui.println("当前余额：" + reader.getBalance() + "，VIP状态：" + (reader.isVip() ? "是" : "否"));
        } catch (NumberFormatException e) {
            ui.println("充值失败，金额格式不正确。");
        }
    }
}
