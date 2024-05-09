package search;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        File file = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("--data")) {
                String filePath = args[i + 1];
                try {
                    file = new File(filePath);
                } catch (Exception ignored) {
                }
                break;
            }
        }

        SearchEngine engine = new SearchEngine(file);
        engine.run();
    }
}
