package pieces;

import main.Board;
import main.MoveClass;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bishop extends Piece{
    public Bishop(Board board, int col, int row, boolean isWhite){
        super(board);
        this.name = "Bishop";
        this.col = col;
        this.row = row;
        this.xPos = board.tileSize * col;
        this.yPos = board.tileSize * row;
        this.isWhite = isWhite;
        try {
            if (this.isWhite){
                this.sprite = ImageIO.read(new FileInputStream("res/WhiteBishop.png")).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
            } else{
                this.sprite = ImageIO.read(new FileInputStream("res/BlackBishop.png")).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Function takes in board and move and checks if its valid
    public boolean isValidMovement(Board board, MoveClass move){
        //Do not allow movement to the same square
        if (Math.abs(move.oldRow - move.newRow) != Math.abs(move.oldCol - move.newCol)){
            return false;
        }
        //If on the diagonal
        else if (Math.abs(move.oldRow - move.newRow) == Math.abs(move.oldCol - move.newCol)){

            int directionRow = 0;
            int directionCol = 0;

            if (Math.abs(move.newRow - move.oldRow) != 0){
                directionRow = (move.newRow - move.oldRow) / Math.abs(move.newRow - move.oldRow);
            }
            if (Math.abs(move.newCol - move.oldCol) != 0){ 
                directionCol = (move.newCol - move.oldCol) / Math.abs(move.newCol - move.oldCol);
            }
            
            int calcRow = move.oldRow;
            int calcCol = move.oldCol;

            boolean pieceFound = false;

            while(calcRow != move.newRow && calcCol != move.newCol){
                if (pieceFound){
                    return false;
                }
                calcRow += directionRow;
                calcCol += directionCol;
                if (board.getPiece(calcCol, calcRow) != null){
                    pieceFound = true;
                }
            }

            return true;
        } 
        //If the none of the criteria is met, return false
        else{
            return false;
        }
    }
}