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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public Response createNewGame(CreateGameRequest request) {
        logger.info("request={}", request);
        return Response.ok(new CreateGameResponse()).build();
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
    public Response postMove(@PathParam("id")String gameId, @PathParam("playerId") String playerId, PostMoveRequest request) {
        logger.info("gameId={}, playerId={}, move={}", gameId, playerId, request);
        return Response.ok(new PostMoveResponse()).build();
    }

    @Path("/{id}/{playerId}")
    @DELETE
    public Response playerQuit(@PathParam("id")String gameId, @PathParam("playerId") String playerId) {
        logger.info("gameId={}, playerId={}", gameId, playerId);
        return Response.status(202).build();
    }
    @Path("/{id}/moves")
    @GET
    @UnitOfWork
    public Response getMoves(@PathParam("id") String gameId, @QueryParam("start") Integer start, @QueryParam("until") Integer until) {
        logger.info("gameId={}, start={}, until={}", gameId, start, until);
        logger.info("moves={}", moveDAO.getMoves(gameId, start, until));
        GetMovesResponse.Builder getMovesResponse = new GetMovesResponse.Builder()
            .moves(moveDAO.getMoves(gameId, start, until));
        return Response.ok(getMovesResponse.build()).build();
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
