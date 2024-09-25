package org.openjfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Player {
	private static final List<Integer> INITIAL_HAND = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
	String name;
	List<Integer> hand = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
	int thr;
	int guess;
	int score = 0;

	public Player(){}

	public Player(String name){
		this.name = name;
	}

	public void updateHand() {
		if(!hand.isEmpty()){
			hand.remove(hand.size() - 1);
		}
	}

	public abstract int setThr();
	public abstract int setGuess(List<Integer> existingGuesses);

	public int getScore() { return score; }
	public void setScore(int score) { this.score = score; }

	public void resetHand() {
		this.hand.clear();
		this.hand.addAll(INITIAL_HAND);
		//this.score = 0;
	}
}
