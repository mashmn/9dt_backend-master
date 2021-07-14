package com._98point6.droptoken.mapper;

import com._98point6.droptoken.core.GetPlayers;
import com._98point6.droptoken.model.GameStatusResponse;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PlayersMapper implements ResultSetMapper<GetPlayers> {
    private static final Logger logger = LoggerFactory.getLogger(PlayersMapper.class);

    public GetPlayers map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {

        logger.info(resultSet.getString("player_id"));

        return new GetPlayers(resultSet.getString("player_id"));
    }
}