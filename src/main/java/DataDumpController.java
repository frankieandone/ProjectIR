import log.FMLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataDumpController {
    static String filePath = System.getProperty("user.home") + "/data/ProjectIR/";
    
    static void deleteDataDump() {
        try {
            //noinspection ResultOfMethodCallIgnored
            Files.walk(Paths.get(filePath))
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
            FMLogger.log("#deleteDataDump successfully deleted data dump.");
        } catch (IOException e) {
            // Ignore exception - exception is due to data dump directory/file is already deleted
            // therefore cannot be deleted.
        }
    }
}
