package asciimirror;
import java.util.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the file path:");
        String filePath = scanner.nextLine();
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();
        List<Character> chars = new ArrayList<>(Arrays.asList('<', '>', '[', ']', '{', '}', '(', ')', '/', '\\'));

        try (Scanner scanFile = new Scanner(file)) {
            while (scanFile.hasNext()) {
               lines.add(scanFile.nextLine());
            }
            int longestLine = 0;
            for (String line : lines) {
                longestLine = Math.max(line.length(), longestLine);
            }
            for (String line : lines) {
                line += " ".repeat(Math.max(0, longestLine - line.length()));
                StringBuilder reversedLine = new StringBuilder();
                for (int i = line.length() - 1; i >= 0; i--) {
                    if (chars.contains(line.charAt(i))) {
                        reversedLine.append(chars.indexOf(line.charAt(i)) % 2 == 0 ?
                                chars.get(chars.indexOf(line.charAt(i)) + 1) :
                                chars.get(chars.indexOf(line.charAt(i)) - 1));
                    } else {
                        reversedLine.append(line.charAt(i));
                    }
                }
                System.out.println(line + " | " + reversedLine);
            }
        } catch (Exception e) {
            System.out.println("File not found!");
        }
    }
}