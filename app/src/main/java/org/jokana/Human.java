package org.jokana;

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

		public int setGuess(){
			this.guess = ctrl.guessValue;
			return this.guess;
		}
}
