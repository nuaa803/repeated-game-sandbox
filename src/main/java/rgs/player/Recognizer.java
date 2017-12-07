package rgs.player;

import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;

import rgs.sandbox.Sandbox;
import rgs.game.IStageGame;
import rgs.game.PayoffMatrix;

public class Recognizer extends APlayer {

    protected int[] myCode;
    protected Set<Integer> enemies;
    protected Map<Integer, Integer> friends;
    protected static int codeLength = -1;
    
    public Recognizer() {
	super("Recognizer");
    }

    protected int[] getCode(int ID) {
	if(Recognizer.codeLength == -1) {
	    Recognizer.codeLength = (int)(Math.log((double)Sandbox.getNumPlayer()/2) / Math.log((double)2));// 
	    // System.out.println("code length: " + codeLength);
	}
	int[] code = new int[codeLength];
	int d = Sandbox.getStageGame().actionDimension();
	int buf = ID;
	for(int i=0; i<codeLength; i++) {
	    code[i] = buf%d;
	    buf /= d;
	}
	/*
	System.out.print("ID:" + ID + "\tcode:");
	for(int eachC : code)
	    System.out.print(eachC + ",");
	System.out.println();
	*/
	return code;
    }

    /**
     * @return whether opnID is a friend.
     *         0 indicates opnID is NOT a Recognizer;
     *         1 indicates opnID's code is still under checking
     *         2 indicates opnID's code is already checked
     */
    protected int isRecognizer(int opnID) {
	if(enemies.contains(opnID)) // recognized enemy
	    return 0;
	else if(friends.keySet().contains(opnID)) { // temporary friend
	    // get code and opn's last action
	    int[] opnCode = getCode(opnID);
	    int lastOpnAct = Sandbox.getLastAction(opnID, getID());

	    int isRec = -1;
	    // check code
	    if(friends.get(opnID) >= codeLength) {
		// code checked, let's check his last action
		if(lastOpnAct != Sandbox.getStageGame().generousAction() && lastOpnAct != -1) {
		    // he betraied me, enemy
		    isRec = 0;
		} else {
		    // remain friend
		    isRec = 2;
		}
	    } else {
		// code unchecked
		if(opnCode[friends.get(opnID)] == lastOpnAct) {
		    // pass
		    friends.put(opnID, (friends.get(opnID)+1));
		    isRec = (friends.get(opnID)>=codeLength ? 2 : 1);
		} else {
		    // code check failed, not recognizer
		    isRec = 0;
		}
	    }

	    if(isRec == 0) {
		// he failed the code check, or betraied me
		friends.remove(opnID);
		enemies.add(opnID);
	    }
	    return isRec;
	} else {
	    // stranger
	    friends.put(opnID, new Integer(0));
	    return 1;
	}
    }

    public int act(int opnID) {
	if(enemies == null && friends == null) {
	    enemies = new TreeSet<Integer>();
	    friends = new HashMap<Integer, Integer>();
	    myCode = getCode(getID());
	}
	int action = -1;
	switch(isRecognizer(opnID)) {
	case 0: // not recognizer
	    action = Sandbox.getStageGame().selfishAction();
	    break;
	case 1: // checking code
	    action = myCode[friends.get(opnID)];
	    break;
	case 2: // maybe recognizer
	    action = Sandbox.getStageGame().generousAction();
	    break;
	}
	return action;
    }
    
    public IPlayer duplicate() {
	return new Recognizer();
    }

}
