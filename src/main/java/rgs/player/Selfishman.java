package rgs.player;

import rgs.sandbox.Sandbox;
import rgs.game.IStageGame;
import rgs.game.PayoffMatrix;

public class Selfishman extends APlayer {

    public Selfishman() {
	super("Selfishman");
    }

    public int act(int opnID) {
	return Sandbox.getStageGame().selfishAction();
    }
    
    public IPlayer duplicate() {
	return new Selfishman();
    }
}
