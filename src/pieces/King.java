package pieces;

import main.Board;
import main.MoveClass;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class King extends Piece{

    private int kingRowCastle;
    private int kingColCastle;

    public King(Board board, int col, int row, boolean isWhite){
        super(board);
        this.name = "King";
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

        try {
            if (this.isWhite){
                this.sprite = ImageIO.read(new FileInputStream("res/WhiteKing.png")).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
            } else{
                this.sprite = ImageIO.read(new FileInputStream("res/BlackKing.png")).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (rook == null || rook.isWhite != this.isWhite){
            return false;
        }
        if ((kingRowCastle == move.oldRow && kingColCastle == move.oldCol) && (kingColCastle + 2 == move.newCol && kingRowCastle == move.newRow) && (board.getPiece(kingColCastle + 1, kingRowCastle) == null) && (board.getPiece(kingColCastle + 2, kingRowCastle) == null) && (rook.name == "Rook")){
            move.isShortCastle = true;
            return true;
        }
        return false;
    }

    private boolean canLongCastle(MoveClass move){
        Piece rook = board.getPiece(kingColCastle - 4, kingRowCastle);
        if (rook == null || rook.isWhite != this.isWhite){
            return false;
        }
        if ((kingRowCastle == move.oldRow && kingColCastle == move.oldCol) && (kingColCastle - 2 == move.newCol && kingRowCastle == move.newRow) && (board.getPiece(kingColCastle - 1, kingRowCastle) == null) && (board.getPiece(kingColCastle - 2, kingRowCastle) == null) && (board.getPiece(kingColCastle - 3, kingRowCastle) == null) && (rook.name == "Rook")){
            move.isLongCastle = true;
            return true;
        }
        return false;
    }
}