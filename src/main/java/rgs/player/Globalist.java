package rgs.player;

import rgs.sandbox.Sandbox;
import rgs.game.IStageGame;
import rgs.game.PayoffMatrix;

public class Globalist extends APlayer {

    protected static int commonAction = -1;

    public Globalist() {
	super("Globalist");
    }

    protected Globalist(String alias) {
	super(alias);
    }

    public int act(int opnID) {
	return Sandbox.getStageGame().generousAction();
    }
    
    public IPlayer duplicate() {
	return new Globalist();
    }

}
