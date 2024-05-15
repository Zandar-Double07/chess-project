package com.chessendings;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class ChessCombinations {
    static Logger logger = Logger.getAnonymousLogger();
    
    public static void getAllGamesFromPosition(Board board, MoveList gameMoves, FileWriter fileToWriteTo, boolean doInsufficientMaterial, boolean doStaleMate, boolean doRepetition, boolean do50MoveRule, boolean doCheckMate, int depth, BigInteger gamesAnalyzed) throws MoveGeneratorException {
        //if the game ends, log the game and information about it.
        if (isGameOver(board)) {
            String game = addGameDescriptor(gameMoves.toSanWithMoveNumbers(), board, doInsufficientMaterial, doStaleMate, doRepetition, do50MoveRule, doCheckMate);
            gamesAnalyzed = gamesAnalyzed.add(BigInteger.ONE);
            try {
                if(game != "")
                    fileToWriteTo.write(game);
            } catch (IOException e) {
                logger.warning("something went wrong with writing to the file");
                try {
                    fileToWriteTo.close();
                } catch (IOException e1) {
                    logger.warning("something went wrong with closing the file");
                }
            }
            return;
        }
        //return if no more depth
        if (depth == 0){
            return;
        }
        //For every move
        List<Move> moves = board.legalMoves();
        for (Move move : moves) {
            //Do all moves for that set
            board.doMove(move, true);
            gameMoves.add(move);
            getAllGamesFromPosition(board, new MoveList(gameMoves), fileToWriteTo, doInsufficientMaterial, doStaleMate, doRepetition, do50MoveRule, doCheckMate, depth - 1, gamesAnalyzed);
            board.undoMove();
            gameMoves.removeLast();
        }
    }

    private static boolean isGameOver(Board board){
        return board.isMated() || board.isDraw();
    }
    //adds a description of the game based on the result of the game and whether that option is allowed
    private static String addGameDescriptor(String san, Board board, boolean doInsufficientMaterial, boolean doStaleMate, boolean doRepetition, boolean do50MoveRule, boolean doCheckMate){
        if(board.isInsufficientMaterial() && doInsufficientMaterial){
            return san + " -- This Game ends in a draw by insufficient material.\n";
        }
        if(board.isStaleMate() && doStaleMate){
            return san + " -- This game ends in a draw by staleMate.\n";
        }
        if(board.isRepetition() && doRepetition){
            return san + " -- This game ends with a draw by repetition.\n";
        }
        if(board.isDraw() && do50MoveRule){
            return san + " -- This game ends with a draw by 50 move rule\n";
        }
        if(board.isMated() && doCheckMate){
            return san + " -- This game ends with " + (board.getSideToMove() == Side.WHITE ? "Black": "White") + " winning by checkmate.\n";
        }
        return "";
    }
}
