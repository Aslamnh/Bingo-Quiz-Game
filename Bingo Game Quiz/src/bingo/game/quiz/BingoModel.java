/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bingo.game.quiz;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author aslam
 */

// semua struktur kode atau penyimpanan variabel disini
class Player {
    private String name;
    private int winCount = 0;
    private int playerTurn = 1;
}
class BingoBoard {
    private BingoTile[][] tiles = new BingoTile[5][5];
    
    BingoModel model = new BingoModel();
    
    public BingoBoard(JPanel board) {
        Component[] comps = board.getComponents();
        Integer[] numbers = model.generateRandomNumbers();
        int pos = 0;
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = (JButton) comps[i * 5 + j];  // Ambil label dari panel
                int num = numbers[pos++];
		        tiles[i][j] = new BingoTile(num, button);   // Kirim label ke BingoTile
                button.setText(String.valueOf(num));        // Tampilkan angka di GUI
            }
        }
    }
    public boolean checkWin(int player) {
	int[] rowCount = new int[5];
        int[] colCount = new int[5];
        int mainDiagonalCount = 0;
        int antiDiagonalCount = 0;
	boolean menang = false;
        JButton button;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tiles[i][j].getMarked() == true && tiles[i][j].getPlayerMark() == player ) {			
                    rowCount[i]++;
                    colCount[j]++;

                    if (i == j) mainDiagonalCount++;
                    if (i + j == 4) antiDiagonalCount++;

                    if (rowCount[i] == 5 || colCount[j] == 5 || mainDiagonalCount == 5 || antiDiagonalCount == 5) {
                    
                    menang = true;
                    }
                }
            }
        }   
        
        if (menang) {
            //System.out.println(playerName + " menang");
            String[] bingo = {"B", "I", "N", "G", "O"};
            // Cek baris
    for (int i = 0; i < 5; i++) {
        if (rowCount[i] == 5) {
            for (int j = 0; j < 5; j++) {
                tiles[i][j].getLabel().setText(bingo[j]);
                tiles[i][j].getLabel().setBackground(Color.YELLOW);
            }
            return true;
        }
    }

    // Cek kolom
    for (int j = 0; j < 5; j++) {
        if (colCount[j] == 5) {
            for (int i = 0; i < 5; i++) {
               tiles[i][j].getLabel().setText(bingo[i]);
               tiles[i][j].getLabel().setBackground(Color.YELLOW);
            }
            return true;
        }
    }

    // Cek diagonal utama
    if (mainDiagonalCount == 5) {
        for (int i = 0; i < 5; i++) {
            tiles[i][i].getLabel().setText(bingo[i]);
            tiles[i][i].getLabel().setBackground(Color.YELLOW);
        }
        return true;
    }

    // Cek diagonal sekunder
    if (antiDiagonalCount == 5) {
        for (int i = 0; i < 5; i++) {
            tiles[i][4-i].getLabel().setText(bingo[i]);
            tiles[i][4-i].getLabel().setBackground(Color.YELLOW);
        }
        return true;
    }
         
    }
        return false;
    }

        public void markTile(int number, int player) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tiles[i][j].getNumber() == number && !tiles[i][j].getMarked()) {
                    tiles[i][j].mark(player);
                }
            }	
        }
        checkWin(player);
    }
}
class BingoTile {
    private int number;
    private boolean marked = false;
    private int playerMark = 0; 
 
    //private Question question;
    private JButton button;
	
    public BingoTile(int number, JButton button) {
        this.number = number;
        this.button = button;
        //this.question = model.generateQuiz();
    }

    
    public int getNumber() {
	return number;
    }

    public boolean getMarked() {
    return marked;
    }

    public JButton getLabel() {
        return button;
    }

    public void mark(int player) {
        marked = true;
        playerMark = player;
        button.setText(player == 1 ? "X" : "O");
        button.setBackground(Color.GREEN);
        button.setBackground(player == 1 ? Color.GREEN : Color.BLUE);
    }
    public int getPlayerMark() {
        return playerMark;
    }


        
}
class Quiz {
    Random random = new Random();
    private char operator;
    private int addSubMin;
    private int addSubMax;
    private int mulDivMin;
    private int mulDivMax;
    
    public Quiz(int addSubMin, int addSubMax, int mulDivMin, int mulDivMax) {
        this.addSubMin = addSubMin;
        this.addSubMax = addSubMax;
        this.mulDivMin = mulDivMin;
        this.mulDivMax = mulDivMax;
    }
    
    public char getOperator() {
        int i = random.nextInt(4) + 1;
        if (i == 1) return '+';
        else if (i == 2) return '-';
        else if (i == 3) return '*';
        else if (i == 4) return '/';
        else return '0';
    }
    
    public int getAddSubMin() {
        return addSubMin;
    }
    public int getAddSubMax() {
        return addSubMax;
    }
    public int getMulDivMin() {
        return mulDivMin;
    }
    public int getMulDivMax() {
        return mulDivMax;
    }
}
class Easy extends Quiz {
    private static final int addSubMin = 20;
    private static final int addSubMax = 50;
    private static final int mulDivMin = 1;
    private static final int mulDivMax = 10;
    
//    public Easy(int addsubMin, int addsubMax, int muldivMin, int muldivMax) {
//        super(addsubMin, addsubMax, muldivMin, muldivMax);
//    }
    public Easy() {
        super(addSubMin, addSubMax, mulDivMin, mulDivMax);
    }
}
class Normal extends Quiz {
    private static final int addSubMin = 50;
    private static final int addSubMax = 100;
    private static final int mulDivMin = 5;
    private static final int mulDivMax = 20;
    
    public Normal() {
        super(addSubMin, addSubMax, mulDivMin, mulDivMax);
    }
}
class Hard extends Quiz {
    private static final int addSubMin = 100;
    private static final int addSubMax = 300;
    private static final int mulDivMin = 10;
    private static final int mulDivMax = 30;
    
    public Hard() {
        super(addSubMin, addSubMax, mulDivMin, mulDivMax);
    }
}
class Question {
    private String text;
    private int answer;
    
    public Question(String text, int answer) {
        this.text = text;
        this.answer = answer;
    }
    
    public String getText() {
        return text;
    }
    public int getAnswer() {
        return answer;
    }
}
public class BingoModel {
    Random random = new Random();
    private Player[] players = new Player[2];
    private BingoBoard board;
    private Quiz difficulty;

    public BingoBoard getBoard() {
        return board;
    }
    
    public Integer[] generateRandomNumbers() {
        Integer[] numbers = new Integer[25];
        for (int i = 1; i <= 25; i++) {
            numbers[i - 1] = i;
        }
        List<Integer> numberList = Arrays.asList(numbers);
//        Collections.shuffle(numberList);
        return numberList.toArray(new Integer[0]);
    }
    public void setDifficulty(Quiz q) {
        this.difficulty = q;
    }
    public void setBoard(BingoBoard board) {
        this.board = board;
    }
    public Question generateQuiz() {
        Quiz d = difficulty;
        String s = "What is ";
        char op = d.getOperator();
        int num1 = 0;
        int num2 = 0;
        int ans = 0;
        if (op == '+' || op == '-') {
            num1 = random.nextInt(d.getAddSubMax()) + d.getAddSubMin();
            num2 = random.nextInt(d.getAddSubMax()) + d.getAddSubMin();
            if (op == '+') {
                ans = num1 + num2;
                s += num1 + " + " + num2 + " = ";
            } else {
                ans = num1 - num2;
                s += num1 + " - " + num2 + " = ";
            }
        } else {
            num1 = random.nextInt(d.getMulDivMax()) + d.getMulDivMin();
            num2 = random.nextInt(d.getMulDivMax()) + d.getMulDivMin();
            
            if (op == '*') {
                ans = num1 * num2;
                s += num1 + " * " + num2 + " = ";
            } else {
                ans = num1;
                num1 = ans * num2;
                s += num1 + " / " + num2 + " = ";
            }
        }
        return new Question(s, ans);
    }
}
