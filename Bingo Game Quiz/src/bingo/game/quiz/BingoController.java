/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bingo.game.quiz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author aslam
 */
public class BingoController {
    private BingoModel model;
    private BingoView viewBingo;
    private MenuView viewMenu;
    private HistoryView viewHistory;
    private int currentPlayer = 1;
    private int currentRound = 1;
    private int currentTurn = 1;

    public int getRound() {
        return currentRound;
    }

    public void setRound(int round) {
        this.currentRound = round;
    }
    
    private JButton createSizedButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        
        return button;
    }
      
    public void Start(){
            viewBingo.getbtnTryAgain().setEnabled(false);
            viewBingo.setVisible(false); //dinonaktifkan
            viewMenu.setVisible(true); //diaktifkan diawal menjalankan kode
            viewHistory.setVisible(false);
        };
    
    public BingoController(BingoModel model, BingoView viewBingo, MenuView viewMenu, HistoryView viewHistory) {
        this.model = model;
        this.viewBingo = viewBingo;
        this.viewMenu = viewMenu;
        this.viewHistory = viewHistory;
        
        Player playerX1 = new Player("1");
        //playerX1.setName("1");
    
        Player playerX2 = new Player("2");
        //playerX2.setName("2");
        //semua button disini dari BingoView dan MenuView
        // Main Menu (MainView)
        viewMenu.getBtnStartGame().addActionListener(e -> {
            //aksi button setelah diklik
            //viewBingo.setVisible(true);
            //viewMenu.difficultyMenuView();

            JFrame frame = new JFrame("Choose Difficulty");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 300); 
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            Dimension buttonSize = new Dimension(100, 50);

            JLabel label = new JLabel("Choose a difficulty");
            Font currentFont = label.getFont();
            label.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 20));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnBack = createSizedButton("Back", buttonSize);
            JButton btnEasy = createSizedButton("Easy", buttonSize);
            JButton btnNormal = createSizedButton("Normal", buttonSize);
            JButton btnHard = createSizedButton("Hard", buttonSize);

            // Set alignment for buttons to center them horizontally within the BoxLayout
            btnEasy.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnNormal.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnHard.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);


            btnBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });
            btnEasy.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    model.setDifficulty(new Easy());
                    viewBingo.setVisible(true);
                    frame.dispose();
                }
            });
            btnNormal.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    model.setDifficulty(new Normal());
                    viewBingo.setVisible(true);
                    frame.dispose();
                }
            });
            btnHard.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    model.setDifficulty(new Hard());
                    viewBingo.setVisible(true);
                    frame.dispose();
                }
            });


            // Tambahkan semua tombol ke panel
            panel.add(label);
            panel.add(Box.createVerticalStrut(20));
            panel.add(btnEasy);
            panel.add(Box.createVerticalStrut(10));
            panel.add(btnNormal);
            panel.add(Box.createVerticalStrut(10));
            panel.add(btnHard);
            panel.add(Box.createVerticalStrut(20));
            panel.add(btnBack);

            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
        
        viewMenu.getBtnGameHistory().addActionListener(e -> {
            viewHistory.loadHistoryFromFile();
            viewHistory.setVisible(true);
        });
        
        // Menu Bingo (BingoView)
        viewBingo.getBtnStartGame().addActionListener(e -> {
            
            model.setBoard(new BingoBoard(viewBingo.getBoard()));
            viewBingo.getbtnTryAgain().setEnabled(true);
            Component[] tileButtons = viewBingo.getBoard().getComponents();
            for (Component comp : tileButtons) {
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    button.addActionListener(ev -> {
                        if ("X".equals(button.getText()) || "O".equals(button.getText())) {
                            JOptionPane.showMessageDialog(viewBingo, "Tile sudah ditandai.");
                            return;
                        }
                        int tileNumber = Integer.parseInt(button.getText());
                        Question q = model.generateQuiz();
                        JFrame quizFrame = new JFrame("Quiz");
                        quizFrame.setLayout(new GridLayout(3, 1, 10, 10));
                        JLabel questionLabel = new JLabel(q.getText());
                        JTextField answerField = new JTextField();
                        JButton confirmBtn = new JButton("Confirm Answer");
                        confirmBtn.addActionListener(ev2 -> {
                            String answer = answerField.getText();
                            try {
                                if (answer.equals(Integer.toString(q.getAnswer()))) {
                                    model.getBoard().markTile(tileNumber, currentPlayer);
                                    JOptionPane.showMessageDialog(quizFrame, "Correct!");
                                    quizFrame.dispose();
                                    if(model.getBoard().checkWin(currentPlayer)){
                                       if(playerX1.getName().equals(Integer.toString(currentPlayer))){
                                          playerX1.incrementWinCount();
                                          viewBingo.getwin1Field().setText(Integer.toString(playerX1.getWinCount()));
                                          BingoModel.writeHistory(currentRound, playerX1, playerX2, playerX1); 
                                          JOptionPane.showMessageDialog(viewBingo, "Player 1 wins!"); 
                                        
                                         
                                       } else if(playerX2.getName().equals(Integer.toString(currentPlayer))){
                                          playerX2.incrementWinCount();
                                         viewBingo.getwin2Field().setText(Integer.toString(playerX2.getWinCount()));
                                         BingoModel.writeHistory(currentRound, playerX1, playerX2, playerX2);  
                                        JOptionPane.showMessageDialog(viewBingo, "Player 2 wins!");
                                       }
                                    } else if (model.getBoard().checkTie()) {
                                        BingoModel.writeHistory(currentRound, playerX1, playerX2, playerX2); 
                                        JOptionPane.showMessageDialog(viewBingo, "It's a tie!");

                                    
                                    }
                                    currentTurn++;
                                    currentPlayer = (currentPlayer == 1) ? 2 : 1; // switch turns
                                } else {
                                    JOptionPane.showMessageDialog(quizFrame, "Wrong answer!");
                                    quizFrame.dispose();
                                    currentTurn++;
                                    currentPlayer = (currentPlayer == 1) ? 2 : 1;
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(quizFrame, "Please enter a valid number.");
                            }
                        });
                        quizFrame.add(questionLabel);
                        quizFrame.add(answerField);
                        quizFrame.add(confirmBtn);
                        quizFrame.setSize(300, 150);
                        quizFrame.setLocationRelativeTo(null);
                        quizFrame.setVisible(true);
                    });
                }
            }
            
        });
        viewBingo.getbtnTryAgain().addActionListener(e -> {
            currentRound++;
            model.getBoard().resetGame();
            
//            //belom dimasukkin pertanyaannya
//            JFrame quizFrame = new JFrame("Quiz");
//            quizFrame.setLayout(new GridLayout(3, 1, 10, 10));
//            
//            Question q = model.generateQuiz();
//            
//            JLabel questionLabel = new JLabel(q.getText());
//            JTextField answerField = new JTextField();
//            JButton confirmBtn = new JButton("Confirm Answer");
//            confirmBtn.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    String answer = answerField.getText();
//                    if (answer.equals(Integer.toString(q.getAnswer()))) {
//                        JOptionPane.showMessageDialog(quizFrame, "Correct!");
//                        quizFrame.dispose(); // ngeclose window
//                    } else {
//                        JOptionPane.showMessageDialog(quizFrame, "Wrong. Try again.");
//                    }
//                }
//            });
//            quizFrame.add(questionLabel);
//            quizFrame.add(answerField);
//            quizFrame.add(confirmBtn);
//
//            quizFrame.setSize(300, 150);
//            quizFrame.setLocationRelativeTo(null); // Center the window
//            quizFrame.setVisible(true);
        });
        viewBingo.getBtnEndGame().addActionListener(e -> {
            if (!(playerX1 == null || playerX2 == null)) {
                BingoModel.writeHistory(playerX1, playerX2);  
        }
            
        });
        
        // Menu History (HistoryView)
        
    }
}