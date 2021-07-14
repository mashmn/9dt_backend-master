package com._98point6.droptoken.mapper;

import com._98point6.droptoken.core.GetGames;
import com._98point6.droptoken.model.GetGamesResponse;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class GetGamesMapper implements ResultSetMapper<GetGames> {
    private static final String ID = "game_id";

    public GetGames map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
        return new GetGames(resultSet.getString(ID));
    }
}
