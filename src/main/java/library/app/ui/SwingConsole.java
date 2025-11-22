package library.app.ui;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SwingConsole implements UserInterface {
    private final JTextArea outputArea = new JTextArea();
    private final JTextField inputField = new JTextField();
    private CompletableFuture<String> pendingInput = new CompletableFuture<>();

    public SwingConsole() {
        JFrame frame = new JFrame("图书借阅系统");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton submit = new JButton("提交");
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(submit, BorderLayout.EAST);
        frame.add(bottom, BorderLayout.SOUTH);

        submit.addActionListener(e -> submitInput());
        inputField.addActionListener(e -> submitInput());

        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void submitInput() {
        String text = inputField.getText();
        inputField.setText("");
        if (pendingInput != null && !pendingInput.isDone()) {
            pendingInput.complete(text);
        }
    }

    @Override
    public void print(String message) {
        outputArea.append(message);
    }

    @Override
    public void println(String message) {
        outputArea.append(message + "\n");
    }

    @Override
    public String readLine(String prompt) {
        println(prompt);
        pendingInput = new CompletableFuture<>();
        try {
            return pendingInput.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        } catch (ExecutionException e) {
            println("读取输入失败：" + e.getMessage());
            return "";
        }
    }
}
