// APCSA Final based on https://www.youtube.com/watch?v=UILUMvjLEVU 

import javax.swing.*;

public class App{
    public static void main(String[] args) {
        System.out.println("Running...");
      
        // window vars
        int tileSize = 32;
        // int columns = 16;
        int rows = 16;
        // 512 px
        int boardWidth = 512;
        // 512 px
        int boardHeight = tileSize * rows;
        
        // creates new window
        JFrame frame = new JFrame("Space Invaders");
        // makes window correct size
        frame.setSize(boardWidth, boardHeight);
        // centers window on screen
        frame.setLocationRelativeTo(null);
        // stops user from resizing window
        frame.setResizable(false);
        // terminates program when x is pressed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        // creates new SpaceInvaders object
        SpaceInvaders spaceInvaders = new SpaceInvaders();
        // adds spaceInvaders to the window
        frame.add(spaceInvaders);
        // sizes board correctly in window
        frame.pack();
        // asks to be window that keyboard is focused on
        spaceInvaders.requestFocus();
        // makes window visible after adding components
        frame.setVisible(true);
    }
}
