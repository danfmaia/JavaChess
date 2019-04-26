package guiconsole;

import handler.IPlayerHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import logic.ChessGame;
import logic.Move;
import logic.Piece;
import logic.ChessGame.GameState;
import logic.Piece.Team;

public class ChessConsole implements IPlayerHandler {
	
	private ChessGame chessGame;
	
	public ChessConsole( ChessGame chessGame ) {
		// Create a new chess game.
		this.chessGame = chessGame;
		
		ChessConsole.printCurrentGameState( chessGame );
	}
	
	@Override
	public Move getMove() {
		System.out.println( "your move (format: e2-e3): " );
		
		Move move = null;
		
		while( move == null ){
			// Instantiate a new input reader.
			BufferedReader inputReader = new BufferedReader( new InputStreamReader(System.in) );
			String input;
			try {
				// Read user input.
				input = inputReader.readLine();
				
				// Exit, if user types 'exit'
				if( input.equalsIgnoreCase( "exit" ) ){
					System.exit( 0 );
				} else {
					move = this.convertStringToMove( input );
				}
			} catch( IOException e ){
				System.out.println( e.getClass() + ": " + e.getMessage() );
				e.printStackTrace();
				continue;
			}

		}
		
		return move;
	}
	
	@Override
	public void moveSuccessfullyExecuted( Move move ){
		ChessConsole.printCurrentGameState( chessGame );
		
		if( chessGame.getGameState() == GameState.END ){
			if( chessGame.getLastGameState() == GameState.WHITE ){
				System.out.println( "End of game! White won!" );
			} else if( chessGame.getLastGameState() == GameState.BLACK ){
				System.out.println( "End of game! Black won!" );
			}
		}
	}
	
	/**
	 * Contains the main loop of the application. The application will print the
	 * current game state and ask the user for his move. If the user enters
	 * "exit", the application ends. Otherwise the user input is interpreted as
	 * a move and the application tries to execute that move.
	 */

	
	
	/**
	 * Moves piece to the specified location.
	 * @param input A valid move string (e.g. "e7-e5" ).
	 */
	private Move convertStringToMove( String input ){
		if( input == null || input.length() != 5 ){
			return null;
		}
		
		String strSourceCol = input.substring( 0, 1 );
		String strSourceRow = input.substring( 1, 2 );
		String strTargetCol = input.substring( 3, 4 );
		String strTargetRow = input.substring( 4, 5 );
		
		Move move = new Move(
			convertRowStrToRowInt( strSourceRow ),
			convertColStrToColInt( strSourceCol ),
			convertRowStrToRowInt( strTargetRow ),
			convertColStrToColInt( strTargetCol )
		 );
		
		return move;
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
	private static void printCurrentGameState( ChessGame chessGame ) {
		System.out.println("  a  b  c  d  e  f  g  h  ");
		for( int row = Piece.ROW_8; row >= Piece.ROW_1; row-- ){
			System.out.println(" +--+--+--+--+--+--+--+--+");
			String strRow = (row + 1) + "|";
			for( int col = Piece.COL_A; col <= Piece.COL_H; col++ ){
				Piece piece = ChessGame.getNonCapturedPieceAtLocation( chessGame.getPieces(), row, col );
				String pieceStr = ChessConsole.getNameOfPiece( piece );
				strRow += pieceStr + "|";
			}
			System.out.println( strRow + (row + 1) );
		}
		System.out.println(" +--+--+--+--+--+--+--+--+");
		System.out.println("  a  b  c  d  e  f  g  h  ");
		
		System.out.print("turn: ");
		System.out.println(
			chessGame.getGameState().equals( Team.BLACK )
				? "black" : "white"
		);
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
	private static String getNameOfPiece( Piece piece ){
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
		ChessGame chessGame = new ChessGame();
		ChessConsole console = new ChessConsole( chessGame );
		chessGame.setPlayer( Team.WHITE, console );
		chessGame.setPlayer( Team.BLACK, console );
		new Thread( chessGame ).start();
	}
	
}
