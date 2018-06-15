package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FMLogger {
    public static String filePath = "src/main/resources/messages.log";
    
    public static void log(String message) {
        if (Files.notExists(Paths.get(filePath))) {
            try {
                Files.createFile(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteLogs() {
        try {
            //noinspection ResultOfMethodCallIgnored
            Files.walk(Paths.get(filePath))
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
        } catch (IOException e) {
            // Ignore exception - exception is due to logging directory/file is already deleted
            // therefore cannot be deleted.
        }
    }
}
