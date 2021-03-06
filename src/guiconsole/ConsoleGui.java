package guiconsole;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import logic.ChessGame;
import logic.Move;
import logic.Piece;
import logic.Piece.Team;

public class ConsoleGui {
	
	private ChessGame chessGame;
	private Move move;
	private Move lastMove;
	
	public ConsoleGui() {
		// Creates a new chess game.
		this.chessGame = new ChessGame();
		this.run();
	}
	
	/**
	 * Contains the main loop of the application. The application will print the
	 * current game state and ask the user for his move. If the user enters
	 * "exit", the application ends. Otherwise the user input is interpreted as
	 * a move and the application tries to execute that move.
	 */
	public void run() {		
		// Prepares for reading input
		String input = "";
		BufferedReader inputReader = new BufferedReader( new InputStreamReader(System.in) );
		
		while( true ) {
			// Prints game state and ask for user input
			this.printCurrentGameState();
			System.out.println("your move (e.g. e2-e4): ");
			try {
				// Reads user input.
				input = inputReader.readLine();
				
				// Exits if user types 'exit'.
				if( input.equalsIgnoreCase("exit") ){
					return;
				}else{
					this.handleMove( input );
				}
			} catch( Exception e ){
				System.out.println( e.getClass() + ": " + e.getMessage() );
			}
		}
	}
	
	/**
	 * Moves piece to the specified location.
	 * @param input A valid move string (e.g. "e7-e5" ).
	 */
	private void handleMove( String input ){
		String strSourceCol = input.substring( 0, 1 );
		String strSourceRow = input.substring( 1, 2 );
		String strTargetCol = input.substring( 3, 4 );
		String strTargetRow = input.substring( 4, 5 );
		
		this.move = new Move(
			convertColStrToColInt( strSourceCol ),
			convertRowStrToRowInt( strSourceRow ),
			convertColStrToColInt( strTargetCol ),
			convertRowStrToRowInt( strTargetRow )
		 );
		
		this.chessGame.movePiece( this.move );
	}
	
	/**
	 * Converts a column string( e.g. 'a' ) into its internal representation.
	 * @param strCol A valid column string (e.g. 'a').
	 * @return Internal integer representation of the column.
	 */
	private int convertColStrToColInt( String strCol ) {
		if( strCol.equalsIgnoreCase( "a" ) ){
			return Piece.COL_A;
		} else if( strCol.equalsIgnoreCase( "b" ) ){
			return Piece.COL_B;
		} else if( strCol.equalsIgnoreCase( "c" ) ){
			return Piece.COL_C;
		} else if( strCol.equalsIgnoreCase( "d" ) ){
			return Piece.COL_D;
		} else if( strCol.equalsIgnoreCase( "e" ) ){
			return Piece.COL_E;
		} else if( strCol.equalsIgnoreCase( "f" ) ){
			return Piece.COL_F;
		} else if( strCol.equalsIgnoreCase( "g" ) ){
			return Piece.COL_G;
		} else if( strCol.equalsIgnoreCase( "h" ) ){
			return Piece.COL_H;
		} else {
			throw new IllegalArgumentException("invalid column: " + strCol );
		}
	}

	/**
	 * Converts a row string( e.g. '1' ) into its internal representation.
	 * @param strCol A valid row string (e.g. '1').
	 * @return Internal integer representation of the row.
	 */
	private int convertRowStrToRowInt( String strRow ){
		if( strRow.equalsIgnoreCase( "1" ) ){
			return Piece.ROW_1;
		} else if( strRow.equalsIgnoreCase( "2" ) ){
			return Piece.ROW_2;
		} else if( strRow.equalsIgnoreCase( "3" ) ){
			return Piece.ROW_3;
		} else if( strRow.equalsIgnoreCase( "4" ) ){
			return Piece.ROW_4;
		} else if( strRow.equalsIgnoreCase( "5" ) ){
			return Piece.ROW_5;
		} else if( strRow.equalsIgnoreCase( "6" ) ){
			return Piece.ROW_6;
		} else if( strRow.equalsIgnoreCase( "7" ) ){
			return Piece.ROW_7;
		} else if( strRow.equalsIgnoreCase( "8" ) ){
			return Piece.ROW_8;
		} else {
			throw new IllegalArgumentException("invalid row: " + strRow );
		}
	}
	
	/**
	 * Prints current game board and game state info.
	 */
	private void printCurrentGameState() {
		System.out.println("  a  b  c  d  e  f  g  h  ");
		for( int row = Piece.ROW_8; row >= Piece.ROW_1; row-- ){
			System.out.println(" +--+--+--+--+--+--+--+--+");
			String strRow = (row + 1) + "|";
			for( int col = Piece.COL_A; col <= Piece.COL_H; col++ ){
				Piece piece = this.chessGame.getNonCapturedPieceAtLocation( row, col );
				String pieceStr = getNameOfPiece( piece );
				strRow += pieceStr + "|";
			}
			System.out.println( strRow + (row + 1) );
		}
		System.out.println(" +--+--+--+--+--+--+--+--+");
		System.out.println("  a  b  c  d  e  f  g  h  ");
		
		String turnColor = (
			chessGame.getGameState().toString() == Piece.Team.BLACK.toString()
				? "black" : "white"
		);
		System.out.println("turn: " + turnColor);
	}
	
	//
	//	::: GETTERS & SETTERS :::
	//
	
	
	
	/**
	 * Returns the name of the specified piece. The name is based on color and
	 * type.
	 * 
	 * The first letter represents the color: b=black, w=white, ?=unknown
	 * 
	 * The second letter represents the type: B=bishop, K=king, N=knight,
	 * P=pawn, Q=queen, R=rook, ?=unknown
	 * 
	 * A two chars empty string is returned in case the specified piece is null.
	 * 
	 * @param piece A chess piece.
	 * @return String representation of the piece or a two letter empty string
	 * 		   if the specified piece is null.
	 */
	private String getNameOfPiece( Piece piece ){
		if( piece == null ){
			return "  ";
		}
			
		String strColor = "";
		switch( piece.getTeam() ){
		case BLACK:
			strColor = "b";
			break;
		case WHITE:
			strColor = "w";
			break;
		default:
			strColor = "?";
		}
		
		String strType = "";
		switch( piece.getType() ){
		case BISHOP:
			strType = "B";
			break;
		case KING:
			strType = "K";
			break;
		case KNIGHT:
			strType = "N";
			break;
		case PAWN:
			strType = "P";
			break;
		case QUEEN:
			strType = "Q";
			break;
		case ROOK:
			strType = "R";
			break;
		default:
			strType = "?";
		}
		
		return strColor + strType;
	}
	
	public static void main(String[] args) {
		new ConsoleGui();
	}
	
}
