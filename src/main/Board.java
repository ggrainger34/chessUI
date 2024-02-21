package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Board extends JPanel{
    //Size of square
    public int tileSize = 85;

    //Number of rows and cols
    int rows = 8;
    int cols = 8;

    boolean whiteInCheck;
    boolean blackInCheck;

    //Array of all the pieces on the board
    ArrayList<Piece> pieceList = new ArrayList<>();
    //Stack of all moves played on the board.
    public Stack<MoveClass> moveStack = new Stack<>();

    //Piece that is currently being dragged
    public Piece selectedPiece;

    public Piece whiteKing;
    public Piece blackKing;

    //Instantiate the input class (this call recieves all mouse inputs from the user)
    Input input = new Input(this);

    public boolean playerColorWhite = true;
    public boolean isWhiteTurn = true;

    public ArrayList<MoveClass> legalMoves = new ArrayList<MoveClass>();

    public boolean whiteShortCastlingRights = true;
    public boolean blackShortCastlingRights = true;

    public boolean whiteLongCastlingRights = true;
    public boolean blackLongCastlingRights = true;

    int startingWhiteKingCol = 4;
    int startingBlackKingCol = 4;

    int startingWhiteKingRow = 7;
    int startingBlackKingRow = 0;

    //Instantiation
    public Board(){
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);   

        addPieces();

        this.endTurn();
    }

    //Needs work
    public void changeTurn(){
        isWhiteTurn = !isWhiteTurn;
        //Delete this line when the AI is made
        playerColorWhite = !playerColorWhite;
        
        this.repaint();
    }

    public void endTurn(){
        legalMoves = generateLegalMoves(isWhiteTurn);
        if (!moveStack.isEmpty()){
            MoveClass previousMove = moveStack.peek();

            previousMove.piece.pieceMoved = true;
        }

        if (isCheckMate(playerColorWhite)){
            System.out.println("Checkmate...\n");
            System.exit(0);
        }
        if (isStaleMate(playerColorWhite)){
            System.out.println("Stalemate...\n");
            System.exit(0);
        }


        //This is not technically correct as we compare the POSITON not the moves
        //I will improve this when zobrist hashing is implemented (this may be a while away)
        int moveStackSize = moveStack.size();
        
        if (moveStackSize >= 9){
            MoveClass prevMove0 = moveStack.get(moveStackSize - 1);
            MoveClass prevMove1 = moveStack.get(moveStackSize - 5);
            MoveClass prevMove2 = moveStack.get(moveStackSize - 9);

            if (isSameMove(prevMove0, prevMove1) && isSameMove(prevMove1, prevMove2)){
                System.out.println("Stalemate\n");
                System.exit(0);
            }
        }
    }

    //Are 2 moves the same
    public boolean isSameMove(MoveClass m1, MoveClass m2){
        if (m1.piece != m2.piece){
            return false;
        }

        if (m1.oldCol != m2.oldCol || m1.newRow != m2.newRow){
            return false;
        }

        if (m1.oldCol != m2.oldCol || m1.newCol != m2.newCol){
            return false;
        }

        return true;
    }

    public boolean isCheckMate(boolean playerColor){
        if (legalMoves.isEmpty() && isCheck(playerColor)){
            return true;
        }
        return false;
    }

    public boolean isStaleMate(boolean playerColor){
        if (legalMoves.isEmpty() && !isCheck(playerColor)){
            return true;
        }
        return false;
    }

    //Given the coordinates find the piece
    public Piece getPiece(int col, int row){
        for (Piece piece : pieceList){
            if (piece.col == col && piece.row == row){
                return piece;
            }
        }
        //If no piece in this location return null
        return null;
    }

    //Take in a move and whos turn it is and return true if valid and false if not
    //Input: move, which player's turn it is, what colour is the player -> Output: isLegal
    public boolean isLegalMove(MoveClass move, boolean isTurnWhite, boolean playerColorWhite){
        //Disallow movement if criteria is met (the boolean logic needs simplifying)
        if (!((isTurnWhite && playerColorWhite && move.piece.isWhite) || (!isTurnWhite && !playerColorWhite && !move.piece.isWhite))){
            return false;
        }
        //Cannot capture pieces of the same side
        if (isSameSide(move.piece, move.capture)){
            return false;
        }
        //Check if the movement is valid
        if (!move.piece.isValidMovement(this, move)){
            return false;
        }

        return true;
    }

    //If the pieces are on the same side return true else return false
    public boolean isSameSide(Piece p1, Piece p2){
        if (p1 == null || p2 == null){
            return false;
        }else{
            return p1.isWhite == p2.isWhite;
        }
    }

    //Implement the move
    public void makeMove(MoveClass move){
        if (move.piece.name == "Pawn"){
            makePawnMove(move);
        }
        else if (move.piece.name == "King"){
            makeKingMove(move);
        }

        //Update the column and the row
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;

        //Update where the piece is on the board
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;

        //Capture whatever piece is on the new tile
        capture(move);

        //Add the new move to the stack of previous moves
        //System.out.printf("%d:%d:%b\n", move.newCol, move.newRow, move.isShortCastle);
        moveStack.add(move);
        //MoveClass previousMove = moveStack.peek();
        //System.out.printf("%d:%d:%b\n", previousMove.newCol, previousMove.newRow, previousMove.isShortCastle);
        changeTurn();
    }

    private void makeKingMove(MoveClass move){
        if (Math.abs(move.piece.col - move.newCol) == 2){
            //System.out.printf("%b:%b\n", move.isLongCastle, move.isShortCastle);
            Piece rook;
            if (move.piece.col < move.newCol){
                rook = getPiece(7,move.piece.row);
                rook.col = 5;
            }
            else{
                rook = getPiece(0,move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * tileSize;
            rook.yPos = rook.row * tileSize;
        }
    }

    private void makePawnMove(MoveClass move){
        MoveClass previousMove;

        int startingPosition = move.piece.isWhite ? 6 : 1;
        int movementDirection = move.piece.isWhite ? -1 : 1;
        int promotionSquare = move.piece.isWhite ? 0 : 7;

        if (!moveStack.isEmpty()){
            previousMove = moveStack.peek();
        }
        else{
            previousMove = null;
        }

        if (move.newRow == promotionSquare){
            //System.out.printf("%d\n", move.newRow);
            move.isPromotion = true;
            pieceList.remove(move.piece);
            pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        }

        //If the move is a double move, make note so that en passant can be implemented
        if ((move.oldRow == startingPosition) && (move.newRow == move.oldRow + (2 * movementDirection))){
            move.doubleMove = true;
        }

        if (previousMove != null){
            if ((previousMove.doubleMove) && (previousMove.newCol == move.newCol) && (move.oldRow == previousMove.newRow)){
                move.capture = getPiece(previousMove.newCol, previousMove.newRow);
            }
        }
    }

    //Used to reverse the previously made move
    //Something weird happens on undoing en passant
    public void unMakeMove(){
        //Load the previous move
        MoveClass previousMove = moveStack.pop();

        //System.out.printf("%b\n", previousMove.isShortCastle);
        
        //Update the position to what the old location was
        previousMove.piece.col = previousMove.oldCol;
        previousMove.piece.row = previousMove.oldRow;

        previousMove.piece.xPos = previousMove.oldCol * tileSize;
        previousMove.piece.yPos = previousMove.oldRow * tileSize;

        //Find the piece that was taken in the previous move
        Piece takenPiece = previousMove.capture;

        //If something was taken last move, add the previous move to the list
        if (takenPiece != null){
            pieceList.add(takenPiece);
        }

        if (previousMove.isPromotion){
            pieceList.add(previousMove.piece);
        }

        //Undo move does not work for castling
        //System.out.printf("%d:%d, %b\n", previousMove.newCol, previousMove.newRow, previousMove.isShortCastle);

        if (previousMove.isShortCastle){
            //System.out.println("Short");

            Piece rook = getPiece(5, previousMove.newRow);

            rook.col = 7;

            rook.xPos = rook.col * tileSize;
            rook.yPos = rook.row * tileSize;
        }

        if (previousMove.isLongCastle){
            //System.out.println("Long");

            Piece rook = getPiece(3, previousMove.newRow);

            rook.col = 0;

            rook.xPos = rook.col * tileSize;
            rook.yPos = rook.row * tileSize;
        }

        if(previousMove.isPromotion){
            Piece promotedPiece = getPiece(previousMove.newCol, previousMove.newRow);

            pieceList.remove(promotedPiece);
        }

        changeTurn();
    }

    public void capture(MoveClass move){
        pieceList.remove(move.capture);
    }

    //Given the current board return all legal moves
    //Works properly
    public ArrayList<MoveClass> generatePseudoLegalMoves(boolean colorTurnWhite){
        ArrayList<MoveClass> legalMoves = new ArrayList<MoveClass>();

        for (Piece piece : pieceList){
            for (int r=0; r<8; r++){
                for (int c=0; c<8; c++){
                    MoveClass currentMove = new MoveClass(this, piece, c, r);
                    if (currentMove.piece.isValidMovement(this, currentMove) && colorTurnWhite == currentMove.piece.isWhite && !isSameSide(currentMove.piece, currentMove.capture)){
                        legalMoves.add(currentMove);
                    }
                }
            }
        }
        return legalMoves;
    }

    //Input: Color -> Output: King object
    public Piece getKing(boolean isWhite){
        for (int r=0; r<8; r++){
            for (int c=0; c<8; c++){
                //Find the piece at the location
                Piece currentPiece = getPiece(c, r);
                if (currentPiece != null){
                    //If we have found the king return its location
                    if (currentPiece.name == "King" && currentPiece.isWhite == isWhite){
                        return currentPiece;
                    }
                }
            }
        }
        //If the king is not on the board return null (This should not happen in a normal game)
        return null;
    }

    public boolean isCheck(boolean playerColor){
        Piece king = getKing(playerColor);
        ArrayList<MoveClass> enemyLegalMoves = generatePseudoLegalMoves(!playerColor);

        for (MoveClass move : enemyLegalMoves){
            if (move.newCol == king.col && move.newRow == king.row){
                return true;
            }
        }

        return false;
    }

    public ArrayList<MoveClass> generateLegalMoves(boolean playerColor){
        ArrayList<MoveClass> pseudoLegalMoves = generatePseudoLegalMoves(playerColor);
        ArrayList<MoveClass> legalMoves = new ArrayList<MoveClass>();

        for (MoveClass move : pseudoLegalMoves){
            makeMove(move);
            if (!isCheck(playerColor)){
                legalMoves.add(move);
            }
            unMakeMove();
        }

        return legalMoves;

    }

    public void addPieces(){
        //Starting position in chess
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));

        pieceList.add(new Pawn(this, 0, 6, true));
        pieceList.add(new Pawn(this, 1, 6, true));
        pieceList.add(new Pawn(this, 2, 6, true));
        pieceList.add(new Pawn(this, 3, 6, true));
        pieceList.add(new Pawn(this, 4, 6, true));
        pieceList.add(new Pawn(this, 5, 6, true));
        pieceList.add(new Pawn(this, 6, 6, true));
        pieceList.add(new Pawn(this, 7, 6, true));

        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));
        
        pieceList.add(new Pawn(this, 0, 1, false));
        pieceList.add(new Pawn(this, 1, 1, false));
        pieceList.add(new Pawn(this, 2, 1, false));
        pieceList.add(new Pawn(this, 3, 1, false));
        pieceList.add(new Pawn(this, 4, 1, false));
        pieceList.add(new Pawn(this, 5, 1, false));
        pieceList.add(new Pawn(this, 6, 1, false));
        pieceList.add(new Pawn(this, 7, 1, false));
    }

    //Draw all of the components - paint component is a special method from the swing package
    //This code always runs 
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        //Draw Board
        for (int r=0;r<rows;r++){
            for (int c=0;c<cols;c++){
                g2d.setColor((c+r) % 2 == 0 ? new Color(232, 235, 239) : new Color(125, 135, 150));
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }
        }

        //Display where the selected piece can move to
        if (selectedPiece != null){
            for (MoveClass move : legalMoves){
                if (selectedPiece == move.piece){
                    g2d.setColor(new Color(0,255,0, 145));
                    g2d.fillRect(move.newCol * tileSize, move.newRow * tileSize, tileSize, tileSize);
                }
            }
        }

        if (isCheck(true)){
            Piece whiteKing = getKing(true);
            g2d.setColor(new Color(255,0,0, 145));
            g2d.fillRect(whiteKing.col * tileSize, whiteKing.row * tileSize, tileSize, tileSize);
        }

        if (isCheck(false)){
            Piece whiteKing = getKing(false);
            g2d.setColor(new Color(255,0,0, 145));
            g2d.fillRect(whiteKing.col * tileSize, whiteKing.row * tileSize, tileSize, tileSize);
        }

        //Selected piece is selected in gold
        if (selectedPiece != null){
            g2d.setColor(new Color(219, 214, 48, 45));
            g2d.fillRect(selectedPiece.col * tileSize, selectedPiece.row * tileSize, tileSize, tileSize);
        }

        //Highlight the last move with gold colour
        if (!moveStack.isEmpty()){
            MoveClass previousMove = moveStack.peek();
            g2d.setColor(new Color(219, 214, 48, 145));
            g2d.fillRect(previousMove.oldCol * tileSize, previousMove.oldRow * tileSize, tileSize, tileSize);
            g2d.fillRect(previousMove.newCol * tileSize, previousMove.newRow * tileSize, tileSize, tileSize);
        }

        //Draw all the pieces currently on the board
        for (Piece piece : pieceList){
            piece.paint(g2d);
        }
    }
}