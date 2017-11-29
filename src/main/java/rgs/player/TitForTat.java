package rgs.player;

import rgs.sandbox.Sandbox;
import rgs.game.IStageGame;
import rgs.game.PayoffMatrix;

public class TitForTat extends Globalist {

    public TitForTat() {
	super("TitForTat");
    }
    
    public int act(int opnID) {
	int lastOpnAct = Sandbox.getLastAction(opnID, getID());
	return lastOpnAct == -1 ? Sandbox.getStageGame().generousAction() : lastOpnAct;
    }
    
    public IPlayer duplicate() {
	return new TitForTat();
    }
}
