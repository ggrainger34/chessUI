package pieces;

import main.Board;
import main.MoveClass;

import java.awt.image.BufferedImage;


public class Pawn extends Piece{
    public Pawn(Board board, int col, int row, boolean isWhite){
        super(board);
        this.name = "Pawn";
        this.col = col;
        this.row = row;
        this.xPos = board.tileSize * col;
        this.yPos = board.tileSize * row;
        this.isWhite = isWhite;

        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 1 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    public boolean isValidMovement(Board board, MoveClass move){
        //Depending on playing black or white, the starting squares for pawns are different and so are their movement directions
        int movementDirection = this.isWhite ? 1 : -1;
        int startingPosition = this.isWhite ? 6 : 1;
        int enPassantPosition = this.isWhite ? 3 : 4;

        //If a piece is on its starting position, it can move twice
        if (move.oldRow == startingPosition){
            if ((((move.newRow + movementDirection == move.oldRow) || (move.newRow + 2 * (movementDirection) == move.oldRow)) && (move.oldCol == move.newCol) && (board.getPiece(move.newCol, move.newRow) == null)) || ((board.getPiece(move.newCol, move.newRow) != null) && (move.newRow + movementDirection == move.oldRow) && ((move.newCol + 1 == move.oldCol) || (move.newCol - 1 == move.oldCol)))){
                return true;
            }
            return false;
        }
        else{
            MoveClass previousMove = board.moveStack.peek();
            if ((move.newRow + movementDirection == move.oldRow) && ((move.oldCol - 1 == move.newCol) || (move.oldCol + 1 == move.newCol)) && (((board.getPiece(move.newCol, move.newRow + movementDirection)) != null)) && (move.oldRow == enPassantPosition) && ((previousMove.piece.name == "Pawn") && (previousMove.doubleMove) && (previousMove.newCol == move.newCol))){
                return true;
            }
            //Criteria for movement of pawns - very long but does work
            if (((move.newRow + movementDirection == move.oldRow) && (move.oldCol == move.newCol) && (board.getPiece(move.newCol, move.newRow) == null)) || ((board.getPiece(move.newCol, move.newRow) != null) && (move.newRow + movementDirection == move.oldRow) && ((move.newCol + 1 == move.oldCol) || (move.newCol - 1 == move.oldCol)))){
                return true;
            }
            return false;
        }
    }
}