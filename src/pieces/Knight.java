package pieces;

import main.Board;
import main.MoveClass;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Knight extends Piece{
    public Knight(Board board, int col, int row, boolean isWhite){
        super(board);
        this.name = "Knight";
        this.col = col;
        this.row = row;
        this.xPos = board.tileSize * col;
        this.yPos = board.tileSize * row;
        this.isWhite = isWhite;
        try {
            if (this.isWhite){
                this.sprite = ImageIO.read(new FileInputStream("res/WhiteKnight.png")).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
            } else{
                this.sprite = ImageIO.read(new FileInputStream("res/BlackKnight.png")).getScaledInstance(board.tileSize, board.tileSize, BufferedImage.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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