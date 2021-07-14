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
import java.util.Optional;

public class GamesMapper implements ResultSetMapper<GameStatusResponse> {
    private static final Logger logger = LoggerFactory.getLogger(GamesMapper.class);
    private static final String ID = "game_id";
    private static final String STATE = "state";

    public GameStatusResponse map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
//        GetPlayers getPlayers = new GetPlayers(resultSet.getString("player_id"));
//        List<GetPlayers> getPlayersList = new LinkedList<GetPlayers>();
//        getPlayersList.add(getPlayers);
        logger.info("win ", resultSet.getString("winner"));
        logger.info("state ", resultSet.getString("state"));

        Optional<String> winner = Optional.ofNullable(resultSet.getString("winner"));
        GameStatusResponse.Builder gameStatusResponse = new GameStatusResponse.Builder()
//                .winner(resultSet.getString("winner"))
//                .state(resultSet.getString("state"))
                ;

        return gameStatusResponse.build();
    }
}