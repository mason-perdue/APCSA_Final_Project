import java.awt.*;
import java.awt.event.*;
// for storing objects
import java.util.ArrayList;
// for adding unpredictability
import java.util.Random;
// for displaying images
import javax.swing.*;

// SpaceInvaders inherits from JPanel to be able to display images
// implements ActionListener to allow for the screen to be refreshed
// implements KeyListener to allow for keyboard inputs
public class SpaceInvaders extends JPanel implements ActionListener, KeyListener{
    // dimensions of window and squares
    int tileSize = 32;
    int columns = 16;
    int rows = 16;
    // 512 px
    int boardWidth = tileSize * columns;
    // 512 px 
    int boardHeight = tileSize * rows;
    
    // variables to hold Images
    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    
    // ArrayList to hold Image objects of aliens
    ArrayList<Image> alienImgArray;
    
    // variables for ship
    // 64 px
    int shipWidth = tileSize * 2;
    // 32 px
    int shipHeight = tileSize;
    // centers ship on window
    int shipX = (tileSize * columns) / 2 - tileSize;
    // puts ship one tile above bottom of window
    int shipY = boardHeight - tileSize * 2;
    // sets distance that ship moves to be same as tileSize
    int shipVelocityX = tileSize;
    // Block variable to hold a ship block object
    Block ship;
    
    // variables for alien
    // list to hold Block objects for aliens
    ArrayList<Block> alienArray;
    // sets widht and height of aliens
    int alienWidth = tileSize * 2;
    int alienHeight = tileSize;
    // puts first alien on top left corner of board
    int alienX = tileSize;
    int alienY = tileSize;
    // determines how many aliens
    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0;
    // sets speed of aliens in x direction in px
    int alienVelocityX = 1;

    // variables for bullets
    // array list to store block objects for bullets
    ArrayList<Block> bulletArray;
    // sets bullet size
    int bulletWidth = tileSize / 8;
    int bulletHeight = tileSize / 2;
    // speed at which bullet moves
    int bulletVelocityY = -10;
    
    // class to use as base for images of ship and aliens
    class Block{
        // x and y are for top left corner of images
        int x;
        int y;
        int width;
        int height;
        Image img;
        // used for aliens
        boolean alive = true;
        // used for bullets
        boolean used = false;
        
        // constructs block object with parameters
        public Block(int x, int y, int width, int height, Image img){
            // instantiates instance variables based on arguments
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }
    
    // declares Timer object to allow screen to be refreshed
    Timer gameLoop;

    // score tracker
    int score = 0;

    // boolean to track game over
    boolean gameOver = false;
    
    // constructs object 
    public SpaceInvaders(){
        // sets board size to 512 pixels square
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // sets background to black color
        setBackground(Color.black);
        // sets window to be what is locking the keyboard
        setFocusable(true);
        // listens for key inputs and then calls method
        addKeyListener(this);
        
        // load images
        // getClass() gets file location of this class file
        // getResource() gets file based on directory of the class (./)
        // getImage() makes the file an Image
        shipImg = new ImageIcon(getClass().getResource("./rsc/ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./rsc/alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("./rsc/alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("./rsc/alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("./rsc/alien-yellow.png")).getImage();
        
        // instanciates alien ArrayList
        alienImgArray = new ArrayList<Image>();
        // adds alien Image objects to array
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);
        
        // instantiates ship variable to a Block object
        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
        
        // creates alien array list
        alienArray = new ArrayList<Block>();

        // creates a bullet array list
        bulletArray = new ArrayList<Block>();
        
        // makes game loop timer
        // instantiates gameLoop with a delay of 1/60th of a second and an action to repeat (this is the class)
        gameLoop = new Timer(1000/60, this);
        // runs function to create random aliens
        createAliens();
        // starts game loop
        gameLoop.start();
    }
    
    // 
    public void paintComponent(Graphics g){
        // calls method from parent class
        super.paintComponent(g);
        // calls draw method with Graphic g as arguement
        draw(g);
    }
    
    public void draw(Graphics g){
        // draws ship image on screen
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);
        
        // draws aliens images on screen
        for(int i = 0; i < alienArray.size(); i++){
            // sets alien variable to each block object in array list
            Block alien = alienArray.get(i);
            // checks if the alien is alive or not
            if(alien.alive){
                // draws aliens on screen
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        // moves bullets
        // changes pen color to white
        g.setColor(Color.white);
        // iterates through bullet array 
        for(int i = 0; i < bulletArray.size(); i++){
            // gets bullet from array
            Block bullet = bulletArray.get(i);
            // checks if bullet has collided with alien yet
            if(!bullet.used){
                // draws filled rectangle
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        // draws score
        // sets pen color
        g.setColor(Color.green);
        // sets text font
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        // checks if game has ended
        if(gameOver){
            // prints string and score to the screen at 10, 35
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }else{
            // just draws score
            g.drawString(String.valueOf(score), 10, 35);
        }
    }
    
    public void createAliens(){
        // creates random object
        Random random = new Random();
        
        // iterates through each row
        for(int r = 0; r < alienRows; r++){
            // iterates through each column
            for(int c = 0; c < alienColumns; c++){
                // picks random index of alien in image array
                int randomImgIndex = random.nextInt(alienImgArray.size());
                // creates new block object to hold an alien
                Block alien = new Block(alienX + c * alienWidth, alienY + r * alienHeight, alienWidth, alienHeight, alienImgArray.get(randomImgIndex));
                // adds random alien to array list
                alienArray.add(alien);
            }
        }
        // sets alien count variable to length 
        alienCount = alienArray.size();
    }
    
    // hanles movement for aliens and bullets
    public void move(){
        // iterates through alien array list
        for(int i = 0; i < alienArray.size(); i++){
            // sets var alien equal to each alien in array list
            Block alien = alienArray.get(i);
            
            // checks if alien is alive
            if(alien.alive){
                // increases x value by velocity
                alien.x += alienVelocityX;
                
                // checks if alien is touching border
                if(alien.x + alien.width >= boardWidth || alien.x <= 0){
                    // reverses movement direction
                    alienVelocityX *= -1;
                    // moves aliens back one pixel
                    alien.x += alienVelocityX * 2;
                    
                    // iterates through indexes of each alien
                    for(int j = 0; j < alienArray.size(); j++){
                        // changes y of each alien to be one tile lower
                        alienArray.get(j).y += alienHeight;
                    }
                }

                // checks if aliens are on bottom row then goes game over
                if(alien.y >= ship.y){
                    gameOver = true;
                }
            }
        }

        // bullets
        // iterates through bullet array
        for(int i = 0; i < bulletArray.size(); i++){
            // gets bullet at index i in array
            Block bullet = bulletArray.get(i);
            // updates y position by velocity
            bullet.y += bulletVelocityY;

            // checks for bullet collisions with aliens
            // iterates through alien array
            for(int j = 0; j < alienArray.size(); j++){
                // gets alien at index j
                Block alien = alienArray.get(j);
                // checks if bullet and alien are alive and have collided
                if(!bullet.used && alien.alive && detectCollision(bullet, alien)){
                    // sets bullet to be used
                    bullet.used = true;
                    // sets alien to be dead
                    alien.alive = false;
                    // decrements alien count
                    alienCount--;
                    // increments score;
                    score += 100;
                }
            }
        }

        // bullet clean up
        // if array is not empty and either first bullet is used or is off the screen
        while(bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)){
            // removes first bullet
            bulletArray.remove(0);
        }

        // creates a second level
        if(alienCount == 0){
            // resets alien velocity
            alienVelocityX = 1;
            // ads bonus points to score
            score += alienColumns * alienRows * 100;
            // adds more columns and rows
            // no more columns than 6
            alienColumns = Math.min(alienColumns + 1, columns / 2 - 2);
            // no more than 10 rows
            alienRows = Math.min(alienRows + 1, rows - 6);
            // clears list of aliens
            alienArray.clear();
            // clears list of bullets
            bulletArray.clear();
            // creates new level of aliens
            createAliens();
        }
    }
    
    // checks for alien bullet collisions
    public boolean detectCollision(Block a, Block b){
        // formula to check intersection of two rectangles
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    // called 60 times per seconds
    @Override
    public void actionPerformed(ActionEvent e){
        // calls move function to update position of each element to move 60 px per minute
        move(); 
        // built-in method to call paintComponent
        repaint();
        // checks if game is over and then stops game loop
        if(gameOver){
            gameLoop.stop();
        }
    }
    
    // determines what key is being pressed 
    @Override
    public void keyTyped(KeyEvent e){}
    
    // determines if key is pressed down
    @Override
    public void keyPressed(KeyEvent e){}
    
    // determines if key is released
    @Override
    public void keyReleased(KeyEvent e){
        // if game over then allow any key to restart game
        if(gameOver){
            // resets variables
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows = 2;
            gameOver = false;
            createAliens();

            // restarts game loop
            gameLoop.start();
        }

        // checks if key that is released is left arrow key and that ship is not on left side of board
        if(e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0){
            // moves x value of ship to be one tile left
            ship.x -= shipVelocityX;
        // checks if key that is released is right arrow key and that ship is not on right side of board
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x < boardWidth - shipWidth){
            // moves x value of ship to be one tile right
            ship.x += shipVelocityX;
        // checks if key that is released is space key
        }else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            // makes new Block object for a bullet
            Block bullet = new Block(ship.x + shipWidth * 15 / 32, ship.y, bulletWidth, bulletHeight, null);
            // adds new bullet to array
            bulletArray.add(bullet);
        }
    }
}
