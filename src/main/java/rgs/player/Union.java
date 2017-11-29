package rgs.player;

import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import rgs.sandbox.Sandbox;
import rgs.game.IStageGame;
import rgs.game.PayoffMatrix;

public class Union extends Recognizer {

    public Union() {
	super("Union");
    }

    public int act(int opnID) {
	if(cooperateAction == -1 && offenceAction == -1) {
	    computeAction();
	    // System.out.println("Recognizer: We only do '" + cooperateAction + "' for the common goods of Recognizers, and do '" + offenceAction + "' to others.");
	}
	
	if(enemies == null && friends == null) {
	    enemies = new TreeSet<Integer>();
	    friends = new HashMap<Integer, Integer>();
	    myCode = getCode(getID());
	}
	int action = -1;
	switch(isRecognizer(opnID)) {
	case 0: // not recognizer
	    List<Integer[]> history = Sandbox.getHistory(getID());
	    int lastOpnAct = -1;
	    for(int i=history.size() - 1; i>=0; i--) {
		Integer[] result = history.get(i);
		if(result[1] == opnID) {
		    lastOpnAct = result[3];
		    break;
		} else if(result[2] == opnID) {
		    lastOpnAct = result[4];
		    break;
		}
	    }
	    action = (lastOpnAct==-1 ? cooperateAction : lastOpnAct);
	    break;
	case 1: // checking code
	    action = myCode[friends.get(opnID)];
	    break;
	case 2: // maybe recognizer
	    action = cooperateAction;
	    break;
	}
	return action;
    }
    
    public IPlayer duplicate() {
	return new Union();
    }

}
