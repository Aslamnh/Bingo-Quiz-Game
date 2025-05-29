/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bingo.game.quiz;

import java.util.Random;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
}
class BingoTile {
    private int number;
    private boolean marked = false;
    private JButton button;
	
    public BingoTile(int number, JButton button) {
	this.number = number;
        this.button = button;
    }
	
    public int getNumber() {
	return number;
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
    
    public Integer[] generateRandomNumbers() {
        Integer[] numbers = new Integer[25];
        for (int i = 1; i <= 25; i++) {
            numbers[i - 1] = i;
        }
        List<Integer> numberList = Arrays.asList(numbers);
        Collections.shuffle(numberList);
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
