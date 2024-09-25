package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GuessingGameController {
    @FXML public Label messageLabel;
    @FXML public Label statusLabel;
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
            
            gameLog.appendText("Game started with players: " + playerName + ", " + Game.players.get(1).name + ", and " + Game.players.get(2).name + ".\nGood Luck!");

            game.setPlayerThr(humanPlayer);
        } else {
            statusLabel.setText("I need to know your name first.");
        }
    }


    @FXML
    public void getThrInput() throws InterruptedException {
            throwValue = Integer.parseInt(throwInput.getText());
            humanPlayer.setThr();
            gameLog.appendText("You threw: " + throwValue + "\n");
            throwInput.clear();

            game.setPlayerThr(Game.players.get(1));
            game.setPlayerThr(Game.players.get(2));

            statusLabel.setText(Game.players.get(Game.currentPlayerIndex).name + " starts guessing this time.");

            game.setPlayerGuess(Game.players.get(Game.currentPlayerIndex));
    }


    @FXML
    public void getGuessInput() {
            guessValue = Integer.parseInt(guessInput.getText());
            humanPlayer.setGuess(Game.existingGuesses);
            gameLog.appendText("Your guess: " + guessValue + "\n");
            guessInput.clear();
            Game.turnControl++;

            gameLog.appendText("This is turn control count: " + Game.turnControl);
            game.rotateTurn(Game.players);
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
        Game.turnControl = 0;
        Game.existingGuesses.clear();
    }
}
