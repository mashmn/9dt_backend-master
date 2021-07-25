--
-- PostgreSQL "Drop Token" database.
--

--
CREATE TABLE MOVES_TABLE (
   move_id     UUID           PRIMARY KEY    NOT NULL,
   player_id   varchar(20)    NOT NULL,
   game_id     UUID           NOT NULL,
   move_seq    INT,
   column_move INT,
   row_move    INT,
   move_type   varchar(20),
   moved_on    timestamp
);

CREATE TABLE GAMES (
   game_id        UUID           PRIMARY KEY    NOT NULL,
   player_one_id  varchar(20),
   player_two_id  varchar(20),  
   columns        INT,
   rows           INT,
   state          varchar(20),
   winner         varchar(20),
   created_on     timestamp
);

CREATE TABLE PLAYERS (
   player_id    varchar(20)    NOT NULL,
   game_id      varchar(20) 
);

