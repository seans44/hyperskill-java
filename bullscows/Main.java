package bullscows;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int numLength;
        int possibleSymbols;
        String input;

        // Taking information about the secret code //
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the length of the secret code:");
        input = scanner.nextLine();
        try {
            numLength = Integer.parseInt(input);
            if (numLength < 1) {
                System.out.println("Error: minimum number of possible symbols in the code is 1 (0-9, a-z).\"");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error: \"" + input + "\" isn't a valid number");
            return;
        }

        System.out.println("Input the number of possible symbols in the code:");
        try {
            possibleSymbols = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Error: \"" + input + "\" isn't a valid number");
            return;
        }

        if (numLength > possibleSymbols) {
            System.out.println("Error: it's not possible to generate a code with a length of " +
                    numLength + " with " + possibleSymbols + " unique symbols.");
            return;
        } else if (possibleSymbols > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        }

        // Creating an array of possible symbols and secret number //
        char[] arrayOfSymbols = new char[possibleSymbols];
        for (int i = 0; i < possibleSymbols; i++) {
            if (i < 10) {
                arrayOfSymbols[i] = (char) ('0' + i);
            } else {
                arrayOfSymbols[i] = (char) ('W' + i);
            }
        }
        String secretNumber = createSecretNum(numLength, arrayOfSymbols).toString();

        // Printing the message with prepared secret code //
        StringBuilder codeIsPrepared = new StringBuilder(" (0-9).");
        if (arrayOfSymbols.length <= 10) {
            codeIsPrepared.replace(4, 5, "" + arrayOfSymbols[arrayOfSymbols.length - 1]);
        } else {
            codeIsPrepared.replace(5, 5, ", a-" + arrayOfSymbols[arrayOfSymbols.length - 1]);
        }
        String stars = "*".repeat(Math.max(0, numLength));
        System.out.println("The secret is prepared: " + stars + codeIsPrepared);

        // Starting the game //
        System.out.println("Okay, let's start a game!");
        int turn = 1;
        String userInput = "";
        while (!userInput.equals(secretNumber)) {
            System.out.println("Turn " + turn + ":");
            userInput = scanner.nextLine();
            checkTheCode(secretNumber, userInput);
            turn++;
        }
        System.out.println("Congratulations! You guessed the secret code.");
    }

    public static StringBuilder createSecretNum (int numLength, char[] arrayOfSymbols) {
        StringBuilder secretNumber = new StringBuilder();
        char[] shuffledArray = shuffleArray(arrayOfSymbols);
        for (int i = 0; i < numLength; i++) {
            secretNumber.append(shuffledArray[i]);
        }
        return secretNumber;
    }

    public static void checkTheCode (String secretNumber, String userInput) {
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < userInput.length(); i++) {
            if (userInput.charAt(i) == secretNumber.charAt(i)) {
                bulls++;
                cows--;
            }
            for (int j = 0; j < userInput.length(); j++) {
                if (secretNumber.charAt(j) == userInput.charAt(i)) {
                    cows++;
                }
            }
        }

        String bullOrBulls = bulls == 1 ? " bull" : " bulls";
        String cowOrCows = cows == 1 ? " cow" : " cows";
        if (bulls == secretNumber.length()) {
            System.out.println("Grade: " + bulls + bullOrBulls);
        } else {
            System.out.println("Grade: " + bulls + bullOrBulls + " and " + cows + cowOrCows);
        }
    }

    public static char[] shuffleArray(char[] array) {
        char[] newArray = new char[array.length];
        ArrayList<Integer> numbers = new ArrayList<>();

        // Fill NUMBERS ARRAY with 0 ~ array.length //
        for (int i = 0; i < array.length; i++) {
            numbers.add(i);
        }

        // Writing new array //
        for (int i = 0; i < array.length ; i++) {
            Random random = new Random();
            int rnd = random.nextInt(numbers.size());
            newArray[i] = array[numbers.get(rnd)];
            numbers.remove(rnd);
        }

        return newArray;
    }
}
