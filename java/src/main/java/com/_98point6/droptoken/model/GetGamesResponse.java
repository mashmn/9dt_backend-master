package com._98point6.droptoken.model;

import com._98point6.droptoken.core.GetGames;
import com._98point6.droptoken.dao.GetGamesDao;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 *
 */
@JsonDeserialize(builder = GetGamesResponse.Builder.class)
public class GetGamesResponse {
    private List<GetGames> games;
    GetGamesDao getGamesDao;

    public GetGamesResponse() {}

    public GetGamesResponse(Builder builder) {
        this.games = Preconditions.checkNotNull(builder.games);
    }

    public List<GetGames> getGames() {
        return games;
    }

    @Override
    public String toString() {
        return "games: {" +
                games +
                "}";
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
//        private List<String> games;
        private List<GetGames> games;

        public Builder games(List<GetGames> games) {
            this.games = games;
            return this;
        }

        public Builder fromPrototype(GetGamesResponse prototype) {
            games = prototype.games;
            return this;
        }

        public GetGamesResponse build() {
            return new GetGamesResponse(this);
        }
    }
}
