package com._98point6.droptoken.dao;

import com._98point6.droptoken.core.GetGames;
import com._98point6.droptoken.core.GetPlayers;
import com._98point6.droptoken.mapper.GamesMapper;
import com._98point6.droptoken.mapper.PlayersMapper;
import com._98point6.droptoken.model.GameStatusResponse;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(value = PlayersMapper.class)
public interface GameDao {

    final String SQL_GAME_STATUS = "select g.state, g.winner " +
            "from games g where g.game_id = :id;";
    final String SQL_GAME_STATUS_PLAYERS = "SELECT p.player_id " +
            "FROM players p WHERE p.game_id = :id;";

//    @Mapper(GamesMapper.class)
//    @SqlQuery(SQL_GAME_STATUS)
//    public GameStatusResponse getGameStatus(@Bind("id") final String id);

    @SqlQuery(SQL_GAME_STATUS_PLAYERS)
//    @Mapper(PlayersMapper.class)
    List<GetPlayers> getGameStatusPlayers(@Bind("id") String id);

//    @SqlUpdate("insert into game(id) values(:id)")
//    void createGame(@BindBean final GetGamesResponse getGamesResponse);
//
//    @SqlUpdate("update game set name = coalesce(:name, name), code = coalesce(:code, code) where id = :id")
//    void editGame(@BindBean final GetGamesResponse getGamesResponse);
//
//    @SqlUpdate("delete from parts where id = :id")
//    int deleteGame(@Bind("id") final int id);
//
//    @SqlQuery("select last_insert_id();")
//    public int lastInsertId();
}
