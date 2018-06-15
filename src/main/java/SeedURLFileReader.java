import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SeedURLFileReader {
    /**
     * The data set must be in a one-line-per-url format, must start with http:// or https:// and not with #.
     * Lines starting with # are declared as comments and filtered out.
     *
     * @return a list of seed URLs, trimmed and in lower case form, extracted from a file
     * ("~/Dev/ProjectIR/src/main/resources/seed.txt").
     */
    static List<String> read() {
        String fileName = getFilePath();
        List<String> urls = new ArrayList<>();
        
        if (Files.notExists(Paths.get(fileName))) {
            return urls;
        }
        
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            urls = stream
                    // Excludes lines that start with # as that is used for comments
                    // and must start with http:// as that is required by crawler4j.
                    .filter(line -> !line.startsWith("#") &&
                            line.startsWith("http://") || line.startsWith("https://"))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return urls;
    }
    
    static String getFilePath() {
        return System.getProperty("user.home") + "/Dev/ProjectIR/src/main/resources/seed.txt";
    }
}
