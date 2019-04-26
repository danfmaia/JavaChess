package logic;

public class Piece {
	
	public static enum Team {
		WHITE,
		BLACK
	}
	public static enum Type {
		ROOK,
		KNIGHT,
		BISHOP,
		QUEEN,
		KING,
		PAWN
	}
	
	public static final int ROW_1 = 0;
	public static final int ROW_2 = 1;
	public static final int ROW_3 = 2;
	public static final int ROW_4 = 3;
	public static final int ROW_5 = 4;
	public static final int ROW_6 = 5;
	public static final int ROW_7 = 6;
	public static final int ROW_8 = 7;
	
	public static final int COL_A = 0;
	public static final int COL_B = 1;
	public static final int COL_C = 2;
	public static final int COL_D = 3;
	public static final int COL_E = 4;
	public static final int COL_F = 5;
	public static final int COL_G = 6;
	public static final int COL_H = 7;

	private Team team;
	private Type type;
	private int row;
	private int col;
	private boolean captured = false;

	public Piece( Team team, Type type, int row, int col ){
		this.team = team;
		this.type = type;
		this.row = row;
		this.col = col;
	}
	
	// ::: GETTERS & SETTERS :::
	
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		if( row < 0 || row > 7 ){
			throw new IllegalArgumentException( "Row must be between 0 and 7." );
		}
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		if( row < 0 || row > 7 ){
			throw new IllegalArgumentException( "Column must be between 0 and 7." );
		}
		this.col = col;
	}
	public boolean isCaptured() {
		return captured;
	}
	public void setCaptured(boolean captured) {
		this.captured = captured;
	}
	
	@Override
	public String toString() {
		String result = "";
		
		switch( this.team ){
		case BLACK:
			result += "Black ";
			break;
		case WHITE:
			result += "White ";
			break;
		default:
			throw new IllegalStateException("unknown team");
		}
				
		switch( this.type ){
		case BISHOP:
			result += "Black";
			break;
		case KING:
			result += "King";
			break;
		case KNIGHT:
			result += "Knight";
			break;
		case PAWN:
			result += "Pawn";
			break;
		case QUEEN:
			result += "Queen";
			break;
		case ROOK:
			result += "Rook";
			break;
		default:
			throw new IllegalStateException("unknown type");
		}
		
		return result;
	}
	
}
