package com._98point6.droptoken.dao;

import com._98point6.droptoken.entities.Games;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class GameDAO extends AbstractDAO<Games> {
    private static final Logger logger = LoggerFactory.getLogger(GameDAO.class);

    public GameDAO(SessionFactory factory) {
        super(factory);
    }

    public Games create(Games games) {
        return persist(games);
    }

    public List<Games> getAllGames() {
        return list(namedQuery("GAMES.getAllGames"));
    }

    public List<Games> getGames() {
        List<Games> games = list(namedQuery("GAMES.getGames"));
        logger.info("games={}", games);
        return games;
    }

    public List<Object> getGameStatus(String gameId) {
        Query query = namedQuery("GAMES.getGameStatus");
//        query.setParameter("gameId", gameId);
//        Games getGameStatus = (Games) query.list().get(0);
        query.setString("gameId", gameId);
        logger.info("games={}", query.list().get(0));
        return (List<Object>) query.list();
    }
}