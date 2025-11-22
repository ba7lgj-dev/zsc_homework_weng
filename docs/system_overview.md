采用面向对象程序设计思想，系统围绕读者、管理员、图书三类核心数据模型，并通过服务层与存储层协同完成图书借阅业务。主体类包括：读者类 Reader（继承自基类 Person）、管理员类 Admin（继承自基类 Person）、图书类 Book，以及负责业务流程的 Library、界面接口 UserInterface 及其 Swing 实现 SwingConsole、数据持久化仓储 Repository 及其 BookRepository/ReaderRepository/AdminRepository 子类。主程序 LibraryApp 启动 Swing 控制台，循环展示主菜单，根据用户操作调用服务方法实现功能。

1. Admin 类（继承 Person）
成员变量：继承自 Person 的 id（int）、账号 account（String）、密码 password（String）、姓名 name（String）。
用途：表示后台管理员身份，提供构造与格式化展示。构造方法 Admin()；Admin(int id, String username, String password, String name)。
主要方法：
- String toString()：按“管理员编号 | 登录名 | 姓名”格式输出管理员信息。

2. Person 抽象基类
成员变量：id（int）、account（String）、password（String）、name（String）。
用途：封装通用身份属性，供 Admin 与 Reader 继承使用。
主要方法：
- get/setId(int)、get/setAccount(String)、get/setPassword(String)、get/setName(String)：基础属性读写。
- boolean equals(Object)、int hashCode()：基于 id 判等与哈希。

3. Reader 类（继承 Person）
成员变量：borrowedBookIds（List<Integer> 已借图书编号列表）、borrowMax（int 借阅上限，默认 5）、balance（double 余额）、vip（boolean 是否 VIP）。
用途：表示读者账号，维护借阅列表、余额与 VIP 状态，提供借阅、归还与充值/购买 VIP 等操作。
主要方法：
- List<Integer> get/setBorrowedBookIds(List<Integer>)、int get/setBorrowMax(int)、double get/setBalance(double)、boolean isVip()/setVip(boolean)。
- int borrowBook(int bookId)：借阅图书，返回 0（成功）、1（重复借阅）、2（超上限）。
- boolean returnBook(int bookId)：归还图书并移除借阅记录。
- void recharge(double amount)：正数充值到账户余额。
- void buyVip(double price)：余额足够时扣费并开通 VIP，借阅上限+3。
- String toString()：展示读者编号、登录名、姓名、VIP 状态、已借数量与余额。

4. Book 类
成员变量：id（int）、title（String）、author（String）、publisher（String）、isbn（String）、category（String）、publishDate（LocalDate）、pages（int）、deposit（double 押金）、totalCount（int 总数量）、availableCount（int 可借数量）、statusHistory（List<String> 借阅状态记录）。
用途：表示图书实体，提供借还操作、状态记录与信息展示。
主要方法：
- 属性读写：get/setId(int)、get/setTitle(String)、get/setAuthor(String)、get/setPublisher(String)、get/setIsbn(String)、get/setCategory(String)、get/setPublishDate(LocalDate)、get/setPages(int)、get/setDeposit(double)、get/setTotalCount(int)、get/setAvailableCount(int)、get/setStatusHistory(List<String>)。
- void appendStatus(String status)：新增借阅状态记录。
- int borrowBook()：可借数量>0 时减 1，返回剩余可借数，否则返回 -1。
- int returnBook()：归还图书，availableCount 不超过 totalCount。
- void showStatus()：逐行输出状态记录。
- equals/hashCode：基于 id 判等。
- String toString()：格式化输出编号、书名、作者、出版社、ISBN、分类、库存信息。

5. Repository 抽象类（持久化基类）
成员变量：filePath（Path 数据文件路径）。
用途：封装基于文本文件的通用加载/保存逻辑，子类实现具体模型的解析与格式化。
主要方法：
- List<T> loadAll()：从文件读取非空行，经 parse 转换为实体列表。
- void saveAll(List<T> items)：调用 format 序列化后写入文件并自动创建目录。
- Path getFilePath()：返回文件路径。
- protected abstract T parse(String line)：从行文本解析实体（子类实现）。
- protected abstract String format(T item)：将实体序列化为行文本（子类实现）。

6. AdminRepository（继承 Repository<Admin>）
用途：管理管理员数据文件 data/admins.txt（默认）。
主要方法：
- parse(String line)：按 id,account,password,name 解析管理员，忽略格式错误行。
- format(Admin admin)：序列化管理员属性为逗号分隔行。

7. BookRepository（继承 Repository<Book>）
用途：管理图书数据文件 data/books.txt（默认）。
主要方法：
- parse(String line)：支持精简或完整字段，构造 Book 实例；遇格式错误行则跳过。
- format(Book book)：序列化图书完整字段（含 ISBN、分类、出版日期、页数、押金、总数、可借数）。

8. ReaderRepository（继承 Repository<Reader>）
用途：管理读者数据文件 data/readers.txt（默认）。
主要方法：
- parse(String line)：解析读者基础信息、借阅编号列表（以竖线分隔）、借阅上限、余额、VIP 状态；忽略格式错误行。
- format(Reader reader)：序列化读者属性与借阅列表。

9. Library 服务类
成员变量：bookRepository、readerRepository、adminRepository；books、readers、admins 内存集合。
用途：业务核心，负责数据加载/保存、默认数据初始化、登录校验、图书检索排序、借阅归还与余额/VIP 校验、增删改书籍等功能。
主要方法：
- 构造 Library()：初始化仓储并加载文件数据，缺省时写入默认数据。
- Reader loginReader(String username, String password)、Admin loginAdmin(String username, String password)：登录校验。
- List<Book> listAllBooks()、Book findBookById(int id)、List<Book> searchBooksByTitle(String keyword)：查询功能。
- boolean borrowBook(Reader reader, int bookId)：校验存在性、重复借阅、借阅上限、余额与库存后扣押金、记录状态并保存。
- boolean returnBook(Reader reader, int bookId)：校验借阅记录与图书存在性后归还、退押金、记录状态并保存。
- List<Book> listBorrowedBooks(Reader reader)：根据借阅编号映射图书。
- boolean addBook(Book book)：按编号去重后新增并保存。
- boolean removeBook(int id)：删除图书并同步移除读者借阅记录，再保存。
- boolean updateBook(Book newBook)：更新书名、作者、出版社、总数量，并根据已借数量重算可借数后保存。
- List<Book> sortBooksById()、List<Book> sortBooksByTitle()：排序输出。
- void saveAll()：将书籍、读者、管理员列表持久化。

10. UserInterface 接口
用途：抽象输出与输入能力，支持不同终端实现。
主要方法：void print(String)、void println(String)、String readLine(String prompt)；默认方法 int readInt(String prompt) 循环读取整数并提示格式错误。

11. SwingConsole 类（实现 UserInterface）
成员变量：JTextArea outputArea、JTextField inputField、CompletableFuture<String> pendingInput。
用途：基于 Swing 的简易控制台界面，负责展示输出并读取用户输入。
主要方法：
- SwingConsole()：构建窗口、输出区、输入框与提交按钮，注册监听并显示。
- void print(String)、void println(String)：向文本区追加输出。
- String readLine(String prompt)：打印提示后阻塞等待用户提交输入。

12. LibraryApp 主程序
成员变量：Library library、UserInterface ui。
用途：应用入口，基于界面实现驱动业务菜单，支持读者与管理员两种登录与操作流程。
主要方法：
- LibraryApp(UserInterface ui)：注入界面实现并初始化 Library。
- static void main(String[] args)：启动 SwingConsole 并在新线程运行应用。
- run()：主循环，展示主菜单并根据选择进入读者/管理员登录或退出。
- readerLogin()/adminLogin()：处理登录与反馈。
- readerMenu(Reader reader)：读者菜单，提供查看、检索、借书、还书、查看已借书、充值/购VIP 等操作。
- adminMenu()：管理员菜单，提供查看、增删改书、检索与排序。
- printBooks(List<Book>)：格式化列表输出。
- searchBooks()、borrowBook(Reader)、returnBook(Reader)：分别完成检索、借阅、归还的交互。
- addBook()、removeBook()、updateBook()：管理员增删改图书的交互流程。
- readerRecharge(Reader)：充值并可选购买 VIP，成功后保存并显示余额/VIP 状态。
