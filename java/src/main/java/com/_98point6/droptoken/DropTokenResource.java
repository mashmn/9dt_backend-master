package com._98point6.droptoken;

import com._98point6.droptoken.dao.GameDAO;
import com._98point6.droptoken.dao.MoveDAO;
import com._98point6.droptoken.entities.Games;
import com._98point6.droptoken.entities.Moves;
import com._98point6.droptoken.model.CreateGameRequest;
import com._98point6.droptoken.model.CreateGameResponse;
import com._98point6.droptoken.model.GameStatusResponse;
import com._98point6.droptoken.model.GetGamesResponse;
import com._98point6.droptoken.model.GetMoveResponse;
import com._98point6.droptoken.model.GetMovesResponse;
import com._98point6.droptoken.model.PostMoveRequest;
import com._98point6.droptoken.model.PostMoveResponse;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 *
 */
@Path("/drop_token")
@Produces(MediaType.APPLICATION_JSON)
public class DropTokenResource {
    private static final Logger logger = LoggerFactory.getLogger(DropTokenResource.class);
    private final GameDAO gameDAO;
    private final MoveDAO moveDAO;

    public DropTokenResource(GameDAO gameDAO, MoveDAO moveDAO) {
        this.gameDAO = gameDAO;
        this.moveDAO = moveDAO;
    }

    @GET
    @UnitOfWork
    public Response getGames() {
        GetGamesResponse.Builder getGamesResponse = new GetGamesResponse.Builder()
                .games(gameDAO.getGames());
        return Response.ok(getGamesResponse.build()).build();
    }

    @POST
    @UnitOfWork
    public Response createNewGame(CreateGameRequest request) {
        try {
            logger.info("request={}", request);
            Games games = new Games();
            games.setPlayerOneId(request.getPlayers().get(0));
            games.setPlayerOneId(request.getPlayers().get(1));
            games.setColumns(request.getColumns());
            games.setRows(request.getRows());
            games.setCreatedOn(new Date());

            gameDAO.create(games);

            CreateGameResponse.Builder createGameRequest = new CreateGameResponse.Builder();
            createGameRequest.gameId("some_string_token");
            return Response.ok(createGameRequest.build()).build();
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
            return Response.status(400).entity("Malformed request").build();
        }
    }

    @Path("/{id}")
    @GET
    @UnitOfWork
    public Response getGameStatus(@PathParam("id") String gameId) {
        logger.info("gameId = {}", gameId);
        String winner = null;
        String state = null;
        List<String> players = new ArrayList<>();

        List<Games> getGameStatus = gameDAO.getGameStatus(gameId);
        for (Object gameStatus : getGameStatus) {
            Object[] getGameStatusObj = (Object[]) gameStatus;
            players.add(String.valueOf(getGameStatusObj[0]));
            players.add(String.valueOf(getGameStatusObj[1]));
            winner = String.valueOf(getGameStatusObj[2]);
            state = String.valueOf(getGameStatusObj[3]);
        }

        GameStatusResponse.Builder gameStatusResponse = new GameStatusResponse.Builder()
                .players(players)
                .state(state);

        if(winner !="null") {
            gameStatusResponse.winner(winner);
        }
        return Response.ok(gameStatusResponse.build()).build();
    }

    @Path("/{id}/{playerId}")
    @POST
    @UnitOfWork
    public Response postMove(@PathParam("id")String gameId, @PathParam("playerId") String playerId, PostMoveRequest request) {
        logger.info("gameId={}, playerId={}, move={}", gameId, playerId, request);
        return Response.ok(new PostMoveResponse()).build();
    }

    @Path("/{id}/{playerId}")
    @DELETE
    @UnitOfWork
    public Response playerQuit(@PathParam("id")String gameId, @PathParam("playerId") String playerId) {
        logger.info("gameId={}, playerId={}", gameId, playerId);
        return Response.status(202).build();
    }

    @Path("/{id}/moves")
    @GET
    @UnitOfWork
    public Response getMoves(
            @PathParam("id") String gameId,
            @DefaultValue("1000") @QueryParam("start") Integer start,
            @DefaultValue("999") @QueryParam("until") Integer until) {
        try {
            if (start<until) {
                logger.info("gameId={}, start={}, until={}", gameId, start, until);
                GetMovesResponse.Builder getMovesResponse = new GetMovesResponse.Builder()
                        .moves(moveDAO.getMoves(gameId, start, until));
                return Response.ok(getMovesResponse.build()).build();
            } else if ( String.valueOf(start).equals("1000") && String.valueOf(until).equals("999") ) {
                GetMovesResponse.Builder getMovesResponse = new GetMovesResponse.Builder()
                        .moves(moveDAO.getMoves(gameId));
                return Response.ok(getMovesResponse.build()).build();
            } else return Response.status(404).entity("Game/moves not found").build();
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
            return Response.status(400).entity("Malformed request").build();
        }
    }

    @Path("/{id}/moves/{moveId}")
    @GET
    @UnitOfWork
    public Response getMove(@PathParam("id") String gameId, @PathParam("moveId") String moveId) {
        logger.info("gameId={}, moveId={}", gameId, moveId);
        String type = null;
        String playerId = null;
        Integer column = null;
        Integer row = null;

        List<Moves> getMoveIdList = moveDAO.getMove(gameId, moveId);

        for (Object getMoveIdObj : getMoveIdList) {
            Object[] obj = (Object[]) getMoveIdObj;
            type = String.valueOf(obj[0]);
            playerId = String.valueOf(obj[1]);
            column = Integer.valueOf(String.valueOf(obj[2]));
            row = Integer.valueOf(String.valueOf(obj[3]));
        }

        GetMoveResponse.Builder getMoveResponse = new GetMoveResponse.Builder()
                .type(type)
                .player(playerId)
                .column(column)
                .row(row);
        return Response.ok(getMoveResponse.build()).build();
    }

}
