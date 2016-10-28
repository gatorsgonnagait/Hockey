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



    public GameDriver(){
        ui = new UI("Hockey");

        puck = new Puck(0,new Point(500, 275), 0, 0, 8, Color.BLACK);


        p1   = new Player(1,new Point(480, 275), 0, 0, 12, Color.RED, puck);
        p2   = new Player(2,new Point(320, 170), 0, 0, 12, Color.BLUE, puck);
        p3   = new Player(3,new Point(530, 275), 0, Math.PI, 12, Color.YELLOW, puck);
        p4   = new Player(4,new Point(690, 370), 0, Math.PI, 12, Color.GREEN, puck);
        g1   = new Goalie1(5,new Point(190+20, 275), 3, 0, 10, Color.LIGHT_GRAY, puck);
        g2   = new Goalie2(6,new Point(810-20, 275), 3, Math.PI, 10, Color.LIGHT_GRAY, puck);

        Rink.selectedPlayer = p1;
        Rink.selectedPlayer2 = p2;
        Rink.selectedPlayer3 = p3;
        Rink.selectedPlayer4 = p4;
        activePlayers = new Player[] {p1,p2,p3,p4};
        int i = 0;
        searchForControllers();
        for(Controller controller : foundControllers){
            activePlayers[i].controller = controller;
            i++;
        }

        JFrame menuFrame = new JFrame();
        Menu menu = new Menu(1366, 768, activePlayers);
        menuFrame.add(menu);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuFrame.pack();
        menuFrame.setVisible(true);

        /*
        if(!foundControllers.isEmpty()){
            Rink.selectedPlayer.controller = foundControllers.get(0);
            //Rink.selectedPlayer2.controller = foundControllers.get(1);

            rink = new Rink();
            //rink    = new Rink(foundControllers.get(0));
        }
        else{
            rink = new Rink();
        }


        rink.addKeys();
        //s1.setPlayer(p1);


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
        // rink.addKeyListener(rink);*/
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

