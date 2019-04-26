package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logic.ChessGame;
import logic.ChessGame.GameState;
import logic.Move;
import logic.MoveValidator;
import logic.Piece;
import logic.Piece.Team;
import logic.Piece.Type;

public final class ChessGui extends JPanel {
	
	private static final long serialVersionUID = 3114147670071466558L;
	
	private static final int BOARD_START_X = 301;
	private static final int BOARD_START_Y = 51;
	
	private static final int SQUARE_WIDTH = 50;
	private static final int SQUARE_HEIGHT = 50;
	
	private static final int PIECE_WIDTH = 48;
	private static final int PIECE_HEIGHT = 48;
	
	private static final int PIECES_START_X = BOARD_START_X + (int)(SQUARE_WIDTH/2.0 - PIECE_WIDTH/2.0);
	private static final int PIECES_START_Y = BOARD_START_Y + (int)(SQUARE_HEIGHT/2.0 - PIECE_HEIGHT/2.0);
	
	private static final int DRAG_TARGET_SQUARE_START_X = BOARD_START_X - (int)(PIECE_WIDTH/2.0);
	private static final int DRAG_TARGET_SQUARE_START_Y = BOARD_START_Y - (int)(PIECE_HEIGHT/2.0);	

	JFrame frame;
	private Image imgBackground; // background image	
	private JLabel lblGameState; // label displaying the current game state
	
	// All GUI game pieces of the running game.
	// The GUI game pieces wrap the actual game pieces of the chess game.
	private List<GuiPiece> guiPieces = new ArrayList<GuiPiece>();
	
	private GuiPiece dragPiece; // currently dragged game piece
	
	private ChessGame chessGame;
	
	private Move lastMove; // the last executed move (used for highlighting)

	public ChessGui() {
		this.setLayout( null );
		
		// Loads and sets background image
		URL urlBackgroundImg = getClass().getResource("/img/board.png");
		this.imgBackground = new ImageIcon( urlBackgroundImg ).getImage();
		
		// Creates chess game.
		this.chessGame = new ChessGame();
		
		// Wraps game pieces into their graphical representation
		for( Piece piece : this.chessGame.getPieces() ){
			createAndAddGuiPiece( piece );
		}
		
        // Adds mouse listeners to enable drag and drop
        PiecesDragAndDropListener listener = new PiecesDragAndDropListener( this );
        this.addMouseListener( listener );
        this.addMouseMotionListener( listener );
        
        // Label to display game sate.
        String labelText = chessGame.getGameState().toString() + "'S TURN";
        this.lblGameState = new JLabel( labelText, SwingConstants.CENTER );
        lblGameState.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblGameState.setBounds( 55, 205, 200, 60 );
        lblGameState.setForeground( Color.WHITE );
        this.add( lblGameState );
        
        // Creates application frame and sets visible
        frame = new JFrame();
        frame.setSize( 80, 80 );
        frame.setVisible( true );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.add( this );
        frame.setSize( imgBackground.getWidth(null), imgBackground.getHeight(null) );
        
        // Button to change game state.
        /*JButton btnChangeGameState = new JButton( "change" );
        btnChangeGameState.addActionListener( new ChangeGameStateButtonActionListener(this) );
        btnChangeGameState.setBounds( 0, 0, 80, 30 );
        this.add( btnChangeGameState );*/
        

	}
	
	/**
	 * Creates a GUI piece.
	 * @param piece
	 */
	private void createAndAddGuiPiece( Piece piece ){
		Image image = this.getImageForPiece( piece.getTeam(), piece.getType() );
		GuiPiece guiPiece = new GuiPiece( image, piece );
		this.guiPieces.add( guiPiece );
	}
	
	/**
	 * Switches between the different game states.
	 */
	public void changeGameState() {
		this.chessGame.changeGameState();
		this.lblGameState.setText( this.getGameStateAsText() );
	}
	
  /** Loads image for given color and type. This method translates the color and
   * type information into a filename and loads that particular file.
   * 
   * @param color color constant
   * @param type type constant
   * @return image
   */
	private Image getImageForPiece( Team color, Type type ){
		String filename = "";
		filename += (color == Team.WHITE ? "w" : "b" );
		switch( type ){
			case BISHOP:
				filename += "b";
				break;
			case KING:
				filename += "k";
				break;
			case KNIGHT:
				filename += "n";
				break;
			case PAWN:
				filename += "p";
				break;
			case QUEEN:
				filename += "q";
				break;
			case ROOK:
				filename += "r";
				break;
		}
		filename += ".png";
		
		URL urlPieceImg = getClass().getResource( "/img/" + filename );
		return new ImageIcon( urlPieceImg ).getImage();			
	}
	
	@Override
	protected void paintComponent( Graphics graphics ){
		// Draw background.
		graphics.drawImage( this.imgBackground, 0, 0, null );
		
		// Draw pieces.
		for( GuiPiece guiPiece : this.guiPieces ){
			if( ! guiPiece.getPiece().isCaptured() ){
				graphics.drawImage( guiPiece.getImage(), guiPiece.getX(), guiPiece.getY(), null );
			}
		}
		
		// Draw last move, if user is not dragging game piece.
		if( ! isUserDraggingPiece() && lastMove != null ){
			int highlightSourceX = convertColToX( lastMove.sourceCol );
			int highlightSourceY = convertRowToY( lastMove.sourceRow );
			int highlightTargetX = convertColToX( lastMove.targetCol );
			int highlightTargetY = convertRowToY( lastMove.targetRow );
			
			graphics.setColor( Color.YELLOW );
			graphics.drawRoundRect( highlightSourceX+4, highlightSourceY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8, 10, 10 );
			graphics.drawRoundRect( highlightTargetX+4, highlightTargetY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8, 10, 10 );
		}
		
		// Draw valid target locations, if user is dragging a game piece.
		if( isUserDraggingPiece() ){
			MoveValidator moveValidator = this.chessGame.getMoveValidator();
			
			// Iterate the complete board to check if target locations are valid.
			for( int col = Piece.COL_A; col <= Piece.COL_H; col++ ){
				for( int row = Piece.ROW_1; row <= Piece.ROW_8; row++ ){
					int sourceRow = dragPiece.getPiece().getRow();
					int sourceCol = dragPiece.getPiece().getCol();
					
					Move move = new Move( sourceRow, sourceCol, row, col );
					
					// Checks if target location is valid.
					if( moveValidator.isMoveValid(move) ){
						int highlightX = convertColToX( col );
						int highlightY = convertRowToY( row );
						
						// Draw a black drop shadow by drawing a black rectangle with an offset of 1 pixel.
						graphics.setColor( Color.BLACK );
						graphics.drawRoundRect( highlightX+5, highlightY+5, SQUARE_WIDTH-8, SQUARE_HEIGHT-8, 10, 10);
						// Draw the highlight
						graphics.setColor( Color.GREEN );
						graphics.drawRoundRect( highlightX+4, highlightY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8, 10, 10 );
					}
				}
			}
		}
		
		// Draw game state label.
		if( chessGame.getGameState() != GameState.END ){
			this.lblGameState.setText( chessGame.getGameState().toString() + "'S TURN" );
		} else if( chessGame.getGameState() == GameState.END ){
			this.lblGameState.setText( chessGame.getLastGameState() + "'S VICTORY!" );
		}
	}

	public List<GuiPiece> getGuiPieces() {
		return this.guiPieces;
	}

	/**
	 * @return textual description of current game state
	 */
	private String getGameStateAsText(){
		return( this.chessGame.getGameState() == ChessGame.GameState.BLACK
			? "black" : "white" );
	}
	
	/**
	 * Converts logical column into x coordinate
	 * @param col
	 * @return x coordinate for column
	 */
	public static int convertColToX( int col ){
		return PIECES_START_X + SQUARE_WIDTH * col;
	}
	
	/**
	 * Converts logical row into x coordinate
	 * @param row
	 * @return y coordinate for row
	 */
	public static int convertRowToY( int row ){
		return PIECES_START_Y + SQUARE_HEIGHT * (Piece.ROW_8 - row);
	}
	
	/**
	 * Converts x coordinate into logical column
	 * @param x
	 * @return logical column for x coordinate
	 */
	public static int convertXToCol( int x ){
		return (x - DRAG_TARGET_SQUARE_START_X)/SQUARE_WIDTH;
	}

	/**
	 * Converts y coordinate into logical row
	 * @param y
	 * @return logical row for y coordinate
	 */
	public static int convertYToRow( int y ){
		return Piece.ROW_8 - (y - DRAG_TARGET_SQUARE_START_Y)/SQUARE_HEIGHT;
	}
	
	/**
	 * Changes location of given piece, if the location is valid.
	 * If the location is not valid, move the piece back to its original
	 * position.
	 * @param dragPiece
	 * @param x
	 * @param y
	 */
	public void setNewPieceLocation( GuiPiece dragPiece, int x, int y ){
		int sourceRow = dragPiece.getPiece().getRow();
		int sourceCol = dragPiece.getPiece().getCol();
		int targetRow = ChessGui.convertYToRow( y );
		int targetCol = ChessGui.convertXToCol( x );
		
		Move move = null;
		boolean wasMoveSuccessful = false;
		
		if( targetRow < Piece.ROW_1
				|| targetRow > Piece.ROW_8
				|| targetCol < Piece.COL_A
				|| targetCol > Piece.COL_H ){
			// Resets piece position if move is not valid.
			dragPiece.resetToUnderlyingPiecePosition();
		}else{
			// Changes model and updates GUI piece afterwards.
			System.out.println( "Moving piece to " + targetRow + "/" + targetCol );
			move = new Move( sourceRow, sourceCol, targetRow, targetCol );
			wasMoveSuccessful = chessGame.movePiece( move );
			
			// If the last move was successfully executed, remembers it for
			// highlighting it in the UI.
			if( wasMoveSuccessful == true ){
				this.lastMove = move;
			}
			
			dragPiece.resetToUnderlyingPiecePosition();
		}
	}
	
	private boolean isUserDraggingPiece() {
		return this.dragPiece != null;
	}
	
	//
	// ::: GETTERS & SETTERS :::
	//
	public GuiPiece getDragPiece() {
		return dragPiece;
	}
	public void setDragPiece(GuiPiece dragPiece) {
		this.dragPiece = dragPiece;
	}	
	public ChessGame getChessGame() {
		return chessGame;
	}

	//
	// ::: MAIN :::
	//
	public static void main(String[] args) {
		new ChessGui();
	}

}
