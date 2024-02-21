package main;

import pieces.*;

public class BoardFormatConverter {
    public static char[][] boardFormatToCharBoard(Board board){
        char[][] charBoard = new char[8][8];

        for (int row=0; row < 8; row++){
            for (int col=0; col < 8; col++){
                Piece piece = board.getPiece(col, row);
                charBoard[row][col] = piece != null ? piece.letter : '.';
            }
        }

        return charBoard;
    }

    public static String convertCharBoardToFEN(char[][] charBoard){
        String fenString = "";
        int spaceCounter = 0;

        for (int row=0; row < 8; row++){
            for (int col=0; col < 8; col++){
                if (charBoard[row][col] != '.'){
                    if (spaceCounter > 0){fenString += Integer.toString(spaceCounter);}
                    fenString += charBoard[row][col];
                    spaceCounter = 0;
                }else {
                    spaceCounter += 1;
                }
            }
            if(spaceCounter > 0){fenString += Integer.toString(spaceCounter);}
            if(row <= 6){fenString += "/";}
            spaceCounter = 0;
        }

        return fenString;
    }
}
