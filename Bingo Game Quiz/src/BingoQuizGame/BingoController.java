/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BingoQuizGame;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
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

    //quiz components
//    private JLabel turnDisplay;
    private JFrame quizFrame;
//    private JTextField answerField;
//    private JButton confirmBtn;
            
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
    
    //sistem timer dibuat Aslam
    private void setupTimer() {
        remainingSeconds = QUIZ_TIME;

        quizTimer = new Timer(1000, new ActionListener() {
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
    
     private void handleSubmit() {
        quizTimer.stop();
        viewQuiz.resetQuiz();
    }
     
    private void timeUp() {
        Component[] tileButtons = viewBingo.getBoard().getComponents();
        JOptionPane.showMessageDialog(quizFrame, "Time's up! Your answer is not submitted.");
        BingoModel.logEvent("Player "  + currentPlayer + " was late, tile is not mark");
        viewQuiz.resetQuiz();
        viewQuiz.dispose();
        changePlayerTurn();
        for (Component comps : tileButtons) {
            comps.setEnabled(true);
        }
    }
    
    private void changePlayerTurn() {
        currentTurn++;
        playerX1.incrementTurn();
        playerX2.incrementTurn();
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        viewQuiz.setTurnLabel("Current Turn: Player " + currentPlayer);
        //System.out.println("Player: " + currentPlayer);
        BingoModel.logEvent("Player "  + currentPlayer + " Turn");
    }
    
    //Zienard dan Razy
    public void checkWin() {
        if(playerX1.getName().equals(Integer.toString(currentPlayer))){
            playerX1.incrementWinCount();
            viewBingo.getwin1Field().setText(Integer.toString(playerX1.getWinCount()));
            model.writeHistory(currentRound, playerX1, playerX2, playerX1);
            viewBingo.getbtnTryAgain().setEnabled(true);
            BingoModel.logEvent("Player 1 wins!");
            BingoModel.setGameOver(true);
            JOptionPane.showMessageDialog(viewBingo, "Player 1 wins!");
            viewBingo.getBtnEndGame().setEnabled(false);
        } else if(playerX2.getName().equals(Integer.toString(currentPlayer))){
            playerX2.incrementWinCount();
            viewBingo.getwin2Field().setText(Integer.toString(playerX2.getWinCount()));
            model.writeHistory(currentRound, playerX1, playerX2, playerX2);
            viewBingo.getbtnTryAgain().setEnabled(true);
            BingoModel.logEvent("Player 2 wins!");
            BingoModel.setGameOver(true);
            JOptionPane.showMessageDialog(viewBingo, "Player 2 wins!");
            viewBingo.getBtnEndGame().setEnabled(false);
        } else if (model.getBoard().checkTie()) {
            model.writeHistory(currentRound, playerX1, playerX2, playerX2);
            viewBingo.getbtnTryAgain().setEnabled(true);
            BingoModel.logEvent("Tie");
            BingoModel.setGameOver(true);
            JOptionPane.showMessageDialog(viewBingo, "It's a tie!");
            viewBingo.getBtnEndGame().setEnabled(false);
        }
    }
      
    public void Start(){
            viewBingo.getbtnTryAgain().setEnabled(false);
            viewBingo.getBtnEndGame().setEnabled(false);
            viewBingo.setVisible(false);
            viewMenu.setVisible(true);
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
        playerX2 = new Player("2");
        
        
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
        //Zienard dan Aslam
        viewBingo.getBtnStartGame().addActionListener(e -> {
            viewBingo.getroundCountField().setText(Integer.toString(currentRound));
            model.setBoard(new BingoBoard(viewBingo.getBoard()));
            BingoModel.logEvent("Game Start !!!");
            System.out.println(playerX1.getWinCount() + " " + playerX2.getWinCount());
            BingoModel.logEvent("Player "  + currentPlayer + " Turn");
            viewBingo.getBtnEndGame().setEnabled(true);
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
                        for (Component comps : tileButtons) {
                            comps.setEnabled(false);
                        }
                        
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
                                    for (Component comps : tileButtons) {
                                        comps.setEnabled(true);
                                    }
                                    model.getBoard().markTile(tileNumber, currentPlayer);
                                    BingoModel.logEvent("Player "  + currentPlayer + " is Correct!");
                                    JOptionPane.showMessageDialog(viewQuiz, "Correct!");
                                    handleSubmit();
                                    if (model.getBoard().checkWin(currentPlayer)) {
                                        checkWin(); //method controller, beda dengan di model
                                        //System.out.println(playerX1.getWinCount() + " " + playerX2.getWinCount());
                                        changePlayerTurn();
                                    }
                                    if (currentTurn == 25 && model.getBoard().checkTie()) {
                                        model.writeHistory(currentRound, playerX1, playerX2, playerX2);
                                        viewBingo.getbtnTryAgain().setEnabled(true);
                                        BingoModel.logEvent("Tie");
                                        BingoModel.setGameOver(true);
                                        JOptionPane.showMessageDialog(viewBingo, "It's a tie!");
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
        
        //Razy
        viewBingo.getbtnTryAgain().addActionListener(e -> {
            currentRound++;
            model.getBoard().resetGame();
            playerX1.setPlayerTurn(0);
            playerX2.setPlayerTurn(0);
            currentTurn = 1;
            
            viewBingo.getroundCountField().setText(Integer.toString(currentRound));
            viewBingo.getbtnTryAgain().setEnabled(false);
            viewBingo.getBtnEndGame().setEnabled(true);
        });
        
        //Razy
        viewBingo.getBtnEndGame().addActionListener(e -> {
            BingoModel.logEvent("== End game ==");
            viewBingo.getbtnTryAgain().setEnabled(true);
            
            Component[] comps = viewBingo.getBoard().getComponents();
            for (Component c : comps) {
            if (c instanceof JButton) {
                ((JButton) c).setEnabled(false);
                      }
                 }

                if (!(playerX1 == null || playerX2 == null)) {
                    model.writeHistory(currentRound, playerX1, playerX2, playerX2);
                }
            
           
            viewBingo.getBtnEndGame().setEnabled(false);
            //System.exit(0);
        });
        
        viewBingo.getBtnBack().addActionListener(e -> {
            model.writeHistory(playerX1, playerX2); 
            viewBingo.dispose();    
        });
        
        
        // Menu History (HistoryView)       
        viewHistory.getBtnClear().addActionListener(e -> {
            model.clearHistory();
            viewHistory.loadHistory();
            JOptionPane.showMessageDialog(viewHistory, "History Cleared!");
        });
        
        viewHistory.getBtnBack().addActionListener(e -> {
            viewHistory.dispose();
        });
        
    }
}