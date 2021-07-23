package com._98point6.droptoken;

import com._98point6.droptoken.core.GameBoard;
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
@Consumes(MediaType.APPLICATION_JSON)
public class DropTokenResource {
    private static final Logger logger = LoggerFactory.getLogger(DropTokenResource.class);
    private final GameDAO gameDAO;
    private final MoveDAO moveDAO;

    int playerOneChar = 101;
    int playerTwoChar = 102;
    int DRAW = 3;

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
//        if (request == null) return Response.status(400).entity("Malformed request").build();
        try {
            logger.info("request={}", request);

            if(request.getColumns() < 4 || request.getRows() < 4) {
                return Response.status(404).entity("At least 4 rows or columns required to create a game.").build();
            }

            UUID newGameId = UUID.randomUUID();

            Games games = new Games();
            games.setGameId(newGameId);
            games.setPlayerOneId(request.getPlayers().get(0));
            games.setPlayerTwoId(request.getPlayers().get(1));
            games.setColumns(request.getColumns());
            games.setRows(request.getRows());
            games.setState("IN_PROGRESS");
            games.setCreatedOn(new Date());
            gameDAO.create(games);

            CreateGameResponse.Builder createGameResponse = new CreateGameResponse.Builder();
            createGameResponse.gameId(newGameId.toString());
            return Response.ok(createGameResponse.build()).build();
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
        try {
            String winner = null;
            String state = null;
            List<String> players = new ArrayList<>();
            List<Games> getGameStatus;

            try {
                getGameStatus = gameDAO.getGameStatus(gameId);
            } catch (Exception e) {
                logger.info("exception={}", e.toString());
                return Response.status(404).entity("Game/moves not found.").build();
            }

            logger.info("gameId = {}", getGameStatus.isEmpty());
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
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
            return Response.status(400).entity("Malformed request").build();
        }
    }

    @Path("/{id}/{playerId}")
    @POST
    @UnitOfWork
    public Response postMove(@PathParam("id")String gameId, @PathParam("playerId") String playerId, PostMoveRequest request) {
        logger.info("gameId={}, playerId={}, move={}", gameId, playerId, request);
        String playerOneId = null;
        String playerTwoId = null;
        Integer columns = 0;
        Integer rows = 0;
        String state = null;
        String winner = null;

        String previousPlayer = null;
        int currentPlayer;
        String type = null;
//            Integer seq = null;
        Integer column = null;
        Integer row = null;

        int valid = 0;
        int result;

        try {
            Games foundGame = gameDAO.getGameByGameId(gameId);
            if(foundGame == null) return Response.status(404).entity("Game not found or player is not a part of it.").build();
            playerOneId = String.valueOf(foundGame.getPlayerOneId());
            playerTwoId = String.valueOf(foundGame.getPlayerTwoId());
            columns = (Integer) foundGame.getColumns();
            rows = (Integer) foundGame.getRows();
            state = String.valueOf(foundGame.getState());
            winner = String.valueOf(foundGame.getWinner());

            if(request.getColumn() > columns-1 || state.equals("DONE")) return Response.status(400).entity("Malformed request").build();

//            if (state.equals("DONE")) return Response.status(400).entity("Malformed requestttt").build();
//            if (nextPlayer.equals(playerId)) return Response.status(409).entity("Player tried to post when it's not their turn.").build();
            if(!playerId.equals(playerOneId) && !playerId.equals(playerTwoId)) {
                return Response.status(404).entity("Game not found or player is not a part of it.").build();
            }

            GameBoard gameBoard = new GameBoard();
            gameBoard.initializeBoard(rows, columns);

            List<Object[]> previousMoves = new ArrayList<>(moveDAO.getAllMovesByGameId(gameId));
//            logger.info("moveListSize={}", previousMoves.size());

            if(previousMoves.size() > 0) {
                for (Object[] previousMove : previousMoves) {
//                    logger.info("previousMove={}", previousMove[0]);
//                    logger.info("movePlayerId={}", previousMove[1]);
//                    logger.info("moveColumn={}", previousMove[2]);
//                    logger.info("moveRow={}", previousMove[3]);
                    type = String.valueOf(previousMove[0]);
                    previousPlayer = String.valueOf(previousMove[1]);
                    column = Integer.valueOf(String.valueOf(previousMove[2]));
                    row = Integer.valueOf(String.valueOf(previousMove[3]));

                    if(previousPlayer.equals(playerOneId)) {
                        valid = gameBoard.dropTheToken(playerOneChar,column);
                    } else {
                        valid = gameBoard.dropTheToken(playerTwoChar,column);
                    }
                    logger.info("previousPlayerValid={}", valid);
                    logger.info("previousPlayerArrayVal={}, row={}, col={}", gameBoard.getBoard(row,column), gameBoard.getCurrentRow(), gameBoard.getCurrentColumn());
                }
                if(previousPlayer.equals(playerId)) {
                    return Response.status(409).entity("Player tried to post when it's not their turn.").build();
                }
            }

            if(playerId.equals(playerOneId)) {
                currentPlayer = playerOneChar;
            } else {
                currentPlayer = playerTwoChar;
            }
//            logger.info("currentPlayer={}", currentPlayer);
            valid = gameBoard.dropTheToken(currentPlayer,request.getColumn());
            logger.info("currentPlayerArrayVal={}, row={}, col={}", gameBoard.getBoard(gameBoard.getCurrentRow(),request.getColumn()), gameBoard.getCurrentRow(), gameBoard.getCurrentColumn());
//            logger.info("row={}, col={}", gameBoard.getCurrentRow(),request.getColumn());
            logger.info("currentPlayerValid={}", valid);

            if(valid == 6) return Response.status(400).entity("Malformed request. Illegal move").build();;

            result = gameBoard.checkWinner(currentPlayer);
            logger.info("result={}", result);

            UUID newMoveId = UUID.randomUUID();
            logger.info("newMoveId={}", newMoveId);
            Moves moves = new Moves();
            moves.setMoveId(newMoveId);
            moves.setPlayerId(playerId);
            moves.setGameId(UUID.fromString(gameId));
            moves.setColumn(request.getColumn());
            moves.setRow(gameBoard.getCurrentRow());
            moves.setMovedOn(new Date());
            moves.setType("MOVE");

            switch(result) {
                case 101:

                    foundGame.setState("DONE");
                    foundGame.setWinner(playerOneId);
                    gameDAO.create(foundGame);
                    break;
                case 102:
                    foundGame.setState("DONE");
                    foundGame.setWinner(playerTwoId);
                    gameDAO.create(foundGame);
                    break;
                case 3:
                    moves.setType("DRAW");
                    foundGame.setState("DONE");
                    gameDAO.create(foundGame);
                    break;
                default:
                    moves.setType("MOVE");
            }

            moveDAO.create(moves);

            PostMoveResponse.Builder postMoveResponse = new PostMoveResponse.Builder();
            postMoveResponse.moveLink(gameId+"/moves/"+newMoveId);
            return Response.ok(postMoveResponse.build()).build();
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
            return Response.status(400).entity("Malformed request. Illegal move").build();
        }
    }

    @Path("/{id}/{playerId}")
    @DELETE
    @UnitOfWork
    public Response playerQuit(@PathParam("id")String gameId, @PathParam("playerId") String playerId) {
        logger.info("gameId={}, playerId={}", gameId, playerId);

        Games foundGame = gameDAO.getGameByGameId(gameId);
        if(
                foundGame == null || !(foundGame.getPlayerOneId().equals(playerId) || foundGame.getPlayerTwoId().equals(playerId))
        ) {
            return Response.status(404).entity("Game not found or player is not a part of it.").build();
        }

        logger.info("foundGame.getState()={}", foundGame.getState());

        //check if player is valid as well check if game is IN_PROGRESS before proceeding
        if (foundGame.getState().equals("IN_PROGRESS")) {
            foundGame.setState("DONE");
            gameDAO.create(foundGame);

            UUID newMoveId = UUID.randomUUID();
            logger.info("newMoveId={}", newMoveId);
            Moves moves = new Moves();
            moves.setMoveId(newMoveId);
            moves.setPlayerId(playerId);
            moves.setGameId(UUID.fromString(gameId));
            moves.setMovedOn(new Date());
            moves.setType("QUIT");
            moveDAO.create(moves);
            return Response.status(202).entity("OK. On success").build();
        } else return Response.status(410).entity("Game is already in DONE state.").build();
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
        try {
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
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
            return Response.status(400).entity("Malformed request").build();
        }

    }

}
