package org.openjfx;

import java.util.List;

public class Human extends Player {
		GuessingGameController ctrl;

		public Human(GuessingGameController ctrl){
			this.ctrl = ctrl;
		}

		public String getName(){return this.name;}

		public void setName(String name) {this.name = name;}

		public int getThr(){return this.thr;}

		public int setThr(){
			this.thr = ctrl.throwValue;
			return this.thr;
		}


		public int setGuess(List<Integer> existingGuesses){
			do {
				this.guess = ctrl.guessValue;

				if(existingGuesses.contains(this.guess)){
					ctrl.statusLabel.setText("Guess " + ctrl.guessValue + " is taken. Guess again!");
				}

			} while (existingGuesses.contains(this.guess));
			existingGuesses.add(this.guess);
			return this.guess;
		}
}
