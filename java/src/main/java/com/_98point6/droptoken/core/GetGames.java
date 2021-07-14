package com._98point6.droptoken.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class GetGames {

    public GetGames() {
        // Jackson deserialization
    }

    public GetGames(String gameId) {
        this.gameId = gameId;
    }

    @NotNull
    @JsonProperty
    private String gameId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return gameId.toString();
    }
}
