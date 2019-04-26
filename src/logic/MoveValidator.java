package logic;

public class MoveValidator {
	
	ChessGame chessGame;
	Piece sourcePiece;
	Piece targetPiece;
	
	int sourceRow;
	int sourceCol;
	int targetRow;
	int targetCol;
	
	public MoveValidator( ChessGame chessGame ){
		this.chessGame = chessGame;
	}
	
	public boolean isMoveValid( Move move ){
		sourceRow = move.sourceRow;
		sourceCol = move.sourceCol;
		targetRow = move.targetRow;
		targetCol = move.targetCol;
		
		sourcePiece = ChessGame.getNonCapturedPieceAtLocation( 
			chessGame.getPieces(), sourceRow, sourceCol
		);
		targetPiece = ChessGame.getNonCapturedPieceAtLocation(
			chessGame.getPieces(), targetRow, targetCol
		);
		
		// If source piece does not exist, returns null.
		if( this.sourcePiece == null ){
			System.out.println("no source piece");
			return false;
		}
		
		// Source piece has right color?
		if( ! chessGame.getGameState().equals( sourcePiece.getTeam() ) ){
			return false;
		}
		
		// Checks if target location is within boundaries.
		if( targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8
				|| targetCol < Piece.COL_A || targetCol > Piece.COL_H ){
			return false;
		}
		
		// Validates piece movement rules.
		boolean validPieceMove = false;
		switch( sourcePiece.getType() ){
		case BISHOP:
			validPieceMove = isValidBishopMove();
			break;
		case KING:
			validPieceMove = isValidKingMove();
			break;
		case KNIGHT:
			validPieceMove = isValidKnightMove();
			break;
		case PAWN:
			validPieceMove = isValidPawnMove();
			break;
		case QUEEN:
			validPieceMove = isValidQueenMove();
			break;
		case ROOK:
			validPieceMove = isValidRookMove();
			break;
		default:
			break;
		}
		if( ! validPieceMove ){
			return false;
		}
		
		// TODO: Handle stalemate and checkmate.
		
		return true;
	}
	
	private boolean isTargetLocationCaptureable() {
		if( targetPiece == null ){
			return false;
		} else if( targetPiece.getTeam() != sourcePiece.getTeam() ){
			System.out.println("target location captureable");
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isTargetLocationFree() {
		return targetPiece == null;
	}
	
	private boolean isTargetLocationFreeOrCaptureable() {
		if( isTargetLocationFree() || isTargetLocationCaptureable() ){
			return true;
		} else {
			System.out.println("target location not free and not captureable");
			return false;
		}
	}
	
	private boolean isValidKnightMove() {
		// The knight moves to any of the closest squares which are not on the same rank,
		// file or diagonal, thus the move forms an "L"-shape two squares long and one
		// square wide. The knight is the only piece which can leap over other pieces.
		
		if( ! isTargetLocationFreeOrCaptureable() ){
			return false;
		}
		
		if( sourceRow+2 == targetRow && sourceCol+1 == targetCol ){
			// move up up right
			return true;
		} else if( sourceRow+1 == targetRow && sourceCol+2 == targetCol ){
			// move up right right
			return true;
		} else if( sourceRow-1 == targetRow && sourceCol+2 == targetCol ){
			// move down right right
			return true;
		} else if( sourceRow-2 == targetRow && sourceCol+1 == targetCol ){
			// move down down right
			return true;
		} else if( sourceRow-2 == targetRow && sourceCol-1 == targetCol ){
			// move down down left
			return true;
		} else if( sourceRow-1 == targetRow && sourceCol-2 == targetCol ){
			// move down left left
			return true;
		} else if( sourceRow+1 == targetRow && sourceCol-2 == targetCol ){
			// move up left left
			return true;
		} else if( sourceRow+2 == targetRow && sourceCol-1 == targetCol ){
			// move up up left
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isValidKingMove() {
		// The king moves one square in any direction, the king has also a
		// special move which is called castling and also involves a rook.
		
		if( ! isTargetLocationFreeOrCaptureable() ){
			return false;
		}
		
		boolean result = true;
		
		if( sourceRow+1 == targetRow && sourceCol == targetCol ){
			// up
			result = true;
		} else if( sourceRow+1 == targetRow && sourceCol+1 == targetCol ){
			// up right
			result = true;
		} else if( sourceRow == targetRow && sourceCol+1 == targetCol ){
			// right
			result = true;
		} else if( sourceRow-1 == targetRow && sourceCol+1 == targetCol ){
			// down right
			result = true;
		} else if( sourceRow-1 == targetRow && sourceCol == targetCol ){
			// down
			result = true;
		} else if( sourceRow-1 == targetRow && sourceCol-1 == targetCol ){
			// down left
			result = true;
		} else if( sourceRow == targetRow && sourceCol-1 == targetCol ){
			// left
			result = true;
		} else if( sourceRow+1 == targetRow && sourceCol-1 == targetCol ){
			// up left
			result = true;
		} else {
			result = false;
		}
		
		// TODO: Castling
		
		return result;
	}
	
	private boolean isValidRookMove() {
		// The rook can move any number of squares along any rank or file, but
		// may not leap over other pieces. Along with the king, the rook is also
		// involved during the king's castling move.
		
		if( ! isTargetLocationFreeOrCaptureable() ){
			return false;
		}
		
		boolean result = false;

		int rowDiff = targetRow - sourceRow;
		int colDiff = targetCol - sourceCol;
		
		if( rowDiff > 0 && colDiff == 0 ){
			// up
			result = ! arePiecesBetween( +1, 0 );
		} else if( rowDiff == 0 && colDiff > 0 ){
			// right
			result = ! arePiecesBetween( 0, +1 );
		} else if( rowDiff < 0 && colDiff == 0 ){
			// down
			result = ! arePiecesBetween( -1, 0 );
		} else if( rowDiff == 0 && colDiff < 0 ){
			// left
			result = ! arePiecesBetween( 0, -1 );
		} else {
			// not moving straight
			System.out.println("can't move diagonally");
			result = false;
		}
		
		return result;
	}
	
	private boolean isValidBishopMove() {
		// The bishop can move any number of squares diagonally, but may not
		// leap over other pieces.
		
		if( ! isTargetLocationFreeOrCaptureable() ){
			return false;
		}
		
		boolean result = false;
		
		int rowDiff = targetRow - sourceRow;
		int colDiff = targetCol - sourceCol;
		
		if( rowDiff == colDiff && colDiff > 0 ){
			// up right
			result = ! arePiecesBetween( +1, +1 );
		} else if( rowDiff == -colDiff && colDiff > 0 ){
			// down right
			result = ! arePiecesBetween( -1, +1 );
		} else if( rowDiff == colDiff && colDiff < 0 ){
			// down right
			result = ! arePiecesBetween( -1, -1 );
		} else if( rowDiff == -colDiff && colDiff < 0 ){
			// down right
			result = ! arePiecesBetween( +1, -1 );
		} else {
			// not moving diagonally
			System.out.println("can't move straight");
			result = false;
		}
		
		return result;
	}
	
	private boolean isValidQueenMove() {
		// The queen combines the power of the rook and bishop and can move any 
		// number of squares along rank, file, or diagonal, but it may not leap
		// over other pieces.
		return isValidBishopMove() || isValidRookMove();
	}
	
	private boolean isValidPawnMove() {
		// The pawn may move forward to the unoccupied square immediately in front
		// of it on the same file, or on its first move it may advance two squares
		// along the same file provided both squares are unoccupied. The pawn
		// has two special moves: the en passant capture and the pawn promotion.
		boolean result = false;
		
		if( isTargetLocationFree() ){
			
			if( sourceCol == targetCol ){
				// same column
				if( sourcePiece.getTeam() == Piece.Team.WHITE ){
					// white
					if( sourceRow+1 == targetRow ){
						// move one up
						result = true;
					} else if( sourceRow+2 == targetRow && sourcePiece.getRow() == Piece.ROW_2 ){
						// move two up
						result = true;
					} else {
						System.out.println("can't move backwards");
						result = false;
					}
				} else {
					// black
					if( sourceRow-1 == targetRow ){
						// move one down
						result = true;
					} else if( sourceRow-2 == targetRow && sourcePiece.getRow() == Piece.ROW_7 ){
						// move two down
						result = true;
					} else {
						System.out.println("can't move backwards");
						result = false;
					}
				}
			} else {
				// not the same column
				System.out.println("can't move to different column");
				result = false;
			}
			
		// Or it may move to a square occupied by an opponent's piece, which is
		// diagonally in front of it on an adjacent file, capturing that piece.
		} else if( isTargetLocationCaptureable() ){
			
			if( sourceCol+1 == targetCol || sourceCol-1 == targetCol ){
				// one column to the left or right
				if( sourcePiece.getTeam() == Piece.Team.WHITE ){
					// white
					if( sourceRow+1 == targetRow ){
						// capture up right or up left
						result = true;
					} else {
						System.out.println("can't move backwards");
						result = false;
					}
				} else {
					// black
					if( sourceRow-1 == targetRow ){
						// capture down right or down left
						result = true;
					} else {
						System.out.println("can't move backwards");
						result = false;
					}
				}
			} else {
				// not one column to the left or right
				System.out.println("can't move more than one column to the left or right");
				result = false;
			}
		}
		
		// TODO: On its first move it may advance two squres.
		
		// TODO: En passant capture.
		
		return result;
	}
	
	private boolean arePiecesBetween( int rowIncrementPerStep, int colIncrementPerStep ){
		int currentRow = sourceRow + rowIncrementPerStep;
		int currentCol = sourceCol + colIncrementPerStep;
		
		while( true ){
			if( currentRow == targetRow && currentCol == targetCol ){
				break;
			}
			if( currentRow < Piece.ROW_1 || currentRow > Piece.ROW_8
					|| currentCol < Piece.COL_A || currentCol > Piece.COL_H ){
				break;
			}
			if( chessGame.isNonCapturedPieceAtLocation(currentRow, currentCol) ){
				System.out.println("pieces between source and target");
				return true;
			}
			currentRow += rowIncrementPerStep;
			currentCol += colIncrementPerStep;
		}
		
		return false;
	}
	
}
