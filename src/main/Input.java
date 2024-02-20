package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import pieces.Piece;


public class Input extends MouseAdapter{

    Board board;

    public Input(Board board){
        this.board = board;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null){
            board.selectedPiece.xPos = e.getX() - board.tileSize / 2;
            board.selectedPiece.yPos = e.getY() - board.tileSize / 2;

            board.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Find the coordinates of the square from the mouse location
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        Piece pieceXY = board.getPiece(col, row);
        
        if (pieceXY != null){
            board.selectedPiece = pieceXY;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Only if a piece is selected, run the code to implement the move
        if (board.selectedPiece == null){
            return;
        }

        //This code bounds the pieces from moving off the edge of the board
        //Where the selected piece currently is
        int col = board.selectedPiece.col;
        int row = board.selectedPiece.row;
        //Where the new coords should be
        int newCol = e.getX() / board.tileSize;
        int newRow = e.getY() / board.tileSize;

        //Only allow the new move if it is within the bounds of the board
        if (newCol >= 0 && newCol <= 7 && newRow >= 0 && newRow <= 7){
            col = newCol;
            row = newRow;
        }

        if (board.selectedPiece != null){
            MoveClass move = new MoveClass(board, board.selectedPiece, col, row);
            ArrayList<MoveClass> possibleMoves = board.generateLegalMoves(board.isWhiteTurn);

            boolean isLegalMove = false;

            //Go through all possible moves and if we find a legal move in possible moves we return true
            //Could use a break statement 
            //Looks weird but does work
            for (MoveClass possibleMove : possibleMoves){
                if (!isLegalMove){
                    isLegalMove = (move.newCol == possibleMove.newCol && move.newRow == possibleMove.newRow && possibleMove.piece == move.piece);
                }
            }

            //If the move is in the set of possible moves, allow the user to make the move and change turn
            if (isLegalMove){
                board.makeMove(move);
            } else{
                //Move the piece back to where it was
                board.selectedPiece.xPos = board.selectedPiece.col * board.tileSize;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tileSize;                
            }
        }
        
        board.endTurn();
        board.selectedPiece = null;
        board.repaint();
    }
}