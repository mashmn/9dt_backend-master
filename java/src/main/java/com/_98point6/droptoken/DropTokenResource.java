package com._98point6.droptoken;

import com._98point6.droptoken.dao.GameDao;
import com._98point6.droptoken.dao.GetGamesDao;
import com._98point6.droptoken.mapper.GamesMapper;
import com._98point6.droptoken.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("/drop_token")
@Produces(MediaType.APPLICATION_JSON)
public class DropTokenResource {
    private static final Logger logger = LoggerFactory.getLogger(DropTokenResource.class);
    GetGamesDao getGamesDao;
    GameDao gameDao;

    public DropTokenResource() {
    }

    public DropTokenResource(GetGamesDao getGamesDao) {
        this.getGamesDao = getGamesDao;
    }

    @GET
//    public List<Game> getGames() {
//        return gamesDao.getGames();
    public Response getGames() {
        try {
            GetGamesResponse.Builder getGamesResponse = new GetGamesResponse.Builder()
                    .games(getGamesDao.getGames());
            logger.info("games = {}", getGamesResponse.build());
            return Response.ok(getGamesResponse.build()).build();
        } catch (Exception e) {
            throw new NotFoundException();
        }
    }

    @POST
    public Response createNewGame(CreateGameRequest request) {
        request.getColumns();
        logger.info("request={}", request);
        return Response.ok(new CreateGameResponse()).build();
    }

    @Path("/{id}")
    @GET
    public Response getGameStatus(@PathParam("id") String gameId) {
        try {
            logger.info("gameId = {}", gameId);
//            GameStatusResponse.Builder gameStatusResponse = new GameStatusResponse.Builder()
//                    .players(gameDao.getGameStatusPlayers(gameId))
//                    .state(gameDao.getGameStatus(gameId).getState())
//                    .winner(gameDao.getGameStatus(gameId).getWinner())
                    ;

            logger.info("state of the game = {}", gameDao.getGameStatusPlayers(gameId));
//            return Response.ok(gameStatusResponse.build()).build();
            return Response.ok(gameDao.getGameStatusPlayers(gameId).toString()).build();
        } catch (NullPointerException  ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }
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
    public Response getMoves(@PathParam("id") String gameId, @QueryParam("start") Integer start, @QueryParam("until") Integer until) {
        logger.info("gameId={}, start={}, until={}", gameId, start, until);
        return Response.ok(new GetMovesResponse()).build();
    }

    @Path("/{id}/moves/{moveId}")
    @GET
    public Response getMove(@PathParam("id") String gameId, @PathParam("moveId") Integer moveId) {
        logger.info("gameId={}, moveId={}", gameId, moveId);
        return Response.ok(new GetMoveResponse()).build();
    }

}
