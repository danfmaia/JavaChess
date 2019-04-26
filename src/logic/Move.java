package logic;

public class Move {
	public int sourceRow;
	public int sourceCol;
	public int targetRow;
	public int targetCol;
	
	public Move( int sourceRow, int sourceCol, int targetRow, int targetCol ){
		this.sourceRow = sourceRow;
		this.sourceCol = sourceCol;
		this.targetRow = targetRow;
		this.targetCol = targetCol;
	}
}
