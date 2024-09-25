package org.openjfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static List<Player> players;
    public static List<Integer> existingGuesses = new ArrayList<>();
    public static int currentPlayerIndex;
    public static int turnControl = 0;
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
        players.add(new Machine("Sophia"));
        players.add(new Machine("Laura"));
    }

    public String getCurrentPlayerName() {
        return players.get(currentPlayerIndex).name;
    }

    public Human rtnHumanPlayer() {
        return (Human) players.get(0);
    }

    public int getRandomIndex(){
        currentPlayerIndex = rd.nextInt(3);
        return currentPlayerIndex;
    }

    public void rotateTurn(List<Player> p) {
        if(turnControl < 3){
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            setPlayerGuess(p.get(currentPlayerIndex));
        } else {
            checkForWinner();
        }
    }

    public boolean isInstanceOfHuman(Player p) {
        return p instanceof Human;
    }

    public void setPlayerThr(Player p) throws InterruptedException{
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
        for (Player p : players) {
            if (p.guess == totalOfSticks) {
                p.updateHand();
                p.score++;
                if (p.score == 3) {
                    getGameState();
                } else {
                    nextRound();
                }
            }
        }
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
        ctrl.statusLabel.setText("I am rotating the round.");
        existingGuesses.clear();
        turnControl = 0;
        rotateTurn(players);
    }

    public void resetGame() {
        for (Player p : players) {
            p.resetHand();
        }
        currentPlayerIndex = rd.nextInt(players.size());
    }
}
