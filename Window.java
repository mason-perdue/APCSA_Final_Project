// imports required classes
import java.awt.*;
// for keyboard input
import java.awt.event.*;
// for keeping track of objects
import java.util.ArrayList;
// for using random values
import java.lang.Math;
// for displaying graphics to screen
import javax.swing.*;

// makes window and instance of MyGame
public class Window{
    // main method that is run at start
    public static void main(String[] args) {
        // creats a new window
        JFrame frame = new JFrame("Game");
        // sets size of window
        frame.setSize(500, 500);
        // centers window on screen
        frame.setLocationRelativeTo(null);
        // prevents user from resizing window
        frame.setResizable(false);
        // allows x on window border to quit program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // creates instance of game
        MyGame game = new MyGame();
        // adds game frame to window
        frame.add(game);
        // sizes the game frame on the window
        frame.pack();
        // grabs focus for mouse and keyboard input
        game.requestFocus();
        // makes window visible to user
        frame.setVisible(true);
    }
}

// class that holds entire game
// extends and implements classes in order to use methods from them for advanced functionality
class MyGame extends JPanel implements ActionListener, KeyListener{
    // vars
    // size of each block in game
    private static int px = 20;
    // number of columns in window
    private static int cols = 25;
    // number of rows in window
    private static int rows = 25;
    // width of the window
    private static int width = px * cols;
    // height of the window
    private static int height = px * rows;
    // stores image for player
    private Image playerImg;
    // ArrayList to store images for enemies
    private ArrayList<Image> enemyImgArr;
    // Player object
    private Player player;
    // ArrayList to hold Enemy objects
    private ArrayList<Enemy> enemyArr;
    // ArrayList to hold Bullet objects
    private ArrayList<Bullet> bulletArr;
    // ArrayList to hold Asteroid objects
    private ArrayList<Asteroid> asteroidArr;
    // ArrayList to hold Integer objects as scores
    private ArrayList<Integer> scoreArr;
    // primative type integer to hold score for each round
    private int score;
    // primative type boolean to hold true/false value for if the game has ended or not
    private boolean gameOver;
    // Timer object to keep game refeshing
    private Timer gameTimer;

    // classes
    // parent class for objects on screen
    // x, y, w, h, img
    class Aspect{
        // instance variables unique to each object
        int x;
        int y;
        int w;
        int h;
        Image img;

        // constructor with parameters
        Aspect(int x, int y, int w, int h, Image img){
            // sets instance variables to have value of argument
            // uses this. keyword to differentiate between arguments and instance vars
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.img = img;
        }
    }

    // alive, health, xDir
    // child class of Aspect Class
    class Enemy extends Aspect{
        // new instance variables for only the child class
        boolean alive;
        int health;
        int xDir;

        // constructor
        Enemy(int w, int h, Image img){
            // super keyword used to call parent class constructor with arguements
            // Math.random() used to generate random x position
            super((((int) (Math.random() * (cols - 1))) * px), px, w, h, img);
            // sets instance variables
            alive = true;
            health = 4;
            xDir = 1;
        }
    }

    class Player extends Aspect{
        Player(int x, int y, int w, int h, Image img){
            super(x, y, w, h, img);
        }
    }

    // used
    // static: w, h
    class Bullet extends Aspect{
        boolean used;

        Bullet(int x, int y){
            super(x, y, MyGame.px / 8, MyGame.px / 2, null);
            used = false;
        }
    }

    class Asteroid extends Aspect{
        public Asteroid(int randR){
            super(0, ((int) (Math.random() * (height - 50))), randR, randR, null);
        }
    }

    // constructor for game
    public MyGame(){
        // window setup
        // sets size of window
        setPreferredSize(new Dimension(width, height));
        // sets background of window
        setBackground(Color.black);
        // allows window to be focused on
        setFocusable(true);
        // starts listening for input from keyboard
        addKeyListener(this);

        // import images
        // creates new ImageIcon object then uses getImage() to make it an Image object
        // getClass() gets file location of this file, getResource gets file based on location of this file, ./ is the working directory
        playerImg = new ImageIcon(getClass().getResource("./rsc/ship.png")).getImage();
        // instantiates ArrayList for holding Image objects
        enemyImgArr = new ArrayList<Image>();
        enemyImgArr.add(new ImageIcon(getClass().getResource("./rsc/alien.png")).getImage());
        enemyImgArr.add(new ImageIcon(getClass().getResource("./rsc/alien-cyan.png")).getImage());
        enemyImgArr.add(new ImageIcon(getClass().getResource("./rsc/alien-magenta.png")).getImage());
        enemyImgArr.add(new ImageIcon(getClass().getResource("./rsc/alien-yellow.png")).getImage());

        // create Player object by calling Player() constructor
        player = new Player(((px * cols) / 2 - px), (height - (px * 2)), (px * 2), px, playerImg);
        
        // create Enemy object array
        enemyArr = new ArrayList<Enemy>();
        // calls Enemy constructor 10 times and then adds the new objects to the ArrayList
        for(int i = 0; i < 10; i++){
            enemyArr.add(new Enemy(2 * px, px, enemyImgArr.get((int) (Math.random() * enemyImgArr.size()))));
        }

        // create Bullet object array
        bulletArr = new ArrayList<Bullet>();

        // create Asteroid object array
        asteroidArr = new ArrayList<Asteroid>();
        for(int i = 0; i < 5; i++){
            asteroidArr.add(new Asteroid((int) (Math.random() * 10) + 10));
        }

        // reset game
        score = 0;
        // instantiates ArrayList for Integer score values
        scoreArr = new ArrayList<Integer>();
        // begins game by setting primative boolean type gameOver to false
        gameOver = false;

        // start game timer
        // starts at 60 Hz
        gameTimer = new Timer(1000/60, this);
        // begins timer that will run for entire round
        gameTimer.start();
    }

    // refreshees screen
    // overrides method from parent class
    @Override
    public void actionPerformed(ActionEvent e){
        // calls move() and repaint() functions to move each object and then redisplay them
        move();
        repaint();

        // conditional to check if game has ended
        if(gameOver){
            // stops gameTimer
            gameTimer.stop();
            // adds privative int score to ArrayList of Integers by auto type casting 
            scoreArr.add(score);
        }
    }

    // updates screen objects
    @Override
    public void paintComponent(Graphics g){
        // class parent method that is being overridden
        super.paintComponent(g);
        // adds call to method from this child class
        draw(g);
    }

    // draws objects on screen
    public void draw(Graphics g){
        // draws player image based on 
        g.drawImage(player.img, player.x, player.y, player.w, player.h, null);

        // loops through each enemy object
        for(int i = 0; i < enemyArr.size(); i++){
            // null Image object to hold Image
            Image img = null;

            // if enemy at index i is not alive then replaces it with new Enemy object
            if(!enemyArr.get(i).alive){
                enemyArr.set(i, new Enemy(2 * px, px, enemyImgArr.get((int) (Math.random() * enemyImgArr.size()))));
            }
            
            // else if conditionals use boolean comparison operator for equals to change the image of each enemy depending on their health
            if(enemyArr.get(i).health == 4){
                img = enemyImgArr.get(0);
            }else if(enemyArr.get(i).health == 3){
                img = enemyImgArr.get(1);
            }else if(enemyArr.get(i).health == 2){
                img = enemyImgArr.get(2);
            }else if(enemyArr.get(i).health == 1){
                img = enemyImgArr.get(3);
            }

            g.drawImage(img, enemyArr.get(i).x, enemyArr.get(i).y, enemyArr.get(i).w, enemyArr.get(i).h, null);
        }

        // changes pen color to white
        g.setColor(Color.white);
        // loops through bullet arraylist
        for(int i = 0; i < bulletArr.size(); i++){
            if(!bulletArr.get(i).used){
                // draws a filled in rectangle based on Bullet object's instance variables
                g.fillRect(bulletArr.get(i).x, bulletArr.get(i).y, bulletArr.get(i).w, bulletArr.get(i).h);
            }else{
                // removes bullet from arraylist and decrements i if bullet has been used
                bulletArr.remove(i);
                i--;
            }
        }

        g.setColor(Color.gray);
        for(int i = 0; i < asteroidArr.size(); i++){
            // draws filled in oval
            g.fillOval(asteroidArr.get(i).x, asteroidArr.get(i).y, asteroidArr.get(i).w, asteroidArr.get(i).h);
        }

        g.setColor(Color.green);
        // checks if game has ended
        if(gameOver){
            // declares and initializes empty string 
            String scores = "";
            // adds each score from Integer ArrayList to String score
            for(Integer i : scoreArr){
                scores += i + ", ";
            }
            // changes font of pen
            g.setFont(new Font("Cascadia Code", Font.PLAIN, 32));
            // draws text on screen
            g.drawString("Game Over!", 10, 35);
            g.setFont(new Font("Cascadia Code", Font.PLAIN, 20));
            g.drawString("Scores: " + scores.substring(0, scores.length() - 2), 10, 75);
            g.drawString("Press ENTER to Restart", 10, 100);
        }else{
            // draws score on screen
            g.setFont(new Font("Cascadia Code", Font.PLAIN, 32));
            g.drawString("" + score, 10, 35);
        }
    }

    // moves objects on screen
    public void move(){
        // loops through enemy arraylist
        for(Enemy enemy : enemyArr){
            // changes boolean alive instance variable to false if enemy is past bottom of screen
            if(enemy.y >= (height - px)){
                enemy.alive = false;
            }
            
            // checks for collision between enemy and player
            if((enemy.x + enemy.w / 2 ) >= player.x && (enemy.x + enemy.w / 2 ) <= player.x + player.w && enemy.y + enemy.h >= player.y){
                gameOver = true;
            }

            // loops through bullet arraylist to check for collisions and then decrease the enemy health if it is hit
            for(Bullet bullet : bulletArr){
                if(bullet.x + bullet.w >= enemy.x && bullet.x <= enemy.x + enemy.w && bullet.y <= enemy.y + enemy.h && bullet.y >= enemy.y){
                    bullet.used = true;
                    enemy.health--;
                    score += 1;

                    // changes primative boolean to false if enemy health is zero (hit four times)
                    if(enemy.health == 0){
                        enemy.alive = false;
                    }
                }
            }

            // changes direction of x movement of enemies if they hit the edges of screen
            // uses boolean logic operator || for or between two boolean statements
            if(enemy.x <= 0 || enemy.x + enemy.w >= width){
                enemy.xDir *= -1;
            }else{
                // changes direction of enemy roughly 2% of the time
                if(Math.random() > 0.98){
                    enemy.xDir *= -1;
                }
            }

            // moves enemy on screen if still alive
            if(enemy.alive){
                enemy.y += 1;
                enemy.x += enemy.xDir;
            }
        }

        // loops through bullet objects
        for(Bullet bullet : bulletArr){
            // sets used to true if above top of screen
            if(bullet.y <= 0){
                bullet.used = true;
            }

            // if not used then move up screen
            if(!bullet.used){
                bullet.y -= 3;
            }
        }

        // loops through asteroid objects
        for(int i = 0; i < asteroidArr.size(); i++){
            // sets gameOver to false if collision between player and asteroid
            if((asteroidArr.get(i).x + asteroidArr.get(i).w / 2 ) >= player.x && (asteroidArr.get(i).x + asteroidArr.get(i).w / 2 ) <= player.x + player.w && asteroidArr.get(i).y + asteroidArr.get(i).h >= player.y){
                gameOver = true;
            }

            // removes and then adds in new asteroid if an asteroid is past the bottom of the screen
            // no check for right side of screen creates randomness by delaying when asteroids come back
            if(asteroidArr.get(i).y >= (height - px)){
                asteroidArr.remove(i);
                asteroidArr.add(i, new Asteroid((int) (Math.random() * 10) + 10));
            }

            // updates positon of asteroids to move from left side to bottom right corner at 45 deg angle
            asteroidArr.get(i).x += 1;
            asteroidArr.get(i).y += 1;
        }
    }

    // checks if a key has been pressed and then released
    @Override
    public void keyReleased(KeyEvent e){
        if(gameOver){
            // enter key
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                // clears objects on screen
                enemyArr.clear();
                bulletArr.clear();
                asteroidArr.clear();

                // creates new enemy and asteroid objects
                for(int i = 0; i < 10; i++){
                    enemyArr.add(new Enemy(2 * px, px, enemyImgArr.get((int) (Math.random() * enemyImgArr.size()))));
                }
                for(int i = 0; i < 5; i++){
                    asteroidArr.add(new Asteroid((int) (Math.random() * 10) + 10));
                }

                // resets game variables (player position, socre, gameOver, gameTimer)
                player.x = (px * cols) / 2 - px;
                score = 0;
                gameOver = false;
                gameTimer.start();
            }   
        }

        // left arrow moves player left on px
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(player.x > px){
                player.x -= px;
            }
        // right arrow moves player right on px
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(player.x < (width - px * 3)){
                player.x += px;
            }
        // space creates new bullet object at player's position
        }else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            bulletArr.add(new Bullet(player.x + px, player.y));
        // escape ends game
        }else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            gameOver = true;
        }
    }

    // overrides parent classes methods
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyPressed(KeyEvent e){}
}
