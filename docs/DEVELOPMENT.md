# 图书借阅系统开发文档

## 架构概览
- **Person（抽象基类）**：统一账号、密码、姓名等身份信息，便于管理员与读者的扩展复用。
- **Admin（继承 Person）**：管理员实体，负责图书与读者数据管理。
- **Reader（继承 Person）**：读者实体，包含借阅上限、余额、VIP 状态与借阅记录。
- **Book**：图书实体，覆盖 ISBN、分类、押金、出版信息和借阅状态流水。
- **Library（服务层）**：封装登录、图书增删改查、借阅/归还、排序和数据持久化逻辑。
- **仓储层（Repository）**：基于文本文件的简单持久化，兼容旧数据格式并写入新字段。
- **SwingConsole（界面层）**：基于 Swing 的交互窗口，使用输出框与输入框实现菜单式交互并自动滚动刷新。

## 主要用例与入口
- `library.app.LibraryApp` 为主入口，默认启动 `SwingConsole` 图形界面，并在后台线程运行业务循环。
- 读者功能：
  - 查看/检索图书
  - 借书（校验余额、押金和借阅上限）
  - 还书（归还押金并记录状态）
  - 查看已借列表
  - 充值与购买 VIP（提升借阅上限）
- 管理员功能：
  - 查看/排序全部图书
  - 新增、删除、修改图书（含押金、分类、ISBN 等字段）
  - 关键字检索

## 数据文件格式
- `data/books.txt`
  - 新格式：`id,title,author,publisher,isbn,category,publishDate,pages,deposit,total,available`
  - 兼容旧格式：`id,title,author,publisher,total,available`
- `data/readers.txt`
  - `id,account,password,name,borrowedIds(| 分隔),borrowMax,balance,isVip`
- `data/admins.txt`
  - `id,account,password,name`

## 界面与交互
- Swing 输出框自动滚动，输入框/按钮同步读取用户指令，保持命令式菜单体验。
- 所有提示信息通过 `UserInterface` 抽象输出，方便切换到其它前端实现。

## 扩展建议
- 将图书状态流水持久化至独立文件或数据库。
- 为 `Library` 增加并发锁，保障多人操作一致性。
- 引入表单式 GUI（JPanel + 表格）进一步提升可用性。
