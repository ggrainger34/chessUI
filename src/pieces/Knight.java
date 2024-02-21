package pieces;

import main.Board;
import main.MoveClass;

import java.awt.image.BufferedImage;


public class Knight extends Piece{
    public Knight(Board board, int col, int row, boolean isWhite){
        super(board);
        this.name = "Knight";
        this.col = col;
        this.row = row;
        this.xPos = board.tileSize * col;
        this.yPos = board.tileSize * row;
        this.isWhite = isWhite;
        
        this.sprite = sheet.getSubimage(3 * sheetScale, isWhite ? 1 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(Board board, MoveClass move){
        if ((Math.abs(move.newCol - move.oldCol) == 1) && (Math.abs(move.newRow - move.oldRow) == 2) || (Math.abs(move.newCol - move.oldCol) == 2) && (Math.abs(move.newRow - move.oldRow) == 1)){
            return true;
        }
        else{
            return false;
        }
    }
}