package pieces;

import main.Board;
import main.MoveClass;

import java.awt.image.BufferedImage;


public class King extends Piece{

    private int kingRowCastle;
    private int kingColCastle;

    public King(Board board, int col, int row, boolean isWhite){
        super(board);
        this.letter = isWhite ? 'K' : 'k';
        this.col = col;
        this.row = row;
        this.xPos = board.tileSize * col;
        this.yPos = board.tileSize * row;
        this.isWhite = isWhite;
        this.pieceMoved = false;

        if (this.isWhite){
            kingRowCastle = 7;
            kingColCastle = 4;
        }
        else{
            kingRowCastle = 0;
            kingColCastle = 4;
        }

        this.sprite = sheet.getSubimage(0 * sheetScale, isWhite ? 1 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(Board board, MoveClass move){
        if ((move.oldRow - 1 <= move.newRow && move.newRow <= move.oldRow + 1) && (move.oldCol - 1 <= move.newCol && move.newCol <= move.oldCol + 1) || (canShortCastle(move)) || (canLongCastle(move))){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean canShortCastle(MoveClass move){
        Piece rook = board.getPiece(kingColCastle + 3, kingRowCastle);
        if (rook == null || rook.isWhite != this.isWhite || (rook.letter != 'R' && rook.letter != 'r')){
            return false;
        }
        if ((kingRowCastle == move.oldRow && kingColCastle == move.oldCol) && (kingColCastle + 2 == move.newCol && kingRowCastle == move.newRow) && (board.getPiece(kingColCastle + 1, kingRowCastle) == null) && (board.getPiece(kingColCastle + 2, kingRowCastle) == null)){
            move.isShortCastle = true;
            return true;
        }
        return false;
    }

    private boolean canLongCastle(MoveClass move){
        Piece rook = board.getPiece(kingColCastle - 4, kingRowCastle);
        if (rook == null || rook.isWhite != this.isWhite || (rook.letter != 'R' && rook.letter != 'r')){
            return false;
        }
        if ((kingRowCastle == move.oldRow && kingColCastle == move.oldCol) && (kingColCastle - 2 == move.newCol && kingRowCastle == move.newRow) && (board.getPiece(kingColCastle - 1, kingRowCastle) == null) && (board.getPiece(kingColCastle - 2, kingRowCastle) == null) && (board.getPiece(kingColCastle - 3, kingRowCastle) == null)){
            move.isLongCastle = true;
            return true;
        }
        return false;
    }
}