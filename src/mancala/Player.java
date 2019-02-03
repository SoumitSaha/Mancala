package mancala;

import java.util.HashMap;
import java.util.ArrayList;

public class Player {
    private Board board;
    private boolean isPlayer1;
    private int sideOfBoard;
    private ArrayList<Beans> playerBoardView;
    private HashMap<Integer, Beans> indexHoleMap;


    public Player(Board board, boolean isPlayer1) {
        this.board = board;
        this.isPlayer1 = isPlayer1;
        if(isPlayer1) {
            sideOfBoard = 0;
        } else {
            sideOfBoard = 1;
        }
        playerBoardView = new ArrayList<>();
        indexHoleMap = new HashMap<>();
        initializePlayerBoardView();
    }

    public Player copy() {
        return new Player(new Board(board), isPlayer1);
    }

    public int getSideOfBoard() {
        return sideOfBoard;
    }

    //Convert 2D array to Single Array for easy bi-directional navigation
    public void initializePlayerBoardView() {
        int counter = 0;
        if(isPlayer1) {

            for(int i = board.getWidth() - 1 ; i >= 0 ; i--) {
                Beans holeToAdd = board.getHole(0,i);
                playerBoardView.add(holeToAdd);
                indexHoleMap.put(counter, holeToAdd);
                counter++;
            }

            for(int i = 0; i < board.getWidth(); i++) {
                Beans holeToAdd = board.getHole(1,i);
                playerBoardView.add(holeToAdd);
                indexHoleMap.put(counter, holeToAdd);
                counter++;
            }
        } else {
            for(int i = 0; i < board.getWidth(); i++) {
                Beans holeToAdd = board.getHole(1,i);
                playerBoardView.add(holeToAdd);
                indexHoleMap.put(counter, holeToAdd);
                counter++;
            }

            for(int i = board.getWidth() - 1 ; i >= 0 ; i--) {
                Beans holeToAdd = board.getHole(0,i);
                playerBoardView.add(holeToAdd);
                indexHoleMap.put(counter, holeToAdd);
                counter++;
            }
        }
    }

    public void moveAI(Player AI_player, int h){
        ArrayList<Integer> choicesMovement = new ArrayList<>();
        for(int i = 1; i < board.getWidth() - 1 ; i++) {
            if(!board.getHole(sideOfBoard, i).isEmpty()) {
                choicesMovement.add(i);
            }
        }
        States root = new States(board, this, AI_player);
        root.createFringe(true);
        int max = -99999999;
        for(States aNode : root.getChildren()) {
            //aNode.setHeuristicValue(alphaBeta(aNode, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, false, h));
            aNode.setHeuristicValue(miniMax(aNode, 4, false, h));
            if(aNode.getHeuristicValue() >= max) {
                max = aNode.getDecisionChosen();
            }
        }

        int choice = max;
        executeMove(choice);
    }

    public void executeMove(int choice) {
        Beans chosenHole = board.getHole(sideOfBoard, choice);
        int numStones = chosenHole.getStones();
        chosenHole.removeStones();
        if(isPlayer1) {
            choice = board.getWidth() - 1 - choice;
        }
        int counter = 1;
        int stoneIncrementer = 1;
        while(stoneIncrementer <= numStones) {
            if(choice + counter > board.getWidth()*2 - 1) {
                counter = 0;
                choice = 0;
            }
            Beans currentHole = indexHoleMap.get(choice + counter);
            if(isPlayer1) {
                if(currentHole.isMancala() && !currentHole.isBlue()) {
                    counter++;
                } else {
                    indexHoleMap.get(choice + counter).addStone();
                    counter++;
                    stoneIncrementer++;
                }
            } else {
                if(currentHole.isMancala() && currentHole.isBlue()) {
                    counter++;
                } else {
                    indexHoleMap.get(choice + counter).addStone();
                    counter++;
                    stoneIncrementer++;
                }
            }
        }
    }

    public int miniMax(States root, int depth, boolean isMaximizingPlayer, int h) {
        if(depth == 0 || root == null) {
            return root.getBoard().calculate_heuristic_value(root.getisPlayer1Max(), h); //heuristic h
        }

        if(isMaximizingPlayer) {
            root.createFringe(true);
            int bestValue = Integer.MIN_VALUE;
            int value;
            for(States aNode : root.getChildren()) {
                value = miniMax(aNode, depth - 1, false, h);
                bestValue = Math.max(bestValue, value);
            }
            return bestValue;
        } else {
            root.createFringe(false);
            int bestValue = Integer.MAX_VALUE;
            int value;
            for(States aNode : root.getChildren()) {
                value = miniMax(aNode, depth - 1, true, h);
                bestValue = Math.min(bestValue, value);
            }
            return bestValue;
        }
    }

    public int alphaBeta(States root, int depth, int alpha, int beta, boolean isMaximizingPlayer, int h) {
        //System.out.println("Node number : " + root.getNodeCount() + " opened.");
        if(depth == 0 || root == null) {
            //Check if the game is done
            return root.getBoard().calculate_heuristic_value(root.getisPlayer1Max(), h); //heuristic 1
            //return root.getBoard().calculate_heuristic_value(root.getisPlayer1Max(), 2); ////heuristic 2
        }

        if(isMaximizingPlayer) {
            root.createFringe(true);
            int value;
            for(States aNode : root.getChildren()) {
                value = alphaBeta(aNode, depth - 1, alpha, beta, false, h);
                alpha = Math.max(alpha, value);
                if(alpha >= beta) {
                    break;
                }
            }
            return alpha;
        } else {
            root.createFringe(false);
            int value;
            for(States aNode : root.getChildren()) {
                value = alphaBeta(aNode, depth - 1, alpha, beta, true, h);
                beta = Math.min(beta, value);
                if(alpha >= beta) {
                    break;
                }
            }
            return beta;
        }
    }
}