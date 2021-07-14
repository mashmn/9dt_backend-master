package com._98point6.droptoken.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class GetPlayers {

    public GetPlayers() {
        // Jackson deserialization
    }

    public GetPlayers (String playerId) {
        this.playerId = playerId;
    }

    @NotNull
    @JsonProperty
    private String playerId;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return playerId.toString();
    }
}