// TicTacToe.java
//
//Implementation of Tic Tac Toe
//
// Andrew Rigas and Kyle Calder
// CS201 Final Project


import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

@SuppressWarnings("serial")

public class TicTacToe extends Applet implements ActionListener {

    TTTGrid c;
    Button clearButton, backButton, startButtonOne, startButtonTwo;
    boolean isStarted = false;
    Label spacer, bSpacer;
    Label xCount;
    Label turn;
    Label cCount;
    PopupMenu xWinner;
    PopupMenu cWinner;
    Panel p, b, s;



    //  StartScreen s
    public void init () {


        this.setLayout(new BorderLayout());

        startButtonOne = new Button("1-Player Start");    
        startButtonOne.addActionListener(this);
        startButtonTwo = new Button("2-Player Start");
        startButtonTwo.addActionListener(this);
        s = new Panel();

        s.setLayout(new GridLayout(2, 2));

        Panel u = new Panel();
        u.setLayout(new BorderLayout());

        Label ttt = new Label("Tic Tac Toe");
        u.add("Center", ttt);
        ttt.setAlignment(Label.CENTER);
        Font font = new Font("Sans-Serif", Font.BOLD, 60);
        ttt.setFont(font);

        Panel l = new Panel();
        l.setLayout(new BorderLayout());
        Panel lb = new Panel();
        lb.setLayout(new FlowLayout());
        lb.add(startButtonOne);
        lb.add(startButtonTwo);
        l.add("Center", lb);
        
        s.add(u);
        s.add(l);
        Color bg = new Color(0, 184, 201);
        s.setBackground(bg);
        add("Center", s);

        Image crossImage = getImage(getDocumentBase(), "cross.jpg");
        Image circleImage = getImage(getDocumentBase(), "circle.jpg");
       
        clearButton = new Button("Clear");
        clearButton.addActionListener(this);
        backButton = new Button("Home");
        backButton.addActionListener(this);
        c = new TTTGrid(crossImage, circleImage, this);
        p = new Panel();
        p.setBackground(Color.black);
        bSpacer = new Label("    ");
        spacer = new Label("                         ");
        turn = new Label("Turn: Cross");
        xCount = new Label("Cross wins: 0/2");
        cCount = new Label("Circle wins: 0/2");

        c.setBackground(Color.white);
        c.addMouseListener(c);
        
        p.add(backButton);
        p.add(bSpacer);
        p.add(clearButton);
        
        b = new Panel();
        b.add(turn);
        b.add(spacer);
        b.add(xCount);
        b.add(cCount);


    }
    // button handlers
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == clearButton) {
            c.clear();
        } else if (e.getSource() == backButton) {
            starter();
        } else if (e.getSource() == startButtonTwo) {
            starter(); 
        } else if (e.getSource() == startButtonOne) {
            c.onePlayer = true;
            starter();
        }
    }

    // Changes from menu to game canvas
    public void startGame() {

        if (isStarted) {
            this.remove(this.getComponent(0));

            this.add("Center", c);

            this.add("North", p);

            this.add("South", b);
            
        } else { // for home button
            this.remove(c);

            this.remove(p);

            this.remove(b);
            this.add("Center", s);
            c.clear();
            c.onePlayer = false;
            c.xWins = 0;
            c.cWins = 0;
            xCount.setText("Cross wins: 0/2");
            cCount.setText("Circle wins: 0/2");
            spacer.setText("                         ");
            
        }


    }
    // calls startGame and repaints
    public void starter() {
        if (isStarted == true)
            isStarted = false;
        else isStarted = true;      
        startGame();
        revalidate(); // this line with help from Scott Westvold
        repaint();
    }
}
// END TIC TAC TOE CLASS

@SuppressWarnings("serial")


class TTTGrid extends Canvas implements MouseListener {

    // instance variables representing the game go here
    int n = 9;
    boolean[] box = new boolean[n];
    int size = 120;
    int border = 110;
    Image crossImage, circleImage;
    int[][] board = new int[3][3];
    boolean turnCross = true;
    int k, j;
    int clicks = 0;
    int xWins = 0;
    int cWins = 0;
    int toW;
    TicTacToe parent;
    boolean onePlayer;
    Random rand = new Random();

    public TTTGrid(Image c1, Image cb, TicTacToe t) {
        crossImage = c1;
        circleImage = cb;
        parent = t;
    }

    // draw the boxes
    public void paint(Graphics g) {

        for (int i = 0; i < n; i++) {
            if (i <= 2) {
                int x = i * size + border;
                int y = border;
                g.setColor(Color.black);
                g.drawRect(x, y, size, size);
                if (board[i][0] == 1)
                    g.drawImage(crossImage, x+10, y+10, this);
                if (board[i][0] == 2)
                    g.drawImage(circleImage, x+10, y+10, this);

            }
            else if (i > 2 && i <= 5) {
                int x = (i-3) * size + border;
                int y = border + size;
                g.setColor(Color.black);
                g.drawRect(x, y, size, size);
                if (board[i-3][1] == 1)
                    g.drawImage(crossImage, x+10, y+10, this);
                if (board[i-3][1] == 2)
                    g.drawImage(circleImage, x+10, y+10, this);
            }
            else {
                int x = (i-6) * size + border;
                int y = border + 2 * size;
                g.setColor(Color.black);
                g.drawRect(x, y, size, size);
                if (board[i-6][2] == 1)
                    g.drawImage(crossImage, x+10, y+10, this);
                if (board[i-6][2] == 2)
                    g.drawImage(circleImage, x+10, y+10, this);
            }


        }
    }

    // handle mouse events
    public void mousePressed(MouseEvent event) {
        Point p = event.getPoint();

        // check if clicked in box area

        int x = p.x - border;
        int y = p.y - border;

        if (x >= 0 && x < n/3*size &&
                y >= 0 && y < n/3*size) {

            k = x / size;
            j = y / size;
            if (board[k][j]==0) {

                if (turnCross) {
                    board[k][j] = 1;
                }
                else board[k][j] = 2;
                turnSwitch();
                
                
                
                clicks++;
            }
        }
        repaint();
        endRound();
        compPlay();


    }

    // methods called from the event handler of the main applet

    // clears and resets game from current round
    public void clear() {
        for (int i = 0; i <= 2; i++) {
            board[i][0] = 0;
            board[i][1] = 0;
            board[i][2] = 0;
        }
        clicks = 0;
        turnCross = true;
        repaint();
        
    }
    // Switches which player is active
    public void turnSwitch() {
        
        if (turnCross == true) 
            turnCross = false;
        else turnCross = true;
        
    }
    // changes "turn" label and displays what turn it is
    public void turnLabelChange() {
        if (turnCross)
            parent.turn.setText("Turn: Cross");
        else parent.turn.setText("Turn: Circle");
    }


    // Adds images on press
    public void draw(Graphics g, MouseEvent event) {
        Point p = event.getPoint();
        int x = p.x - border;
        int y = p.y - border;
        if (board[k][j] == 1)
            g.drawImage(crossImage, x+10, y+10, this);
        if (board[k][j] == 2)
            g.drawImage(circleImage, x+10, y+10, this);
    }



    // looks for 3 in a row to win game
    // returns 1 for Cross win, 2 for Circle win
    public int checkWin() {
        int c;
        for (c=0; c<=2; c++) {
            // 3 straight in row/column check
            // Player 1 check
            if (board[0][c] == 1 && board[0][c] == board[1][c] && board[0][c] == board[2][c]) {               
                turnCross = true;
                return 1;
            }
            else if (board[c][0] == 1 && board[c][0] == board[c][1] && board[c][0] == board[c][2]) {
                turnCross = true;
                return 1;
            }
            // Player 2 check
            else if (board[0][c] == 2 && board[0][c] == board[1][c] && board[0][c] == board[2][c]) {
                turnCross = true;
                return 2;
            }
            else if (board[c][0] == 2 && board[c][0] == board[c][1] && board[c][0] == board[c][2]) {
                turnCross = true;
                return 2;
            }
        }   

        // Diagonal Checks
        if (board[0][0] == 1 && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            turnCross = true;
            return 1;
        }
        else if (board[2][0] == 1 && board[2][0] == board[1][1] && board[2][0] == board[0][2]) {
            turnCross = true;
            return 1;
        }
        else if (board[0][0] == 2 && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            turnCross = true;
            return 2;
        }
        else if (board[2][0] == 2 && board[2][0] == board[1][1] && board[2][0] == board[0][2]) {
            turnCross = true;
            return 2;
        }
        // else no winner
        else return 0;

    }

    // Check to see if there is winner this round &
    // check if there is a winner overall (2/3)
    public void endRound() {
        // if no winner
        if (checkWin() == 0 && clicks ==9) {
     
            new Thread(new Runnable(){           // delays with help from from stack overflow
                public void run(){               // URL: https://stackoverflow.com/questions/15660758/setting-a-delay-in-a-java-program
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                    clear();
                    repaint();

                }
            }).start();

        }
        // cross wins round
        else if (checkWin() == 1) {
            xWins++;
            
            parent.xCount.setText("Cross wins: 1/2");
            
            new Thread(new Runnable(){
                public void run(){
                    try{
                        Thread.sleep(1500);
                    }catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                    clear();
                    repaint();


                }
            }).start();
        }
        // circle wins round
        else if (checkWin() == 2) {
            cWins++;
            parent.cCount.setText("Circle wins: 1/2");
            new Thread(new Runnable(){
                public void run(){
                    try{
                        Thread.sleep(1500);
                    }catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                    clear();
                    repaint();

                }
            }).start();
        }
        // Cross wins  2 rounds and game
        if (xWins == 2) {
            parent.spacer.setText(" Cross WINS!!!");
            parent.xCount.setText("");
            parent.cCount.setText("");
            parent.turn.setText("           ");
            
            xWins = 0;
            cWins = 0;
            new Thread(new Runnable(){
                public void run(){
                    try{
                        Thread.sleep(1500);
                    }catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                    clear();
                    repaint();
                    parent.xCount.setText("Cross wins: 0/2");
                    parent.cCount.setText("Circle wins: 0/2");
                    parent.spacer.setText("                         ");
                    turnLabelChange();

                }
            }).start();
        }
        // circle wins two rounds and game
        else if (cWins == 2) {
            
            parent.spacer.setText(" Circle WINS!!!");
            parent.xCount.setText("");
            parent.cCount.setText("");
            parent.turn.setText("");
            xWins = 0;
            cWins = 0;
            new Thread(new Runnable(){
                public void run(){
                    try{
                        Thread.sleep(1500);
                    }catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                    clear();
                    repaint();
                    parent.xCount.setText("Cross wins: 0/2");
                    parent.cCount.setText("Circle wins: 0/2");
                    parent.spacer.setText("                         ");
                    turnLabelChange();

                }
            }).start();
        }
        
    }
    
    // Activate Computer player if onePlayer is true
    // twoPlayer == true when onePlayerButton is pressed
    // Plays in the order of:
    // 1. Tries to win
    // 2. Prevents human from winning
    // 3. Plays random move
    public void compPlay() {
        
        
        turnLabelChange();
        if (onePlayer) {
            if (turnCross == false)
                winningMove();
            if (turnCross == false)
                defendingMove();
            if (turnCross == false)
                randomMove();

            repaint();
            if (checkWin() == 2) {
                cWins++;
                parent.cCount.setText("Circle wins: 1/2");
                new Thread(new Runnable(){
                    public void run(){
                        try{
                            Thread.sleep(1500);
                        }catch(InterruptedException ex){
                            ex.printStackTrace();
                        }
                        clear();
                        repaint();

                    }
                }).start();
            }
            turnLabelChange();
            if (cWins == 2) {
                
                parent.spacer.setText(" Circle WINS!!!");
                parent.xCount.setText("");
                parent.cCount.setText("");
                parent.turn.setText("");
                xWins = 0;
                cWins = 0;
                new Thread(new Runnable(){
                    public void run(){
                        try{
                            Thread.sleep(1500);
                        }catch(InterruptedException ex){
                            ex.printStackTrace();
                        }
                        clear();
                        repaint();
                        parent.xCount.setText("Cross wins: 0/2");
                        parent.cCount.setText("Circle wins: 0/2");
                        parent.spacer.setText("                         ");

                    }
                }).start();
                turnLabelChange();
            }
        }
    }


    // computer plays winning move if available
    public void winningMove() {
        if (turnCross == false) {
            for (int c=0; c<=2; c++) {
                if (board[0][c] == 2 && board[0][c] == board[1][c] && board[2][c] == 0) {
                    board[2][c] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
                else if (board[1][c] == 2 && board[1][c] == board[2][c] && board[0][c] == 0) {
                    board[0][c] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
                else if (board[0][c] == 2 && board[0][c] == board[2][c] && board[1][c] == 0) {
                    board[1][c] = 2; 
                    clicks++;
                    turnSwitch();
                    return;
                }

                else if (board[c][0] == 2 && board[c][0] == board[c][1] && board[c][2] == 0) {
                    board[c][2] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
                else if (board[c][1] == 2 && board[c][1] == board[c][2] && board[c][0] == 0) {
                    board[c][0] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
                else if (board[c][0] == 2 && board[c][0] == board[c][2] && board[c][1] == 0) {
                    board[c][1] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }

            }
            if (board[0][0] == 2 && board[0][0] == board[1][1] && board[2][2] == 0) {
                board[2][2] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            else if (board[0][0] == 2 && board[0][0] == board[2][2] && board[1][1] == 0) {
                board[1][1] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            else if (board[1][1] == 2 && board[2][2] == board[1][1] && board[2][2] == 0) {
                board[0][0] = 2;
                clicks++;
                turnSwitch();
                return;
            }

            else if (board[2][0] == 2 && board[2][0] == board[1][1] && board[0][2] == 0) {
                board[0][2] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            else if (board[0][2] == 2 && board[2][0] == board[1][1] && board[2][0] == 0) {
                board[2][0] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            else if (board[2][0] == 2 && board[2][0] == board[0][2] && board[1][1] == 0) {
                board[1][1] = 2;
                clicks++;
                turnSwitch();
                return;
            }




        }
    }

    // if no winning move, plays move to defend against
    // human player winning
    public void defendingMove() {
        if (turnCross == false) {
            for (int c=0; c<=2; c++) {
                if (board[0][c] == 1 && board[0][c] == board[1][c] && board[2][c] == 0) {
                    board[2][c] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
                else if (board[1][c] == 1 && board[1][c] == board[2][c] && board[0][c] == 0) {
                    board[0][c] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
                else if (board[0][c] == 1 && board[0][c] == board[2][c] && board[1][c] == 0) {
                    board[1][c] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
                else if (board[c][0] == 1 && board[c][0] == board[c][1]  && board[c][2] == 0) {
                    board[c][2] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
                else if (board[c][1] == 1 && board[c][1] == board[c][2] && board[c][0] == 0) {
                    board[c][0] = 2;
                    clicks++;
                    turnSwitch();
                    return;

                }
                else if (board[c][0] == 1 && board[c][0] == board[c][2] && board[c][1] == 0) {
                    board[c][1] = 2;
                    clicks++;
                    turnSwitch();
                    return;
                }
            }
            if (board[0][0] == 1 && board[0][0] == board[1][1] && board[2][2] == 0) {
                board[2][2] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            else if (board[0][0] == 1 && board[0][0] == board[2][2] && board[1][1] == 0) {
                board[1][1] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            else if (board[1][1] == 1 && board[2][2] == board[1][1] && board[0][0] == 0) {
                board[0][0] = 2;
                clicks++;
                turnSwitch();
                return;
            }

            else if (board[2][0] == 1 && board[2][0] == board[1][1] && board[0][2] == 0) {
                board[0][2] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            else if (board[0][2] == 1 && board[2][0] == board[1][1] && board[2][0] == 0) {
                board[2][0] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            else if (board[2][0] == 1 && board[2][0] == board[0][2] && board[1][1] == 0) {
                board[1][1] = 2;
                clicks++;
                turnSwitch();
                return;
            }
            


        }
    }

    // if no winning or defending moves, computer 
    // plays a randomized move
    public void randomMove() {
        if (turnCross == false && clicks <= 8) {
            int  r1 = 2;
            int  r2 = 0;
            
            while (board[r1][r2] != 0) {
                r1 = rand.nextInt(3);
                r2 = rand.nextInt(3);
                
            }

            board[r1][r2] = 2;
            clicks++;
            turnSwitch();
            return;
        }
    }

    // need these also because we implement a MouseListener
    public void mouseReleased(MouseEvent event) { }
    public void mouseClicked(MouseEvent event) { }
    public void mouseEntered(MouseEvent event) { }
    public void mouseExited(MouseEvent event) { }
}
