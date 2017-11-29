package rgs.game;

public class PrisonersDilemma extends AStageGame {

    public PrisonersDilemma() {
	actionDimension = 2;
	payoffMatrix = new PayoffMatrix(actionDimension);
	payoffMatrix.matrix[0][0][0] = 3;
	payoffMatrix.matrix[0][0][1] = 3;
	payoffMatrix.matrix[0][1][0] = 0;
	payoffMatrix.matrix[0][1][1] = 5;
	payoffMatrix.matrix[1][0][0] = 5;
	payoffMatrix.matrix[1][0][1] = 0;
	payoffMatrix.matrix[1][1][0] = 1;
	payoffMatrix.matrix[1][1][1] = 1;
    }

}
