package com._98point6.droptoken.model;

import com._98point6.droptoken.entities.Games;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 *
 */
public class GetGamesResponse {
    private List<Games> games;

    public GetGamesResponse() {}

    private GetGamesResponse(Builder builder) {
        this.games = Preconditions.checkNotNull(builder.games);
    }

    public List<Games> getGames() {
        return games;
    }


    public static class Builder {
        private List<Games> games;

        public Builder games(List<Games> games) {
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
