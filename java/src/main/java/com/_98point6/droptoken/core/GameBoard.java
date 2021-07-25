package com._98point6.droptoken.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameBoard implements GameBoardConstants {
    private static final Logger logger = LoggerFactory.getLogger(GameBoard.class);
    int rows=0;
    int columns=0;
    int currentRow;
    int currentColumn;
    int turn=1;
    int[][] Board;

//    int PLAYER_ONE = 1;
//    int PLAYER_TWO = 2;
//    int DRAW = 3;
//    int BOARD_FULL = 4;
//    int TURN_SUCCESSFUL = 5;
//    int INVALID_MOVE = 6;
//    int IN_PROGRESS = 7;


    public void initializeBoard(int rows, int columns) {
        int[][] Board = new int[rows][columns];
        setRows(rows);
        setColumns(columns);
        for (int row = 0; row < Board.length; row++){
            for (int col = 0; col < Board[0].length; col++){
                Board[row][col] = 0;
            }
        }
        setBoard(Board);
    }

    public int dropTheToken(int player, int play) {
        int turn = getTurn();
        int[][] Board = getBoard();
        logger.info("turn={}, rows={}, columns={}", turn, rows, columns);
        boolean VALID = false;
        //play a turn
        if (turn <= rows*columns){
            for (int row = 0; row < Board.length; row++){
                if(Board[row][play] == 0){
                    logger.info("boardRow={}", row);
                    logger.info("boardVal={}", Board[row][play]);
                    Board[row][play] = player;
                    setBoard(Board);
                    setCurrentRow(row);
                    setCurrentColumn(play);
                    VALID = true;
                    break;
                }
            }
            if(!VALID) return INVALID_MOVE;
            turn++;
            setTurn(turn);
            return TURN_SUCCESSFUL;
        } else return INVALID_MOVE;
    }

    public int checkWinner(int player) {
        boolean winner = false;
        int[][] Board = getBoard();
        int turn = getTurn();

        //determine if there is a winner
        winner = isWinner(player,Board);

        if (winner){
            if (player==PLAYER_ONE){
                return PLAYER_TWO;
            }else{
                return PLAYER_ONE;
            }
        } else if (turn == rows*columns) return DRAW;
        else return IN_PROGRESS;
    }

    public boolean isWinner(int player, int[][] grid){
        //check for 4 across
        for(int row = 0; row<grid.length; row++){
            for (int col = 0;col < grid[0].length - 3;col++){
                if (grid[row][col] == player   &&
                        grid[row][col+1] == player &&
                        grid[row][col+2] == player &&
                        grid[row][col+3] == player){
                    return true;
                }
            }
        }
        //check for 4 up and down
        for(int row = 0; row < grid.length - 3; row++){
            for(int col = 0; col < grid[0].length; col++){
                if (grid[row][col] == player   &&
                        grid[row+1][col] == player &&
                        grid[row+2][col] == player &&
                        grid[row+3][col] == player){
                    return true;
                }
            }
        }
        //check upward diagonal
        for(int row = 3; row < grid.length; row++){
            for(int col = 0; col < grid[0].length - 3; col++){
                if (grid[row][col] == player   &&
                        grid[row-1][col+1] == player &&
                        grid[row-2][col+2] == player &&
                        grid[row-3][col+3] == player){
                    return true;
                }
            }
        }
        //check downward diagonal
        for(int row = 0; row < grid.length - 3; row++){
            for(int col = 0; col < grid[0].length - 3; col++){
                if (grid[row][col] == player   &&
                        grid[row+1][col+1] == player &&
                        grid[row+2][col+2] == player &&
                        grid[row+3][col+3] == player){
                    return true;
                }
            }
        }
        return false;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }

    public int[][] getBoard() {
        return Board;
    }

    public int getBoard(int row, int column) {
        return Board[row][column];
    }

    public void setBoard(int[][] board) {
        Board = board;
    }

    public GameBoard() { }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
