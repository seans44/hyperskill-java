package minesweeper;

import java.util.*;

class Minesweeper {
    int width;
    int height;
    final char empty = '0';
    int minesAmount;
    char[][] originalField;
    char[][] exploredField;
    boolean gameFailed = false;
    Set<List<Integer>> mines;

    public Minesweeper(int width, int height) {
        this.width = width;
        this.height = height;
        this.originalField = new char[height][width];
        this.exploredField = new char[height][width];
        this.mines = new HashSet<>();
    }

    public void play() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");
        minesAmount = Integer.parseInt(scanner.nextLine());
        fillAllFields();
        printField(exploredField);

        boolean isFirstMove = true;

        while (true) {
            String move;
            while (true) {
                System.out.print("Set/unset mines marks or claim a cell as free: ");
                move = scanner.nextLine();
                if (!move.matches("[1-" + height + "]\\s+[1-" + width + "]\\s+(free|mine)")) {
                    System.out.println("Wrong input!");
                } else {
                    break;
                }
            }

            String[] moveArray = move.split("\\s+");
            int x = Integer.parseInt(moveArray[1]) - 1;
            int y = Integer.parseInt(moveArray[0]) - 1;
            String command = moveArray[2];

            if (command.matches("free")) {
                if (isFirstMove) {
                    originalField[x][y] = '/';
                    placeMines();
                    originalField[x][y] = empty;
                    updateNeighbours();
                    isFirstMove = false;
                }
                explore(x, y);
            } else {
                markMine(x, y);
            }

            printField(exploredField);

            if (gameWon()) {
                System.out.println("Congratulations! You found all the mines!");
                break;
            } else if (gameFailed) {
                System.out.println("You stepped on a mine and failed!");
                break;
            }
        }
    }

    private void fillAllFields() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                originalField[i][j] = empty;
                exploredField[i][j] = '.';
            }
        }
    }

    private void markMine(int x, int y) {
        switch (exploredField[x][y]) {
            case '.':
                exploredField[x][y] = '*';
                break;
            case '*':
                exploredField[x][y] = '.';
                break;
            default:
                System.out.println("Choose another cell!");
                break;
        }
    }

    private void explore(int x, int y) {
        if (originalField[x][y] == 'X') {
            for (List<Integer> mine : mines) {
                exploredField[mine.get(0)][mine.get(1)] = 'X';
            }
            gameFailed = true;
        } else if (exploredField[x][y] != '.') {
            System.out.println("Choose another cell!");
        } else {
            exploreCell(x, y);
        }
    }

    private void exploreCell(int x, int y) {
        if (originalField[x][y] == '0') {
            exploredField[x][y] = '/';
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newX = x + i;
                    int newY = y + j;

                    if (newX >= 0 && newX < height && newY >= 0 && newY < width) {
                        if (exploredField[newX][newY] == '*' && originalField[newX][newY] != 'X') {
                            exploredField[newX][newY] = '.';
                        }
                        if (exploredField[newX][newY] == '.') {
                            exploreCell(newX, newY);
                        }
                    }
                }
            }
        } else if (String.valueOf(originalField[x][y]).matches("[1-8]")) {
            exploredField[x][y] = originalField[x][y];
        }
    }

    public void printField(char[][] field) {
        StringBuilder numbers = new StringBuilder();
        for (int i = 1; i <= width; i++) {
            numbers.append(i);
        }
        System.out.println("\n |" + numbers + "|\n-|---------|");
        int count = 0;
        for (char[] f : field) {
            System.out.print(++count + "|");
            for (char c : f) {
                if (c == empty) {
                    System.out.print('.');
                } else {
                    System.out.print(c);
                }
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public void placeMines() {
            Random random = new Random();
            for (int i = 0; i < minesAmount; i++) {
                int x = random.nextInt(9);
                int y = random.nextInt(9);
                if (originalField[x][y] == empty) {
                    originalField[x][y] = 'X';
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(x);
                    list.add(y);
                    mines.add(list);
                } else {
                    i--;
                }
            }
    }

    public boolean gameWon() {
        for (List<Integer> list : mines) {
            if (exploredField[list.get(0)][list.get(1)] != '*') {
                return false;
            }
        }

        int stars = 0;
        for (char[] chars : exploredField) {
            for (char c : chars) {
                if (c == '*') {
                    stars++;
                }
            }
        }
        return stars == minesAmount;
    }

    public void updateNeighbours() {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < originalField.length; i++) {
            for (int j = 0; j < originalField[i].length; j++) {
                if (originalField[i][j] == 'X') {
                    for (int k = 0; k < dx.length; k++) {
                        try {
                            if (originalField[i + dx[k]][j + dy[k]] != 'X') {
                                originalField[i + dx[k]][j + dy[k]] = (char) (originalField[i + dx[k]][j + dy[k]] + 1);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
    }

}

public class Main {
    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper(9, 9);
        minesweeper.play();
    }
}