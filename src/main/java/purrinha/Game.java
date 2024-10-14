package purrinha;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final LanguageManager langManager = LanguageManager.getInstance();
    public List<Player> players;
    public List<Integer> existingGuesses = new ArrayList<>();
    public int currentPlayerIndex;
    public int turnControl = 0;
    public GuessingGameController ctrl;
    public Random rd = new Random();
    public int totalOfSticks;

    public Game(GuessingGameController ctrl) {
        this.ctrl = ctrl;
        this.players = new ArrayList<>();
        initPlayers();
        this.currentPlayerIndex = getRandomIndex();
    }

    public void initPlayers() {
        Human humanPlayer = new Human(ctrl);
        players.add(humanPlayer);
        players.add(new Machine("Sophia", this));
        players.add(new Machine("Laura", this));
    }

    public String getCurrentPlayerName() {
        return players.get(currentPlayerIndex).name;
    }

    public Human rtnHumanPlayer() {
        return (Human) players.get(0);
    }

    public int getRandomIndex(){
        currentPlayerIndex = rd.nextInt(players.size());
        return currentPlayerIndex;
    }

    public void rotateTurn(List<Player> p) {
        if(turnControl < 3){
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            setPlayerGuess(p.get(currentPlayerIndex));
        } else {
            checkForWinner();
            turnControl = 0;
        }
    }

    public boolean isInstanceOfHuman(Player p) {
        return p instanceof Human;
    }

    public void setPlayerThr(Player p){
        if (isInstanceOfHuman(p)) {
            ctrl.setStatus("status.game.howMany", p.name);
            ctrl.getThrInput();
        } else {
            p.setThr();
            ctrl.updatePlayersLogs(p);
        }
    }

    public void setPlayerGuess(Player p) {
        if (isInstanceOfHuman(p)){
            ctrl.setStatus("status.game.turn", p.name);
            ctrl.getGuessInput();
        } else {
            p.setGuess();
            existingGuesses.add(p.guess);
            ctrl.updatePGuessLogs(p);
            turnControl++;
            rotateTurn(players);
        }
    }

    public void checkForWinner() {
        ctrl.resultDetails.setText("");
        totalOfSticks = calculateTotalSticks();
        ctrl.totalSticks.setText(langManager.getString("game.total.sticks", totalOfSticks));
        boolean winnerFound = false;
        for (Player p : players) {
            if (p.guess == totalOfSticks) {
                
                ctrl.popLabel.setText(langManager.getString("game.round.winner", p.name));
                p.updateHand();
                p.score++;
                ctrl.revealPlayersThr(players);
                ctrl.droppedASticks(p);
                ctrl.showPopup();
                winnerFound = true;
                if (p.score == 3) {
                    ctrl.statusLabel.setText(langManager.getString("status.winner"));
                    ctrl.popLabel.setText(langManager.getString("game.winner",p.name));
                    ctrl.continueBtn.setText(langManager.getString("button.continueBtn.keep"));
                    ctrl.showPopup();
                    ctrl.updateGameLog(players);
                    
                    resetGame(players);
                    return;
                }
                break;
            }
        }
        if(!winnerFound){
            ctrl.popLabel.setText(langManager.getString("game.round.noWinner"));
            ctrl.revealPlayersThr(players);
            ctrl.showPopup();
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        nextRound();
    }

    private int calculateTotalSticks() {
        int total = 0;
        for (Player p : players) {
            total += p.thr;
        }
        return total;
    }

    public void nextRound() {
        ctrl.setStatus("status.game.howMany", ctrl.humanPlayer.name);
        ctrl.gameLog.clear();
        existingGuesses.clear();
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        turnControl = 0;
        setPlayerThr(ctrl.humanPlayer);
    }

    public void resetGame(List<Player> players) {
        for (Player p : players) {
            p.resetHand();
        }
        existingGuesses.clear();
        currentPlayerIndex = rd.nextInt(players.size());
    }
}
