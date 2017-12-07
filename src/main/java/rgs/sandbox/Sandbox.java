package rgs.sandbox;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import rgs.game.*;
import rgs.player.*;

public class Sandbox {
    private static int clock = 0;
    private static int maxT = 0;
    private static int nEvo = 0;
    private static int maxE = 0;
    private static int nPlayer = 0;
    private static IStageGame sGame;
    private static List<Integer[]> history; // time, p1, p2, a1, a2, s1, s2

    public static int getTime() {
	return clock;
    }

    public static int getMaxT() {
	return maxT;
    }

    public static int getEvo() {
	return nEvo;
    }

    public static int getMaxE() {
	return maxE;
    }

    public static int getNumPlayer() {
	return nPlayer;
    }

    public static IStageGame getStageGame() {
	return sGame;
    }

    public static List<Integer[]> getHistory(int ID) {
	List<Integer[]> myHistory = new ArrayList<Integer[]>();
	for(Integer[] eachResult : history)
	    if(eachResult[1] == ID || eachResult[2] == ID)
		myHistory.add(eachResult);
	return myHistory;
    }

    public static int getLastAction(int opnID, int myID) {
	int lastOpnAct = -1;
	for(int i=history.size() - 1; i>=0; i--) {
	    Integer[] result = history.get(i);
	    if(result[1] == opnID && result[2] == myID) {
		lastOpnAct = result[3];
		break;
	    } else if(result[2] == opnID && result[1] == myID) {
		lastOpnAct = result[4];
		break;
	    }
	}
	return lastOpnAct;
    }

    private static int[] match(IPlayer p1, IPlayer p2) {
	int a1 = p1.act(p2.getID());
	int a2 = p2.act(p1.getID());
	int[] scores = sGame.getScores(a1, a2);
	Integer[] result = new Integer[7];
	result[0] = Sandbox.clock;
	result[1] = p1.getID();
	result[2] = p2.getID();
	result[3] = a1;
	result[4] = a2;
	result[5] = scores[0];
	result[6] = scores[1];
	history.add(result);
	// System.out.println("T:" + Sandbox.clock + "\t" + p1 + " plays " + a1 + " gets " + scores[0] + "\t" + p2 + " plays " + a2 + " gets " + scores[1]);
	return scores;
    }

    private static void repeatedGame(IPlayer p1, IPlayer p2, IStageGame stageGame, int maxTime) {
	clock = 0;
	maxT = maxTime;
	sGame = stageGame;
	history = new ArrayList<Integer[]>();
	for(Sandbox.clock = 0; Sandbox.clock<maxT; Sandbox.clock++) {
	    match(p1, p2);
	}
	int score1 = 0;
	int score2 = 0;
	for(Integer[] eachResult : history) {
	    score1 += eachResult[5];
	    score2 += eachResult[6];
	}
	System.out.println(p1 + " gets " + score1);
	System.out.println(p2 + " gets " + score2);
    }

    private static Map<IPlayer, Integer[]> roundRobin(IPlayer[] playerPool, int np, IStageGame stageGame, int maxTime) {
	clock = 0;
	maxT = maxTime;
	nPlayer = np;
	sGame = stageGame;
	history = new ArrayList<Integer[]>();

	Map<IPlayer, Integer[]> distribution = new HashMap<IPlayer, Integer[]>();

	for(Sandbox.clock = 0; Sandbox.clock<maxT; Sandbox.clock++) {
	    // System.out.print("t" + Sandbox.clock + "\t");
	    for(int p1=0; p1<nPlayer; p1++) {
		for(int p2=p1+1; p2<nPlayer; p2++) {
		    int[] s = match(playerPool[p1], playerPool[p2]);
		    boolean typeExist = false;
		    for(IPlayer eachType : distribution.keySet()) {
			if(eachType.sameTypeWith(playerPool[p1])) {
			    typeExist = true;
			    Integer[] newInt = new Integer[2];
			    newInt[0] = distribution.get(eachType)[0] + 1; // one more player
			    newInt[1] = distribution.get(eachType)[1] + s[0]; // add its score
			    distribution.put(eachType, newInt);
			    break;
			}
		    }
		    if(!typeExist) {
			Integer[] newInt = new Integer[2];
			newInt[0] = 1;
			newInt[1] = s[0];
			distribution.put(playerPool[p1], newInt);
		    }

		    typeExist = false;
		    for(IPlayer eachType : distribution.keySet()) {
			if(eachType.sameTypeWith(playerPool[p2])) {
			    typeExist = true;
			    Integer[] newInt = new Integer[2];
			    newInt[0] = distribution.get(eachType)[0] + 1; // one more match
			    newInt[1] = distribution.get(eachType)[1] + s[1]; // add its score
			    distribution.put(eachType, newInt);
			    break;
			}
		    }
		    if(!typeExist) {
			Integer[] newInt = new Integer[2];
			newInt[0] = 1;
			newInt[1] = s[1];
			distribution.put(playerPool[p2], newInt);
		    }
		}
	    }
	}
	System.out.println();
	for(IPlayer eachType : distribution.keySet()) {
	    double avgScore = (double)(distribution.get(eachType)[1]) / distribution.get(eachType)[0];
	    System.out.println(eachType + ", "+ avgScore + ", " + (distribution.get(eachType)[0]/maxTime/(nPlayer-1)));
	}
	return distribution;
    }

    private static IPlayer[] evolutionalRobin(IPlayer[] playerPool, int np, IStageGame stageGame, int maxTime, int maxEvo) {
	maxE = maxEvo;
	for(nEvo=0; nEvo<maxEvo; nEvo++) {
	    System.out.print("Round, " + nEvo + ",================");
	    Map<IPlayer, Integer[]> distribution = roundRobin(playerPool, np, new PrisonersDilemma(), maxTime);

	    double maxScore = Double.MIN_VALUE;
	    double minScore = Double.MAX_VALUE;
	    IPlayer winner=null, loser=null;
	    for(IPlayer eachType : distribution.keySet()) {
		double avgScore = (double)(distribution.get(eachType)[1]) / distribution.get(eachType)[0];
		if(avgScore > maxScore) {
		    maxScore = avgScore;
		    winner = eachType;
		}
		if(avgScore < minScore) {
		    minScore = avgScore;
		    loser = eachType;
		}
	    }
	    // System.out.println("Winner: " + winner);
	    // System.out.println("Loser:" + loser);
	    for(int p=0; p<nPlayer; p++) {
		if(playerPool[p].sameTypeWith(loser)) {
		    playerPool[p] = winner.duplicate();
		    break;
		}
	    }
	}
	return playerPool;
    }
    
    public static void main(String[] args) {
	System.out.println("sandbox start running.");

	// repeatedGame(new TitForTat(), new Recognizer(), new PrisonersDilemma(), 1000);

	
	int nEachPlayer = 10;
	int nRobin = 100;
	int nAI = 5;
	int nEvo = nEachPlayer*nAI;
	IPlayer[] playerPool = new IPlayer[nAI*nEachPlayer];
	for(int i=0; i<1*nEachPlayer; i++)
	    playerPool[i] = new Idiot();
	for(int i=1*nEachPlayer; i<2*nEachPlayer; i++)
	    playerPool[i] = new Selfishman();
	for(int i=2*nEachPlayer; i<3*nEachPlayer; i++)
	    playerPool[i] = new Globalist();
	for(int i=3*nEachPlayer; i<4*nEachPlayer; i++)
	    playerPool[i] = new TitForTat();
	for(int i=4*nEachPlayer; i<5*nEachPlayer; i++)
	   playerPool[i] = new Recognizer();

	
	IPlayer[] finalPlayerPool = evolutionalRobin(playerPool, nAI*nEachPlayer, new PrisonersDilemma(), nRobin, nEvo);
	

    }

}
