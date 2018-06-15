import java.util.regex.Pattern;

class URLFilters {
    static final Pattern URL_FILTER = constructFilterPattern();
    
    private static Pattern constructFilterPattern() {
        String[] excludedUrls = {
                // Image.
                "mng", "pct", "bmp", "gif", "jpg", "jpeg", "png", "pst", "psp", "tif",
                "tiff", "ai", "drw", "dxf", "eps", "ps", "svg",
                
                // Video
                "3gp", "asf", "asx", "avi", "mov", "mp4", "mpg", "qt", "rm", "swf", "wmv",
                "m4a",
                
                // Audio.
                "mp3", "wma", "ogg", "wav", "ra", "aac", "mid", "au", "aiff",
                
                // Misc.
                "css", "js", "pdf", "doc", "exe", "bin", "rss", "zip", "rar"
        };
        StringBuilder urlsToFilter = new StringBuilder();
        for (int i = 0; i < excludedUrls.length; i++) {
            urlsToFilter.append(excludedUrls[i].toLowerCase());
            if (i != excludedUrls.length - 1) {
                urlsToFilter.append("|");
            }
        }
        return Pattern.compile("\".*(\\.(" + urlsToFilter.toString() + "))$");
    }
}
