package com._98point6.droptoken.dao;

import com._98point6.droptoken.core.GetGames;
import com._98point6.droptoken.mapper.GetGamesMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(value = GetGamesMapper.class)
public interface GetGamesDao {
    @SqlQuery("select * from games;")
    public List<GetGames> getGames();
}
