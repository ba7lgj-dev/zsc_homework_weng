package library.repository;

import library.model.Reader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderRepository extends Repository<Reader> {
    public ReaderRepository() {
        this(Paths.get("data", "readers.txt"));
    }

    public ReaderRepository(Path filePath) {
        super(filePath);
    }

    @Override
    protected Reader parse(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            return null;
        }
        try {
            List<Integer> borrowedIds = new ArrayList<>();
            if (parts.length >= 5 && !parts[4].isEmpty()) {
                borrowedIds = Arrays.stream(parts[4].split("\\|"))
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
            }
            int borrowMax = parts.length >= 6 ? Integer.parseInt(parts[5]) : 5;
            double balance = parts.length >= 7 ? Double.parseDouble(parts[6]) : 0;
            boolean vip = parts.length >= 8 && Boolean.parseBoolean(parts[7]);
            return new Reader(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], borrowedIds, borrowMax, balance, vip);
        } catch (NumberFormatException e) {
            System.out.println("跳过格式错误的读者记录: " + line);
            return null;
        }
    }

    @Override
    protected String format(Reader reader) {
        return String.join(",",
                String.valueOf(reader.getId()),
                reader.getAccount(),
                reader.getPassword(),
                reader.getName(),
                reader.getBorrowedBookIds().stream().map(String::valueOf).collect(Collectors.joining("|")),
                String.valueOf(reader.getBorrowMax()),
                String.valueOf(reader.getBalance()),
                String.valueOf(reader.isVip()));
    }
}
