package com._98point6.droptoken.dao;

import com._98point6.droptoken.entities.Games;
import com._98point6.droptoken.entities.Moves;
import com._98point6.droptoken.model.GetMoveResponse;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MoveDAO extends AbstractDAO<Moves> {
    private static final Logger logger = LoggerFactory.getLogger(MoveDAO.class);

    public MoveDAO(SessionFactory factory) {
        super(factory);
    }

    public Moves create(Moves moves) {
        return persist(moves);
    }

    public List<Object[]> getAllMovesByGameId(String gameId) {
        Query query = namedQuery("MOVES.getAllMovesByGameId");
        query.setParameter("gameId", UUID.fromString(gameId));
//        logger.info("moves={}", query.list().get(0));
        return query.list();
//        Criteria criteria = currentSession().createCriteria(Moves.class);
//        criteria.add(Restrictions.eq("gameId", UUID.fromString(gameId)));
//        logger.info("moves={}", criteria.list());
//        return criteria.list();
    }

    public List<GetMoveResponse> getMoves(String gameId) {
        Query query = namedQuery("MOVES.getMovesByGameId");
            query.setParameter("gameId", UUID.fromString(gameId));
//        logger.info("moves={}", query.list().get(0));
        return query.list();
    }

    public List<GetMoveResponse> getMoves(String gameId, Integer start, Integer until) {
        Query query = namedQuery("MOVES.getMovesFromStartToUntil");
        query.setParameter("gameId", UUID.fromString(gameId))
                .setInteger("start", start)
                .setInteger("until", until);
        return query.list();
    }

    public List<Moves> getMove(String gameId, String moveId) {
        Query query = namedQuery("MOVES.getMove");
        query.setParameter("gameId", UUID.fromString(gameId))
            .setParameter("moveId",  UUID.fromString(moveId));
        return query.list();
    }
}
