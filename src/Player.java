import net.java.games.input.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * creates a player object
 * @author Aditi Datta
 * @author Evan Mesa
 * @version 1
 */

public class Player extends MovingObject {

    Color teamColor;
    Puck puck;
    Stick stick;
    int release = 0;
    double possessPuck = 0;

    double puckGrabArea;
    double slideAngle= 0;
    int start = 0;
    LinkedList <Double> slideList = new LinkedList<>();
    int bodyCheckFrames = 0;
    int slapShotFrames = 0;
    boolean bodyCheckFlag = false;
    boolean slapShotFlag = false;
    int stealFrames = 0;
    boolean stealFlag = false;
    int slideLimit = 9;
    int buttonInputLimitFrames;
    int stickInputLimitFrames = 0;
    double tempSpeed;
    double tempAngleFacing;
    double startX;
    double startY;
    static int i = 0;

    Controller controller;
    int xAxisPercentage;
    int yAxisPercentage;
    String buttonIndex = "";
    String previousButton = "";
    double initAngle;

    MouseEvent e = null;
    boolean dragged = false;
    static boolean moved = false;
    Image img;

    double xNeutral = 49;
    double yNeutral = 49;
    double distance;
    double controllerX;
    double controllerY;

    boolean stopped = true;

    int accelerationFramesMouse = 0;

    double slapShotSpeed = Math.round(GameDriver.rinkWidth/100 );
    double wristShotSpeed = Math.round(GameDriver.rinkWidth/156);
    double startingSpeed = radius/10;
    double playerSpeed;
    double playerSpeedMouse = Math.round(GameDriver.rinkWidth/270) ;
    double playerSpeedLimit;
    double driftAngle;
    double prevAngle;
    double angleFacing;
    double newAngle;
    double accelerationMouse;
    boolean goingBackwards = false;
    int angleCount = 0;




    public Player(int id, PointDouble point, double speed, double angle, double radius, Color color, double mass, Puck puck, Image img) {
        super(id, point, speed, angle, radius, color, mass);
        this.teamColor = color;
        this.puck = puck;
        this.stick = new Stick(radius * 3/2);
        dummy_radius = stick.length + adjustment;
        //System.out.println(dummy_radius);
        startX = point.x;
        startY = point.y;
        initAngle = angle;
        buttonInputLimitFrames = 0;
        stickInputLimitFrames = 0;

        this.img = img;
        frictionCoefficient = .99;
        puckGrabArea = stick.length * 3/4;
        angleFacing = angle;
        prevAngle = angleFacing;
        tempAngleFacing = angle;
        location.prevX = location.x;
        location.prevY = location.y;
    }


    public Player(int id, PointDouble point, int speed, double angle, int radius, Color color, double mass, Puck puck) {
        super(id, point, speed, angle, radius, color, mass);
        this.teamColor = color;
        this.puck = puck;
        this.stick = new Stick(radius * 3/2);
        dummy_radius = stick.length + adjustment;
        startX = point.x;
        startY = point.y;
        initAngle = angle;
        buttonInputLimitFrames = 0;
        stickInputLimitFrames = 0;

        frictionCoefficient = .99;
        puckGrabArea = stick.length * 3/4;
        angleFacing = angle;
        prevAngle = angleFacing;
        tempAngleFacing = angle;
        location.prevX = location.x;
        location.prevY = location.y;
    }


    public void setPuck(Puck pk){
        puck = pk;
    }

    public void setAngle(double angle){
        this.angle = angle;
        angleFacing = angle;
    }

    public void draw(Graphics2D g2d){
        stick.draw(g2d);
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
        g2d.fillOval((int)Math.round(location.x-dummy_radius), (int)Math.round(location.y-dummy_radius), (int)Math.round(dummy_radius*2), (int)Math.round(dummy_radius*2));
        g2d.setColor(color);
        g2d.fillOval((int)Math.round(location.x - radius), (int)Math.round(location.y - radius), (int)Math.round(radius*2), (int)Math.round(radius*2)); // i think this is right
    }


    protected void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //g2d.drawImage(img, xLocation1, yLocation1, circleDiameter, circleDiameter, this);
    }

    public int getAxisValueInPercentage(float axisValue) {
        return (int)(((2 - (1 - axisValue)) * 100) / 2);
    }

    public void gamepad(){
        //System.out.println(previousButton);
        boolean set = false;
        // Currently selected controller.
        //int selectedControllerIndex = window.getSelectedControllerName();
        //Controller controller = foundControllers.get(selectedControllerIndex);

        controller.poll();
        net.java.games.input.Component[] components = controller.getComponents();

        buttonInputLimitFrames++;
        //preventHoldingButtons = buttonInputLimitFrames;
        stickInputLimitFrames++;

        for(int i=0; i < components.length; i++) {
            //System.out.println(components[i].getName());
            net.java.games.input.Component component = components[i];
            net.java.games.input.Component.Identifier componentIdentifier = component.getIdentifier();
            //previousButton = component.getIdentifier().toString();
            //System.out.println(previousButton);



            if (componentIdentifier.getName().matches("^[0-9]*$")) { // If the component identifier name contains only numbers, then this is a button.
                // Is button pressed?
                boolean isItPressed = true;
                if (component.getPollData() == 0.0f) {
                    isItPressed = false;
                }
                else{
                    //System.out.println(buttonInputLimitFrames);
                    set = true;
                    buttonIndex = component.getIdentifier().toString();
                    if (!buttonIndex.equals(previousButton)) {
                        //System.out.println("test previous button");
                        buttonActions();
                        previousButton = buttonIndex;
                    }
                }
                continue;
            }

            if (component.isAnalog()) {
                float axisValue = component.getPollData();
                //System.out.println(axisValue);
                int axisValueInPercentage = getAxisValueInPercentage(axisValue);
                // X axis
                if (componentIdentifier == net.java.games.input.Component.Identifier.Axis.X) {
                    xAxisPercentage = axisValueInPercentage;
                    //System.out.println("X " + xAxisPercentage);
                    continue; // Go to next component.
                }
                // Y axis
                if (componentIdentifier == net.java.games.input.Component.Identifier.Axis.Y) {
                    yAxisPercentage = axisValueInPercentage;
                    // System.out.println("Y " + yAxisPercentage);
                    continue; // Go to next component.
                }
            }
            //if button index is not null, wait a half a second il next input
        }
        if(!set) {
            previousButton = "";
        }
    }


    public void buttonActions(){

        if(buttonInputLimitFrames >40) {

            if (buttonIndex.equals("0")) {
                //System.out.println("steal");
                pressZeroButton();

            } else if (buttonIndex.equals("1") || buttonIndex.equals("3")) {
                pressOneButton();

            } else if (buttonIndex.equals("2")) {
                pressTwoButton();
            }

            if (buttonIndex != "") {
                buttonInputLimitFrames = 0;
            }
        }

    }


    public void rubWalls(){


        switch (hitWall){
            case 1:
                location.y = GameDriver.topBoundary + dummy_radius;
                break;
            case 2:
                location.y = GameDriver.bottomBoundary - dummy_radius;
                break;
            case 3:
                location.x = GameDriver.leftBoundary + dummy_radius;
                break;
            case 4:
                location.x = GameDriver.rightBoundary - dummy_radius;
                break;
            case 5:
                location.y = GameDriver.topGoalPost - dummy_radius - smallBuffer;//left and right goal top
                break;
            case 6:
                location.y = GameDriver.bottomGoalPost + dummy_radius + smallBuffer;//left and right goal bottom
                break;
            case 7:
                location.x = GameDriver.leftGoalBack - dummy_radius - smallBuffer;//left  goal back
                break;
            case 8:
                location.x = GameDriver.rightGoalBack + dummy_radius + smallBuffer;//right goal back
                break;
            case 9:
                location.x = GameDriver.leftGoalLine + radius;//left goal front
                break;
            case 10:
                location.x = GameDriver.rightGoalLine - radius;//right goal front
                break;
            case 11://top left net corner
                location.x = GameDriver.leftGoalBack - dummy_radius ;
                //location.y = GameDriver.topGoalPost - dummy_radius ;
                break;
            case 12://bottom left net corner
                location.x = GameDriver.leftGoalBack - dummy_radius;
                //location.y = GameDriver.bottomGoalPost + dummy_radius;
                break;
            case 13://top right net corner
                location.x = GameDriver.rightGoalBack + dummy_radius;
                //location.y = GameDriver.topGoalPost - dummy_radius;
                break;
            case 14://bottom right net corner
                location.x = GameDriver.rightGoalBack + dummy_radius;
                //location.y = GameDriver.bottomGoalPost + dummy_radius;
                break;

            case 15://left goal front corners
                location.x = GameDriver.leftGoalLine + dummy_radius;
                break;
            case 16:
                location.x = GameDriver.rightGoalLine - dummy_radius;
                break;
        }


    }

    double angleWithArcCenter(int cx, int cy){
        double theta = Math.atan2((location.y-cy), (location.x-cx));
        return theta;
    }

    public void hitWalls(){


        if(location.y <= GameDriver.topBoundary + dummy_radius || stick.b <= GameDriver.topBoundary){
            hitWall = 1;
        }
        else if(location.y >= GameDriver.bottomBoundary - dummy_radius || stick.b >= GameDriver.bottomBoundary){
            hitWall = 2;
        }
        else if(location.x <= GameDriver.leftBoundary + dummy_radius || stick.a <= GameDriver.leftBoundary){
            hitWall = 3;
        }
        else if(location.x >= GameDriver.rightBoundary - dummy_radius || stick.a >= GameDriver.rightBoundary){
            hitWall = 4;
        }

        else if(location.x < GameDriver.leftGoalLine && location.x > GameDriver.leftGoalBack && location.y < GameDriver.topGoalPost){//left goal top

            if(location.y >= GameDriver.topGoalPost - dummy_radius - smallBuffer){
                hitWall = 5;
            }
        }

        else if(location.x < GameDriver.leftGoalLine  && location.x > GameDriver.leftGoalBack && location.y > GameDriver.bottomGoalPost){//left goal bottom
            if(location.y <= GameDriver.bottomGoalPost + dummy_radius + smallBuffer){
                hitWall = 6;
            }
        }
        else if(location.x < GameDriver.leftGoalBack && location.y > GameDriver.topGoalPost &&//left goal back
                location.y < GameDriver.bottomGoalPost){
            if(location.x >= GameDriver.leftGoalBack - dummy_radius - smallBuffer)
                hitWall = 7;
        }



        // Right Goal post
        else if(location.x > GameDriver.rightGoalLine && location.x < GameDriver.rightGoalBack && location.y < GameDriver.topGoalPost){//right goal top
            if(location.y >= GameDriver.topGoalPost - dummy_radius - smallBuffer ){
                hitWall = 5;
            }
        }
        else if(location.x > GameDriver.rightGoalLine  && location.x < GameDriver.rightGoalBack && location.y > GameDriver.bottomGoalPost){//right goal bottom
            if(location.y <= GameDriver.bottomGoalPost + dummy_radius + smallBuffer){
                hitWall = 6;
            }
        }
        else if(location.x > GameDriver.rightGoalBack && location.y > GameDriver.topGoalPost &&//right goal back
                location.y < GameDriver.bottomGoalPost){
            if(location.x <= GameDriver.rightGoalBack + dummy_radius + smallBuffer) {
                hitWall = 8;
            }
        }

        else if(location.x > GameDriver.leftGoalLine && location.y > GameDriver.topGoalPost &&//front of gials
                location.y < GameDriver.bottomGoalPost  && location.x < GameDriver.rightGoalLine){
            if(location.x <= GameDriver.leftGoalLine + radius)
                hitWall = 9;
            else if(location.x >= GameDriver.rightGoalLine - radius) //right goal front
                hitWall = 10;
        }

        else if (location.y < GameDriver.topGoalPost && location.x < GameDriver.leftGoalBack) {//left goal top back corner
            if (location.y + dummy_radius >= GameDriver.topGoalPost && location.x + dummy_radius >= GameDriver.leftGoalBack) {
                hitWall = 11;
            }
        }
        else if (location.y > GameDriver.bottomGoalPost && location.x < GameDriver.leftGoalBack) {//left goal bottom  corner
            if (location.y - dummy_radius <= GameDriver.bottomGoalPost && location.x + dummy_radius >= GameDriver.leftGoalBack) {
                hitWall = 12;
            }
        }
        else if (location.y < GameDriver.topGoalPost && location.x > GameDriver.rightGoalBack) {//right goal top back corner
            if (location.y + dummy_radius >= GameDriver.topGoalPost && location.x - dummy_radius <= GameDriver.rightGoalBack) {
                hitWall = 13;
            }
        }
        else if (location.y > GameDriver.bottomGoalPost && location.x > GameDriver.rightGoalBack) {//right goal bottom back corner
            if (location.y - dummy_radius  <= GameDriver.bottomGoalPost && location.x - dummy_radius <= GameDriver.rightGoalBack) {
                hitWall = 14;
            }
        }
        else if (location.y < GameDriver.topGoalPost && location.x > GameDriver.leftGoalLine && location.x < GameDriver.verticalCenter) {//left goal top front corner
            if (location.y + dummy_radius  >= GameDriver.topGoalPost && location.x - dummy_radius <= GameDriver.leftGoalLine) {
                hitWall = 15;
            }
        }
        else if (location.y > GameDriver.bottomGoalPost && location.x > GameDriver.leftGoalLine && location.x < GameDriver.verticalCenter) {//left goal bottom corner
            if (location.y - dummy_radius  <= GameDriver.bottomGoalPost && location.x - dummy_radius <= GameDriver.leftGoalLine) {
                hitWall = 15;
            }
        }

        else if (location.y < GameDriver.topGoalPost && location.x < GameDriver.rightGoalLine && location.x > GameDriver.verticalCenter) {//left goal top back

            if (location.y + dummy_radius >= GameDriver.topGoalPost && location.x + dummy_radius >= GameDriver.rightGoalLine) {
                hitWall = 16;
            }
        }

        else if (location.y > GameDriver.bottomGoalPost && location.x < GameDriver.rightGoalLine && location.x > GameDriver.verticalCenter) {//left goal bottom  corner
            if (location.y - dummy_radius <= GameDriver.bottomGoalPost && location.x + dummy_radius >= GameDriver.rightGoalLine) {
                hitWall = 16;
            }
        }





        if(location.x >= GameDriver.rightBoundary - GameDriver.rinkWidth/8 &&
                location.y >= GameDriver.bottomBoundary - GameDriver.rinkWidth/8){
            Point center = new Point(GameDriver.rightBoundary - GameDriver.rinkWidth/8, GameDriver.bottomBoundary - GameDriver.rinkWidth/8);
            double distance = Math.hypot(location.x-center.x, location.y-center.y);
            if (distance >= GameDriver.rinkWidth/8 - dummy_radius){
                double angle = angleWithArcCenter(center.x, center.y);
                location.x = center.x + (int) ((GameDriver.rinkWidth/8-dummy_radius)*Math.cos(angle));
                location.y = center.y + (int) (( GameDriver.rinkWidth/8 -dummy_radius)*Math.sin(angle));
            }

        }
        else if(location.y <= GameDriver.topBoundary + GameDriver.rinkWidth/8 &&
                location.x <= GameDriver.leftBoundary + GameDriver.rinkWidth/8){
            Point center = new Point(GameDriver.leftBoundary + GameDriver.rinkWidth/8,GameDriver.topBoundary+GameDriver.rinkWidth/8);
            double distance = Math.hypot(location.x-center.x, location.y-center.y);
            if (distance >= GameDriver.rinkWidth/8 - dummy_radius){
                double angle = angleWithArcCenter(center.x, center.y);
                location.x = center.x + (int) ((GameDriver.rinkWidth/8 - dummy_radius)*Math.cos(angle));
                location.y = center.y + (int) ((GameDriver.rinkWidth/8 - dummy_radius)*Math.sin(angle));
            }
        }
        else if(location.x <= GameDriver.leftBoundary + GameDriver.rinkWidth/8 &&
                location.y >= GameDriver.bottomBoundary - GameDriver.rinkWidth/8){
            Point center = new Point(GameDriver.leftBoundary+100,GameDriver.bottomBoundary - GameDriver.rinkWidth/8);
            double distance = Math.hypot(location.x-center.x, location.y-center.y);
            if (distance >= GameDriver.rinkWidth/8 - dummy_radius){
                double angle = angleWithArcCenter(center.x, center.y);
                location.x = center.x + (int) ((GameDriver.rinkWidth/8 - dummy_radius)*Math.cos(angle));
                location.y = center.y + (int) ((GameDriver.rinkWidth/8 - dummy_radius)*Math.sin(angle));
            }
        }
        else if(location.y <= GameDriver.topBoundary + GameDriver.rinkWidth/8 &&
                location.x >= GameDriver.rightBoundary - GameDriver.rinkWidth/8){
            Point center = new Point(GameDriver.rightBoundary - GameDriver.rinkWidth/8,GameDriver.topBoundary + GameDriver.rinkWidth/8);
            double distance = Math.hypot(location.x-center.x, location.y-center.y);
            if (distance >= GameDriver.rinkWidth/8 - dummy_radius){
                double angle = angleWithArcCenter(center.x, center.y);
                location.x = center.x + (int) ((GameDriver.rinkWidth/8 - dummy_radius)*Math.cos(angle));
                location.y = center.y + (int) ((GameDriver.rinkWidth/8 - dummy_radius)*Math.sin(angle));
            }
        }
    }

    public void updateLocationCol(){
        /*double distance = Math.hypot(object2.location.x - object1.location.x,
                object2.location.y - object1.location.y);

        if(distance > object1.speed || distance > object2.speed){
            colliding = false;
        }
        else{
            positionCalculation(angle);
        }       */
        super.updateLocationCol();
        stick.updateLocation();
    }

    /*
    public void updateLocationCol() {
        collisionFrames++;
        if(collisionFrames >= collisionDuration){
            collisionFrames = 0;
            colliding = false;
        }
        else {

            positionCalculation(angle);

        }
        stick.updateLocationMouse();
    }*/

    @Override
    public void updateLocation() {/*
        double Y = puck.location.y - location.y;
        double X = puck.location.x - location.x;


        setAngle(Math.atan2(Y, X));

        location.x = (int) (location.x + getSpeed() * Math.cos(angle));
        location.y = (int) (location.y + getSpeed() * Math.sin(angle));
        stick.updateLocationMouse();*/
    }



    public double controlAngle(double xAxisPercentage, double yAxisPercentage){
        controllerX = xAxisPercentage - xNeutral;
        controllerY = yAxisPercentage - yNeutral;
        return Math.atan2(controllerY, controllerX);
    }

    public double mouseAngle(double mouseX, double mouseY){
        double Y = mouseY - location.y;
        double X = mouseX - location.x;
        return  Math.atan2(Y, X);
    }

    public boolean isClockwise(double angle, double angleFacing){
        double angleFacingTemp = angleFacing;
        double angleTemp = angle;
        boolean clockwise;
        double diff = 0;
        if(angleFacingTemp > angleTemp){
            diff = angleFacingTemp - angleTemp;
            if(diff > Math.PI){
                clockwise = false;
            }
            else {
                clockwise= true;
            }

        }
        else{
            diff = angleTemp - angleFacingTemp;
            if(diff > Math.PI){
                clockwise = true;
            }
            else{
                clockwise = false;
            }
        }

        return clockwise;

    }



    public double angularMomentum(double angle, double angleFacing){

        double angleFacingTemp = angleFacing;
        double angleTemp = angle;

        if(Math.abs(angle) > 2*Math.PI ){
            angleTemp = angle % 2*Math.PI;
        }

        if(Math.abs(angleFacing) > 2*Math.PI ){
            //angleFacingTemp = angleFacing % 2*Math.PI;
        }

        if(angleTemp < 0){
            angleTemp = angleTemp + 2* Math.PI;
        }
        if(angleFacingTemp < 0){
            angleFacingTemp = angleFacingTemp+ 2* Math.PI;
        }

        if (  angleFacingTemp < angleTemp - Math.PI  ){
            angleFacingTemp = angleFacingTemp + 2 * Math.PI;
        }
        else if( angleFacingTemp > angleTemp + Math.PI && angleFacingTemp < 2*Math.PI ){
            angleFacingTemp = angleFacingTemp - 2 * Math.PI;
        }


        if(angleFacingTemp != angleTemp) {
            if (angleFacingTemp > angleTemp) {
                //System.out.println("clockwise");
                if(angleFacingTemp - angleTemp < Math.PI / 30){
                    angle = angle + (angleFacingTemp-angleTemp);
                }
                else {
                    angle = angleTemp + Math.PI / 30;
                }
            } else if (angleFacingTemp < angleTemp) {
                //System.out.println("counter");
                if(angleTemp - angleFacingTemp  < Math.PI / 30) {
                    angle = angle - (angleTemp - angleFacingTemp);
                }
                else{
                    angle = angleTemp - Math.PI / 30;
                }
            }
        }
        /*
        System.out.println(angleFacing + " angleFacing");
        System.out.println(angle + " angle");
        System.out.println();
        */
        return angle;
    }

    public void angularMomentum3(double xAxisPercentage, double yAxisPercentage){
        distance = getDistance(xNeutral, xAxisPercentage, yNeutral, yAxisPercentage);
        double AM = mass * distance * speed;
        double theta = (AM/ (distance * distance * mass));
        //System.out.println( theta + " anglular velocity test");
        //System.out.println((theta * distance * distance) + " displacement");
    }

    public double angularMomentum4(double angle, double angleFacing){

        if (  angleFacing < angle - Math.PI  ){//clockwise
            angleFacing = angleFacing + 2 * Math.PI;
        }
        else if( angleFacing > angle + Math.PI && angleFacing < 2*Math.PI ){//counter
            angleFacing = angleFacing - 2 * Math.PI;
        }

        if(angleFacing != angle) {
            if (angleFacing > angle) {
                //System.out.println("clockwise")
                angleCount++;

            } else if (angleFacing < angle) {
                angleCount--;
            }
        }

        //boolean clockwise = isClockwise(angle, angleFacing);

        if(goingBackwards){
            acceleration = acceleration * (-1);
        }
        if(goingBackwards && speed < .01){
            speed = 0;
            goingBackwards = false;
            angle = angleFacing;

            //System.out.println();
            //System.out.println("going backwards");
            //System.out.println();
        }


        if(accelerationFrames >= 10 && accelerationFrames % 10 == 0){
            //System.out.println(angleCount);
            double changeInAngle = Math.abs(angleFacing - angle);
            //System.out.println(omega);
            if(changeInAngle > Math.PI ){
                changeInAngle = 2 * Math.PI - changeInAngle;
            }
            //System.out.println();
            //System.out.println(changeInAngle + " difference");

            if(changeInAngle > 2.64 && changeInAngle < 3.64){
                acceleration = acceleration * (-1);
                goingBackwards = true;
            }
            else {
                changeInAngle = changeInAngle / 4;

                //acceleration = - change^2 cos(theta i) -  change^2 sin(theta j)

                //angle = (tempAngleFacing + omega);
                if (angleCount > 0) {
                    angle = angle + changeInAngle;
                }
                else if (angleCount < 0) {
                    angle = angle - changeInAngle;
                    //System.out.println("SMALLER");
                }

                double acceleration2 = - Math.pow(changeInAngle, 2) * Math.cos(changeInAngle*4) - Math.pow(changeInAngle, 2) * Math.sin(changeInAngle*4);
                //System.out.println(acceleration2 + " test drift");
                //System.out.println(acceleration + " actual ");
                angleCount = 0;
                //tempAngleFacing = angleFacing;
                //acceleration = acceleration+acceleration2;
            }
        }
        return angle;
    }


    public void updateLocationController(double xAxisPercentage, double yAxisPercentage){

        angleFacing = controlAngle(xAxisPercentage, yAxisPercentage);
        distance = getDistance(xNeutral, xAxisPercentage, yNeutral, yAxisPercentage);
        stick.updateLocation();
        accelerationFrames++;

        double tether;

        if(distance >= 10) {
            acceleration = .03;
            if(distance < 20){
                playerSpeedLimit = GameDriver.rinkWidth/600;
                //acceleration = .18;
                tether = radius * 5/4;
                //System.out.println("speed 0 " + speed);
            }
            else if(distance >= 20 && distance < 30){
                playerSpeedLimit = GameDriver.rinkWidth/525;
                //acceleration = .21;
                tether = radius * 6/4;
                //System.out.println("speed 1 " + speed);
            }
            else if(distance >= 30 && distance < 40){
                playerSpeedLimit = GameDriver.rinkWidth/450;
                //acceleration = .24;
                tether = radius * 6/4;
                //System.out.println("speed 2 " + speed);
            }
            else if(distance >= 40 && distance < 48){
                playerSpeedLimit = GameDriver.rinkWidth/375;
                //acceleration = .27;
                tether = radius * 7/4;
                //System.out.println("speed 3 "+ speed);
            }
            else if(distance >= 48){
                playerSpeedLimit = GameDriver.rinkWidth/270;
                //acceleration = .3;
                tether = radius * 2;
                //System.out.println("speed 4 "+ speed);
            }

            angle = angleAdjustment(angle);
            angleFacing = angleAdjustment(angleFacing);
            angle = angularMomentum4(angle, angleFacing);





            if(speed > playerSpeedLimit){
                speed = playerSpeedLimit;
            }
        }
        else if (distance < 10  ) {
            acceleration = 0;
        }

        positionCalculation(angle);

    }
    /*
    public void updateLocationMouse(double mouseX, double mouseY){

        double newAngle = mouseAngle(mouseX, mouseY);

        distance = Math.sqrt(Math.pow((location.x - mouseX), 2)
                + Math.pow((location.y - mouseY), 2));

        angleFacing = newAngle;
        stick.updateLocationMouse();

        if(slideList.size() > slideLimit){
            slideList.addLast(newAngle);
            slideAngle = slideList.pollFirst();
        }
        else{
            slideList.addLast(newAngle);
            slideAngle = newAngle;
        }

        if( distance < 80){// controller grace area. allows you to turn without moving
            start = 1;
            slideList.clear();
        }
        else {

            pointList.clear();
            setAngle(newAngle);
            setSpeed(playerSpeedMouse);
            if(start == 1) {
                positionCalculation(newAngle);
                start = 0;
                slideAngle = newAngle;
            }
            else{
                positionCalculation(slideAngle);
            }
        }
    }
    */

    public void updateLocationMouse(double mouseX, double mouseY){

        //double angleFacingLast = newAngle;
        angleFacing = mouseAngle(mouseX, mouseY);
        distance = Math.sqrt(Math.pow((location.x - mouseX), 2) + Math.pow((location.y - mouseY), 2));

        //angleFacing = newAngle;
        stick.updateLocation();
        //accelerationFrames++;

        if (distance >= 80){

            acceleration = .1;

            //angleFacing = newAngle;
            if(speed == 0){
                //System.out.println(startingSpeed +" starting speed");
                setSpeed(startingSpeed);
            }


            angle = angularMomentum(angle, angleFacing);


            if(speed >  playerSpeedMouse){
                speed =  playerSpeedMouse;
            }


        }
        else if (distance < 80  ) {
            acceleration = 0;

            //if (Rink.i % 1  == 0) {//call friction method every 10 bodyCheckFrames
            /*
            speed = setSpeedFriction(frictionCoefficient);
            //}

            if(speed <= .1 ){
                speed = 0;
                accelerationFrames = 0;
            }*/
        }
        positionCalculation(angle);
    }

    public void updateLocationNull(){
        stick.updateLocation();
        positionCalculation(angle);
    }

    public void updateLocationKeys(int x, int y){
        location.x =  (location.x + x);
        location.y =  (location.y + y);
        stick.updateLocation();
    }

    public void moveX(int num){
        updateLocationKeys(num, 0);
    }
    public void moveY(int num){
        updateLocationKeys(0, (-1)*num);
    }


    public void stickHandling() {// of its close itll turn on the hold method

        //Puck puck = player.puck;
        double stickHoldingPointX = (location.x + (5/3 * radius ) * Math.cos(angleFacing));
        double stickHoldingPointY = (location.y + (5/3 * radius ) * Math.sin(angleFacing));

        double distance = getDistance(puck.location.x, stickHoldingPointX, puck.location.y, stickHoldingPointY);//distance from puck
        possessPuck = distance - puckGrabArea;
        if (distance <= puckGrabArea && release != 1) {//

            if(puck.hold == 0){
                puck.hold = id;
            }
            else if (stealFlag){
                puck.hold = id;
                stealFlag = false;
                stealFrames = 0;
            }

            //Rink.possession = id;
        }
        else if (distance > puckGrabArea && release == 1) {
            //System.out.println("distance");
            release = 0;
            //Rink.possession = 0;
        }

    }

    public void holdPuck() {
        //possession = id;
        double stickHoldingPointX = Math.round((location.x + (radius ) * Math.cos(angleFacing)));
        double stickHoldingPointY = Math.round((location.y + (radius ) * Math.sin(angleFacing)));

        puck.location.x = stickHoldingPointX;
        puck.location.y = stickHoldingPointY;
        puck.speed = speed;
        puck.angle = angle;
    }

    public void wristShot(){
        //Rink.possession = 0;
        release = 1;
        puck.hold = 0;
        //Rink.possession = 0;
        puck.setAngle(angleFacing);
        puck.setSpeed(wristShotSpeed);
        puck.updateLocation();
    }

    public void slapShot(){
        //Rink.possession = 0;
        if(slapShotFrames == 0){
            driftAngle = angleFacing;
        }

        positionCalculation(driftAngle);
        /*
        if (Rink.i % 3  == 0) {//call friction method every 10 bodyCheckFrames
            speed = setSpeedFriction(frictionCoefficient);
        }*/

        slapShotFrames++;

        double newAngle = controlAngle(xAxisPercentage, yAxisPercentage);
        angleFacing = newAngle;

        /*
        if(pointList.size()==0) {
            interpolationLine(driftAngle);
        }*/

        stopObject();
        stick.updateLocation();

        if(slapShotFrames > 50) {
            release = 1;
            puck.hold = 0;
            puck.setAngle(angleFacing);
            puck.setSpeed(slapShotSpeed);
            puck.updateLocation();
            slapShotFrames = 0;
            slapShotFlag = false;

            /*if(pointList.size() != 0)
                pointList.clear();
            updateLocationMouse();*/
        }
    }

    public void mouseSlapShot(){
        release = 1;
        puck.hold = 0;
        //Rink.possession = 0;
        puck.setAngle(angleFacing);
        puck.setSpeed(slapShotSpeed);
        puck.updateLocation();
    }



    public void pass(Puck puck){
        puck.setAngle(getAngle());
        puck.setSpeed(5);
    }

    public void bodyCheckStart(){
        if(bodyCheckFrames >= 80 || bodyCheckFrames == 0) {
            bodyCheckFlag = true;
            bodyCheckFrames = 0;
        }
    }

    public void bodyCheck(){

        bodyCheckFrames++;
        positionCalculation(angleFacing);
        stick.updateLocation();
        if (bodyCheckFrames > 2 && bodyCheckFrames < 80) {//activate it between frame 2 and frame 80

            if(bodyCheckFrames < 10){ //only make it move forward for 10 bodyCheckFrames at this speed
                speed = GameDriver.rinkWidth/160;
            }
            else if (bodyCheckFrames >= 10) {//after 10 bodyCheckFrames, it has to rest for an amount
                setSpeed(0);
                slideList.clear();
            }
        }
        else if(bodyCheckFrames >= 80 ){
            bodyCheckFlag = false;
            bodyCheckFrames = 0;
        }
    }


    public void steal(){
        stealFrames++;
        //System.out.println(stealFrames);
        if (stealFrames > 10) {//steal flag only last for 10 active frames
            stealFlag = false;
            stealFrames = 0;
        }
    }

    public void pressZeroButton(){
        if(puck.hold == id){
            wristShot();
        }
        else{
            if(stealFrames == 0) {
                stealFlag = true;//starts steal
            }
            else if(stealFrames > 50) {
                //System.out.println("past frame limit");
                stealFrames = 0;
                stealFlag = true;//starts steal
            }
        }
    }

    public void pressOneButton(){
        if (puck.hold == id) {
            wristShot();
        }
    }

    public void pressTwoButton(){
        if (puck.hold == id) {
            slapShotFlag = true;
            //slapShot();
        }
        else {
            bodyCheckStart();
        }
    }
    public void pressTwoButtonKeyBoard(){
        if (puck.hold == id) {
            mouseSlapShot();
        }
        else {
            bodyCheckStart();
        }
    }



    public void afterGoal() {


        stick.updateLocation();


        double Y;
        double X;

        if(release == 0 && puck.hold == 0) {
            stickHandling();
            holdPuck();

            Y = puck.location.y - location.y;
            X = puck.location.x - location.x;
            angleFacing = Math.atan2(Y, X);
            setSpeed(wristShotSpeed);
            //goalie frozen on its track
            positionCalculation(angleFacing);
        }
        else if(puck.hold == 5 || puck.hold == 6){

            Y = GameDriver.horizontalMiddle - location.y;
            X = GameDriver.verticalCenter - location.x;
            angleFacing = Math.atan2(Y, X);
            stick.updateLocation();
            double puckY = GameDriver.horizontalMiddle - puck.location.y;
            double puckX = GameDriver.verticalCenter - puck.location.x;
            speed = 0;

            puck.setAngle(Math.atan2(puckY, puckX));
            puck.setSpeed(wristShotSpeed);
            //slapShot();
            puck.positionCalculation(puck.angle);
            //puck.location.x = (int) (puck.location.x + puck.speed * Math.cos(puck.angle));
            //puck.location.y = (int) (puck.location.y + puck.speed * Math.sin(puck.angle));

        }
    }





    protected class Stick {

        //Player player;
        double x;
        double y;
        double a;
        double b;
        double length;
        //double prevX;
        //double prevY;

        public Stick(double length) {
            x = location.x;
            y = location.y;
            this.length = length;
            a = x + length * Math.cos(getAngle());
            b = y + length * Math.sin(getAngle());

        }

        public void updateLocation() {
            x = location.x;
            y = location.y;
            a = x + length * Math.cos(angleFacing);
            b = y + length * Math.sin(angleFacing);
        }

        public void draw(Graphics2D g2d) {
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(Color.black);
            g2d.drawLine((int)Math.round(x), (int)Math.round(y), (int)Math.round(a), (int)Math.round(b));//from center of player cicle to edge of stick
        }
    }
}
