package mancala;

import java.util.ArrayList;

public class States {
    States parent;
    private Board board;
    private ArrayList<States> children;
    private int decisionChosen;
    private Player player;
    private Player opponent;
    private int heuristicValue;
    private static int nodeCount = 0;

    public States(Board passedBoard, Player player, Player opponent) {
        this(null, passedBoard, player, opponent, 0);
    }

    public States(States parent, Board passedBoard, Player player, Player opponent, int move) {
        this.parent = parent;
        board = new Board(passedBoard);
        children = new ArrayList<>();
        this.player = player.copy();
        this.opponent = opponent.copy();
        decisionChosen = move;
        nodeCount++;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getDecisionChosen() {
        return decisionChosen;
    }

    public void setHeuristicValue(int value) {
        heuristicValue = value;
    }

    public int getHeuristicValue() {
        return heuristicValue;
    }

    public Board getBoard() {
        return board;
    }

    public void createFringe(boolean isMaximizingPlayer) {
        ArrayList<Integer> choicesMovement = new ArrayList<>();
        if(isMaximizingPlayer) {
            for(int i = 1; i < board.getWidth() - 1 ; i++) {
                if(!board.getHole(player.getSideOfBoard(), i).isEmpty()) {
                    choicesMovement.add(i);
                }
            }
            for(int move : choicesMovement) {
                States childAdded = new States(this, board, player, opponent, move);
                children.add(childAdded);
                childAdded.player.executeMove(move);
            }
        } else {
            for(int i = 1; i < board.getWidth() - 1 ; i++) {
                if(!board.getHole(opponent.getSideOfBoard(), i).isEmpty()) {
                    choicesMovement.add(i);
                }
            }
            for(int move : choicesMovement) {
                States childAdded = new States(this, board, player, opponent, move);
                children.add(childAdded);
                childAdded.opponent.executeMove(move);
            }
        }
    }

    public int getisPlayer1Max() {
        return player.getSideOfBoard();
    }

    public ArrayList<States> getChildren() {
        return children;
    }
}