package client.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameBoardJavafxView extends HBox {

    private boolean playable = true;
    //Boardgame dimensions
    private int HEIGHT = 540;
    private int WIDTH = 540;
    private int tileWidth = 180;
    private int tileHeight = 180;
    private int tieCounter = 0;
    //Boardmatrix for containing our tiles
    private Tile[][] board = new Tile[3][3];
    //List for possible win combinations
    private List<Combo> combos = new ArrayList<>();
    //Game graphics and audio
    private Image crossImg = new Image("file:src/client/res/crossWhite.png");
    private Image circleImg = new Image("file:src/client/res/circleWhite.png");
    private FadeTransition fadeTransition;
    private AudioClip crossAudio = new AudioClip(GameBoardJavafxView.class.getResource("../res/plop.wav").toString());
    private AudioClip circleAudio = new AudioClip(GameBoardJavafxView.class.getResource("../res/plop.wav").toString());
    private AudioClip youWin = new AudioClip(GameBoardJavafxView.class.getResource("../res/youWin.wav").toString());
    private AudioClip youLose = new AudioClip(GameBoardJavafxView.class.getResource("../res/youLose.wav").toString());
    //Scoreboard components
    private String playerX = " (X)";
    private String playerO = " (O)";
    private String p1Name = "";
    private String p2Name = "";
    private String tieName = "Ties";
    private int p1Score = 0;
    private int p2Score = 0;
    private int tieScore = 0;
    private Label p1NameLbl = new Label();
    private Label p1TurnLbl = new Label();
    private Label p2NameLbl = new Label();
    private Label p2TurnLbl = new Label();
    private Label tieNameLbl = new Label(tieName);
    private Label p1ScoreLbl = new Label("0");
    private Label p2ScoreLbl = new Label("0");
    private Label tieScoreLbl = new Label("0");
    private Label playAgainLbl = new Label();
    //New game and exit game buttons
    private Button resetBut = new Button("Play again");
    private Button exitBut = new Button("Exit game");
    //Gameboard layouts
    private Pane gameBoard = new Pane();
    private HBox gameBoardHbox = new HBox();
    private VBox p1Vbox = new VBox(p1NameLbl, p1ScoreLbl, p1TurnLbl);
    private VBox p2Vbox = new VBox(p2NameLbl, p2ScoreLbl, p2TurnLbl);
    private VBox tieVbox = new VBox( tieNameLbl, tieScoreLbl);
    private VBox butHbox = new VBox(resetBut, exitBut, playAgainLbl);
    private VBox scoreBoardVbox = new VBox( p1Vbox, tieVbox, p2Vbox, butHbox);

    public GameBoardJavafxView(){

        gameBoardHbox = this;
        resetBut.setVisible(false);
        exitBut.setVisible(false);

        //Placement for the different components
        p1Vbox.setAlignment(Pos.TOP_CENTER);
        p2Vbox.setAlignment(Pos.TOP_CENTER);
        tieVbox.setAlignment(Pos.TOP_CENTER);
        scoreBoardVbox.setAlignment(Pos.TOP_CENTER);
        butHbox.setAlignment(Pos.CENTER);

        resetBut.setPrefWidth(220);
        exitBut.setPrefWidth(220);

        scoreBoardVbox.setPadding(new Insets(80));
        gameBoardHbox.getChildren().addAll(createContent(), scoreBoardVbox);
        gameBoardHbox.setPadding(new Insets(50, 0, 0, 30));
        gameBoardHbox.setAlignment(Pos.TOP_CENTER);
        //Adding css classes to our components
        circleAudio.setRate(1.5);
        p1Vbox.getStyleClass().add("playerScoreVBoxes");
        p2Vbox.getStyleClass().add("playerScoreVBoxes");
        tieVbox.getStyleClass().add("playerScoreVBoxes");
        resetBut.getStyleClass().add("form-button");
        exitBut.getStyleClass().add("form-button");

    }
    //
    private Parent createContent() {
        gameBoard.setPrefSize(WIDTH, HEIGHT);
        //Setting a windowsize for our tictactoeGame
        int id = 1;
        //A loop within a loop to add our tiles to the board matrix
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile(id);
                id++;
                tile.setTranslateX(j * tileWidth);
                tile.setTranslateY(i * tileHeight);
                gameBoard.getChildren().add(tile);
                board[j][i] = tile;
            }
        }
        //Setting up different types of win scenarios
        //Adding all the possible ways to win in an arraylist
        //Which we can later compare to the laid out pieces on our board
        // horizontal
        /*
        *0{0, 1, 2}
        *1{0, 1, 2}
        *2{0, 1, 2}
         */
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[0][x], board[1][x], board[2][x]));
        }
        // vertical
        /*
        * 0, 1, 2
        *{0, 0, 0}
        *{1, 1, 1}
        *{2, 2, 2}
         */
        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[y][0], board[y][1], board[y][2]));
        }
        // diagonals
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return gameBoard;
    }

    public void resetGameListener (EventHandler<ActionEvent> buttonListener){
        resetBut.setOnAction(buttonListener);
    }

    public void exitGameListener (EventHandler<ActionEvent> buttonListener){
        exitBut.setOnAction(buttonListener);
    }

    public void setPlayable(boolean playable){
        this.playable = playable;
    }

    //A boolean that compare our current tileplacements with our saved "Winning combinations"
    public boolean checkTiles() {
        boolean winningPlayer = false;
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                winningPlayer = true;
                playable = false;
                playWinAnimation(combo);
                break;
            }
        }
        return winningPlayer;
    }

    public void resetBoard(){
        Platform.runLater(() -> {
            //Looping through our tiles in the board and reset our images and texts
            for(int i = 0; i < 3; i++ ){
                for(int j = 0; j < 3; j++){
                    board[j][i].imgView.setImage(null);
                    board[j][i].text.setText(null);
                }
            }
            playable = true;
        });
    }


    private void playWinAnimation(Combo combo) {
        Platform.runLater(() -> {
            //Winning animation on the cross and circles
            for(int i = 0; i < 3; i++) {
                fadeTransition = new FadeTransition(Duration.seconds(.2), combo.tiles[i].imgView);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0.1);
                fadeTransition.setCycleCount(4);
                fadeTransition.setAutoReverse(true);
                fadeTransition.play();
            }
        });
    }

    public Tile[][] getBoard(){
        return board;
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayerX(String name){
        Platform.runLater(() -> {
            p1Name = name;
            p1NameLbl.setText(p1Name + playerX);
        });
    }

    public void setPlayerO(String name){
        Platform.runLater(() -> {
            p2Name = name;
            p2NameLbl.setText(p2Name + playerO);
        });
    }

    public void setPlayerTurnX(String turn){
        Platform.runLater(() ->{
            p1TurnLbl.setText(turn);
        });
    }

    public void setPlayerTurnO(String turn){
        Platform.runLater(() ->{
            p2TurnLbl.setText(turn);
        });
    }
    //Methods that increases the score on the scoreboard
    public void incPlayer1Score(){
        Platform.runLater(() -> {
            p1Score++;
            p1ScoreLbl.setText(String.valueOf(p1Score));
        });
    }

    public void incPlayer2Score(){
        Platform.runLater(() ->{
            p2Score++;
            p2ScoreLbl.setText(String.valueOf(p2Score));
        });
    }

    public void incTieScore(){
        Platform.runLater(() ->{
            tieScore++;
            tieScoreLbl.setText(String.valueOf(tieScore));
        });
    }

    public void resetScores(){
        Platform.runLater(() -> {
            //Clear scores:
            p1Score = 0;
            p2Score = 0;
            tieScore = 0;
            p1ScoreLbl.setText(String.valueOf(p1Score));
            tieScoreLbl.setText(String.valueOf(tieScore));
            p2ScoreLbl.setText(String.valueOf(p2Score));
        });
    }

    public void setPlayAgainBtnVisible(boolean visible){
        Platform.runLater(() -> resetBut.setVisible(visible));
    }

    public void setExitBtnVisible(boolean visible){
        Platform.runLater(() -> exitBut.setVisible(visible));
    }

    public void setPlayAgainLbl(String message){
        Platform.runLater(() -> playAgainLbl.setText(message));
    }

    public void playWinSound() {youWin.play();}

    public void playLostSound() {youLose.play();}
}
