package pieces;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;

import java.io.FileInputStream;

import java.awt.Image;

import main.Board;
import main.MoveClass;

public class Piece {
    public int row;
    public int col;

    public int xPos;
    public int yPos;

    public boolean isWhite;

    public BufferedImage piece;

    public String name;

    public boolean pieceMoved;

    Board board;

    Image sprite;

    public Piece(Board board){
        this.board = board;
    }

    BufferedImage sheet;
    {
        try{
            sheet = ImageIO.read(new FileInputStream("res/SheetImage.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    protected int sheetScale = sheet.getWidth()/6;

    public boolean isValidMovement(Board board, MoveClass move){
        return false;
    }

    public void paint(Graphics2D g2d){
        g2d.drawImage(sprite, xPos, yPos, null);
    }
}