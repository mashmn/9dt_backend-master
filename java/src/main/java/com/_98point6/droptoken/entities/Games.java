package com._98point6.droptoken.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="GAMES")
@Access(value=AccessType.FIELD)
//@JsonInclude(JsonInclude.Include.NON_NULL)
@NamedQueries({
        @NamedQuery(
                name = "GAMES.getAllGames",
                query = "SELECT p FROM Games p"
        ),
        @NamedQuery(
                name = "GAMES.getGames",
                query = "SELECT p.gameId FROM Games p"
        ),
        @NamedQuery(
                name = "GAMES.getGameStatus",
                query = "SELECT p.playerOneId, p.playerTwoId, p.winner, p.state FROM Games p WHERE p.gameId = :gameId"
        )
})
public class Games implements Serializable {
    public Games() {
    }

    public Games(String playerOneId, String playerTwoId, String nextPlayer, String state, String winner) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.nextPlayer = nextPlayer;
        this.state = state;
        this.winner = winner;
    }

    @Id
    @Column(name="game_id", unique = true, nullable=false)
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String gameId;

    @Column(name="player_one_id")
    private String playerOneId;

    @Column(name="player_two_id")
    private String playerTwoId;

    @Column(name="next_player")
    private String nextPlayer;

    @Column(name="state")
    private String state;

    @Column(name="winner", nullable = true)
    private String winner;

    public String getGameId() {
        return gameId;
    }
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    public String getPlayerOneId() {
        return playerOneId;
    }
    public void setPlayerOneId(String playerOneId) {
        this.playerOneId = playerOneId;
    }
    public String getPlayerTwoId() {
        return playerTwoId;
    }
    public void setPlayerTwoId(String playerTwoId) {
        this.playerTwoId = playerTwoId;
    }
    public String getNextPlayer() {
        return nextPlayer;
    }
    public void setNextPlayer(String nextPlayer) {
        this.nextPlayer = nextPlayer;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getWinner() {
        return winner;
    }
    public void setWinner(String winner) {
        this.winner = winner;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Games)) {
            return false;
        }

        final Games that = (Games) o;

        return Objects.equals(this.gameId, that.gameId) &&
                Objects.equals(this.playerOneId, that.playerOneId) &&
                Objects.equals(this.playerTwoId, that.playerTwoId) &&
                Objects.equals(this.nextPlayer, that.nextPlayer) &&
                Objects.equals(this.state, that.state) &&
                Objects.equals(this.winner, that.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, playerOneId, playerTwoId, nextPlayer, state, winner);
    }
//    @Override
//    public String toString() {
//        return "Game [gameId=" + gameId + ", playerOneId=" + playerOneId + ", playerTwoId=" + playerTwoId +
//                ", nextPlayer=" + nextPlayer + ", state=" + state + ", winner=" + winner + "]";
//    }
}