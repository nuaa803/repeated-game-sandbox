package rgs.player;

import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import rgs.sandbox.Sandbox;
import rgs.game.IStageGame;
import rgs.game.PayoffMatrix;

public class Recognizer extends APlayer {

    protected static int cooperateAction = -1;
    protected static int offenceAction = -1;
    protected int[] myCode;
    
    protected Set<Integer> enemies;
    protected Map<Integer, Integer> friends;
    
    public Recognizer() {
	super("Recognizer");
    }

    protected Recognizer(String alias) {
	super(alias);
    }

    protected void computeAction() {
	IStageGame sGame = Sandbox.getStageGame();
	PayoffMatrix payoffMatrix = sGame.getPayoffMatrix();
	int d = sGame.actionDimension();
	int globalScore[] = new int[d]; // records the scores including mine and my opponent's under each possible action
	int maxGlobalScore = -1;
	int myScore[] = new int[d]; // records the scores including mine and my opponent's under each possible action
	int maxBenifit = -1;
	for(int a=0; a<d; a++) { // try every possible actions
	    // if I took action a
	    for(int b=0; b<d; b++) {
		globalScore[a] += (payoffMatrix.matrix[a][b][0] + payoffMatrix.matrix[a][b][1]); // we care about all people
		myScore[a] += payoffMatrix.matrix[a][b][0]; // I care about only myself
	    }
	    if(globalScore[a] > maxGlobalScore) {
		cooperateAction = a;
		maxGlobalScore = globalScore[a];
	    }
	    if(myScore[a] > maxBenifit) {
		offenceAction = a;
		maxBenifit = myScore[a];
	    }
	}
    }

    protected int[] getCode(int ID) {
	int[] code = new int[3];
	int d = Sandbox.getStageGame().actionDimension();
	int buf = ID;
	for(int i=0; i<3; i++) {
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

	    int isRec = -1;
	    // check code
	    if(friends.get(opnID) >= 3) {
		// code checked, let's check his last action
		if(lastOpnAct != cooperateAction && lastOpnAct != -1) {
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
		    isRec = (friends.get(opnID)>=3 ? 2 : 1);
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
	    action = offenceAction;
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
	return new Recognizer();
    }

}
