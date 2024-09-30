package org.jokana;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

public class GuessingGameController {
    @FXML public Button thrBtn;
    @FXML public Button guessBtn;
    @FXML public Label statusLabel;
    @FXML public Label player1;
    @FXML public Label player2;
    @FXML public Label player3;
    @FXML public Label resultDetails;
    @FXML public TextField playerNameInput;
    @FXML public TextField throwInput;
    @FXML public TextField guessInput;
    @FXML public TextArea player1Log;
    @FXML public TextArea player2Log;
    @FXML public TextArea player3Log;
    @FXML public TextArea gameLog;
    @FXML public VBox popup;
    @FXML public Label popLabel;
    @FXML public Label totalSticks;

    public Game game;
    public Human humanPlayer;
    public String humanName;
    public int guessValue;
    public int throwValue;
    public String thrInputStr;
    public String guessInputStr;

    @FXML
    public void initialize() {
        game = new Game(this);
        game.rtnHumanPlayer();

        throwInput.setEditable(false);
        thrBtn.setDisable(true);

        throwInput.textProperty().addListener((observable, oldValue, newValue) -> {thrBtn.setDisable(newValue.trim().isEmpty());
        });

        throwInput.setTextFormatter(new TextFormatter<>(change -> (change.getControlNewText().matches("\\d*")) ? change:null));

        guessInput.setEditable(false);
        guessBtn.setDisable(true);

        guessInput.textProperty().addListener((observable, oldValue, newValue) -> {guessBtn.setDisable(newValue.trim().isEmpty());
        });

        guessInput.setTextFormatter(new TextFormatter<>(change -> (change.getControlNewText().matches("\\d*")) ? change:null));
    }

    @FXML
    public void startGame() {
        String playerName = playerNameInput.getText();

        if (!playerName.isEmpty()) {
            humanPlayer = game.rtnHumanPlayer();
            humanPlayer.setName(playerName);
            //statusLabel.setText("Awesome, " + playerName + "! Time to test your intuition!");

            for(int i = 0; i< game.players.size(); i++){
                player1.setText(game.players.get(i).name);
            }
            
            gameLog.appendText("Game started with players: " + playerName + ", " + game.players.get(1).name + ", and " + game.players.get(2).name + ".\nGood Luck!\n");

            updatePlayersNames();

            throwInput.setEditable(true);
            thrBtn.setDisable(false);

            game.setPlayerThr(humanPlayer);
        } else {
            statusLabel.setText("I need to know your name first.");
        }
    }

    @FXML
    public void updatePlayersNames(){
        Label[] playersLabelArr = {player1, player2, player3};
        for (int i = 0; i < game.players.size(); i++){
            playersLabelArr[i].setText(game.players.get(i).name);
        }
    }

    @FXML
    public void revealPlayersThr(List<Player> players){
        TextArea[] playersLogArr = {player1Log, player2Log, player3Log};
        for (int i = 0; i < game.players.size(); i++){
            playersLogArr[i].appendText(game.players.get(i).name + " threw: " + game.players.get(i).thr);
        }
    }

    @FXML
    public void updatePlayersLogs(Player p){
        int index = game.players.indexOf(p);

        if (index == 1){
            player2Log.appendText(p.name + " is set!\n");
        } else {
            player3Log.appendText(p.name + " is set!\n");
        }
    }

    @FXML
    public void updatePGuessLogs(Player p){
        int index = game.players.indexOf(p);

        if (index == 1){
            player2Log.appendText(p.name +" guessed " + p.guess +".\n");
        } else {
            player3Log.appendText(p.name +" guessed " + p.guess +".\n");
        }
    }



    @FXML
    public void showPopup(){
        popup.setVisible(true);
    }

    @FXML
    public void closePopup(){
        popup.setVisible(false);
        throwInput.setEditable(true);
        thrBtn.setDisable(true);
        resultDetails.setText("");
        resetLogs();
    }

    @FXML
    public void resetLogs(){
        player1Log.clear();
        player2Log.clear();
        player3Log.clear();
    }  


    @FXML
    public void getThrInput(){
        if(!throwInput.isEditable()){
            return;
        }

        thrInputStr = throwInput.getText().trim();

        if(thrInputStr.isEmpty()){
            return;
        }

            throwValue = Integer.parseInt(thrInputStr);

            if (throwValue > humanPlayer.hand.size() - 1 || throwValue < 0) {
                gameLog.appendText("You can only hide the amount of sticks you have or 0.\n");
                throwInput.clear();
                return;         	
            }

            humanPlayer.setThr();
            player1Log.appendText("You threw: " + throwValue + "\n");
            
            
            throwInput.setEditable(false);
            thrBtn.setDisable(true);
            
            guessInput.setEditable(true);
            guessBtn.setDisable(false);
            
            throwInput.clear();

            game.setPlayerThr(game.players.get(1));
            game.setPlayerThr(game.players.get(2));
            resultDetails.setText(game.players.get(game.currentPlayerIndex).name + " starts guessing this time.");
            game.setPlayerGuess(game.players.get(game.currentPlayerIndex));
        }

    @FXML
    public void getGuessInput() {
            if (!guessInput.isEditable()){
                return;
            }

            guessInputStr = guessInput.getText().trim();

            if(guessInputStr.isEmpty()){
                return;
            }

            guessValue = Integer.parseInt(guessInputStr);

            if (game.existingGuesses.contains(guessValue)) {
                gameLog.appendText("Guess " + guessValue + " is taken. Guess again!");
                guessInput.clear();
                return;
            }

            humanPlayer.setGuess();
            game.existingGuesses.add(humanPlayer.guess);

            player1Log.appendText("Your guess: " + guessValue + "\n");
            guessInput.clear();
            game.turnControl++;

            guessInput.setEditable(false);
            guessBtn.setDisable(true);

            game.rotateTurn(game.players);
    }

    @FXML
    public void updateGameLog(String message) {
        gameLog.appendText(message);
    }

    @FXML
    public void startNewGame() {
        initialize();
        statusLabel.setText("New game started. Enter your name to begin.");
        playerNameInput.clear();
        throwInput.clear();
        guessInput.clear();
        game.turnControl = 0;
        game.existingGuesses.clear();
    }
}