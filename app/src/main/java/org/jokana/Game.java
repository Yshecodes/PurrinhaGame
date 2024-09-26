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
            ctrl.updateGameLog("\nAlright, " + p.name + "! How many are you hiding?");
            ctrl.getThrInput();
        } else {
            p.setThr();
            ctrl.updateGameLog(p.name + " is set!");
        }
    }

    public void setPlayerGuess(Player p) {
        if (isInstanceOfHuman(p)){
            ctrl.statusLabel.setText("Your turn "+ p.name + "!" + "What is your guess? ");
            ctrl.getGuessInput();
        } else {
            p.setGuess(existingGuesses);
            existingGuesses.add(p.guess);
            ctrl.updateGameLog(p.name +" guessed " + p.guess +".");
            turnControl++;
            rotateTurn(players);
        }
    }

    public void checkForWinner() {
        ctrl.statusLabel.setText("I am checking the winner!");
        totalOfSticks = calculateTotalSticks();
        boolean winnerFound = false;
        for (Player p : players) {
            if (p.guess == totalOfSticks) {
                ctrl.resultLabel.setText(p.name + " was spot on!");
                p.updateHand();
                p.score++;
                winnerFound = true;
                if (p.score == 3) {
                    ctrl.statusLabel.setText(p.name + " WON!");
                    getGameState();
                    return;
                }
                break;
            }
        }
        if(!winnerFound){
            ctrl.resultLabel.setText("No one got it right!");
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

    public String getGameState() {
        StringBuilder state = new StringBuilder();
        for (Player p : players) {
        state.append(String.format("Player: %s, Hand: %s, Throw: %d, Guess: %d, Score: %d%n", 
        p.name, p.hand, p.thr, p.guess, p.score));
        }
        return state.toString();
    }

    public void nextRound() {
        ctrl.resultDetails.setText("I am rotating the round.");
        existingGuesses.clear();
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        turnControl = 0;
        setPlayerThr(ctrl.humanPlayer);
    }

    public void resetGame(List<Player> players) {
        for (Player p : players) {
            p.resetHand();
        }
        //Generating a new player
        currentPlayerIndex = rd.nextInt(players.size());
    }
}
