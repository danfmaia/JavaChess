package logic;

import java.util.ArrayList;
import java.util.List;

import logic.Piece.Team;
import logic.Piece.Type;

public class ChessGame {
	
	public static enum GameState {
		WHITE,
		BLACK,
		END;
		
		public boolean equals(Team team) {
			return
				this == GameState.WHITE && team == Team.WHITE ||
				this == GameState.BLACK && team == Team.BLACK;
		}
	}
	
	private GameState gameState = GameState.WHITE;
	private GameState lastGameState;
	private List<Piece> pieces = new ArrayList<Piece>();
	private MoveValidator moveValidator = new MoveValidator( this );
	
	/**
	 * Initializes game.
	 */
	public ChessGame(){		
		// :: CREATES AND PLACES PIECES ::
		
		// White rook, knight, bishop, queen, king, bishop, knight and rook
		createAndAddPiece( Team.WHITE, Type.ROOK, Piece.ROW_1, Piece.COL_A );
	    createAndAddPiece( Team.WHITE, Type.KNIGHT, Piece.ROW_1, Piece.COL_B );
	    createAndAddPiece( Team.WHITE, Type.BISHOP, Piece.ROW_1, Piece.COL_C );
	    createAndAddPiece( Team.WHITE, Type.QUEEN, Piece.ROW_1, Piece.COL_D );
	    createAndAddPiece( Team.WHITE, Type.KING, Piece.ROW_1, Piece.COL_E );
	    createAndAddPiece( Team.WHITE, Type.BISHOP, Piece.ROW_1, Piece.COL_F );
	    createAndAddPiece( Team.WHITE, Type.KNIGHT, Piece.ROW_1, Piece.COL_G );
	    createAndAddPiece( Team.WHITE, Type.ROOK, Piece.ROW_1, Piece.COL_H );
	    
	    // White pawns
	    int currentCol = Piece.COL_A;
	    for( int i=0; i<8; i++ ){
	    	createAndAddPiece( Team.WHITE, Type.PAWN, Piece.ROW_2, currentCol );
	    	currentCol++;
	    }
	    
	    // Black rook, knight, bishop, queen, king, bishop, knight and rook
	    createAndAddPiece( Team.BLACK, Type.ROOK, Piece.ROW_8, Piece.COL_A );
	    createAndAddPiece( Team.BLACK, Type.KNIGHT, Piece.ROW_8, Piece.COL_B );
	    createAndAddPiece( Team.BLACK, Type.BISHOP, Piece.ROW_8, Piece.COL_C );
	    createAndAddPiece( Team.BLACK, Type.QUEEN, Piece.ROW_8, Piece.COL_D );
	    createAndAddPiece( Team.BLACK, Type.KING, Piece.ROW_8, Piece.COL_E );
	    createAndAddPiece( Team.BLACK, Type.BISHOP, Piece.ROW_8, Piece.COL_F );
	    createAndAddPiece( Team.BLACK, Type.KNIGHT, Piece.ROW_8, Piece.COL_G );
	    createAndAddPiece( Team.BLACK, Type.ROOK, Piece.ROW_8, Piece.COL_H );
        
        // Black pawns
	    currentCol = Piece.COL_A;
	    for( int i=0; i<8; i++ ){
	    	createAndAddPiece( Team.BLACK, Type.PAWN, Piece.ROW_7, currentCol );
	    	currentCol++;
	    }	
	}
	
  /** Creates piece instance and add it to the internal list of pieces.
	*
	* @param team of Pieces.Color
	* @param type of Pieces.Type
	* @param x of Pieces.ROW_..
	* @param y of Pieces.COL_..
	*/
	public void createAndAddPiece( Team team, Type type, int row, int col ) {
		Piece piece = new Piece( team, type, row, col );
		this.pieces.add( piece );
	}
	
	/**
	 * Moves piece to the specified location. If the target location is occupied
	 * by an opponent piece, that piece is marked as 'captured'.
	 * @param piece The piece to move. Can be set null.
	 * @param sourceRow	The source row if piece was set null.
	 * @param sourceCol The source column if piece was set null.
	 * @param row The target row (Piece.ROW_..)
	 * @param col The target column (Piece.COL_..)
	 */
	public boolean movePiece( Move move ){
		int sourceRow = move.sourceRow;
		int sourceCol = move.sourceCol;
		int targetRow = move.targetRow;
		int targetCol = move.targetCol;
		
		if( ! this.moveValidator.isMoveValid( move ) ){
			System.out.println("invalid move");
			return false;
		}
		
		Piece piece = getNonCapturedPieceAtLocation( sourceRow, sourceCol );
			
		// Checks if the move is capturing an opponent piece.
		Team opponentTeam = (piece.getTeam() == Team.BLACK ? Team.WHITE : Team.BLACK );
		if( isNonCapturedPieceAtLocation(opponentTeam, targetRow, targetCol) ){
			Piece opponentPiece = getNonCapturedPieceAtLocation( targetRow, targetCol );
			opponentPiece.setCaptured( true );
			System.out.println( piece + " captured " + opponentPiece );
		}
		
		piece.setRow( targetRow );
		piece.setCol( targetCol );
		
		if( isGameEndConditionReached() ){
			this.gameState = GameState.END;
			System.out.println( piece.getTeam() + " WINS!" );
		} else {
			this.changeGameState();
			this.lastGameState = this.gameState;
		}
		
		return true;
	}
	
	/**
	 * Checks if the game end condition is met: one color has a captured king.
	 * @return True if the game end condition is met.
	 */
	private boolean isGameEndConditionReached() {
		for( Piece piece : this.pieces ){
			if( piece.getType() == Type.KING && piece.isCaptured() ){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the first piece at the specified location that is not marked as
	 * 'captured'.
	 * @param row of Piece.ROW_..
	 * @param col of Piece.COL_..
	 * @return the first not captured piece at the specified location
	 */
	public Piece getNonCapturedPieceAtLocation( int row, int col ){
		for( Piece piece : this.pieces ){
			if( piece.getRow() == row 
					&& piece.getCol() == col
					&& piece.isCaptured() == false ){
				return piece;
			}
		}
		return null;
	}
	
	/**
	 * Checks whether there is a piece at the specified location that is not
	 * marked as 'captured' and has the specified color.
	 * @param team of Piece.Color
	 * @param row of Piece.ROW_..
	 * @param col of Piece.COL_..
	 * @return True, if the location contains a non captured piece of the
	 * 		   specified color.
	 */
	public boolean isNonCapturedPieceAtLocation( Team team, int row, int col ){
		for( Piece piece : this.pieces ){
			if( piece.getRow() == row
					&& piece.getCol() == col
					&& piece.isCaptured() == false
					&& piece.getTeam() == team ){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether there is a non captured piece at the specified location.
	 * @param team of Piece.Color
	 * @param row of Piece.ROW_..
	 * @param col of Piece.COL_..
	 * @return True, if the location contains a non captured piece.
	 */
	public boolean isNonCapturedPieceAtLocation( int row, int col ){
		for( Piece piece : this.pieces ){
			if( piece.getRow() == row
					&& piece.getCol() == col
					&& piece.isCaptured() == false ){
				return true;
			}
		}
		return false;
	}
	
	// ::: GETTERS & SETTERS :::
	
	public GameState getGameState() {
		return this.gameState;
	}
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	public GameState getLastGameState() {
		return lastGameState;
	}
	public List<Piece> getPieces() {
		return this.pieces;
	}
	public MoveValidator getMoveValidator() {
		return moveValidator;
	}

	/**
	 * Switches between the different game states.
	 */
	public void changeGameState() {
		GameState lastGameState = this.gameState;
		
		// Checks if game end condition has been reached.
		if( this.isGameEndConditionReached() ){
			if( this.gameState == GameState.BLACK ){
				System.out.println("Game over! Black won!");
			} else if( this.gameState == GameState.WHITE ){
				System.out.println("Game over! White won!");
			} else {
				throw new IllegalStateException( "Internal error! GameState shouldn't be END yet.");
			}
			this.gameState = GameState.END;
			System.out.println( lastGameState + " WINS!" );
		}
		
		switch( this.gameState ){
		case BLACK:
			this.gameState = GameState.WHITE;
			break;
		case WHITE:
			this.gameState = GameState.BLACK;
			break;
		case END:
			break;
		default:
			throw new IllegalStateException( "Unknown game state: " + this.gameState );
		}
	}
	
}
