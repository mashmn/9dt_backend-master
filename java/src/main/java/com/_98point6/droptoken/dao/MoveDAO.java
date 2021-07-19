package com._98point6.droptoken.dao;

import com._98point6.droptoken.entities.Moves;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MoveDAO extends AbstractDAO<Moves> {
    private static final Logger logger = LoggerFactory.getLogger(MoveDAO.class);

    public MoveDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Moves> getMoves(String gameId) {
        Query query = namedQuery("Moves.getMovesByGameId");
            query.setParameter("gameId", gameId);
        return query.list();
    }

    public List<Moves> getMoves(String gameId, Integer start, Integer until) {
        Query query = namedQuery("Moves.getMovesFromStartToUntil");
        query.setParameter("gameId", gameId)
                .setInteger("start", start)
                .setInteger("until", until);
        logger.info("moves={}", query.list().get(0));
        return query.list();
    }

    public List<Moves> getMove(String gameId, String moveId) {
        Query query = namedQuery("Moves.getMove");
        query.setParameter("gameId", gameId)
            .setParameter("moveId",  moveId);
        logger.info("moves={}", query.list().get(0));
        return query.list();
    }
}
