package rgs.player;

import java.util.Random;

import rgs.sandbox.Sandbox;
import rgs.game.IStageGame;
import rgs.game.PayoffMatrix;

public class Idiot extends APlayer {

    public Idiot() {
	super("Idiot");
    }
    
    public int act(int opnID) {
	Random rnd = new Random();
	return rnd.nextInt(Sandbox.getStageGame().actionDimension());
    }
    
    public IPlayer duplicate() {
	return new Idiot();
    }

}
