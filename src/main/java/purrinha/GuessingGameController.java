package purrinha;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class GuessingGameController {
    private final LanguageManager langManager = LanguageManager.getInstance();
    private String statusKey;
    public Object[] statusArgs;

    @FXML public Button startBtn;
    @FXML public Button thrBtn;
    @FXML public Button guessBtn;
    @FXML public Button continueBtn;
    @FXML public Button newGameBtn;
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

        statusKey = "status.initial";
        statusArgs = new Object[]{};

        langManager.getCurrentLocale().addListener((observable, oldValue, newValue) -> updateUIText());

        updateUIText();

        continueBtn.setOnAction(event -> {
            closePopup();
            setStatus("status.game.howMany", humanPlayer.name);
        });

        throwInput.setEditable(false);
        thrBtn.setDisable(true);

        throwInput.textProperty().addListener((observable, oldValue, newValue) -> thrBtn.setDisable(newValue.trim().isEmpty()));

        throwInput.setTextFormatter(new TextFormatter<>(change -> (change.getControlNewText().matches("\\d*")) ? change:null));

        guessInput.setEditable(false);
        guessBtn.setDisable(true);

        guessInput.textProperty().addListener((observable, oldValue, newValue) -> {guessBtn.setDisable(newValue.trim().isEmpty());
        });

        guessInput.setTextFormatter(new TextFormatter<>(change -> (change.getControlNewText().matches("\\d*")) ? change:null));

    }

    private void updateUIText() {
        startBtn.setText(langManager.getString("button.startBtn"));
        thrBtn.setText(langManager.getString("button.thrBtn"));
        guessBtn.setText(langManager.getString("button.guessBtn"));
        continueBtn.setText(langManager.getString("button.continueBtn"));
        newGameBtn.setText(langManager.getString("button.newGameBtn"));
        playerNameInput.setPromptText(langManager.getString("input.name.prompt"));
        throwInput.setPromptText(langManager.getString("input.throw.prompt"));
        guessInput.setPromptText(langManager.getString("input.guess.prompt"));
        updateStatusLabel();
    }

    @FXML
    public void setEn(){
        langManager.changeLanguage("en");
        updateUIText();
    }

    @FXML
    public void setPt(){
        langManager.changeLanguage("pt");
        updateUIText(); 
    }

    @FXML
    public void setJa(){
        langManager.changeLanguage("ja");
        updateUIText();
    }

    @FXML
    public void startGame() {
        String playerName = playerNameInput.getText();

        if (!playerName.isEmpty()) {
            humanPlayer = game.rtnHumanPlayer();
            humanPlayer.setName(playerName);

            setStatus("status.game.started", playerName);

            for(int i = 0; i< game.players.size(); i++){
                player1.setText(game.players.get(i).name);
            }
            
            gameLog.appendText(langManager.getString("game.log.started", playerName, game.players.get(1).name, game.players.get(2).name));

            updatePlayersNames();

            throwInput.setEditable(true);
            thrBtn.setDisable(false);

            game.setPlayerThr(humanPlayer);
        } else {
            setStatus("error.name.required");
        }
    }

    private void updateStatusLabel() {
        if (statusKey != null) {
            statusLabel.setText(langManager.getString(statusKey, statusArgs));
        } else {
            statusLabel.setText(langManager.getString("status.initial"));
        }
    }

    public void setStatus(String key, Object... args) {
        statusKey = key;
        statusArgs = args;
        updateStatusLabel();
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
            playersLogArr[i].appendText(langManager.getString("game.log.thrReveal", game.players.get(i).name, game.players.get(i).thr));
        }
    }

    @FXML
    public void droppedASticks(Player p){
        TextArea[] playersLogArr = {player1Log, player2Log, player3Log};
        int index = game.players.indexOf(p);
        playersLogArr[index].appendText(langManager.getString("game.log.dropped.stick", p.name));
    }

    @FXML
    public void updatePlayersLogs(Player p){
        int index = game.players.indexOf(p);

        if (index == 1){
            player2Log.appendText(langManager.getString("game.log.player.ready", p.name));
        } else {
            player3Log.appendText(langManager.getString("game.log.player.ready", p.name));
        }
    }

    @FXML
    public void updatePGuessLogs(Player p){
        int index = game.players.indexOf(p);

        if (index == 1){
            player2Log.appendText(langManager.getString("game.log.player.guessed", p.name, p.guess));
        } else {
            player3Log.appendText(langManager.getString("game.log.player.guessed", p.name, p.guess));
        }
    }


    @FXML
    public void showPopup(){
        popup.setVisible(true);
        setStatus("status.winner");
    }

    @FXML
    public void closePopup(){
        popup.setVisible(false);
        throwInput.setEditable(true);
        thrBtn.setDisable(true);
        resultDetails.setText("");
        resetLogs();
        gameLog.clear();
        continueBtn.setText(langManager.getString("button.continueBtn"));
    }

    @FXML
    public void resetLogs(){
        player1Log.clear();
        player2Log.clear();
        player3Log.clear();
    }  


    @FXML
    public void getThrInput(){
        setStatus("status.game.howMany", humanPlayer.name);
        startBtn.setDisable(true);

        if(!throwInput.isEditable()){
            return;
        }

        thrInputStr = throwInput.getText().trim();

        if(thrInputStr.isEmpty()){
            return;
        }

        throwValue = Integer.parseInt(thrInputStr);
            
        int remainingSticks = humanPlayer.hand.size() - 1;
        if (throwValue > remainingSticks || throwValue < 0) {
        gameLog.appendText(langManager.getString("error.invalid.throw", remainingSticks, remainingSticks));
            throwInput.clear();
            return;         	
        }

            humanPlayer.setThr();
            player1Log.appendText(langManager.getString("game.log.you.threw", throwValue));
            
            throwInput.setEditable(false);
            thrBtn.setDisable(true);
            
            guessInput.setEditable(true);
            guessBtn.setDisable(false);
            
            throwInput.clear();

            game.setPlayerThr(game.players.get(1));
            game.setPlayerThr(game.players.get(2));
            resultDetails.setText(langManager.getString("game.turn.start", game.players.get(game.currentPlayerIndex).name));
            game.setPlayerGuess(game.players.get(game.currentPlayerIndex));
        }


    @FXML
    public void getGuessInput() {
        if (!guessInput.isEditable()) {
            return;
        }

        guessInputStr = guessInput.getText().trim();

        if(guessInputStr.isEmpty()) {
            return;
        }

        guessValue = Integer.parseInt(guessInputStr);

        if (game.existingGuesses.contains(guessValue)) {
            gameLog.appendText(langManager.getString("error.guess.taken", guessValue));
            guessInput.clear();
            return;
        }

        humanPlayer.setGuess();
        game.existingGuesses.add(humanPlayer.guess);

        player1Log.appendText(langManager.getString("game.log.player.guessed", humanPlayer.name, guessValue));
        guessInput.clear();
        game.turnControl++;

        guessInput.setEditable(false);
        guessBtn.setDisable(true);

        game.rotateTurn(game.players);
        setStatus("status.winner");
    }

    @FXML
    public void updateGameLog(List<Player> players) {
        for(Player p : players) {
            gameLog.appendText(langManager.getString("game.log.final.score", 
                p.name, p.score));
        }
    }

    @FXML
    public void startNewGame() {
        initialize();

        playerNameInput.clear();

        throwInput.clear();
        guessInput.clear();
        player1Log.clear();
        player2Log.clear();
        player3Log.clear();
        gameLog.clear();
        closePopup();

        game.turnControl = 0;
        game.existingGuesses.clear();

        startBtn.setDisable(false);
        setStatus("status.new.name");

        playerNameInput.setEditable(true);
    }
}
