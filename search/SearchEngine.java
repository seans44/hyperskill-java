package search;

import java.io.File;
import java.util.*;

public class SearchEngine {
    static File file;
    static List<String> listOfLines = new ArrayList<>();
    static Map<String, ArrayList<Integer>> words = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);


    public SearchEngine(File file) {
        SearchEngine.file = file;
    }

    public void run() {
        enterPeople();
        menu();
    }

    public static void enterPeople() {
        try (Scanner fileScan = new Scanner(file)) {

            while (fileScan.hasNext()) {
                listOfLines.add(fileScan.nextLine());
            }

            for (int i = 0; i < listOfLines.size(); i++) {
                String[] wordsInLine = listOfLines.get(i).split(" ");
                for (String word : wordsInLine) {
                    if (words.containsKey(word.toLowerCase())) {
                        words.get(word.toLowerCase()).add(i);
                    } else {
                        ArrayList<Integer> numberOfLine = new ArrayList<>();
                        numberOfLine.add(i);
                        words.put(word.toLowerCase(), numberOfLine);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static void menu() {
        boolean programIsRunning = true;
        while (programIsRunning) {
            System.out.println("""
                
                === Menu ===
                1. Find a person
                2. Print all people
                0. Exit""");
            switch (scanner.nextLine()) {
                case "1" : findPerson(); break;
                case "2" : printAllPeople(); break;
                case "0" : programIsRunning = false; System.out.println("\nBye!"); scanner.close(); break;
                default: System.out.println("\nIncorrect option! Try again.");
            }
        }
    }

    private static void printAllPeople() {
        System.out.println("\n=== List of people ===");
        listOfLines.forEach(System.out::println);
    }

    private static void findPerson() {

        System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");
        String strategy = scanner.nextLine();

        System.out.println("\nEnter a name or email to search all suitable people.");
        String[] data = scanner.nextLine().toLowerCase().trim().split(" ");

        PersonFounder personFounder = new PersonFounder();

        while (true) {
            boolean correctInput = true;
            switch (strategy) {
                case "ALL" : personFounder.setSearchingStrategy(new SearchAllStrategy()); break;
                case "ANY" : personFounder.setSearchingStrategy(new SearchAnyStrategy()); break;
                case "NONE" : personFounder.setSearchingStrategy(new SearchNoneStrategy()); break;
                default : System.out.println("Wrong input!"); correctInput = false; break;
            }
            if (correctInput) { break; }
        }

        personFounder.find(data, listOfLines, words);
    }
}

interface SearchingStrategy {
    void search(String[] query, List<String> listOfLines, Map<String, ArrayList<Integer>> words);
}

class SearchAllStrategy implements SearchingStrategy{
    @Override
    public void search(String[] query, List<String> listOfLines, Map<String, ArrayList<Integer>> words) {

        // Creating an array with numbers of lines that contains first word //
        ArrayList<Integer> numbersOfLinesWithFirstWord = new ArrayList<>();
        if (words.containsKey(query[0])) {
            numbersOfLinesWithFirstWord = words.get(query[0]);
        }

        // Filtering the array by checking if there are other words from query //
        if (query.length > 1 && !numbersOfLinesWithFirstWord.isEmpty()) {
            for (int i = 1; i < query.length; i++) {
                for (int j = 0; j < query[i].length(); j++) {
                    if (!numbersOfLinesWithFirstWord.contains(words.get(query[i]).get(j))) {
                        numbersOfLinesWithFirstWord.remove(words.get(query[i]).get(j));
                    }
                }
            }
        }

        // Printing result //
        System.out.println(!numbersOfLinesWithFirstWord.isEmpty() ? numbersOfLinesWithFirstWord.size()
                        + " persons found:\n" : "No matching people found.\n");
        for (int num : numbersOfLinesWithFirstWord) {
            System.out.println(listOfLines.get(num));
        }
    }
}

class SearchAnyStrategy implements SearchingStrategy{
    @Override
    public void search(String[] query, List<String> listOfLines, Map<String, ArrayList<Integer>> words) {
        Set<Integer> numberOfLines = new HashSet<>();
        for (String q : query) {
            if (words.containsKey(q)) {
                numberOfLines.addAll(words.get(q));
            }
        }
        ArrayList<Integer> numbersList = new ArrayList<>(numberOfLines);
        Collections.sort(numbersList);

        // Printing result //
        System.out.println(!numbersList.isEmpty() ? numbersList.size()
                + " persons found:\n" : "No matching people found.\n");
        for (int num : numbersList) {
            System.out.println(listOfLines.get(num));
        }
    }
}

class SearchNoneStrategy implements SearchingStrategy{
    @Override
    public void search(String[] query, List<String> listOfLines, Map<String, ArrayList<Integer>> words) {

        Set<Integer> numberOfLines = new HashSet<>();
        for (String q : query) {
            if (words.containsKey(q)) {
                numberOfLines.addAll(words.get(q));
            }
        }
        ArrayList<Integer> numbersList = new ArrayList<>(numberOfLines);
        Collections.sort(numbersList);

        // Printing result //
        System.out.println(numbersList.size() < listOfLines.size() ? listOfLines.size() - numbersList.size()
                + " persons found:\n" : "No matching people found.\n");
        for (int i = 0; i < listOfLines.size(); i++) {
            if (!numbersList.contains(i)) {
                System.out.println(listOfLines.get(i));
            }
        }
    }
}

class PersonFounder {
    private SearchingStrategy searchingStrategy;

    public void setSearchingStrategy(SearchingStrategy strategy) {
        this.searchingStrategy = strategy;
    }

    public void find(String[] query, List<String> listOfLines, Map<String, ArrayList<Integer>> words) {
        searchingStrategy.search(query, listOfLines, words);
    }
}