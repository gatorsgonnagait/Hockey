import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;


/**
 * creates a Rink on the main panel.
 * @author Evan Mesa
 * @version 1
 */
public class Rink extends JPanel implements Runnable , MouseMotionListener{

    Thread t;
    //ArrayList<Player> players = new ArrayList<>();
    Player[] players = new Player[7];
    static Player selectedPlayer;
    static Player selectedPlayer2;
    static Player selectedPlayer3;
    static Player selectedPlayer4;
    boolean dragged = false;
    static boolean moved = false;
    MouseEvent e = null;
    static int possession = 0;
    Puck puck;
    int frames = 0;
    int goalieTimer = 0;
    int resetTimer = 0;
    int afterGoalTimer = 0;


    boolean flag = false;
    static int reset = 0;
    static int score = 0;
    int positionSwitch = 0;

    ScorePanel scorePanel = new ScorePanel();
    boolean setScore1 = false;
    boolean setScore2 = false;


    static int i = 0;

    Rink() {
        setPreferredSize(new Dimension(GameDriver.width, GameDriver.height));
        add(scorePanel);
        setVisible(true);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D rink = (Graphics2D) g;

        rink.setStroke(new BasicStroke(3));

        rink.setColor(Color.BLUE);//crease
        //fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
        //rink.fillArc(190-40, 232, 86, 86, 90, -180);
        rink.fillArc(GameDriver.leftGoalLine - GameDriver.goalLength/2+GameDriver.width/500, GameDriver.topGoalPost, GameDriver.goalLength, GameDriver.goalLength, 90, -180);
        rink.fillArc(GameDriver.rightGoalLine - GameDriver.goalLength/2, GameDriver.topGoalPost, GameDriver.goalLength, GameDriver.goalLength, 90, 180);


        rink.setColor(Color.RED);
        //rink.draw(new Line2D.Double(190, 100, 190, 450)); // first vertical lines on rink
        rink.draw(new Line2D.Double(GameDriver.leftGoalLine, GameDriver.topBoundary, GameDriver.leftGoalLine, GameDriver.bottomBoundary));
        rink.setColor(Color.BLUE);
        //rink.draw(new Line2D.Double(340, 100, 340, 450));
        rink.draw(new Line2D.Double(GameDriver.leftBoundary + GameDriver.rinkWidth/3, GameDriver.topBoundary, GameDriver.leftBoundary + GameDriver.rinkWidth * 1/3, GameDriver.bottomBoundary));
        rink.setColor(Color.RED);
        //rink.draw(new Line2D.Double(500, 100, 500, 450));
        rink.draw(new Line2D.Double(GameDriver.verticalCenter, GameDriver.topBoundary, GameDriver.verticalCenter, GameDriver.bottomBoundary));
        rink.setColor(Color.BLUE);
        //rink.draw(new Line2D.Double(660, 100, 660, 450));
        rink.draw(new Line2D.Double(GameDriver.leftBoundary + GameDriver.rinkWidth * 2/3, GameDriver.topBoundary, GameDriver.leftBoundary + GameDriver.rinkWidth * 2/3, GameDriver.bottomBoundary));
        rink.setColor(Color.RED);
        //rink.draw(new Line2D.Double(810, 100, 810, 450)); // last line
        rink.draw(new Line2D.Double(GameDriver.leftBoundary + GameDriver.rinkWidth * 8/9, GameDriver.topBoundary, GameDriver.leftBoundary + GameDriver.rinkWidth * 8/9, GameDriver.bottomBoundary));



        /*
        rink.drawOval(445, 220, 110, 110);
        rink.setColor(Color.BLACK);
        rink.draw(new RoundRectangle2D.Double(100, 100, 800, 350, 200, 200));
        */

        //rink.setColor(Color.GREEN);// center X line
        //rink.draw(new Line2D.Double(GameDriver.leftGoalLine, GameDriver.horizontalMiddle, GameDriver.rightGoalLine, GameDriver.horizontalMiddle));

        rink.setColor(Color.RED); //goals
        //rink.draw(new Rectangle2D.Double(MovingObject.leftGoalBack, MovingObject.topGoalPost, 30, 80));
        rink.draw(new Rectangle2D.Double(GameDriver.leftGoalBack, GameDriver.topGoalPost, GameDriver.goalWidth, GameDriver.goalLength));
        //rink.draw(new Rectangle2D.Double(MovingObject.rightGoalLine, MovingObject.topGoalPost, 30, 80));
        rink.draw(new Rectangle2D.Double(GameDriver.rightGoalLine, GameDriver.topGoalPost, GameDriver.goalWidth, GameDriver.goalLength));
        //center circle
        rink.setColor(Color.BLUE);// center circle
        rink.drawOval(GameDriver.verticalCenter - GameDriver.height/12, GameDriver.horizontalMiddle - GameDriver.height/12, GameDriver.height/6, GameDriver.height/6);
        rink.setColor(Color.BLACK);
        //public abstract void setRoundRect(double x,double y,double w,double h,double arcWidth, double arcHeight)
        rink.draw(new RoundRectangle2D.Double(GameDriver.leftBoundary, GameDriver.topBoundary, GameDriver.rinkWidth, GameDriver.rinkHeight, GameDriver.rinkWidth/5, GameDriver.rinkWidth/5));




        rink.setColor(Color.BLACK);
        //Arc2D arc1 = new Arc2D.Double(100, 100, 200, 200, 90, 90, Arc2D.OPEN);
        //Arc2D arc1 = new Arc2D.Double(GameDriver.leftBoundary, GameDriver.topBoundary, GameDriver.topBoundary*5/4, GameDriver.topBoundary*5/4, 90, 90, Arc2D.OPEN);
        Arc2D arc1 = new Arc2D.Double(GameDriver.leftBoundary, GameDriver.topBoundary, GameDriver.rinkWidth/5, GameDriver.rinkWidth/5, 90, 90, Arc2D.OPEN);
        rink.draw(arc1);
        //Arc2D arc2 = new Arc2D.Double(100, 250, 200, 200, 180, 90, Arc2D.OPEN);
        Arc2D arc2 = new Arc2D.Double(GameDriver.leftBoundary, GameDriver.bottomBoundary - GameDriver.rinkWidth, GameDriver.rinkWidth/5 , GameDriver.rinkWidth/5, 180, 90, Arc2D.OPEN);
        rink.draw(arc2);

        //Arc2D arc3 = new Arc2D.Double(700, 100, 200, 200, 0, 90, Arc2D.OPEN);
        Arc2D arc3 = new Arc2D.Double(GameDriver.width, GameDriver.topBoundary, GameDriver.rinkWidth/5, GameDriver.rinkWidth/5, 0, 90, Arc2D.OPEN);
        rink.draw(arc3);

        //Arc2D arc4 = new Arc2D.Double(700, 250, 200, 200, 270, 90, Arc2D.OPEN);
        Arc2D arc4 = new Arc2D.Double(GameDriver.width, GameDriver.bottomBoundary - GameDriver.rinkWidth, GameDriver.rinkWidth/5, GameDriver.rinkWidth/5, 270, 90, Arc2D.OPEN);
        rink.draw(arc4);


        for(int i = 1; i < players.length; i++){
            if(players[i] == null){
                continue;
            }
            players[i].draw(rink);
        }
        puck.draw(rink);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        t = new Thread(this);
        t.start();
    }

    public void startGame(){
        if(t == null) {
            t = new Thread(this, "Rink");
            t.start();
        }
    }

    @Override
    public void run() {
        System.out.println("RUNNING");
        long m = 0;
        int frames = 0;
        long nano = System.nanoTime();

        while(true) {
            i++;
            scorePanel.fps++;

            //System.out.println("FPS " + scorePanel.fps);

            /*
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateAll();
            repaint();
            */
            //40 fps 25000000 60 fps 16666667
            long initTime = System.nanoTime();
            updateAll();
            long elapsed = (System.nanoTime() - initTime);
            //System.out.println(elapsed);
            int sleepTime = (int) ((12000000.0 - elapsed ) / 1000000.0);
            //System.out.println(sleepTime);
            if( sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            repaint();
            /*
            frames++;
                System.out.println("frames " +frames);
            if (frames % 60 == 0) {
                long current = System.nanoTime();
                    //System.out.println(current - nano);
                nano = current;
            }*/

        }
    }


    public void add(Player mo){
        players[mo.id] = mo;
        super.add(mo);
    }

    public void add(Puck puck){
        this.puck = puck;
        //players[0] = puck;
        super.add(puck);
    }

    public void updateAll(){

        //int testX = puck.location.x;
        //int testY = puck.location.y;

        possession = puck.hold;

        puck.hitWalls();
        puck.hitGoals();
        goalScored();

        if(puck.speed > GameDriver.rinkWidth /200 ) {

            if (i % 15 == 0) {
                puck.speed = puck.setSpeedFriction(puck.frictionCoefficient);
            }
            puck.updateLocation();
        }
        else if ( puck.speed < GameDriver.rinkWidth/200 && puck.speed > 0.1 && puck.hold == 0){

            if(puck.pointList.size()==0) {
                puck.interpolationLine(puck.angle);//run this once
            }
            puck.stopObject();
        }
        else if(puck.speed <= .1 && puck.hold == 0){
            puck.speed = 0;
            if(puck.pointList.size() != 0)
                puck.pointList.clear();
            puck.updateLocation();
        }

        //int tester =  Math.abs(puck.location.x) - Math.abs(testX);



        for(int i = 1; i < players.length; i++){

            if(players[i].controller != null){
                players[i].gamepad();
            }
            if(players[i] == null){
                continue;
            }

            Player mo = players[i];
            if(reset == 0) {// if its in reset mode it will skip everything

                movement(mo);

                mo.hitWalls();

                if (mo.hitWall != 0) {
                    mo.rubWalls();
                    mo.hitWall = 0;
                }

                if (i != possession) {
                    mo.stickHandling();
                }

                if (puck.hold != 0) {
                    //System.out.println(Player.hold);
                    players[puck.hold].holdPuck();
                    puck.pointList.clear();

                    //when goalie gets the puck
                    if (puck.hold == 5 || puck.hold == 6) {
                        goalieHold();
                    }
                }
            }
        }




        Collision collision = new Collision(players.length);
        if(puck.hold == 0) {
            for (int j = 1; j < players.length; j++) {
                //collision.objectsCollide(players[i], players[j]);
                if (collision.objectsCollide(puck, players[j])) {
                    collision.calculateCollisions(puck, players[j]);
                }
            }
        }

        for(int i = 1; i < players.length; i++){
            for (int j = i+1; j < players.length; j++){
                //collision.objectsCollide(players[i], players[j]);
                if(collision.objectsCollide(players[i], players[j])) {
                    collision.calculateCollisions(players[i], players[j]);
                }
            }
        }

    }


    public void movement(Player mo){

        if(mo.stealFlag){
            mo.steal();
        }

        if(mo.slapShotFlag){
            mo.slapShot();
        }

        else if(mo.bodyCheckFlag){
            mo.bodyCheck();
        }
        else if (mo.controller != null) {
            //System.out.println("move");
            if (mo.colliding) {
                mo.updateLocationCol();
                //mo.colliding = false;
            }
            else {
                mo.updateLocationController(mo.xAxisPercentage, mo.yAxisPercentage);
            }
        }

        if (mo.colliding) {
            mo.updateLocationCol();
            //mo.colliding = false;
        } else {// for goalies
            mo.updateLocation();
        }


        if (mo == selectedPlayer4){
            //else if(mo.controller.getType().equals( Controller.Type.MOUSE)){
            //if player.gamepadtype = pad, call updateLocationController();
            //if player.gamepadtype == kb call updateLocation

            if (mo.colliding) {
                mo.updateLocationCol();
                mo.colliding = false;
            }
            else if (dragged || moved) {
                mo.updateLocation(e.getX(), e.getY());
            }
        }
    }

    private void switchStartPositions(){
        double tempX, tempY;
        double tempA;
        tempX = players[1].startX;
        tempY = players[1].startY;
        players[1].startY = players[2].startY;
        players[1].startX = players[2].startX;
        players[2].startY = tempY;
        players[2].startX = tempX;

        tempX = players[3].startX;
        tempY = players[3].startY;
        players[3].startY = players[4].startY;
        players[3].startX = players[4].startX;
        players[4].startY = tempY;
        players[4].startX = tempX;

    }


    public void goalScored(){
        if(score == 0 && puck.goalScoredLeft()){

            score = 1;
            players[5].release = 0;
            puck.hold = 0;
        }
        else if(score == 0 && puck.goalScoredRight()){
            score = 2;
            players[6].release = 0;
            puck.hold = 0;
        }

        if( score == 1 ){

            if(!setScore1) {
                setScore1 = true;
                scorePanel.addScore1(1);
                positionSwitch++;
                switchStartPositions();
            }
            reset = 1;
            afterGoalTimer++;
            reset();
            //puck.hold = 0;
            if(afterGoalTimer >= 40) {

                players[5].afterGoal();
            }

        }
        else if(score == 2){
            if(!setScore2) {
                setScore2 = true;
                scorePanel.addScore2(1);
                positionSwitch++;
                switchStartPositions();
            }
            reset = 2;
            afterGoalTimer++;
            reset();
            //puck.hold = 0;
            if(afterGoalTimer >= 40) {
                players[6].afterGoal();
            }
        }
    }

    public void goalieHold(){
        goalieTimer++;
        if (goalieTimer == 800) {
            if (puck.hold == 5) {
                if (players[1].location.x > GameDriver.leftGoalLine ||
                        players[2].location.x > GameDriver.leftGoalLine) {
                    goaliePassToTeammates1();
                }
                else {
                    players[puck.hold].wristShot();
                }
                goalieTimer = 0;
            }
            else if (puck.hold == 6) {

                if (players[3].location.x < GameDriver.rightGoalLine ||
                        players[4].location.x < GameDriver.rightGoalLine) {
                    goaliePassToTeammates2();
                }
                else {
                    players[puck.hold].wristShot();
                }

                goalieTimer = 0;
            }
        }
    }

    public void goaliePassToTeammates1(){
        double Y1 = players[1].location.y - players[5].location.y;
        double X1 = players[1].location.x - players[5].location.x;
        double Y2 = players[2].location.y - players[5].location.y;
        double X2 = players[2].location.x - players[5].location.x;

        double toPlayer1 = Math.sqrt(Math.pow((X1), 2)
                + Math.pow((Y1), 2));

        double toPlayer2 = Math.sqrt(Math.pow((X2), 2)
                + Math.pow((Y2), 2));

        double toPlayer3 = Math.sqrt(Math.pow((players[3].location.x - players[5].location.x), 2)
                + Math.pow((players[3].location.y - players[5].location.y), 2));

        double toPlayer4 = Math.sqrt(Math.pow((players[4].location.x - players[5].location.x), 2)
                + Math.pow((players[4].location.y - players[5].location.y), 2));


        if (toPlayer1 < toPlayer3 && toPlayer1 < toPlayer4 && toPlayer1 < toPlayer2) {// if player one is closest
            System.out.println("pass back to player 1");
            players[5].angleFacing = Math.atan2(Y1, X1);
        }
        else if (toPlayer2 < toPlayer3 && toPlayer2 < toPlayer4 && toPlayer2 < toPlayer1) {// if player 2 is closest
            System.out.println("pass back to player 2");
            players[5].angleFacing = Math.atan2(Y2, X2);
        }
        else if( (toPlayer3 < toPlayer1 || toPlayer3 < toPlayer2 || toPlayer4 < toPlayer1 || toPlayer4 < toPlayer2)){

            Line line1 = new Line(players[5].location.x, players[1].location.x,
                    players[5].location.y, players[1].location.y );//goalie to player 1
            Line line2 = new Line(players[5].location.x, players[2].location.x,
                    players[5].location.y, players[2].location.y );//goalie to player 2

            double[] distance = new double[4];
            distance[0] = line1.distanceFrom(players[3].location.x, players[3].location.y);
            distance[1] = line1.distanceFrom(players[4].location.x, players[4].location.y);

            distance[2] = line2.distanceFrom(players[3].location.x, players[3].location.y);
            distance[3] = line2.distanceFrom(players[4].location.x, players[4].location.y);
            double max = distance[0];
            for(int i = 1; i < 4; i++){
                if(distance[i] > max){
                    max = distance[i];
                }
            }

            if(max == distance[0] || max == distance[1]){// if the defenders are farthest from the player 1 passing lane, pass to p1
                players[5].angleFacing = Math.atan2(Y1, X1);
            }
            if(max == distance[2] || max == distance[3]){// if the defenders are farthest from the player 2 passing lane, pass to p2
                players[5].angleFacing = Math.atan2(Y2, X2);
            }
        }

        players[puck.hold].wristShot();
        goalieTimer = 0;
    }

    public void goaliePassToTeammates2(){
        double Y3 = players[3].location.y - players[6].location.y;
        double X3 = players[3].location.x - players[6].location.x;
        double Y4 = players[4].location.y - players[6].location.y;
        double X4 = players[4].location.x - players[6].location.x;

        double toPlayer1 = Math.sqrt(Math.pow((players[1].location.x - players[6].location.x), 2)
                + Math.pow((players[1].location.y - players[6].location.y), 2));

        double toPlayer2 = Math.sqrt(Math.pow((players[2].location.x - players[6].location.x), 2)
                + Math.pow((players[2].location.y - players[6].location.y), 2));

        double toPlayer3 = Math.sqrt(Math.pow((X3), 2)
                + Math.pow((Y3), 2));

        double toPlayer4 = Math.sqrt(Math.pow((X4), 2)
                + Math.pow((Y4), 2));


        if (toPlayer3 < toPlayer1 && toPlayer3 < toPlayer2 && toPlayer3 < toPlayer4) {// if player one is closest
            System.out.println("pass back to player 3");

            players[6].setAngle(Math.atan2(Y3, X3));
        }
        else if (toPlayer4 < toPlayer1 && toPlayer4 < toPlayer2 && toPlayer4 < toPlayer3) {// if player 2 is closest
            System.out.println("pass back to player 4");
            players[6].setAngle(Math.atan2(Y4, X4));
        }
        else if( (toPlayer3 < toPlayer1 || toPlayer3 < toPlayer2 || toPlayer4 < toPlayer1 || toPlayer4 < toPlayer2)){

            Line line1 = new Line(players[6].location.x, players[3].location.x,
                    players[6].location.y, players[3].location.y );//goalie to player 1
            Line line2 = new Line(players[6].location.x, players[4].location.x,
                    players[6].location.y, players[4].location.y );//goalie to player 2

            double[] distance = new double[4];
            distance[0] = line1.distanceFrom(players[1].location.x, players[1].location.y);
            distance[1]= line1.distanceFrom(players[2].location.x, players[2].location.y);

            distance[2] = line2.distanceFrom(players[1].location.x, players[1].location.y);
            distance[3] = line2.distanceFrom(players[2].location.x, players[2].location.y);
            double max = distance[0];
            for(int i = 1; i < 4; i++){
                if(distance[i] > max){
                    max = distance[i];
                }
            }

            if(max == distance[0] || max == distance[1]){// if the defenders are farthest from the player 1 passing lane, pass to p1
                players[6].setAngle(Math.atan2(Y3, X3));
            }
            if(max == distance[2] || max == distance[3]){// if the defenders are farthest from the player 2 passing lane, pass to p2
                players[6].setAngle(Math.atan2(Y4, X4));
            }
        }

        players[puck.hold].wristShot();
        goalieTimer = 0;
    }



    public void reset(){

        for(int i = 1; i < players.length-2; i++){
            double Y = players[i].startY - players[i].location.y;
            double X = players[i].startX - players[i].location.x;
            players[i].angleFacing = Math.atan2(Y, X);
            players[i].location.x = (players[i].location.x + players[i].playerSpeedLimit * Math.cos(players[i].angleFacing));
            players[i].location.y = (players[i].location.y + players[i].playerSpeedLimit * Math.sin(players[i].angleFacing));
            players[i].stick.updateLocation();



            if(players[i].location.y > players[i].startY - GameDriver.rinkWidth/100
                    && players[i].location.y < players[i].startY +  GameDriver.rinkWidth/100
                    && players[i].location.x > players[i].startX -  GameDriver.rinkWidth/100
                    && players[i].location.x < players[i].startX +  GameDriver.rinkWidth/100){

                players[i].location.x = players[i].startX;
                players[i].location.y = players[i].startY;
                players[i].angleFacing = players[i].initAngle;
                players[i].stick.updateLocation();
                players[i].setSpeed(0);
                players[i].pointList.clear();


            }
        }

        if(puck.location.y > GameDriver.horizontalMiddle - GameDriver.rinkWidth/20
                && puck.location.y < GameDriver.horizontalMiddle + GameDriver.rinkWidth/20
                && puck.location.x > GameDriver.verticalCenter - GameDriver.rinkWidth/20
                && puck.location.x < GameDriver.verticalCenter + GameDriver.rinkWidth/20){


            puck.location.y = GameDriver.horizontalMiddle;
            puck.location.x = GameDriver.verticalCenter;
            puck.speed = 0;



            resetTimer++;
            if( resetTimer == 100) {
                puck.hold = 0;
                reset = 0;
                score = 0;
                setScore1 = false;
                setScore2 = false;
                afterGoalTimer = 0;
                resetTimer = 0;
            }
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        dragged = true;
        this.e = e;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        moved = true;
        this.e = e;
    }


    public void addKeys(){


        KeyStroke w = KeyStroke.getKeyStroke("W");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(w, "up");
        getActionMap().put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayer4.moveY(5);
                selectedPlayer4.setAngle(3 *Math.PI/2);
            }
        });


        KeyStroke a = KeyStroke.getKeyStroke("A");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(a, "left");
        getActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayer4.moveX(-5);
                selectedPlayer4.setAngle(Math.PI);
            }
        });

        KeyStroke d = KeyStroke.getKeyStroke("D");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(d, "right");
        getActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayer4.moveX(5);
                selectedPlayer4.setAngle(0);
            }
        });


        KeyStroke s = KeyStroke.getKeyStroke("S");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(s, "down");
        getActionMap().put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("testS");
                selectedPlayer4.moveY(-5);
                selectedPlayer4.setAngle(Math.PI/2);
            }
        });

        KeyStroke Q = KeyStroke.getKeyStroke("Q");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(Q, "diagUpLeft");
        getActionMap().put("diagUpLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("testq");
                selectedPlayer4.moveY(5);
                selectedPlayer4.moveX(-5);
                selectedPlayer4.setAngle(-3 * Math.PI/4);
            }
        });

        KeyStroke Z = KeyStroke.getKeyStroke("Z");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(Z, "diagDownLeft");
        getActionMap().put("diagDownLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayer4.moveY(-5);
                selectedPlayer4.moveX(-5);
                selectedPlayer4.setAngle(3 * Math.PI/4);
            }
        });

        KeyStroke X = KeyStroke.getKeyStroke("X");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(X, "diagDownRight");
        getActionMap().put("diagDownRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayer4.moveY(-5);
                selectedPlayer4.moveX(5);
                selectedPlayer4.setAngle(Math.PI/4);
            }
        });

        KeyStroke E = KeyStroke.getKeyStroke("E");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(E, "diagUpRight");
        getActionMap().put("diagUpRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayer4.moveY(5);
                selectedPlayer4.moveX(5);
                selectedPlayer4.setAngle(-Math.PI/4);
            }
        });




        KeyStroke j = KeyStroke.getKeyStroke("J");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(j, "button1");
        getActionMap().put("button1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("PRESS Jjjjjjjjjjjj");
                selectedPlayer4.pressZeroButton();


            }
        });

        KeyStroke k = KeyStroke.getKeyStroke("K");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(k, "button2");
        getActionMap().put("button2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayer4.pressOneButton();
            }
        });

        KeyStroke l = KeyStroke.getKeyStroke("L");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(l, "button3");
        getActionMap().put("button3", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPlayer4.pressTwoButtonKeyBoard();

            }
        });











    }





}