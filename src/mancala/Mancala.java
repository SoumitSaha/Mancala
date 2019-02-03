package mancala;

import java.util.Random;
import java.util.Scanner;

public class Mancala {

    private Board board;
    private Player player1;
    private Player player2;
    private int winner1;
    private int winner2;

    public Mancala(int option, int win1, int win2, int h) {
        winner1 = win1;
        winner2 = win2;
        board = new Board();
        player1 = new Player(board, true);
        player2 = new Player(board, false);
        if(option == 0) return;
        playAIVsAI(h);

    }

    public void playAIVsAI(int h) {

        Random r = new Random();
        int startingPlayer = r.nextInt(2);

        boolean player1TurnNow = false;

        if (startingPlayer == 0) {
            System.out.print("Starts : " + (startingPlayer + 1) + " ");
            player1.moveAI(player1, h);
        } else {
            System.out.print("Starts : " + (startingPlayer + 1) + " ");
            player2.moveAI(player2, h);
            player1TurnNow = true;
        }

        while (!board.isGameFinished()) {
            if (player1TurnNow) {
                player1.moveAI(player1, h);
                player1TurnNow = false;
            } else {
                player2.moveAI(player2, h);
                player1TurnNow = true;
            }
        }

        boolean winner = board.whoWon();
        if (winner) {
            System.out.println("\tPlayer 1 is the winner");
            winner1++;
        } else {
            System.out.println("\tPlayer 2 is the winner");
            winner2++;
        }
    }

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Heuristic : ");
        int heu = s.nextInt();
        Mancala game = new Mancala(0, 0, 0, heu);

        for (int i = 0; i < 100; i++) {
            System.out.print("Game : " + i + "\t");
            game = new Mancala(3, game.winner1, game.winner2, heu);
        }
        System.out.println("After 100 games : ");
        System.out.println("Player 1 wins : " + game.winner1);
        System.out.println("Player 2 wins : " + game.winner2);

    }
}
