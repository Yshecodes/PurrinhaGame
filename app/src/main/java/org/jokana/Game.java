package org.jokana;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
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
            ctrl.statusLabel.setText("Alright, " + p.name + "! How many are you hiding?");
            ctrl.getThrInput();
        } else {
            p.setThr();
            ctrl.updatePlayersLogs(p);
        }
    }

    public void setPlayerGuess(Player p) {
        if (isInstanceOfHuman(p)){
            ctrl.statusLabel.setText("Your turn "+ p.name + "!" + " What is your guess? ");
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
        ctrl.statusLabel.setText("PURRINHA");
        ctrl.resultDetails.setText("");
        totalOfSticks = calculateTotalSticks();
        ctrl.totalSticks.setText("Total sticks: " + totalOfSticks);
        boolean winnerFound = false;
        for (Player p : players) {
            if (p.guess == totalOfSticks) {
                ctrl.popLabel.setText(p.name + " was spot on!");
                p.updateHand();
                p.score++;
                ctrl.revealPlayersThr(players);
                ctrl.droppedASticks(p);
                ctrl.showPopup();
                winnerFound = true;
                if (p.score == 3) {
                    ctrl.popLabel.setText(p.name + " WON!");
                    ctrl.showPopup();
                    ctrl.updateGameLog(players);
                    resetGame(players);
                    return;
                }
                break;
            }
        }
        if(!winnerFound){
            ctrl.popLabel.setText("No one got it right!");
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
        //Generating a new starting order
        currentPlayerIndex = rd.nextInt(players.size());
    }
}
