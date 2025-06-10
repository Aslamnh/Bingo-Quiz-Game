/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BingoQuizGame;

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
    private QuizFrame viewQuiz;
    
    
     
   
   
    private int currentPlayer = 1;
    private int currentRound = 1;
    private int currentTurn = 1;
    
    //private JLabel timerLabel;
    private JLabel questionLabel;
    private JPanel optionsPanel;
    private JButton submitButton;

    //quiz components
    private JLabel turnDisplay;
    private JFrame quizFrame;
    private JTextField answerField;
    private JButton confirmBtn;
            
    private Timer quizTimer;
    private final int QUIZ_TIME = 15;
    private int remainingSeconds;
    
    private Player playerX1;
    private Player playerX2;

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
    
    private void setupTimer() {
        remainingSeconds = QUIZ_TIME; // Reset time

        quizTimer = new Timer(1000, new ActionListener() { // Timer fires every 1000ms (1 second)
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("1");
                remainingSeconds--;
                viewQuiz.setTimerLabel("Time: " + remainingSeconds + "s");

                if (remainingSeconds <= 0) {
                    viewQuiz.setTimerLabel("Time: 0s - Time's Up!");
                    quizTimer.stop();
                    timeUp(); //melakukan aksi saat waktu abis
                }
            }
        });
        quizTimer.start();
    }
    
//    public void startQuiz(Question q) {
//        remainingSeconds = QUIZ_TIME; // Reset time for a new quiz
//        timerLabel.setText("Time: " + QUIZ_TIME + "s");
//        timerLabel.setForeground(Color.RED); // Reset color
//        questionLabel.setText(q.getText()); // Set your actual question here
//        // Reset options selection if applicable
//        // Enable options and submit button
//        submitButton.setEnabled(true);
//        enableOptions(true);
//        quizTimer.restart(); // Start or restart the timer
//    }
    
     private void handleSubmit() {
        quizTimer.stop();
        viewQuiz.resetQuiz();
    }
     
    private void timeUp() {
        JOptionPane.showMessageDialog(quizFrame, "Time's up! Your answer is not submitted.");
        // Add logic for what happens when time is up (e.g., mark square as failed, move to next question)
        BingoModel.logEvent("Player "  + currentPlayer + " was late, tile is not mark");
        viewQuiz.resetQuiz();
        viewQuiz.dispose();
        changePlayerTurn();
    }
    
    private void changePlayerTurn() {
        currentTurn++;
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        viewQuiz.setTurnLabel("Current Turn: Player " + currentPlayer);
        System.out.println("Player: " + currentPlayer);
        BingoModel.logEvent("Player "  + currentPlayer + " Turn");
    }
    
    private void enableOptions(boolean enable) {
        for (Component comp : optionsPanel.getComponents()) {
            if (comp instanceof JRadioButton) {
                ((JRadioButton) comp).setEnabled(enable);
            }
        }
    }
    
    public void createQuiz(BingoTile bt) {
        
    }
    
    public void checkWin() {
        if(playerX1.getName().equals(Integer.toString(currentPlayer))){
            playerX1.incrementWinCount();
            viewBingo.getwin1Field().setText(Integer.toString(playerX1.getWinCount()));
            BingoModel.writeHistory(currentRound, playerX1, playerX2, playerX1);
            viewBingo.getbtnTryAgain().setEnabled(true);
            System.out.println("hey");
            BingoModel.logEvent("Player 1 wins!");
            JOptionPane.showMessageDialog(viewBingo, "Player 1 wins!");
        } else if(playerX2.getName().equals(Integer.toString(currentPlayer))){
            playerX2.incrementWinCount();
            viewBingo.getwin2Field().setText(Integer.toString(playerX2.getWinCount()));
            BingoModel.writeHistory(currentRound, playerX1, playerX2, playerX2);
            viewBingo.getbtnTryAgain().setEnabled(true);
            BingoModel.logEvent("Player 2 wins!");
            JOptionPane.showMessageDialog(viewBingo, "Player 2 wins!");
        } else if (model.getBoard().checkTie()) {
            BingoModel.writeHistory(currentRound, playerX1, playerX2, playerX2);
            viewBingo.getbtnTryAgain().setEnabled(true);
            BingoModel.logEvent("Tie");
            JOptionPane.showMessageDialog(viewBingo, "It's a tie!");
        }
    }

    // Helper to get selected radio button
    private JRadioButton getSelectedRadioButton(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JRadioButton) {
                JRadioButton rb = (JRadioButton) comp;
                if (rb.isSelected()) {
                    return rb;
                }
            }
        }
        return null;
    }
    
    public void checkGame() {
        
    }
      
    public void Start(){
            viewBingo.getbtnTryAgain().setEnabled(false);
            viewBingo.setVisible(false); //dinonaktifkan
            viewMenu.setVisible(true); //diaktifkan diawal menjalankan kode
            viewHistory.setVisible(false);
            viewMenu.setLocationRelativeTo(null);
        };
    
    public BingoController(BingoModel model, BingoView viewBingo, MenuView viewMenu, HistoryView viewHistory, QuizFrame viewQuiz) {
        this.model = model;
        this.viewBingo = viewBingo;
        this.viewMenu = viewMenu;
        this.viewHistory = viewHistory;
        this.viewQuiz = viewQuiz;
        model.setLogArea(viewBingo.getGameLogArea());
        
        playerX1 = new Player("1");
        //playerX1.setName("1");
    
        playerX2 = new Player("2");
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

            JButton btnBack = createSizedButton("Back to Menu", buttonSize);
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
                    viewBingo.setLocationRelativeTo(null);
                    frame.dispose();
                }
            });
            btnNormal.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    model.setDifficulty(new Normal());
                    viewBingo.setVisible(true);
                    viewBingo.setLocationRelativeTo(null);
                    frame.dispose();
                }
            });
            btnHard.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    model.setDifficulty(new Hard());
                    viewBingo.setVisible(true);
                    viewBingo.setLocationRelativeTo(null);
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
            viewHistory.loadHistory();
            viewHistory.setVisible(true);
            viewHistory.setLocationRelativeTo(null);
        });
        viewMenu.getBtnExit().addActionListener(e -> {
           System.exit(0);
        });
        
        // Menu Bingo (BingoView)
        viewBingo.getBtnStartGame().addActionListener(e -> {
            viewBingo.getroundCountField().setText(Integer.toString(currentRound));
            model.setBoard(new BingoBoard(viewBingo.getBoard()));
           BingoModel.logEvent("Game Start !!!");
           BingoModel.logEvent("Player "  + currentPlayer + " Turn");
           
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
                        BingoBoard board = model.getBoard();
                        BingoTile[][] tiles = board.getTiles();
                        model.generateQuizForTiles(); //menambahkan soal ke tiap tile
                        BingoTile currentTile = null;
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                if (tiles[i][j].getNumber() == tileNumber) {
                                    currentTile = tiles[i][j];
                                    break;
                                }
                            }
                        }
                        Question q = currentTile.getQuestion();
                        viewQuiz.setQuiz(tileNumber, currentTurn, currentTile);
                        viewQuiz.setTurnLabel("Current Turn: Player " + currentPlayer);
                        viewQuiz.setVisible(true);
                        setupTimer(); //memulai timer untuk quiznya
                        
                        //membersihkan action listener yang terbuat lebih dari 1
                        for (ActionListener al : viewQuiz.getBtnConfirm().getActionListeners()) {
                            viewQuiz.getBtnConfirm().removeActionListener(al);
                        }
                        viewQuiz.getBtnConfirm().addActionListener(e2 -> {
                            //System.out.println("Adding new ActionListener...");
                            //mengubah angka jadi int
                            int input = 0, answer = -1;
                            try {
                                input = Integer.parseInt(viewQuiz.getAnswerInput()); //input dari pengguna
                                answer = q.getAnswer(); //jawabannya
                                //cek jika answer sama
                                if (input == answer) {
                                    //System.out.println("Player " + currentPlayer);
                                    model.getBoard().markTile(tileNumber, currentPlayer);
                                    BingoModel.logEvent("Player "  + currentPlayer + " is Correct!");
                                    JOptionPane.showMessageDialog(viewQuiz, "Correct!");
                                    handleSubmit();
                                    if (model.getBoard().checkWin(currentPlayer)) {
                                        checkWin(); //method controller, beda dengan di model
                                        changePlayerTurn();
                                    }
                                    viewQuiz.dispose();
                                    changePlayerTurn();

                                } else {
                                    BingoModel.logEvent("Player "  + currentPlayer + " is wrong!");
                                    JOptionPane.showMessageDialog(viewQuiz, "Wrong answer!");
                                    //bikin jadi try again sampai waktu abis
            //                      viewQuiz.dispose();
            //                      changePlayerTurn();
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(viewQuiz, "Please enter a valid number.");
                            }
                        });
                    });
                }
            }
            viewBingo.getBtnStartGame().setEnabled(false);
        });
        viewBingo.getbtnTryAgain().addActionListener(e -> {
            currentRound++;
            model.getBoard().resetGame();
            viewBingo.getroundCountField().setText(Integer.toString(currentRound));
            viewBingo.getbtnTryAgain().setEnabled(false);
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
            BingoModel.logEvent("== End game ==");
            if (!(playerX1 == null || playerX2 == null)) {
                BingoModel.writeHistory(playerX1, playerX2); 
                if(model.getBoard().checkTie()){
                 BingoModel.writeHistory(currentRound, playerX1, playerX2, playerX2);
            }
        }
           
            
            System.exit(0);
        });
        
        viewBingo.getBtnBack().addActionListener(e -> {
            viewBingo.dispose();
            
        });
        // Menu History (HistoryView)
        
        viewHistory.getBtnClear().addActionListener(e -> {
            BingoModel.clearHistory();
            viewHistory.loadHistory();
        });
        
        viewHistory.getBtnBack().addActionListener(e -> {
            viewHistory.dispose();
        });
        
    }
}