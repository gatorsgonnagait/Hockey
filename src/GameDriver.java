import net.java.games.input.*;
import net.java.games.input.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Main Controller class for the game. It calls update() and step()
 * @author Aditi Datta
 */


//test1
public class GameDriver {
    UI       ui;
    Rink     rink;
    Player   p1;
    Player   p2;
    Player   p3;
    Player   p4;
    Goalie1  g1;
    Goalie2  g2;
    Puck     puck;

    Controller controller = null;

    MouseEvent e;

    public ArrayList<Controller> foundControllers = new ArrayList<>();
    Player[]  activePlayers;

    static int width;
    static int height;

    int circleDiameter;
    int verticalCenterPos;
    int verticalLeftPos;
    int verticalRightPos;
    int horizontalThirds;
    int horizontalTwoThirds;
    int horizontalFifths;
    int horizontalTwoFifths;
    int horizontalThreeFifths;
    int horizontalFourFifths;

    static int topBoundary;
    static int bottomBoundary;
    static int leftBoundary;
    static int rightBoundary;

    static int horizontalMiddle;
    static int verticalCenter;

    static int rinkWidth;
    static int rinkHeight;

    static int leftGoalLine;
    static int rightGoalLine;
    static int topGoalPost;
    static int bottomGoalPost;
    static int leftGoalBack;
    static int rightGoalBack;
    static int goalWidth;
    static int goalLength;


/*
    //static int topBoundary = 100;
    //static int bottomBoundary = 450;
    //static int rightBoundary = 900;
    static int leftGoalLine = 190;
    static int rightGoalLine = 810;
    static int topGoalPost = 235;
    static int bottomGoalPost = 315;
    //static int horizontalMiddle = 275;
    //static int verticalCenter = 500;
    //static int leftGoalBack = 160;
    //static int rightGoalBack = 840;
*/
    Image num1 = Toolkit.getDefaultToolkit().getImage("img/one.png");
    Image num2 = Toolkit.getDefaultToolkit().getImage("img/two.png");
    Image num3 = Toolkit.getDefaultToolkit().getImage("img/three.png");
    Image num4 = Toolkit.getDefaultToolkit().getImage("img/four.png");


    // Component.Identifier componentIdentifier = component.getIdentifier();

    private void searchForControllers() {
        //mouse pad is automatically allowed
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        System.out.println(controllers.length);
        for(int i = 0; i < controllers.length; i++){
            controller = controllers[i];

            if (controller.getType() == Controller.Type.GAMEPAD ) {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);
                System.out.println(controller);
                System.out.println(controller.getPortNumber());
                System.out.println("controller is plugged in ");
                // Add new controller to the list on the window.
                //window.addControllerName(controller.getName() + " - " + controller.getType().toString() + " type");
            }


        }
        //System.out.println(foundControllers);
    }



    public GameDriver(int width, int height){
        this.height = height;
        this.width = width;

        circleDiameter = height/10;
        verticalCenterPos = verticalCenter - circleDiameter/2;
        verticalLeftPos = width/4 - circleDiameter/2;
        verticalRightPos = width/4*3 - circleDiameter/2;
        horizontalThirds =  height/3 - circleDiameter/2;
        horizontalTwoThirds = height/3*2 - circleDiameter/2;
        horizontalFifths = height/5 - circleDiameter/2;
        horizontalTwoFifths = height/5 * 2 - circleDiameter/2;
        horizontalThreeFifths = height/5 * 3 - circleDiameter/2;
        horizontalFourFifths = height/5 * 4 - circleDiameter/2;

        topBoundary = height/5;
        bottomBoundary = height * 4/5;
        leftBoundary = width/8;
        rightBoundary = width * 7/8;

        horizontalMiddle = height/2;
        verticalCenter = width/2;

        rinkWidth = 6 * width/8;
        rinkHeight = 3 * height/5;

        System.out.println();

        leftGoalLine = leftBoundary + rinkWidth/9;
        rightGoalLine = leftBoundary + rinkWidth * 8/9;
        topGoalPost = horizontalMiddle - rinkHeight * 4/35;
        bottomGoalPost = horizontalMiddle + rinkHeight * 4/35;
        leftGoalBack = leftGoalLine - rinkWidth * 3/80;
        rightGoalBack = rightGoalLine + rinkWidth * 3/80;

        goalWidth = leftGoalLine - leftGoalBack;
        goalLength = bottomGoalPost - topGoalPost;


        ui = new UI("Hockey");

        puck = new Puck(0,new Point(verticalCenter, horizontalMiddle), 0, 0, rinkWidth/100, Color.BLACK);

        Point point1 = new Point(verticalCenter, horizontalFifths);
        Point point2 = new Point(verticalCenter, horizontalTwoFifths);
        Point point3 = new Point(verticalCenter, horizontalThreeFifths);
        Point point4 = new Point(verticalCenter, horizontalFourFifths);
        //Point[] startingPoints = new Point[4]{p1, p2, p3, p4};

        //480, 275
        //530, 275
        int playerRadius = (int) Math.round(rinkWidth * 0.015);
        int goalieRadius = (int) Math.round(rinkWidth * 0.0125);
        p1   = new Player(1,new Point(verticalCenter - 3*playerRadius, horizontalMiddle), 0, 0, playerRadius, Color.RED, puck, num1);
        p2   = new Player(2,new Point(leftGoalLine + 10*playerRadius, horizontalMiddle - 8*playerRadius), 0, 0, playerRadius, Color.BLUE, puck, num2);
        p3   = new Player(3,new Point(verticalCenter + 3*playerRadius, horizontalMiddle), 0, Math.PI, playerRadius, Color.YELLOW, puck, num3);
        p4   = new Player(4,new Point(rightGoalLine - 10*playerRadius, horizontalMiddle + 8*playerRadius), 0, Math.PI, playerRadius, Color.GREEN, puck, num4);
        g1   = new Goalie1(5,new Point(leftGoalLine + goalieRadius, horizontalMiddle), 3, 0, goalieRadius, Color.LIGHT_GRAY, puck);
        g2   = new Goalie2(6,new Point(rightGoalLine - goalieRadius, horizontalMiddle), 3, Math.PI, goalieRadius, Color.LIGHT_GRAY, puck);

        Rink.selectedPlayer = p1;
        Rink.selectedPlayer2 = p2;
        Rink.selectedPlayer3 = p3;
        Rink.selectedPlayer4 = p4;
        activePlayers = new Player[] {p1,p2,p3,p4};
        int i = 0;
        searchForControllers();
        for(Controller controller : foundControllers){

            activePlayers[i].controller = controller;
            //System.out.println(activePlayers[i].controller + "active controller");
            i++;
        }

        /*

        JFrame menuFrame = new JFrame();
        Menu menu = new Menu(activePlayers);
        menuFrame.add(menu);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuFrame.pack();
        menuFrame.setVisible(true);
        */

        rink = new Rink();

        rink.addKeys();

        // ADDING OBJECTS TO THE RINK
        System.out.println("test2");
        rink.add(p1);
        rink.add(p2);
        rink.add(p3);
        rink.add(p4);

        rink.add(g1);
        rink.add(g2);
        rink.add(puck);
        // added moving objects



        ui.add(rink);
        ui.pack();
        ui.setVisible(true);


        rink.addMouseMotionListener(rink);
        //rink.addKeyListener(rink);*/
    }

    private class KBListener implements KeyListener {

        /**
         * Invoked when a key has been typed.
         * See the class description for {@link KeyEvent} for a definition of
         * a key typed event.
         *
         * @param e
         */
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if(c == 's' ){
                if( Rink.selectedPlayer == p1)
                    Rink.selectedPlayer = p2;
                else
                    Rink.selectedPlayer = p1;
            }
        }

        /**
         * Invoked when a key has been pressed.
         * See the class description for {@link KeyEvent} for a definition of
         * a key pressed event.
         *
         * @param e
         */
        @Override
        public void keyPressed(KeyEvent e) {
            char c = e.getKeyChar();
            if(c == 's' ){
                if( Rink.selectedPlayer == p1)
                    Rink.selectedPlayer = p2;
                else
                    Rink.selectedPlayer = p1;
            }
        }

        /**
         * Invoked when a key has been released.
         * See the class description for {@link KeyEvent} for a definition of
         * a key released event.
         *
         * @param e
         */
        @Override
        public void keyReleased(KeyEvent e) {
            char c = e.getKeyChar();
            if(c == 's' ){
                if( Rink.selectedPlayer == p1)
                    Rink.selectedPlayer = p2;
                else
                    Rink.selectedPlayer = p1;
            }
        }
    }


}

