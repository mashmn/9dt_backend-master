package com._98point6.droptoken.dao;

import com._98point6.droptoken.entities.Games;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GameDAO extends AbstractDAO<Games> {
    private static final Logger logger = LoggerFactory.getLogger(GameDAO.class);

    public GameDAO(SessionFactory factory) {
        super(factory);
    }

    public Games create(Games games) {
        return persist(games);
    }

    public void update(Games games) {
        update(games);
    }

    public void merge(Games games) {
        merge(games);
    }

//    public Games find(Long id) {
//        return (Games) super.get(id);
//    }

//    public List<Games> getAllGames() {
//        return list(namedQuery("GAMES.getAllGames"));
//    }

    public List<Games> getGames() {
        List<Games> games = list(namedQuery("GAMES.getGames")
                .setMaxResults(20));
        logger.info("games={}", games);
        return games;
    }

    public List<Games> getGameStatus(String gameId) {
        Query query = namedQuery("GAMES.getGameStatus");
        query.setParameter("gameId", UUID.fromString(gameId));
        return query.list();
    }

    public List<Games> getGameDetailsByGameId(String gameId) {
        Query query = namedQuery("GAMES.getGameDetailsByGameId");
        query.setParameter("gameId", UUID.fromString(gameId));
        return query.list();
    }

    public Games getGameByGameId(String gameId) {
        return get(UUID.fromString(gameId));
    }
}