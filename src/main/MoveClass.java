package main;

import pieces.Piece;

public class MoveClass{
    public int oldCol;
    public int oldRow;

    public int newCol;
    public int newRow;

    public Piece piece;
    public Piece capture;

    public boolean doubleMove;
    
    public boolean isShortCastle = false;
    public boolean isLongCastle = false;

    public boolean isPromotion = false;

    public MoveClass(Board board, Piece piece, int newCol, int newRow){
        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = piece;
        this.capture = board.getPiece(newCol, newRow);

        doubleMove = false;
    }
}