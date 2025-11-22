package library.app.ui;

import java.util.Scanner;

public class ConsoleInterface implements UserInterface {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public String readLine(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }
}
