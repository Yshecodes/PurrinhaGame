package org.openjfx;

import java.util.List;
import java.util.Random;

public class Machine extends Player {
	Random rd = new Random();

	public Machine(String name){
		super(name);
	}

	public int setThr(){
		this.thr = rd.nextInt(this.hand.size());
		return this.thr;
	}

	public int setGuess(List<Integer> existingGuesses){
		Player human = Game.players.stream().filter(p -> p instanceof Human).findFirst().orElse(null);
		Player rivalMachine = Game.players.stream().filter(p -> p instanceof Machine && p != this).findFirst().orElse(null);
		int ownHand = this.thr;
		do {
			int humanHand = rd.nextInt(human.hand.size());
			int machineHand = rd.nextInt(rivalMachine.hand.size());
			this.guess = humanHand + machineHand + ownHand;
		} while (existingGuesses.contains(this.guess));
		return this.guess;
	}
}