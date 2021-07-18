package com._98point6.droptoken.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="Moves")
@Access(value=AccessType.FIELD)
@NamedQueries({
        @NamedQuery(
                name = "Moves.getMoves",
                query = "SELECT p.type, p.playerId, p.columns, p.rows " +
                        "FROM Moves p WHERE " +
                        "p.gameId = :gameId AND p.seq BETWEEN :start and :until"
        ),
        @NamedQuery(
                name = "Moves.getMove",
                query = "SELECT p.type, p.playerId, p.columns, p.rows " +
                        "FROM Moves p " +
                        "WHERE p.gameId = :gameId AND p.moveId = :moveId"
        )
})
public class Moves implements Serializable {
    public Moves() {

    }

    public Moves(String moveId, String gameId, String playerId, Integer seq, Integer columns, Integer rows, String type, Date movedOn) {
        this.moveId = moveId;
        this.playerId = playerId;
        this.gameId = gameId;
        this.seq = seq;
        this.columns = columns;
        this.rows = rows;
        this.type = type;
        this.movedOn = movedOn;
    }

    @Id
    @Column(name="move_id", unique = true, nullable = false)
    public String moveId;

    @Column(name="player_id", nullable = false)
    public String playerId;

    @Column(name="game_id", nullable = false)
    public String gameId;

    @Column(name="seq")
    public Integer seq;

    @Column(name="columns")
    public Integer columns;

    @Column(name="rows")
    public Integer rows;

    @Column(name="type")
    public String type;

    @Column(name="moved_on")
    public Date movedOn;

    public Integer start;
    public Integer until;

    public String getMoveId() {
        return moveId;
    }

    public void setMoveId(String moveId) {
        this.moveId = moveId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getMovedOn() {
        return movedOn;
    }

    public void setMovedOn(Date movedOn) {
        this.movedOn = movedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Moves)) {
            return false;
        }

        final Moves that = (Moves) o;

        return Objects.equals(this.moveId, that.moveId) &&
                Objects.equals(this.gameId, that.gameId) &&
                Objects.equals(this.playerId, that.playerId) &&
                Objects.equals(this.columns, that.columns) &&
                Objects.equals(this.rows, that.rows) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.movedOn, that.movedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, moveId, playerId, columns, rows, type, movedOn);
    }
}
