package com._98point6.droptoken.service;

import com._98point6.droptoken.entities.Games;

public interface GameServiceInterface {
    public abstract void createGame(Games games);
    public abstract Games getGamebyId(String gameId);
    public abstract void updateGameById(String gameId, String newStatus);
    public abstract void deleteGameById(String gameId);
}