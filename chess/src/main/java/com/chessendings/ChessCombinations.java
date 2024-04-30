package com.chessendings;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class ChessCombinations {
    static Logger logger = Logger.getAnonymousLogger();
    
    public static void getAllGamesFromPosition(Board board, MoveList gameMoves, FileWriter fileToWriteTo) throws MoveGeneratorException {
        //if the game ends, log the game and information about it.
        if (isGameOver(board)) {
            String game = addGameDescriptor(gameMoves.toSanWithMoveNumbers(), board);
            
            try {
                fileToWriteTo.write(game + "\n");
            } catch (IOException e) {
                logger.info("something went wrong with writing to the file");
                try {
                    fileToWriteTo.close();
                } catch (IOException e1) {
                    logger.info("something went wrong with closing the file");
                }
            }

            return;
        }

        List<Move> moves = board.legalMoves();
        for (Move move : moves) {
            board.doMove(move, true);
            gameMoves.add(move);
            getAllGamesFromPosition(board, new MoveList(gameMoves), fileToWriteTo);
            board.undoMove();
            gameMoves.removeLast();
        }
    }

    private static boolean isGameOver(Board board){
        return board.isMated() || board.isDraw();
    }

    private static String addGameDescriptor(String san, Board board){
        if(board.isInsufficientMaterial()){
            return san + " -- This Game ends in a draw by insufficient material.";
        }
        if(board.isStaleMate()){
            return san + " -- This game ends in a draw by staleMate.";
        }
        if(board.isRepetition()){
            return san + " -- This game ends with a draw by repetition.";
        }
        if(board.isDraw()){
            return san + " -- This game ends with a draw by 50 move rule";
        }
        if(board.isMated()){
            return san + " -- This game ends with " + (board.getSideToMove() == Side.WHITE ? "Black": "White") + " winning by checkmate.";
        }
        return san;
    }
}
