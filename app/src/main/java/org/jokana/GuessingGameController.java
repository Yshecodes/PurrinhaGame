package org.jokana;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GuessingGameController {
    @FXML public Label statusLabel;
    @FXML public Label resultLabel;
    @FXML public Label resultDetails;
    @FXML public TextField playerNameInput;
    @FXML public TextField throwInput;
    @FXML public TextField guessInput;
    @FXML public TextArea gameLog;

    public Game game;
    public Human humanPlayer;
    public String humanName;
    public int guessValue;
    public int throwValue;

    @FXML
    public void initialize() {
        game = new Game(this);
        game.rtnHumanPlayer();
    }

    @FXML
    public void startGame() throws InterruptedException {
        String playerName = playerNameInput.getText();

        if (!playerName.isEmpty()) {
            humanPlayer = game.rtnHumanPlayer();
            humanPlayer.setName(playerName);
            statusLabel.setText("Awesome, " + playerName + "! Time to test your intuition!");
            
            gameLog.appendText("Game started with players: " + playerName + ", " + game.players.get(1).name + ", and " + game.players.get(2).name + ".\nGood Luck!");

            game.setPlayerThr(humanPlayer);
        } else {
            statusLabel.setText("I need to know your name first.");
        }
    }

    @FXML
    public void getThrInput(){
            throwValue = Integer.parseInt(throwInput.getText());
            humanPlayer.setThr();
            gameLog.appendText("You threw: " + throwValue + "\n");
            throwInput.clear();

            game.setPlayerThr(game.players.get(1));
            game.setPlayerThr(game.players.get(2));

            resultDetails.setText(game.players.get(game.currentPlayerIndex).name + " starts guessing this time.");

            

            game.setPlayerGuess(game.players.get(game.currentPlayerIndex));
    }

    @FXML
    public void getGuessInput() {
            guessValue = Integer.parseInt(guessInput.getText());
            humanPlayer.setGuess(game.existingGuesses);
            gameLog.appendText("Your guess: " + guessValue + "\n");
            guessInput.clear();
            game.turnControl++;

            gameLog.appendText("This is turn control count: " + game.turnControl);
            game.rotateTurn(game.players);
    }

    @FXML
    public void updateGameLog(String message) {
        gameLog.appendText(message + "\n");
    }

    @FXML
    public void startNewGame() {
        initialize();
        gameLog.clear();
        statusLabel.setText("New game started. Enter your name to begin.");
        playerNameInput.clear();
        throwInput.clear();
        guessInput.clear();
        game.turnControl = 0;
        game.existingGuesses.clear();
    }
}
