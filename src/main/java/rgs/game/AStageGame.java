package rgs.game;

public abstract class AStageGame implements IStageGame {

    protected int actionDimension = 0;
    protected PayoffMatrix payoffMatrix;
    private int generousAction = -1;
    private int selfishAction = -1;

    @Override
    public final int actionDimension() {
	return actionDimension;
    }

    @Override
    public int generousAction() {
	if(generousAction == -1) {
	    int globalScore[] = new int[actionDimension]; // records the scores including mine and my opponent's under each possible action
	    int maxGlobalScore = -1;
	    for(int a=0; a<actionDimension; a++) { // try every possible actions
		// if I took action a
		for(int b=0; b<actionDimension; b++)
		    globalScore[a] += (payoffMatrix.matrix[a][b][0] + payoffMatrix.matrix[a][b][1]); // we care about all people
		if(globalScore[a] > maxGlobalScore) {
		    generousAction = a;
		    maxGlobalScore = globalScore[a];
		}
	    }
	}
	return generousAction;
    }

    @Override
    public int selfishAction() {
	if(selfishAction == -1) {
	    int myScore[] = new int[actionDimension]; // records the scores including mine and my opponent's under each possible action
	    int maxBenifit = -1;
	    for(int a=0; a<actionDimension; a++) { // try every possible actions
		// if I took action a
		for(int b=0; b<actionDimension; b++)
		    myScore[a] += payoffMatrix.matrix[a][b][0]; // I care about only myself
		if(myScore[a] > maxBenifit) {
		    selfishAction = a;
		    maxBenifit = myScore[a];
		}
	    }
	}
	return selfishAction;
    }
    
    @Override
    public int[] getScores(int a1, int a2) {
	int[] scores = new int[2];
	scores[0] = payoffMatrix.matrix[a1][a2][0];
	scores[1] = payoffMatrix.matrix[a1][a2][1];
	return scores;
    }

    @Override
    public PayoffMatrix getPayoffMatrix() {
	return payoffMatrix;
    }
}
