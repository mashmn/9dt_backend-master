package com._98point6.droptoken.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="MOVES_TABLE")
@Access(value=AccessType.FIELD)
@NamedQueries({
        @NamedQuery(
                name = "MOVES.getAllMovesByGameId",
                query = "SELECT p.type, p.playerId, p.column, p.row " +
                        "FROM Moves p " +
                        "WHERE p.gameId = :gameId"
        ),
        @NamedQuery(
                name = "MOVES.getMovesByGameId",
                query = "SELECT p.type, p.playerId, p.column, p.row " +
                        "FROM Moves p " +
                        "WHERE p.gameId = :gameId"
        ),
        @NamedQuery(
                name = "MOVES.getMovesFromStartToUntil",
                query = "SELECT p.type, p.playerId, p.column, p.row " +
//                        "ROW_NUMBER () OVER (ORDER BY p.movedOn) " +
                        "FROM Moves p " +
                        "WHERE p.gameId = :gameId AND p.seq BETWEEN :start and :until"
        ),
        @NamedQuery(
                name = "MOVES.getMove",
                query = "SELECT p.type, p.playerId, p.column, p.row " +
                        "FROM Moves p " +
                        "WHERE p.gameId = :gameId AND p.moveId = :moveId"
        )
})
public class Moves implements Serializable {
    public Moves() {

    }

    public Moves(UUID moveId, UUID gameId, String playerId, Integer seq, Integer column, Integer row, String type, Date movedOn) {
        this.moveId = moveId;
        this.playerId = playerId;
        this.gameId = gameId;
        this.seq = seq;
        this.column = column;
        this.row = row;
        this.type = type;
        this.movedOn = movedOn;
    }

    @Id
    @Column(name="move_id", unique = true, nullable = false)
    public UUID moveId;

    @Column(name="player_id", nullable = false)
    public String playerId;

    @Column(name="game_id", nullable = false)
    public UUID gameId;

    @Column(name="move_seq")
    public Integer seq;

    @Column(name="column_move")
    public Integer column;

    @Column(name="row_move")
    public Integer row;

    @Column(name="move_type")
    public String type;

    @Column(name="moved_on")
    public Date movedOn;

    public UUID getMoveId() {
        return moveId;
    }
    public void setMoveId(UUID moveId) {
        this.moveId = moveId;
    }
    public String getPlayerId() {
        return playerId;
    }
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    public UUID getGameId() {
        return gameId;
    }
    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }
    public Integer getSeq() {
        return seq;
    }
    public void setSeq(Integer seq) {
        this.seq = seq;
    }
    public Integer getColumn() {
        return column;
    }
    public void setColumn(Integer column) {
        this.column = column;
    }
    public Integer getRow() {
        return row;
    }
    public void setRow(Integer row) {
        this.row = row;
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
                Objects.equals(this.playerId, that.playerId) &&
                Objects.equals(this.gameId, that.gameId) &&
                Objects.equals(this.seq, that.seq) &&
                Objects.equals(this.column, that.column) &&
                Objects.equals(this.row, that.row) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.movedOn, that.movedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moveId, playerId, gameId, seq, column, row, type, movedOn);
    }
}
