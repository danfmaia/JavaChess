package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class PiecesDragAndDropListener implements MouseListener, MouseMotionListener {

	private List<GuiPiece> guiPieces;
	private ChessGui chessGui;
	
	private int dragOffsetX;
	private int dragOffsetY;
	
	public PiecesDragAndDropListener( ChessGui chessGui ){
		this.guiPieces = chessGui.getGuiPieces();
		this.chessGui = chessGui;
	}
	
	@Override
	public void mousePressed( MouseEvent evt ){
		int x = evt.getPoint().x;
		int y = evt.getPoint().y;
		
		// Finds out which piece to move.
		// We check the list from top to bottom.
		// (Therefore we iterate in reverse order.)
		for( int i = this.guiPieces.size()-1; i>=0; i-- ){
			GuiPiece guiPiece = this.guiPieces.get(i);
			if( guiPiece.getPiece().isCaptured() ) continue;
			
			if( mouseOverPiece( guiPiece, x, y ) ){
				if( chessGui.getChessGame().getGameState().equals( guiPiece.getPiece().getTeam() ) ){
					// Calculates offset, because we do not want the drag piece
					// to jump with its upper left corner to the current mouse
					// position.
					this.dragOffsetX = x - guiPiece.getX();
					this.dragOffsetY = y - guiPiece.getY();
					chessGui.setDragPiece( guiPiece );
					chessGui.repaint();
					break;	
				}
			}
		}
		
		// Move drag piece to the top of the list. (To optimize the for loop
		// above.)
		if( chessGui.getDragPiece() != null ){
			guiPieces.remove( chessGui.getDragPiece() );
			guiPieces.add( chessGui.getDragPiece() );
		}
	}
	
    /**
     * Checks whether the mouse is currently over this piece
     * @param guiPiece the playing piece
     * @param x x coordinate of mouse
     * @param y y coordinate of mouse
     * @return true if mouse is over the piece
     */
	private boolean mouseOverPiece( GuiPiece guiPiece, int x, int y ){
		return     guiPiece.getX() <= x
				&& guiPiece.getX() + guiPiece.getWidth() >= x
				&& guiPiece.getY() <= y
				&& guiPiece.getY() + guiPiece.getHeight() >= y;
	}
	
	@Override
	public void mouseReleased( MouseEvent evt ){
		if( chessGui.getDragPiece() != null ){
			int x = evt.getPoint().x - this.dragOffsetX;
			int y = evt.getPoint().y - this.dragOffsetY;
			
			// Set game piece to the new location if possible
			chessGui.setNewPieceLocation( chessGui.getDragPiece(), x, y );
			chessGui.repaint();
			chessGui.setDragPiece( null );
		}
	}
	
	@Override
	public void mouseDragged( MouseEvent evt ){
		if( chessGui.getDragPiece() != null ){
			int x = evt.getPoint().x - this.dragOffsetX;
			int y = evt.getPoint().y - this.dragOffsetY;
			
			System.out.println(
				"row:" + ChessGui.convertYToRow( y )
		        + " col:" + ChessGui.convertXToCol( x )
			);
			
			GuiPiece dragPiece = chessGui.getDragPiece();
			dragPiece.setX( x );
			dragPiece.setY( y );
			
			chessGui.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent evt) {}

	@Override
	public void mouseClicked(MouseEvent evt) {}

	@Override
	public void mouseEntered(MouseEvent evt) {}

	@Override
	public void mouseExited(MouseEvent evt) {}
		
}
