package handler;

import logic.Move;

public interface IPlayerHandler {
	
	public Move getMove();
	
	public void moveSuccessfullyExecuted( Move move );
	
}
