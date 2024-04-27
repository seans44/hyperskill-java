package four;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class ConnectFour extends JFrame {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final String EMPTY = " ";
    private static final String PLAYER_ONE = "X";
    private static final String PLAYER_TWO = "O";
    private static boolean gameNotFinished = true;

    private static boolean playerOneTurn = true;

    public ConnectFour() {
        super("Connect Four");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel gamePanel = new JPanel(new GridLayout(ROWS, COLS, 2, 2));

        HashMap<String, JButton> buttonMap = new HashMap<>();
        for (char i = '6'; i >= '1'; i--) {
            for (char j = 'A'; j <= 'G'; j++) {
                JButton button = getButton(j, i, buttonMap);
                gamePanel.add(button);
            }
        }

        mainPanel.add(gamePanel, BorderLayout.CENTER);

        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetPanel.add(getResetButton(buttonMap));

        mainPanel.add(resetPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private static JButton getButton(char j, char i, HashMap<String, JButton> buttonMap) {
        JButton button = new JButton(EMPTY);
        button.setName("Button" + j + i);
        if ((i + j) % 2 == 0) {
            button.setBackground(new Color(156, 204, 100));
        } else {
            button.setBackground(new Color(156, 204, 100));
        }

        ActionListener changeText = e -> {
            if (gameNotFinished) {
                for (int k = 1; k <= ROWS; k++) {
                    if (Objects.equals(buttonMap.get(button.getName().charAt(6) + String.valueOf(k)).getText(), EMPTY)) {
                        buttonMap.get(button.getName().charAt(6) + String.valueOf(k)).setText(playerOneTurn ? PLAYER_ONE : PLAYER_TWO);
                        playerOneTurn = !playerOneTurn;
                        checkForWinner(buttonMap);
                        break;
                    }
                }
            }
        };

        button.addActionListener(changeText);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 40));
        buttonMap.put("" + j + i, button);
        return button;
    }

    private static JButton getResetButton(HashMap<String, JButton> buttonMap) {
        JButton resetButton = new JButton("Reset");
        resetButton.setName("ButtonReset");
        resetButton.setBackground(Color.orange);
        resetButton.setFocusPainted(false);

        ActionListener resetAllButtons = e -> {
            gameNotFinished = true;
            playerOneTurn = true;
            for (JButton button : buttonMap.values()) {
                button.setText(" ");
                if ((button.getName().charAt(6) + button.getName().charAt(7)) % 2 == 0) {
                    button.setBackground(new Color(156, 204, 100));
                } else {
                    button.setBackground(new Color(156, 204, 100));
                }
            }
        };
        resetButton.addActionListener(resetAllButtons);
        return resetButton;
    }

    private static void checkForWinner(HashMap<String, JButton> buttonMap) {
        int countHorizontal = 1;
        int countVertical = 1;
        char firstLetter = 'A';
        
        // ** VERTICAL ** //
        for (int i = 0; i < COLS; i++) {            // COLS = 7 Буквы 7
            for (int j = 1; j < ROWS; j++) {        // ROWS = 6 Числа 6
                if (!buttonMap.get("" + firstLetter + j).getText().equals(EMPTY)) {
                    if (buttonMap.get("" + firstLetter + j).getText().equals((buttonMap.get("" + firstLetter + (j + 1)).getText()))) {
                        countVertical++;
                        if (countVertical == 4) {
                            buttonMap.get("" + firstLetter + j).setBackground(Color.cyan);
                            buttonMap.get("" + firstLetter + (j + 1)).setBackground(Color.cyan);
                            buttonMap.get("" + firstLetter + (j - 1)).setBackground(Color.cyan);
                            buttonMap.get("" + firstLetter + (j - 2)).setBackground(Color.cyan);
                            gameNotFinished = false;
                            return;
                        }
                    } else {
                        countVertical = 1;
                    }
                }
            }
            countVertical = 1;
            firstLetter++;
        }

        // ** HORIZONTAL ** //
        firstLetter = 'A';
        for (int i = 1; i <= ROWS; i++) {            // ROWS = 6 Числа 6
            for (int j = 0; j < COLS - 1; j++) {        // COLS = 7 Буквы 7
                if (!buttonMap.get("" + firstLetter + i).getText().equals(EMPTY)) {
                    if (buttonMap.get("" + firstLetter + i).getText().equals((buttonMap.get("" + (char) ((int) firstLetter + 1) + i).getText()))) {
                        countHorizontal++;
                        if (countHorizontal == 4) {
                            buttonMap.get("" + firstLetter + i).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter + 1) + i).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter - 1) + i).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter - 2) + i).setBackground(Color.cyan);
                            gameNotFinished = false;
                            return;
                        }
                    } else {
                        countHorizontal = 1;
                    }
                }
                firstLetter++;
            }
            countHorizontal = 1;
            firstLetter = 'A';
        }

        // ** DIAGONAL ** //
        for (int i = 1; i <= ROWS; i++) {            // ROWS = 6 Числа 6
            for (int j = 0; j < COLS; j++) {        // COLS = 7 Буквы 7
                if (!buttonMap.get("" + firstLetter + i).getText().equals(EMPTY)) {
                    String thisButton = buttonMap.get("" + firstLetter + i).getText();
                    try {
                        if (thisButton.equals((buttonMap.get("" + (char) ((int) firstLetter + 1) + (i + 1)).getText())) &&
                                thisButton.equals((buttonMap.get("" + (char) ((int) firstLetter + 2) + (i + 2)).getText())) &&
                                thisButton.equals((buttonMap.get("" + (char) ((int) firstLetter + 3) + (i + 3)).getText()))
                        ) {
                            buttonMap.get("" + firstLetter + i).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter + 1) + (i + 1)).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter + 2) + (i + 2)).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter + 3) + (i + 3)).setBackground(Color.cyan);
                            gameNotFinished = false;
                            return;
                        }
                    } catch (Exception ignored) {}
                    try {
                        if (thisButton.equals((buttonMap.get("" + (char) ((int) firstLetter - 1) + (i + 1)).getText())) &&
                                thisButton.equals((buttonMap.get("" + (char) ((int) firstLetter - 2) + (i + 2)).getText())) &&
                                thisButton.equals((buttonMap.get("" + (char) ((int) firstLetter - 3) + (i + 3)).getText()))
                        ) {
                            buttonMap.get("" + firstLetter + i).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter - 1) + (i + 1)).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter - 2) + (i + 2)).setBackground(Color.cyan);
                            buttonMap.get("" + (char) ((int) firstLetter - 3) + (i + 3)).setBackground(Color.cyan);
                            gameNotFinished = false;
                            return;
                        }
                    } catch (Exception ignored) {}
                }
                firstLetter++;
            }
            firstLetter = 'A';
        }
    }

    public static void main(String[] args) {
        new ConnectFour();
    }
}


