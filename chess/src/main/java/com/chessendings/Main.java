package com.chessendings;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import java.util.logging.Logger;

import org.apache.commons.lang3.BooleanUtils;
public class Main {
//files and logger
static File chessGamesFile;
static FileWriter chessGamesWriter;
static Logger logger = Logger.getAnonymousLogger();
    public static void main(String[] args) throws IOException {
        while (true){
            //file writing set up
            Scanner inputManager = new Scanner(System.in);
            System.out.println("Enter in a file path to log the chess game combinations. Enter \"-1\" to end the program");
            //get file or end program
            String filePath = inputManager.nextLine();
            if(filePath.equals("-1")) break;
            try{
                chessGamesFile = new File(filePath);
                chessGamesWriter = new FileWriter(chessGamesFile);
            }
            catch(IOException e){
                System.out.println("Something went wrong with creating file at path: " + "");
                inputManager.close();
                continue;
            }
            
            //load a board from arguments
            System.out.println("Input a chess SAN String. Leave blank to start from the begining position");
            try{
                String inputSan = inputManager.nextLine();
                MoveList initialList = new MoveList();
                initialList.loadFromSan(inputSan);
                Board startBoard = new Board();
                for(Move move:initialList){
                    startBoard.doMove(move);
                }
                System.out.println("SAN loaded successfully");
                //get All Games
                int depth = getIntFromInput(inputManager, "What is the max depth, in 1/2 moves, you want to view possible game endings for? 1 full move is a move by white, followed by a move by black.\n Enter a negative number to have no limit.\nWARNING: a negative number will make result in extreme run times and extremely large files.");
                boolean doInsufficientMaterial = getBooleanFromInput(inputManager, "Log draw by insuficient material? Enter y/n: ");
                boolean doStaleMate = getBooleanFromInput(inputManager, "Log draw by stalemate? Enter y/n: ");
                boolean doRepetition = getBooleanFromInput(inputManager, "Log draw by repetition? Enter y/n: ");
                boolean doCheckMate = getBooleanFromInput(inputManager, "Log game ends by check mate? Enter y/n: ");
                boolean do50MoveRule = getBooleanFromInput(inputManager, "Log draw by 50 move rule?\n WARNING, this will make your file very large if you have no depth limit. Enter y/n: ");
                if(!BooleanUtils.and(new boolean[]{doInsufficientMaterial, doStaleMate, doRepetition, doCheckMate, do50MoveRule})){
                    System.out.println("No options selected. Skipping file writing");
                    continue;
                }
                if(depth == 0){
                    System.out.println("Nothing will be written by a depth of 0. Skipping file writing.");
                    continue;
                }
                chessGamesWriter.write("Here are all possible game endings within " + depth/2 + " turns from this chess position: \n" + startBoard.toString() + "\n");
                ChessCombinations.getAllGamesFromPosition(startBoard, initialList, chessGamesWriter, doInsufficientMaterial, doStaleMate, doRepetition, do50MoveRule, doCheckMate, depth, BigInteger.ZERO);
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
                System.out.println("Finished logging all chess game combinations");
                chessGamesWriter.close();
                inputManager.close();
            }
        }
    }
        /**
         * Function to get boolean as a y/n answer from an input.. The input must be y or n, and won't return until either is entered
         * @param scanner -- Scanner to get input from
         * @param message -- message to send user
         * @return true if y inputted, false if n inputed
         */
        static boolean getBooleanFromInput(Scanner scanner, String message){
            while (true){
                System.out.println(message);
                String answer = scanner.nextLine();
                if(answer.equalsIgnoreCase("y")) return true;
                if(answer.equalsIgnoreCase("n")) return false;
                System.out.println("Invalid input. Enter y or n");
        }
    }
    /**
     * Function to get integer from an Input. The input will rettrieve input until it get an int
     * @param scanner
     * @param message
     * @return
     */
    static int getIntFromInput(Scanner scanner, String message){
        while (true){
            try{
                System.out.println(message);
                int answer = scanner.nextInt();
                return answer;
            }
            catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }
}
