package library.app.ui;

public interface UserInterface {
    void print(String message);

    void println(String message);

    String readLine(String prompt);

    default int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                println("输入格式错误，请输入数字。");
            }
        }
    }
}
