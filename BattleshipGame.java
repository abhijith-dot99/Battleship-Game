import java.util.Random;
import java.util.Scanner;

public class BattleshipGame {

    private static final int GRID_SIZE = 5;
    private static final char WATER = '~';
    private static final char SHIP = 'S';
    private static final char MISS = 'M';
    private static final char HIT = 'X';

    private char[][] playerGrid;
    private char[][] computerGrid;
    private Scanner scanner;

    public BattleshipGame() {
        playerGrid = new char[GRID_SIZE][GRID_SIZE];
        computerGrid = new char[GRID_SIZE][GRID_SIZE];
        scanner = new Scanner(System.in);
        initializeGrid(playerGrid);
        initializeGrid(computerGrid);
    }

    private void initializeGrid(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = WATER;
            }
        }
    }

    private boolean isValidCoordinate(String coord) {
        if (coord == null || coord.length() != 2) return false;
        char row = coord.charAt(0);
        char col = coord.charAt(1);
        return row >= 'A' && row < 'A' + GRID_SIZE && col >= '1' && col < '1' + GRID_SIZE;
    }

    private boolean isShipAtCoordinate(char[][] grid, String coord) {
        int row = coord.charAt(0) - 'A';
        int col = coord.charAt(1) - '1';
        return grid[row][col] == SHIP;
    }

    private String randomCoordinate() {
        Random rand = new Random();
        char row = (char) ('A' + rand.nextInt(GRID_SIZE));
        char col = (char) ('1' + rand.nextInt(GRID_SIZE));
        return "" + row + col;
    }

    private void displayGrid(char[][] grid, boolean hideShips, String title) {
        System.out.println(title);
        System.out.print("  ");
        for (int i = 1; i <= GRID_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                if (hideShips && grid[i][j] == SHIP) {
                    System.out.print(WATER + " ");
                } else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public void getPlayerShips() {
        System.out.println("Place your ships on the grid:");
        displayGrid(playerGrid, false, "Your Battleground");

        for (int i = 1; i <= 5; i++) {
            System.out.println("Enter the coordinate for ship " + i + ": ");
            String coord;
            do {
                coord = scanner.nextLine().toUpperCase();
                if (!isValidCoordinate(coord)) {
                    System.out.println("Invalid coordinate. Please enter again:");
                } else if (isShipAtCoordinate(playerGrid, coord)) {
                    System.out.println("There's already a ship there. Please choose a different location:");
                }
            } while (!isValidCoordinate(coord) || isShipAtCoordinate(playerGrid, coord));
            int row = coord.charAt(0) - 'A';
            int col = coord.charAt(1) - '1';
            playerGrid[row][col] = SHIP;
            displayGrid(playerGrid, false, "Your Battleground");
        }
        System.out.println("All ships placed! Let the game begin!");
    }

    public void setComputerShips() {
        for (int i = 0; i < 5; i++) {
            String coord;
            do {
                coord = randomCoordinate();
            } while (isShipAtCoordinate(computerGrid, coord));
            int row = coord.charAt(0) - 'A';
            int col = coord.charAt(1) - '1';
            computerGrid[row][col] = SHIP;
        }
    }

    public void gameLoop() {
        System.out.println("This is the enemy's grid. Try to hit all the ships!");
        displayGrid(computerGrid, true, "Enemy's Battleground");
        for (int round = 1; round <= 5; round++) {
            System.out.println("Round " + round);
            displayGrid(playerGrid, false, "Your Battleground");
            
            // Player turn
            String playerGuess;
            do {
                System.out.println("Enter your guess:");
                playerGuess = scanner.nextLine().toUpperCase();
                if (!isValidCoordinate(playerGuess)) {
                    System.out.println("Invalid coordinate. Please enter again:");
                }
            } while (!isValidCoordinate(playerGuess));

            int playerRow = playerGuess.charAt(0) - 'A';
            int playerCol = playerGuess.charAt(1) - '1';

            if (isShipAtCoordinate(computerGrid, playerGuess)) {
                computerGrid[playerRow][playerCol] = HIT;
                System.out.println("You got a hit!");
            } else {
                computerGrid[playerRow][playerCol] = MISS;
                System.out.println("You missed.");
            }
            displayGrid(computerGrid, true, "Enemy's Battleground");

            // Computer turn
            String computerGuess;
            do {
                computerGuess = randomCoordinate();
            } while (playerGrid[computerGuess.charAt(0) - 'A'][computerGuess.charAt(1) - '1'] != WATER);

            int compRow = computerGuess.charAt(0) - 'A';
            int compCol = computerGuess.charAt(1) - '1';

            if (isShipAtCoordinate(playerGrid, computerGuess)) {
                playerGrid[compRow][compCol] = HIT;
                System.out.println("Computer got a hit at " + computerGuess + "!");
            } else {
                playerGrid[compRow][compCol] = MISS;
                System.out.println("Computer missed at " + computerGuess + ".");
            }
            displayGrid(playerGrid, false, "Your Battleground");
        }
    }

    public void endGame() {
        int playerHits = 0;
        int computerHits = 0;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (playerGrid[i][j] == HIT) computerHits++;
                if (computerGrid[i][j] == HIT) playerHits++;
            }
        }

        System.out.println("Game Over!");
        System.out.println("Your hits: " + playerHits);
        System.out.println("Computer hits: " + computerHits);

        if (playerHits > computerHits) {
            System.out.println("Congratulations! You won!");
        } else if (playerHits < computerHits) {
            System.out.println("Computer wins. Better luck next time!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    public static void main(String[] args) {
        BattleshipGame game = new BattleshipGame();
        game.getPlayerShips();
        game.setComputerShips();
        game.gameLoop();
        game.endGame();
    }
}
