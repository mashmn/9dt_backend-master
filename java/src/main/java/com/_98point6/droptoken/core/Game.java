package com._98point6.droptoken.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Game {

    public Game() {
        // Jackson deserialization
    }

    public Game(String state) {
        this.state = state;
    }

//    @NotNull
//    @JsonProperty
//    private String gameId;
//
//    public String getGameId() {
//        return gameId;
//    }
//
//    public void setGameId(String gameId) {
//        this.gameId = gameId;
//    }

    @NotNull
    @JsonProperty
    private String players;

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String moves) {
        this.players = players;
    }

//    @NotNull
//    @JsonProperty
//    private Integer moves;
//
//    public Integer getMoves() {
//        return moves;
//    }
//
//    public void setMoves(Integer moves) {
//        this.moves = moves;
//    }

    @NotNull
    @JsonProperty
    private String winner;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @NotNull
    @JsonProperty
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

//    @NotNull
//    @JsonProperty
//    private String nextPlayer;
//
//    public String getNextPlayer() {
//        return nextPlayer;
//    }
//
//    public void setNextPlayer(String nextPlayer) {
//        this.nextPlayer = nextPlayer;
//    }

//    @NotNull
//    @JsonProperty
//    private String playerOneId;
//
//    public String getPlayerOneId() {
//        return playerOneId;
//    }
//
//    public void setPlayerOneId(String playerOneId) {
//        this.playerOneId = playerOneId;
//    }
//
//    @NotNull
//    @JsonProperty
//    private String playerTwoId;
//
//    public String getPlayerTwoId() {
//        return playerTwoId;
//    }
//
//    public void setPlayerTwoId(String playerTwoId) {
//        this.playerTwoId = playerTwoId;
//    }

    public enum STATE {
        DONE,
        IN_PROGRESS
    }

    @Override
    public String toString() {
        return "Game {" +
                "players:'" + players + "'\'" +
//                    "moves:'" + moves + "'\'" +
                "winner:'" + winner + "'\'" +
                "state:'" + state + "'\'" +
                '}';
    }
}
