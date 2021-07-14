package com._98point6.droptoken.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class GetGamesResponseRepresentation<T> {

    @Length(max = 3)
    private T games;

    public GetGamesResponseRepresentation() {
        // Jackson deserialization
    }

    public GetGamesResponseRepresentation(T games) {
        this.games = games;
    }

    @JsonProperty
    public T getGames() {
        return games;
    }
}
