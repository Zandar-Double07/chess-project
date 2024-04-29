package com.chessendings;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import java.util.logging.Logger;

public class Main {
//files and logger
static File chessGamesFile;
static FileWriter chessGamesWriter;
static Logger logger = Logger.getAnonymousLogger();
    public static void main(String[] args) throws IOException {
        //file writing set up
        Scanner inputManager = new Scanner(System.in);
        logger.info("Enter in a file path to log the chess game combinations");
        String filePath = inputManager.nextLine();
        try{
        chessGamesFile = new File(filePath);
        chessGamesWriter = new FileWriter(filePath);
        }
        catch(IOException e){
            logger.info("Something went wrong with creating file at path: " + "");
            inputManager.close();
        }
        
        //load a board from arguments
        logger.info("Input a chess SAN String");
        try{
            String inputSan = inputManager.nextLine();
            MoveList initialList = new MoveList();
            initialList.loadFromSan(inputSan);
            Board startBoard = new Board();
            for(Move move:initialList){
                startBoard.doMove(move);
            }
            logger.info("SAN loaded successfully");
            //get All Games
            chessGamesWriter.write("Here are all possible game endings from this chess position: \n" + startBoard.toString() + "\n");
            getAllGamesFromPosition(startBoard, initialList);
        }
        catch(MoveConversionException e){
            logger.severe("An Error occured while attempting to load the SAN String: ");
            e.printStackTrace();
        }
        catch(Exception e1){
            logger.severe("An Error occured while attempting to load all of your game combinations: ");
            e1.printStackTrace();
        }
        finally{
            logger.info("Finished logging all chess game combinations");
            chessGamesWriter.close();
            inputManager.close();
        }
    }

    private static void getAllGamesFromPosition(Board board, MoveList gameMoves) throws MoveGeneratorException {
        //if the game ends, log the game and information about it.
        if (isGameOver(board)) {
            String game = addGameDescriptor(gameMoves.toSanWithMoveNumbers(), board);
            
            try {
                chessGamesWriter.write(game + "\n");
            } catch (IOException e) {
                logger.info("something went wrong with writing to the file");
                try {
                    chessGamesWriter.close();
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
            getAllGamesFromPosition(board, new MoveList(gameMoves));
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