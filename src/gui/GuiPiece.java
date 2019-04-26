package gui;

import java.awt.Image;

import logic.Piece;

public class GuiPiece {
	
	private Piece piece;
	private Image image;
	private int x;
	private int y;
	
	public GuiPiece( Image image, Piece piece ){
		this.image = image.getScaledInstance( 40, 40, Image.SCALE_DEFAULT );
		this.piece = piece;
		
		this.resetToUnderlyingPiecePosition();
	}

	/**
	 * Moves the GUI piece back to the coordinates that correspond with the
	 * underlying piece's row and column.
	 */
	public void resetToUnderlyingPiecePosition() {
		this.x = ChessGui.convertColToX( piece.getCol() );
		this.y = ChessGui.convertRowToY( piece.getRow() );
	}

	// ::: GETTERS & SETTERS :::
	
	public Piece getPiece() {
		return piece;
	}
	public Image getImage() {
		return image;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return this.image.getWidth(null);
	}
	public int getHeight() {
		return this.image.getHeight(null);
	}
	
}
