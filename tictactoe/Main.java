package tictactoe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.print("Input command: ");
            String[] parameters = scanner.nextLine().split("\\s+");
            switch (parameters[0]) {
                case "exit" :
                    exitProgram = true;
                    break;
                case "start" :
                    String possibleInput = "easy|medium|hard|user";
                    try {
                        if (parameters[1].matches(possibleInput) && parameters[2].matches(possibleInput)) {
                            startGame(parameters[1], parameters[2]);
                        }
                    } catch (Exception ignore) {
                        System.out.println("Bad parameters!");
                    }
                    break;
                default:
                    System.out.println("Bad parameters!");
                    break;
            }
        }
    }

    private static void startGame(String param1, String param2) {

        char[][] table = new char[3][3];
        for (char[] chars : table) { Arrays.fill(chars, ' '); }
        char turn = 'X';

        Player player1 = param1.equals("user") ? new User() : new AI(param1, 'X');
        Player player2 = param2.equals("user") ? new User() : new AI(param2, 'O');


        while (true) {
            printTable(table);
            if (checkState(table).equals("Game not finished")) {
                if (turn == 'X') {
                    int[] xy = player1.getCoordinates(table);
                    table[xy[0]][xy[1]] = 'X';
                    turn = 'O';
                } else {
                    int[] xy = player2.getCoordinates(table);
                    table[xy[0]][xy[1]] = 'O';
                    turn = 'X';
                }
            } else {
                System.out.println(checkState(table) + "\n");
                break;
            }
        }
    }

    private static String checkState(char[][] table) {
        boolean xWins = checkIfWins('X', table);
        boolean oWins = checkIfWins('O', table);
        boolean tableIsCompleted = true;
        for (char[] chars : table) {
            for (char aChar : chars) {
                if (aChar == ' ') {
                    tableIsCompleted = false;
                    break;
                }
            }
        }
        boolean draw = !xWins && !oWins && tableIsCompleted;
        boolean gameNotFinished = !xWins && !oWins && !tableIsCompleted;
        return gameNotFinished ? "Game not finished" : draw ? "Draw" : xWins ? "X wins" : "O wins";
    }

    private static boolean checkIfWins(char c, char[][] table) {
        return (table[0][0] == c && table[0][1] == c && table[0][2] == c) ||
               (table[1][0] == c && table[1][1] == c && table[1][2] == c) ||
               (table[2][0] == c && table[2][1] == c && table[2][2] == c) ||
               (table[0][0] == c && table[1][0] == c && table[2][0] == c) ||
               (table[0][1] == c && table[1][1] == c && table[2][1] == c) ||
               (table[0][2] == c && table[1][2] == c && table[2][2] == c) ||
               (table[0][0] == c && table[1][1] == c && table[2][2] == c) ||
               (table[0][2] == c && table[1][1] == c && table[2][0] == c);
    }

    public static void printTable(char[][] table) {
        System.out.println("---------");
        for (char[] chars : table) {
            System.out.print("| ");
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public static class Player {
        public int[] getCoordinates(char[][] table) {
            return new int[2];
        }
    }

    public static class User extends Player {
        @Override
        public int[] getCoordinates(char[][] table) {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter the coordinates: ");
                String coordinates = scanner.nextLine();
                if (coordinates.matches("\\d.*\\d")) {
                    String[] xy = coordinates.split("\\s");
                    if (xy[0].matches("[123]") && xy[1].matches("[123]")) {
                        if (table[Integer.parseInt(xy[0]) - 1][Integer.parseInt(xy[1]) - 1] == ' ') {
                            return new int[] {Integer.parseInt(xy[0]) - 1, Integer.parseInt(xy[1]) - 1};
                        } else {
                            System.out.println("This cell is occupied! Choose another one!");
                        }
                    } else {
                        System.out.println("Coordinates should be from 1 to 3!");
                    }
                } else {
                    System.out.println("You should enter numbers!");
                }
            }
        }
    }

    public static class AI extends Player{
        private final String level;
        private final char turn;

        public AI(String level, char turn) {
            this.level = level;
            this.turn = turn;
        }

        @Override
        public int[] getCoordinates(char[][] table) {
            System.out.printf("Making move level \"%s\"\n", this.level);
            int[] coordinates = new int[0];
            switch (this.level) {
                case "easy" :
                    coordinates = makeMoveEasyLevel(table);
                    break;
                case "medium" :
                    coordinates = makeMoveMediumLevel(table, turn);
                    break;
                case "hard" :
                    coordinates = makeMoveHardLevel(table, turn);
                    break;
                default :
                    break;
            }
            return coordinates;
        }

        private int[] makeMoveHardLevel(char[][] table, char turn) {
            int[] bestMove = new int[] {-1, -1};
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    if (table[i][j] == ' ') {
                        table[i][j] = turn;
                        int score = minimax(table, false, turn == 'X' ? 'O' : 'X');
                        table[i][j] = ' ';

                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new int[] {i, j};
                        }
                    }
                }
            }

            return bestMove;
        }

        private int minimax(char[][] table, boolean isMaximizing, char turn) {
            if (isMaximizing) {
                if (checkIfWins(turn, table)) {
                    return 1;
                } else if (checkIfWins(turn == 'X' ? 'O' : 'X', table)) {
                    return -1;
                } else if (checkState(table).equals("Draw")) {
                    return 0;
                }
            } else {
                if (checkIfWins(turn == 'X' ? 'O' : 'X', table)) {
                    return 1;
                } else if (checkIfWins(turn, table)) {
                    return -1;
                } else if (checkState(table).equals("Draw")) {
                    return 0;
                }
            }


            int bestScore;
            if (isMaximizing) {
                bestScore = Integer.MIN_VALUE;
                for (int i = 0; i < table.length; i++) {
                    for (int j = 0; j < table[i].length; j++) {
                        if (table[i][j] == ' ') {
                            table[i][j] = turn;
                            int score = minimax(table, false, turn == 'X' ? 'O' : 'X');
                            table[i][j] = ' ';
                            bestScore = Math.max(score, bestScore);
                        }
                    }
                }
            } else {
                bestScore = Integer.MAX_VALUE;
                for (int i = 0; i < table.length; i++) {
                    for (int j = 0; j < table[i].length; j++) {
                        if (table[i][j] == ' ') {
                            table[i][j] = turn;
                            int score = minimax(table, true, turn == 'X' ? 'O' : 'X');
                            table[i][j] = ' ';
                            bestScore = Math.min(score, bestScore);
                        }
                    }
                }
            }
            return bestScore;
        }

        public static int[] makeMoveEasyLevel (char[][] table) {
            int[] coordinates;
            do {
                coordinates = new int[]{new Random().nextInt(3), new Random().nextInt(3)};
            } while (table[coordinates[0]][coordinates[1]] != (' '));
            return coordinates;
        }

        public static int[] makeMoveMediumLevel (char[][] table, char currentTurn) {
            char previousTurn = currentTurn == 'X' ? 'O' : 'X';
            char[] turns = {currentTurn, previousTurn};
            String[] lines = {
                    String.valueOf(table[0][0]) + table[0][1] + table[0][2],
                    String.valueOf(table[1][0]) + table[1][1] + table[1][2],
                    String.valueOf(table[2][0]) + table[2][1] + table[2][2],
                    String.valueOf(table[0][0]) + table[1][0] + table[2][0],
                    String.valueOf(table[0][1]) + table[1][1] + table[2][1],
                    String.valueOf(table[0][2]) + table[1][2] + table[2][2],
                    String.valueOf(table[0][0]) + table[1][1] + table[2][2],
                    String.valueOf(table[2][0]) + table[1][1] + table[0][2]
            };

            for (char turn : turns) {
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].matches(" " + turn + turn)) {
                        return i < 3 ? new int[] {i, 0} :
                                i < 6 ? new int[] {0, i - 3} :
                                i == 6 ? new int[] {0, 0} : new int[] {2, 0};
                    } else if (lines[i].matches("" + turn + turn + " ")) {
                        return i < 3 ? new int[] {i, 2} :
                                i < 6 ? new int[] {2, i - 3} :
                                i == 6 ? new int[] {2, 2} : new int[] {0, 2};
                    } else if (lines[i].matches(turn + " " + turn)) {
                        return i < 3 ? new int[] {i, 1} :
                                i < 6 ? new int[] {1, i - 3} : new int[] {1, 1};
                    }
                }
            }
            return makeMoveEasyLevel(table);
        }
    }
}