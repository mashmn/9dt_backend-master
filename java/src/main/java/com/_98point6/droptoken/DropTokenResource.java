package com._98point6.droptoken;

import com._98point6.droptoken.core.GameBoard;
import com._98point6.droptoken.core.GameBoardConstants;
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
public class DropTokenResource implements GameBoardConstants {
    private static final Logger logger = LoggerFactory.getLogger(DropTokenResource.class);
    private static final String RESPONSE_CODE_200 = "Ok.";
    private static final String RESPONSE_CODE_400_1 = "Malformed request";
    private static final String RESPONSE_CODE_400_2 = "Malformed input. Illegal move";
    private static final String RESPONSE_CODE_404_1 = "Game/moves not found.";
    private static final String RESPONSE_CODE_404_2 = "Game not found or player is not a part of it.";
//    private static final String RESPONSE_CODE_404_3 = "At least 4 rows or columns required to create a game.";
    private static final String RESPONSE_CODE_409 = "Player tried to post when it's not their turn.";
    private static final String RESPONSE_CODE_410 = "Game is already in DONE state.";

    private final GameDAO gameDAO;
    private final MoveDAO moveDAO;

    public DropTokenResource(GameDAO gameDAO, MoveDAO moveDAO) {
        this.gameDAO = gameDAO;
        this.moveDAO = moveDAO;
    }

    /**
     * GET ALL GAMES
     *
     * @return all the game ids
     */
    @GET
    @UnitOfWork
    public Response getGames() {
        GetGamesResponse.Builder getGamesResponse = new GetGamesResponse.Builder()
                .games(gameDAO.getGames());
        return Response.ok(getGamesResponse.build()).build();
    }

    /**
     * CREATE NEW GAME
     * Construct a new game with newly generated gameId (UUID), the creation datetime
     * and state of the game is set to "IN_PROGRESS".
     *
     * @param request the request should contain player ids, columns and rows.
     * @return the gameId
     * @throws NotAllowedException in case request is missing or malformed.
     */
    @POST
    @UnitOfWork
    public Response createNewGame(CreateGameRequest request) {
//        if (request == null) return Response.status(400).entity("Malformed request").build();
        try {
            logger.info("request={}", request);
            if(
                    request.getColumns() < MIN_COLUMN || request.getRows() < MIN_ROW ||
                    request.getColumns() > MAX_COLUMN || request.getRows() > MAX_ROW
            ) {
                return Response.status(404).entity(RESPONSE_CODE_400_1).build();
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
            return Response.status(400).entity(RESPONSE_CODE_400_1).build();
        }
    }

    /**
     * GET GAME STATUS OF A GAME ID
     *
     * @param gameId string UUID of a game
     * @return players, state and winner (if not null)
     * @throws IllegalArgumentException Invalid UUID string
     */
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
                return Response.status(404).entity(RESPONSE_CODE_404_1).build();
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
            return Response.status(400).entity(RESPONSE_CODE_400_1).build();
        }
    }

    /**
     * POST A MOVE
     *
     * @param gameId String gameId
     * @param playerId String playerId
     * @param request column for the move
     * @return Concatenated string in this format: {gameId}/moves/{MoveId}
     */
    @Path("/{id}/{playerId}")
    @POST
    @UnitOfWork
    public Response postMove(@PathParam("id")String gameId, @PathParam("playerId") String playerId, PostMoveRequest request) throws Exception {
        try {
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
        Integer column = null;
        Integer row = null;
        Integer seq = 0;

        int valid = 0;
        int result;
        boolean firstMove = true;
        

            Games foundGame = gameDAO.getGameByGameId(gameId);
            if(foundGame == null) return Response.status(404).entity(RESPONSE_CODE_404_2).build();
            playerOneId = String.valueOf(foundGame.getPlayerOneId());
            playerTwoId = String.valueOf(foundGame.getPlayerTwoId());
            columns = foundGame.getColumns();
            rows = foundGame.getRows();
            state = String.valueOf(foundGame.getState());
            winner = String.valueOf(foundGame.getWinner());

            if(request.getColumn() > columns-1 || state.equals(DONE_STR)) return Response.status(400).entity(RESPONSE_CODE_400_1).build();

            if(!playerId.equals(playerOneId) && !playerId.equals(playerTwoId)) {
                return Response.status(404).entity(RESPONSE_CODE_404_2).build();
            }

            GameBoard gameBoard = new GameBoard();
            gameBoard.initializeBoard(rows, columns);

            List<Object[]> previousMoves = new ArrayList<>(moveDAO.getAllMovesByGameId(gameId));

            if(!previousMoves.isEmpty()) {
                firstMove = false;
                for (Object[] previousMove : previousMoves) {
                    type = String.valueOf(previousMove[0]);
                    previousPlayer = String.valueOf(previousMove[1]);
                    column = Integer.valueOf(String.valueOf(previousMove[2]));
                    row = Integer.valueOf(String.valueOf(previousMove[3]));

                    if(previousPlayer.equals(playerOneId)) {
                        valid = gameBoard.dropTheToken(PLAYER_ONE,column);
                    } else {
                        valid = gameBoard.dropTheToken(PLAYER_TWO,column);
                    }
                    seq++;
//                    logger.info("seq={}", seq);
//                    logger.info("previousPlayerValid={}", valid);
//                    logger.info("previousPlayerArrayVal={}, row={}, col={}", gameBoard.getBoard(row,column), gameBoard.getCurrentRow(), gameBoard.getCurrentColumn());
                }
                if(previousPlayer.equals(playerId)) {
                    return Response.status(409).entity(RESPONSE_CODE_409).build();
                }
            }

            if(playerId.equals(playerTwoId)) {
                if(firstMove) {
                    return Response.status(409).entity(RESPONSE_CODE_409).build();
                }
                currentPlayer = PLAYER_TWO;
            } else {
                currentPlayer = PLAYER_ONE;
            }
//            logger.info("currentPlayer={}", currentPlayer);
            valid = gameBoard.dropTheToken(currentPlayer,request.getColumn());
            logger.info("currentPlayerArrayVal={}, row={}, col={}", gameBoard.getBoard(gameBoard.getCurrentRow(),request.getColumn()), gameBoard.getCurrentRow(), gameBoard.getCurrentColumn());
//            logger.info("row={}, col={}", gameBoard.getCurrentRow(),request.getColumn());
            logger.info("currentPlayerValid={}", valid);

            if(valid == INVALID_MOVE) return Response.status(400).entity(RESPONSE_CODE_400_2).build();;

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
            moves.setSeq(seq);
            moves.setType(MOVE_STR);

            switch(result) {
                case PLAYER_ONE:
                    foundGame.setState(DONE_STR);
                    foundGame.setWinner(playerOneId);
                    gameDAO.create(foundGame);
                    break;
                case PLAYER_TWO:
                    foundGame.setState(DONE_STR);
                    foundGame.setWinner(playerTwoId);
                    gameDAO.create(foundGame);
                    break;
                case DRAW:
                    moves.setType(DRAW_STR);
                    foundGame.setState(DONE_STR);
                    gameDAO.create(foundGame);
                    break;
                default:
                    moves.setType(MOVE_STR);
            }

            moveDAO.create(moves);

            PostMoveResponse.Builder postMoveResponse = new PostMoveResponse.Builder();
            postMoveResponse.moveLink(gameId+"/moves/"+newMoveId);
            return Response.ok(postMoveResponse.build()).build();
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
//            return Response.status(400).entity(RESPONSE_CODE_400_2).build();
            throw new Exception(RESPONSE_CODE_400_2);
        }
    }

    @Path("/{id}/{playerId}")
    @DELETE
    @UnitOfWork
    public Response playerQuit(@PathParam("id")String gameId, @PathParam("playerId") String playerId) {
        logger.info("gameId={}, playerId={}", gameId, playerId);

        try {
            Games foundGame = gameDAO.getGameByGameId(gameId);
            if(
                    foundGame == null || !(foundGame.getPlayerOneId().equals(playerId) || foundGame.getPlayerTwoId().equals(playerId))
            ) {
                return Response.status(404).entity(RESPONSE_CODE_404_2).build();
            }

            logger.info("foundGame.getState()={}", foundGame.getState());

            //check if player is valid as well check if game is IN_PROGRESS before proceeding
            if (foundGame.getState().equals(IN_PROGRESS_STR)) {

                // set the Winner to the player left.
                if (foundGame.getPlayerOneId().equals(playerId)) {
                    foundGame.setWinner(foundGame.getPlayerTwoId());
                } else {
                    foundGame.setWinner(foundGame.getPlayerOneId());
                }

                foundGame.setState(DONE_STR);
                gameDAO.create(foundGame);

                UUID newMoveId = UUID.randomUUID();
                logger.info("newMoveId={}", newMoveId);
                Moves moves = new Moves();
                moves.setMoveId(newMoveId);
                moves.setPlayerId(playerId);
                moves.setGameId(UUID.fromString(gameId));
                moves.setMovedOn(new Date());
                moves.setType(QUIT_STR);
                moveDAO.create(moves);
                return Response.accepted().build();
            } else return Response.status(410).entity(RESPONSE_CODE_410).build();
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
            return Response.status(404).entity(RESPONSE_CODE_404_2).build();
        }

    }

    @Path("/{id}/moves")
    @GET
    @UnitOfWork
    public Response getMoves(
            @PathParam("id") String gameId,
            @DefaultValue("1000") @QueryParam("start") Integer start,
            @DefaultValue("999") @QueryParam("until") Integer until) {
        try {
            String type = null;
            String playerId = null;
            Integer column = null;
            GetMoveResponse.Builder getMoveResponse = new GetMoveResponse.Builder();
            GetMovesResponse.Builder getMovesResponse = new GetMovesResponse.Builder();
            List<GetMoveResponse> getNewMovesList = new ArrayList<>();
            List<GetMoveResponse> getMovesList = new ArrayList<>();
            logger.info("gameId={}, start={}, until={}", gameId, start, until);

            if (start<until) {
                getMovesList = moveDAO.getMoves(gameId, start, until);
            } else if ( String.valueOf(start).equals("1000") && String.valueOf(until).equals("999") ) {
                getMovesList = moveDAO.getMoves(gameId);
            }

            if(getMovesList.isEmpty()) return Response.status(404).entity(RESPONSE_CODE_404_1).build();

            for (Object getMoveObj : getMovesList) {
                Object[] obj = (Object[]) getMoveObj;
                type = String.valueOf(obj[0]);
                playerId = String.valueOf(obj[1]);
                column = Integer.valueOf(String.valueOf(obj[2]));
//                row = Integer.valueOf(String.valueOf(obj[3]));
                getMoveResponse.type(type)
                        .player(playerId)
                        .column(column)
//                    .row(row)
                ;
                getNewMovesList.add(getMoveResponse.build());
            }
            getMovesResponse
                    .moves(getNewMovesList);
            return Response.ok(getMovesResponse.build()).build();
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
            return Response.status(400).entity(RESPONSE_CODE_400_1).build();
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
//            Integer row = null;

            List<Moves> getMoveIdList = moveDAO.getMove(gameId, moveId);

            for (Object getMoveIdObj : getMoveIdList) {
                Object[] obj = (Object[]) getMoveIdObj;
                type = String.valueOf(obj[0]);
                playerId = String.valueOf(obj[1]);
                column = Integer.valueOf(String.valueOf(obj[2]));
//                row = Integer.valueOf(String.valueOf(obj[3]));
            }

            GetMoveResponse.Builder getMoveResponse = new GetMoveResponse.Builder()
                    .type(type)
                    .player(playerId)
                    .column(column)
//                    .row(row)
                    ;
            return Response.ok(getMoveResponse.build()).build();
        } catch (Exception e) {
            logger.info("exception={}", e.toString());
            return Response.status(400).entity(RESPONSE_CODE_400_1).build();
        }

    }

}
