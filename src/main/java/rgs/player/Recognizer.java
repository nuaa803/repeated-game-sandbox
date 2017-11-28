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
    private int[] myCode;
    
    private Set<Integer> enemies;
    private Map<Integer, Integer> friends;
    
    public Recognizer() {
	super("Recognizer");
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

    private int[] getCode(int ID) {
	int[] code = new int[3];
	int d = Sandbox.getStageGame().actionDimension();
	int buf = ID;
	for(int i=0; i<3; i++) {
	    code[i] = buf%d;
	    buf /= d;
	}
	return code;
    }

    protected boolean isFriend(int opnID) {
	if(enemies.contains(opnID)) // recognized enemy
	    return false;
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

	    boolean friend = false;
	    // check code
	    if(friends.get(opnID) >= 2) {
		// code checked, let's check his last action
		if(lastOpnAct != cooperateAction) {
		    // he betraied me, enemy
		    friend = false;
		    System.out.println(opnID + " betraied me!");
		    if(opnID>=20 && opnID <=29)
			System.exit(0);
		} else {
		    // friend
		    friend = true;
		}
	    } else {
		// code unchecked
		if(opnCode[friends.get(opnID)] == lastOpnAct) {
		    // pass
		    friend = true;
		    friends.put(opnID, (friends.get(opnID)+1));
		} else {
		    // code check failed, enemy
		    friend = false;
		    System.out.println(opnID + " code wrong!");
		    if(opnID>=20 && opnID <=29)
			System.exit(0);
		    //System.out.println("friends.get(opnID) " + friends.get(opnID));
		    //System.out.println("opnCode[friends.get(opnID) " + opnCode[friends.get(opnID)]);
		    //System.out.println("lastOpnAct " + lastOpnAct);
		}
	    }

	    if(!friend) {
		// he failed the code check, or betraied me
		friends.remove(opnID);
		enemies.add(opnID);
	    }
	    return friend;
	} else {
	    // stranger
	    friends.put(opnID, new Integer(0));
	    return true;
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
	return (isFriend(opnID) ? (friends.get(opnID)>=2 ? cooperateAction : myCode[friends.get(opnID)]) : offenceAction);
    }
    
    public IPlayer duplicate() {
	return new Recognizer();
    }

}
