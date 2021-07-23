package com._98point6.droptoken.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

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
        ),
        @NamedQuery(
                name = "GAMES.getGameDetailsByGameId",
                query = "SELECT p.playerOneId, p.playerTwoId, p.columns, p.rows, p.state, p.winner FROM Games p WHERE p.gameId = :gameId"
        )
})
public class Games implements Serializable {
    public Games() {
    }

    public Games(UUID gameId, String playerOneId, String playerTwoId, Integer columns, Integer rows, String state,
                 String winner, Date createdOn) {
        this.gameId = gameId;
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.columns = columns;
        this.rows = rows;
        this.state = state;
        this.winner = winner;
        this.createdOn = createdOn;
    }

    @Id
    @Column(name="game_id", unique = true, nullable=false)
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID gameId;

    @Column(name="player_one_id")
    private String playerOneId;

    @Column(name="player_two_id")
    private String playerTwoId;

    @Column(name="columns")
    private Integer columns;

    @Column(name="rows")
    private Integer rows;

    @Column(name="state")
    private String state;

    @Column(name="winner", nullable = true)
    private String winner;

    @Column(name="created_on")
    private Date createdOn;

    public UUID getGameId() {
        return gameId;
    }
    public void setGameId(UUID gameId) {
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
    public Integer getColumns() {
        return columns;
    }
    public void setColumns(Integer columns) {
        this.columns = columns;
    }
    public Integer getRows() {
        return rows;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
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
    public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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
                Objects.equals(this.columns, that.columns) &&
                Objects.equals(this.rows, that.rows) &&
                Objects.equals(this.state, that.state) &&
                Objects.equals(this.winner, that.winner) &&
                Objects.equals(this.createdOn, that.createdOn) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, playerOneId, playerTwoId, columns, rows, state, winner, createdOn);
    }
//    @Override
//    public String toString() {
//        return "Game [gameId=" + gameId + ", playerOneId=" + playerOneId + ", playerTwoId=" + playerTwoId +
//                ", nextPlayer=" + nextPlayer + ", state=" + state + ", winner=" + winner + "]";
//    }
}