# 图书借阅管理系统开发文档

## 一、项目简介
本项目是一个基于 Java 17 的命令行/桌面双界面图书借阅管理系统，通过 Maven 构建，默认主类为 `library.app.LibraryApp`。项目依赖仅包含 Gson（当前代码未直接使用），并通过 `exec-maven-plugin` 指定程序入口。【F:pom.xml†L1-L44】

系统核心功能涵盖读者登录借阅、管理员图书维护、数据持久化以及基础搜索与排序能力，使用文本文件作为存储介质，提供终端控制台与 Swing 图形窗口两种交互方式。【F:src/main/java/library/app/LibraryApp.java†L12-L281】【F:src/main/java/library/app/ui/TerminalConsole.java†L6-L63】【F:src/main/java/library/app/ui/SwingConsole.java†L9-L70】

## 二、功能总览
- **身份管理**：读者与管理员登录校验。【F:src/main/java/library/service/Library.java†L68-L80】
- **图书浏览与检索**：查看全部图书、按书名关键字搜索。【F:src/main/java/library/app/LibraryApp.java†L166-L193】【F:src/main/java/library/service/Library.java†L82-L95】
- **借阅/归还**：校验余额、借阅上限与库存，记录状态并结算押金。【F:src/main/java/library/service/Library.java†L97-L143】
- **读者侧账户操作**：查询已借图书、充值、购买 VIP 提升借阅额度。【F:src/main/java/library/app/LibraryApp.java†L112-L281】【F:src/main/java/library/model/Reader.java†L7-L93】
- **管理员侧图书维护**：新增、删除、修改图书，支持按编号与书名排序展示。【F:src/main/java/library/app/LibraryApp.java†L138-L269】【F:src/main/java/library/service/Library.java†L152-L199】
- **数据持久化**：图书、读者、管理员均通过文本文件加载与保存，缺失时自动生成默认数据集。【F:src/main/java/library/service/Library.java†L27-L66】【F:src/main/java/library/repository/Repository.java†L28-L65】

## 三、代码结构说明
- `library.app`：应用启动与流程控制。`LibraryApp` 负责菜单、输入处理与业务调用。【F:src/main/java/library/app/LibraryApp.java†L12-L281】
- `library.app.ui`：界面抽象与实现。`UserInterface` 定义 IO 接口，`TerminalConsole` 实现命令行彩色输出，`SwingConsole` 提供简单 Swing 窗口。【F:src/main/java/library/app/ui/UserInterface.java†L1-L28】【F:src/main/java/library/app/ui/TerminalConsole.java†L6-L63】【F:src/main/java/library/app/ui/SwingConsole.java†L9-L70】
- `library.service`：业务核心。`Library` 聚合仓储、加载初始数据并提供借阅、查询、排序等操作。【F:src/main/java/library/service/Library.java†L17-L206】
- `library.model`：领域模型。`Person` 抽象公共身份信息，`Reader`、`Admin` 继承；`Book` 描述图书及库存状态。【F:src/main/java/library/model/Person.java†L5-L71】【F:src/main/java/library/model/Reader.java†L6-L93】【F:src/main/java/library/model/Admin.java†L3-L15】【F:src/main/java/library/model/Book.java†L8-L183】
- `library.repository`：基于文本的持久化仓储抽象与实现，分别对应图书、读者、管理员文件。【F:src/main/java/library/repository/Repository.java†L12-L65】【F:src/main/java/library/repository/BookRepository.java†L9-L57】【F:src/main/java/library/repository/ReaderRepository.java†L12-L56】【F:src/main/java/library/repository/AdminRepository.java†L8-L38】
- `data/`：运行时数据文件输出目录（如 `books.txt`、`readers.txt`、`admins.txt`）。【F:src/main/java/library/repository/Repository.java†L20-L55】

## 四、类设计说明
### 类与关系概览（文本 UML）
```
Person (abstract)
├── Reader
│   └─ fields: borrowedBookIds, borrowMax, balance, vip
│   └─ methods: borrowBook(), returnBook(), recharge(), buyVip()
├── Admin
Book
Library
├─ uses BookRepository, ReaderRepository, AdminRepository
├─ manages List<Book>, List<Reader>, List<Admin>
UserInterface (interface)
├── TerminalConsole
└── SwingConsole
Repository<T> (abstract)
├── BookRepository
├── ReaderRepository
└── AdminRepository
LibraryApp
├─ composes Library, UserInterface
```

### 关键类说明
- **Person**：抽象基类，封装 id、account、password、name 与等值比较逻辑，被 `Reader`、`Admin` 继承。【F:src/main/java/library/model/Person.java†L5-L71】
- **Reader**：扩展借阅列表、借阅上限、余额及 VIP 标志；提供借还书校验、充值与购买 VIP 行为。【F:src/main/java/library/model/Reader.java†L6-L93】
- **Admin**：无新增字段，仅复用 `Person` 字段，并重写 `toString` 展示身份。【F:src/main/java/library/model/Admin.java†L3-L15】
- **Book**：记录图书元数据、押金、总量与可借数量，并保存状态历史；包含借出/归还库存更新与字符串展示。【F:src/main/java/library/model/Book.java†L8-L183】
- **Library**：业务聚合层，加载/初始化数据，提供登录验证、图书查询、借还、增删改、排序与统一保存接口，是 UI 与仓储之间的服务桥梁。【F:src/main/java/library/service/Library.java†L17-L205】
- **Repository<T>**：文本存储通用抽象，定义 `loadAll`/`saveAll`、`parse`、`format` 模板方法，子类实现序列化细节。【F:src/main/java/library/repository/Repository.java†L12-L65】
- **BookRepository/ReaderRepository/AdminRepository**：实现各自模型的解析与格式化规则，绑定具体文件路径。【F:src/main/java/library/repository/BookRepository.java†L9-L57】【F:src/main/java/library/repository/ReaderRepository.java†L12-L56】【F:src/main/java/library/repository/AdminRepository.java†L8-L38】
- **UserInterface** 及实现：接口约束输入输出方法并提供数字读取默认实现，`TerminalConsole` 负责彩色命令行交互，`SwingConsole` 提供图形化输入输出。【F:src/main/java/library/app/ui/UserInterface.java†L1-L28】【F:src/main/java/library/app/ui/TerminalConsole.java†L6-L63】【F:src/main/java/library/app/ui/SwingConsole.java†L9-L70】
- **LibraryApp**：程序入口，构建 UI 与 `Library` 服务，组织主菜单、读者/管理员子菜单与对应操作调用，同时负责退出时持久化。【F:src/main/java/library/app/LibraryApp.java†L12-L281】

## 五、接口与抽象设计
- `UserInterface` 定义输出与读取输入的最小契约，并提供 `readInt`、`readDouble` 默认校验循环，便于在终端与 GUI 间切换而不影响业务逻辑。【F:src/main/java/library/app/ui/UserInterface.java†L1-L28】
- `Repository<T>` 通过模板方法模式抽象文本存取流程，由具体仓储实现序列化/反序列化策略，`Library` 只依赖抽象操作，便于扩展新的存储实现。【F:src/main/java/library/repository/Repository.java†L12-L65】【F:src/main/java/library/service/Library.java†L17-L205】

## 六、持久化模块设计
- **文件格式**：文本按行存储，每行用逗号分隔字段；读者的借阅列表进一步用 `|` 分隔。默认路径位于 `data/books.txt`、`data/readers.txt`、`data/admins.txt`。【F:src/main/java/library/repository/BookRepository.java†L9-L57】【F:src/main/java/library/repository/ReaderRepository.java†L12-L56】【F:src/main/java/library/repository/AdminRepository.java†L8-L38】
- **序列化/反序列化**：`format` 将对象字段拼接为字符串；`parse` 负责拆分、类型转换与容错（长度不足或数值异常会跳过记录并打印提示）。【F:src/main/java/library/repository/Repository.java†L28-L55】【F:src/main/java/library/repository/BookRepository.java†L18-L40】【F:src/main/java/library/repository/ReaderRepository.java†L21-L42】【F:src/main/java/library/repository/AdminRepository.java†L17-L28】
- **数据加载流程**：`Library` 构造时实例化三个仓储，调用 `loadAll` 读取列表；任一列表为空则初始化默认数据后立即持久化，确保基础数据可用。【F:src/main/java/library/service/Library.java†L27-L66】
- **数据保存流程**：所有业务写操作完成后，`Library.saveAll` 统一调用三个仓储的 `saveAll` 写回文件；UI 退出时也会保存。【F:src/main/java/library/service/Library.java†L152-L205】【F:src/main/java/library/app/LibraryApp.java†L34-L38】
- **容错与一致性**：解析阶段对格式或类型异常的行进行忽略并输出告警；删除图书时同步清理读者借阅列表；更新图书时根据已借数量重新计算可借库存，避免出现负库存或超量。【F:src/main/java/library/repository/Repository.java†L28-L55】【F:src/main/java/library/service/Library.java†L161-L187】

## 七、全局搜索模块设计
- **搜索范围**：当前仅支持按书名关键字在全量图书列表中检索。【F:src/main/java/library/service/Library.java†L90-L95】
- **算法与匹配**：将关键字与书名统一转换为小写，使用 `contains` 进行子串匹配，返回所有匹配的 `Book` 列表。【F:src/main/java/library/service/Library.java†L90-L95】
- **输出结构**：`LibraryApp` 将结果格式化为表格行展示编号、书名、作者、分类、出版社、可借/总数量；空结果会提示“没有找到图书”。【F:src/main/java/library/app/LibraryApp.java†L166-L188】
- **流程**：UI 读取关键字 → 调用 `Library.searchBooksByTitle` → 返回列表 → 统一由 `printBooks` 渲染。【F:src/main/java/library/app/LibraryApp.java†L190-L193】【F:src/main/java/library/service/Library.java†L90-L95】

## 八、程序主流程
1. 程序入口创建 `TerminalConsole` 与 `Library`，进入主循环菜单；退出时统一保存数据。【F:src/main/java/library/app/LibraryApp.java†L21-L42】
2. **读者流程**：登录成功后可在子菜单浏览全部书籍、搜索、借书、还书、查看已借列表、充值与购买 VIP，所有操作经 `Library` 完成并自动保存影响余额/库存的结果。【F:src/main/java/library/app/LibraryApp.java†L112-L281】【F:src/main/java/library/service/Library.java†L97-L205】
3. **管理员流程**：登录后可新增/删除/修改图书，按编号或书名排序查看，并可使用搜索功能；变更后调用 `saveAll` 持久化。【F:src/main/java/library/app/LibraryApp.java†L138-L269】【F:src/main/java/library/service/Library.java†L152-L199】

## 九、可扩展性分析
- **界面替换**：`UserInterface` 抽象使得新增 Web 控制台或其他前端实现时无需修改业务层。【F:src/main/java/library/app/ui/UserInterface.java†L1-L28】【F:src/main/java/library/app/LibraryApp.java†L12-L281】
- **存储策略扩展**：`Repository` 抽象与 `Library` 的依赖注入允许添加新的文件格式或数据库实现，只需提供对应 `parse/format` 即可接入。【F:src/main/java/library/repository/Repository.java†L12-L65】【F:src/main/java/library/service/Library.java†L17-L66】
- **领域模型扩展**：`Book` 与 `Reader` 构造函数和 `format/parse` 逻辑集中，新增字段时可同步更新模型与序列化方法；借阅上限与押金策略均在 `Reader`/`Library` 中集中处理，便于调整业务规则。【F:src/main/java/library/model/Book.java†L8-L183】【F:src/main/java/library/model/Reader.java†L6-L93】【F:src/main/java/library/repository/BookRepository.java†L18-L57】【F:src/main/java/library/repository/ReaderRepository.java†L21-L56】
- **排序/搜索策略**：`Library` 中排序与搜索使用标准比较器，可替换为更多字段或复合排序；新增搜索维度可在 `Library` 添加方法并复用 `LibraryApp.printBooks` 展示。【F:src/main/java/library/service/Library.java†L90-L199】【F:src/main/java/library/app/LibraryApp.java†L166-L193】

## 十、总结
项目以清晰的分层（UI 抽象、服务层、仓储层、领域模型）实现基础图书借阅功能，菜单驱动流程简单直接。文本文件持久化便于快速落地，同时通过默认数据保障首次运行体验。改进方向包括：
- 引入单元测试与异常处理优化，提高可靠性。
- 扩展持久化为数据库或二进制格式以增强并发与安全性。
- 完善权限控制与日志记录，提升审计能力。
- 丰富搜索、排序与统计报表以支持复杂运营需求。
