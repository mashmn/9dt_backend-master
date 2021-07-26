package com._98point6.droptoken.core;

public interface GameBoardConstants {
    int PLAYER_ONE = 1;
    int PLAYER_TWO = 2;
    int DRAW = 3;
    int BOARD_FULL = 4;
    int TURN_SUCCESSFUL = 5;
    int INVALID_MOVE = 6;
    int IN_PROGRESS = 7;

    Integer MIN_COLUMN = 4;
    Integer MIN_ROW = 4;
    Integer MAX_COLUMN = 8;
    Integer MAX_ROW = 8;

    String IN_PROGRESS_STR = "IN_PROGRESS";
    String DONE_STR = "DONE";
    String DRAW_STR = "DRAW";
    String MOVE_STR = "MOVE";
    String QUIT_STR = "QUIT";
}
