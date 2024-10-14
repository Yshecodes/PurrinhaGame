package purrinha;

import java.util.Random;

public class Machine extends Player {
	private Game game;
	Random rd = new Random();

	public Machine(String name, Game game){
		super(name);
		this.game = game;
	}

	public int setThr(){
		this.thr = rd.nextInt(this.hand.size());
		return this.thr;
	}

	public int setGuess(){
		Player human = game.players.stream().filter(p -> p instanceof Human).findFirst().orElse(null);
		Player rivalMachine = game.players.stream().filter(p -> p instanceof Machine && p != this).findFirst().orElse(null);
		int ownHand = this.thr;
		do {
			int humanHand = rd.nextInt(human.hand.size());
			int machineHand = rd.nextInt(rivalMachine.hand.size());
			this.guess = humanHand + machineHand + ownHand;
		} while (game.existingGuesses.contains(this.guess));
		return this.guess;
	}
}