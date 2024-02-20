package main;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;

public class Main{
    public static void main(String[] args){
        //Print that the program is beginning
        System.out.println("Running Chess...");
        //Create the frame
        JFrame frame = new JFrame();
        //Set background colour
        frame.getContentPane().setBackground(new Color(70,70,70));
        frame.setTitle("Chess");
        frame.setLayout(new GridBagLayout());
        //Set grid size
        frame.setMinimumSize(new Dimension(1000, 1000));
        //Set location relative to screen (Central in this case)
        frame.setLocationRelativeTo(null);

        Board board = new Board();
        //Add the new board to the frame
        frame.add(board);

        /*
        JButton button = new JButton("Undo");
        //If the undo button is pressed, run the unmakemove method in board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if (!board.moveStack.isEmpty()){
                    //System.out.printf("%d:%d, %b\n", previousMove.newCol, previousMove.newRow, previousMove.isShortCastle);
                    board.unMakeMove();
                }
            }
        }); 
        frame.add(button);
        */
        //Show the new board
        frame.setVisible(true);
    }
}