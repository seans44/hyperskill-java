package readability;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String text = readFileAsString(args[0]);
        System.out.println("The text is:\n" + text + "\n");
        printResult(text);
    }

    public static String readFileAsString(String filePath) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Scanner scanner = new Scanner(new File(filePath));

            while (scanner.hasNext()) {
                stringBuilder.append(scanner.next()).append(" ");
            }

            String fileContent = stringBuilder.toString();
            scanner.close();
            return fileContent;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printResult(String text) {
        int words = countWords(text);
        int sentences = countSentences(text);
        int characters = countCharacters(text);
        int syllables = newCountSyllables(text)[0];
        int polysyllables = newCountSyllables(text)[1];
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        System.out.println();

        String[] commands = { "ARI", "FK", "SMOG", "CL", "all" };
        double[] indices = calculateAllIndices(words, sentences, characters, syllables, polysyllables);
        String[] nameOfTests = { "Automated Readability Index", "Flesch–Kincaid readability tests",
                "Simple Measure of Gobbledygook", "Coleman–Liau index" };
        int age;
        int averageAge = 0;
        StringBuilder[] resultForPrint = new StringBuilder[commands.length];
        resultForPrint[commands.length - 1] = new StringBuilder();

        for (int i = 0; i < commands.length - 1; i++) {
            age = (int) (Math.ceil(indices[i]) >= 14 ? Math.ceil(indices[i]) + 8 : Math.ceil(indices[i]) + 5);
            averageAge += age;
            resultForPrint[i] = new StringBuilder("%s: %.2f (about %d-year-olds).\n"
                    .formatted(nameOfTests[i], indices[i], age));
            resultForPrint[commands.length - 1].append(resultForPrint[i]);
        }
        for (String option : commands) {
            if (command.equals(option)) {
                System.out.println(resultForPrint[Arrays.asList(commands).indexOf(option)].toString());
            }
        }
        
        System.out.printf("This text should be understood in average by %.2f-year-olds.",
                ((double) averageAge / 4));
    }

    public static int countSentences(String text) {
        String[] sentences = text.split("[.?!]");
        int count = sentences.length;
        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) {
                count--;
            }
        }
        return count;
    }

    public static int countWords(String text) {
        String[] words = text.split("\\s+");
        return words.length;
    }

    public static int countCharacters(String text) {
        String textWithoutSpaces = text.replaceAll("[ \n\t]", "");
        return textWithoutSpaces.length();
    }

    public static int[] newCountSyllables(String text) {
        String[] words = text.split("\\W+");
        int countAllSyllables = 0;
        int countPolysyllables = 0;
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            int countCurrentWord = 0;
            word = word.replaceAll("e\\b", "").replaceAll("[aeiouyAEIOUY]+", "e");
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == 'e') {
                    countCurrentWord++;
                }
            }
            if (countCurrentWord > 2) {
                countPolysyllables++;
            }
            countCurrentWord = countCurrentWord == 0 ? 1 : countCurrentWord;
            countAllSyllables += countCurrentWord;
        }
        return new int[]{countAllSyllables, countPolysyllables};
    }

    public static double[] calculateAllIndices(int words, int sentences, int characters, int syllables,
                                               int polysyllables) {
        double indexARI = 4.71 * ((double) characters / words) + 0.5 * ((double) words / sentences) - 21.43;
        double indexFK = 0.39 * ((double) words / sentences) + 11.8 * ((double) syllables / words) - 15.59;
        double indexSMOG = 1.043 * Math.sqrt((double) polysyllables * ((double) 30 / sentences)) + 3.1291;
        double indexCL = (0.0588 * ((double) characters / words * 100) - 0.296 * ((double) sentences / words
                * 100)) - 15.8;
        return new double[]{indexARI, indexFK, indexSMOG, indexCL};
    }
}