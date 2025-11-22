package library.app.ui;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * 基于命令行的界面实现，提供彩色输出和更友好的提示。
 */
public class TerminalConsole implements UserInterface {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";

    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream out = System.out;

    public static String colorize(String text, String color) {
        return color + text + RESET;
    }

    public static String highlight(String text) {
        return colorize(text, BOLD + CYAN);
    }

    public static String success(String text) {
        return colorize(text, BOLD + GREEN);
    }

    public static String warning(String text) {
        return colorize(text, BOLD + YELLOW);
    }

    public static String danger(String text) {
        return colorize(text, BOLD + RED);
    }

    public static String info(String text) {
        return colorize(text, BOLD + BLUE);
    }

    @Override
    public void print(String message) {
        out.print(message);
    }

    @Override
    public void println(String message) {
        out.println(message);
    }

    @Override
    public String readLine(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            out.print(info(prompt + " "));
        }
        String line = scanner.nextLine();
        return line == null ? "" : line.trim();
    }
}
