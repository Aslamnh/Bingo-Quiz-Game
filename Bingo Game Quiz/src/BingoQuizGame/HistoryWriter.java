/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package BingoQuizGame;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author aslam
 */
public interface HistoryWriter {
    void writeHistory(Player p1, Player p2);
    void writeHistory(int round, Player p1, Player p2, Player winner);
    void writeHistory(int round, Player p1, Player p2);
    void clearHistory();
}

class GameHistory implements HistoryWriter {
    //sistem Game History dibuat Zienard
    //History keseluruhan
    @Override
    public void writeHistory(Player p1, Player p2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss a z");
        String currentDate = ZonedDateTime.now().format(formatter);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("history.txt", true))) {
        if (p1.getWinCount() > p2.getWinCount()){
            writer.write("Player " + p1.getName() + " Won the game with " + p1.getWinCount() + " wins against " + p2.getName() + " - " + currentDate + "\n\n");
        } else
        if (p2.getWinCount() > p1.getWinCount()){
            writer.write("Player " + p2.getName() + " Won the game with " + p2.getWinCount() + " wins against " + p1.getName() + " - " + currentDate + "\n\n");
        } else
        if (p1.getWinCount() == p2.getWinCount()) {
            writer.write("Match between Player " + p1.getName() + " and Player " + p2.getName() + " is a tie with both having " + p1.getWinCount() + " wins - " + currentDate + "\n\n");
        }
        } catch (IOException e) {
            System.err.println("Failed to write game history: " + e.getMessage() + " - " + currentDate);
        }

    }

    //History jika menang
    @Override
    public void writeHistory(int round, Player p1, Player p2, Player win) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss a z");
        String currentDate = ZonedDateTime.now().format(formatter);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("history.txt", true))) {
            writer.write("Round " + round + ":" + "\n");
            writer.printf("Player %s wins in %d turns - %s \n", win.getName(), win.getPlayerTurn(), currentDate);
            writer.write("Score:" + "\n");
            writer.printf("Player %s:Player %s\n\n", p1.getName(), p2.getName());
            writer.printf("%-9s:%9s\n\n", p1.getWinCount(), p2.getWinCount());
            
            //writer.write(+ " - " + currentDate + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write game history: " + e.getMessage() + " - " + currentDate);
        }
    }

    //History jika tie
    @Override
    public void writeHistory(int round, Player p1, Player p2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss a z");
        String currentDate = ZonedDateTime.now().format(formatter);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("history.txt", true))) {
            writer.write("Round " + round + ":" + "\n");
            writer.printf("Player %s tied with Player %s with %d turns - %s \n", p1.getName(), p2.getName(), p2.getPlayerTurn(), currentDate);
            writer.write("Score: " + "\n");
            writer.printf("Player %s:Player %s\n\n", p1.getName(), p2.getName());
            writer.printf("%-9s:%9s\n\n", p1.getWinCount(), p2.getWinCount());
            //writer.write(+ " - " + currentDate + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write game history: " + e.getMessage() + " - " + currentDate);
        }
    }
        
    //Membersihkan history
    @Override
    public void clearHistory() {       
        try (PrintWriter writer = new PrintWriter(new FileWriter("history.txt", false))) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("Failed to clear history");
        }
    }
}
