package com._98point6.droptoken.model;

import com.google.common.base.Preconditions;
import java.util.Optional;

public class GetMoveResponse {
    private String type;
    private String player;
    private Integer column;
    private Integer row;

    public GetMoveResponse() {}

    private GetMoveResponse(Builder builder) {
        this.type = Preconditions.checkNotNull(builder.type);
        this.player = Preconditions.checkNotNull(builder.player);
        this.column = builder.column;
        this.row = builder.row;
    }


    public String getType() {
        return type;
    }

    public String getPlayer() {
        return player;
    }

    public Optional<Integer> getColumn() {
        return Optional.ofNullable(column);
    }

    public Optional<Integer> getRow() {
        return Optional.ofNullable(row);
    }

    public static class Builder {
        private String type;
        private String player;
        private Integer column;
        private Integer row;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder player(String player) {
            this.player = player;
            return this;
        }

        public Builder column(Integer column) {
            this.column = column;
            return this;
        }

        public Builder row(Integer row) {
            this.row = row;
            return this;
        }

        public Builder fromPrototype(GetMoveResponse prototype) {
            type = prototype.type;
            player = prototype.player;
            column = prototype.column;
            row = prototype.row;
            return this;
        }

        public GetMoveResponse build() {
            return new GetMoveResponse(this);
        }
    }
}
