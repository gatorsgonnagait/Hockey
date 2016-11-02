import net.java.games.input.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
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

    int puckGrabArea = 16;
    double slideAngle= 0;
    int start = 0;
    LinkedList <Double> slideList = new LinkedList<>();
    double oldDistance = 0;
    int oldX = 0;
    int oldY = 0;
    double distance = 0;
    int bodyCheckFrames = 0;
    boolean bodyCheckFlag = false;
    int stealFrames = 0;
    boolean stealFlag = false;
    int slideLimit = 9;
    double oldAngle = 0;
    int buttonInputLimitFrames;
    int stickInputLimitFrames = 0;
    double tempSpeed;
    double tempAngle;
    double frictionCoefficient = .8;
    int startX;
    int startY;
    static int i = 0;

    Controller controller;
    int xAxisPercentage;
    int yAxisPercentage;
    String buttonIndex = "";
    double initAngle;

    MouseEvent e = null;
    boolean dragged = false;
    static boolean moved = false;
    Image img;

    double slapShotSpeed = Math.round(GameDriver.width/100);
    double wristShotSpeed = Math.round(GameDriver.width/160);
    double playerSpeed = Math.round(GameDriver.width/300);

    public Player(int id, Point point, int speed, double angle, int radius, Color color, Puck puck, Image img) {
        super(id, point, speed, angle, radius, color);
        this.teamColor = color;
        this.puck = puck;
        this.stick = new Stick(radius * 5/3);
        dummy_radius = stick.length + adjustment;
        startX = point.x;
        startY = point.y;
        initAngle = angle;
        buttonInputLimitFrames = 0;
        stickInputLimitFrames = 0;
        this.img = img;
    }


    public Player(int id, Point point, int speed, double angle, int radius, Color color, Puck puck) {
        super(id, point, speed, angle, radius, color);
        this.teamColor = color;
        this.puck = puck;
        this.stick = new Stick(radius * 5/3);
        dummy_radius = stick.length + adjustment;
        startX = point.x;
        startY = point.y;
        initAngle = angle;
        buttonInputLimitFrames = 0;
        stickInputLimitFrames = 0;
    }






    public void setPuck(Puck pk){
        puck = pk;
    }

    public void draw(Graphics2D g2d){
        stick.draw(g2d);
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
        g2d.fillOval(location.x-dummy_radius, location.y-dummy_radius, dummy_radius*2, dummy_radius*2);
        g2d.setColor(color);
        g2d.fillOval(location.x - radius, location.y - radius, radius*2, radius*2); // i think this is right
    }


    protected void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //g2d.drawImage(img, xLocation1, yLocation1, circleDiameter, circleDiameter, this);
    }

    public int getAxisValueInPercentage(float axisValue) {
        return (int)(((2 - (1 - axisValue)) * 100) / 2);
    }

    public void gamepad(){

        // Currently selected controller.
        //int selectedControllerIndex = window.getSelectedControllerName();
        //Controller controller = foundControllers.get(selectedControllerIndex);

        controller.poll();
        net.java.games.input.Component[] components = controller.getComponents();

        buttonInputLimitFrames++;
        stickInputLimitFrames++;

        for(int i=0; i < components.length; i++) {
            //System.out.println(components[i].getName());
            net.java.games.input.Component component = components[i];
            net.java.games.input.Component.Identifier componentIdentifier = component.getIdentifier();

            if (componentIdentifier.getName().matches("^[0-9]*$")) { // If the component identifier name contains only numbers, then this is a button.
                // Is button pressed?
                boolean isItPressed = true;
                if (component.getPollData() == 0.0f) {
                    isItPressed = false;
                }
                else{
                    buttonIndex = component.getIdentifier().toString();
                    buttonActions();
                    //System.out.println(buttonIndex);

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
    }


    public void buttonActions(){

        if(buttonInputLimitFrames >20) {

            if (buttonIndex.equals("0")) {
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
                location.y = GameDriver.topGoalPost - dummy_radius - 5;//left and right goal top
                break;
            case 6:
                location.y = GameDriver.bottomGoalPost + dummy_radius + 5;//left and right goal bottom
                break;
            case 7:
                location.x = GameDriver.leftGoalBack - dummy_radius - 5;//left  goal back
                break;
            case 8:
                location.x = GameDriver.rightGoalBack + dummy_radius + 5;//right goal back
                break;
            case 9:
                location.x = GameDriver.leftGoalLine + radius;//left goal front
                break;
            case 10:
                location.x = GameDriver.rightGoalLine - radius;//right goal front
                break;
            case 11://top left net corner
                location.x = GameDriver.leftGoalBack - dummy_radius;
                location.y = GameDriver.topGoalPost - dummy_radius;
                break;
            case 12://bottom left net corner
                location.x = GameDriver.leftGoalBack - dummy_radius;
                location.y = GameDriver.bottomGoalPost + dummy_radius;
                break;
            case 13://top right net corner
                location.x = GameDriver.rightGoalBack + dummy_radius;
                location.y = GameDriver.topGoalPost - dummy_radius;
                break;
            case 14://bottom right net corner
                location.x = GameDriver.rightGoalBack + dummy_radius;
                location.y = GameDriver.bottomGoalPost + dummy_radius;
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

        else if(location.x < GameDriver.leftGoalLine && location.y  < GameDriver.topGoalPost){//left goal top
            if(location.y >= GameDriver.topGoalPost - dummy_radius - 5 && location.x > GameDriver.leftGoalBack){
                hitWall = 5;
            }
        }
        else if(location.x  < GameDriver.leftGoalLine  && location.y > GameDriver.bottomGoalPost){//left goal bottom
            if(location.y <= GameDriver.bottomGoalPost + dummy_radius + 5 && location.x > GameDriver.leftGoalBack){
                hitWall = 6;
            }
        }
        else if(location.x < GameDriver.leftGoalBack && location.y > GameDriver.topGoalPost &&//left goal back
                location.y < GameDriver.bottomGoalPost){

            if(location.x >= GameDriver.leftGoalBack - dummy_radius - 5)
                hitWall = 7;
        }
        // Right Goal post
        else if(location.x > GameDriver.rightGoalLine && location.y < GameDriver.topGoalPost){//right goal top
            if(location.y >= GameDriver.topGoalPost - dummy_radius - 5 && location.x < GameDriver.rightGoalBack){
                hitWall = 5;
            }
        }
        else if(location.x > GameDriver.rightGoalLine  && location.y > GameDriver.bottomGoalPost){//right goal bottom
            if(location.y <= GameDriver.bottomGoalPost + dummy_radius + 5 && location.x < GameDriver.rightGoalBack){
                hitWall = 6;
            }
        }
        else if(location.x > GameDriver.rightGoalBack && location.y > GameDriver.topGoalPost &&//right goal back
                location.y < GameDriver.bottomGoalPost){
            if(location.x <= GameDriver.rightGoalBack + dummy_radius + 5 ) {
                hitWall = 8;
            }
        }

        else if(location.x > GameDriver.leftGoalLine && location.y > GameDriver.topGoalPost &&//left goal front
                location.y < GameDriver.bottomGoalPost  && location.x < GameDriver.rightGoalLine){
            if(location.x <= GameDriver.leftGoalLine + radius)
                hitWall = 9;
            else if(location.x >= GameDriver.rightGoalLine - radius) //right goal front
                hitWall = 10;
        }

        else if (location.y < GameDriver.topGoalPost && location.x < GameDriver.leftGoalBack) {//left goal top right corner
            if (location.y + dummy_radius + getSpeed() >= GameDriver.topGoalPost && location.x + dummy_radius + getSpeed()>= GameDriver.leftGoalBack) {
                hitWall = 11;
            }
        }
        else if (location.y > GameDriver.bottomGoalPost && location.x < GameDriver.leftGoalBack) {//left goal bottom  corner
            if (location.y - dummy_radius - getSpeed() <= GameDriver.bottomGoalPost && location.x + dummy_radius + getSpeed()>= GameDriver.leftGoalBack) {
                hitWall = 12;
            }
        }
        else if (location.y < GameDriver.topGoalPost && location.x > GameDriver.rightGoalBack) {//right goal top  corner
            if (location.y + dummy_radius + getSpeed() >= GameDriver.topGoalPost && location.x - dummy_radius - getSpeed()<= GameDriver.rightGoalBack) {
                hitWall = 13;
            }
        }
        else if (location.y > GameDriver.bottomGoalPost && location.x > GameDriver.rightGoalBack) {//right goal bottom  corner
            if (location.y - dummy_radius - getSpeed() <= GameDriver.bottomGoalPost && location.x - dummy_radius - getSpeed()<= GameDriver.rightGoalBack) {
                hitWall = 14;
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


    public void updateLocationCol() {
        collisionFrames++;
        if(collisionFrames >= collisionDuration){
            collisionFrames = 0;
            colliding = false;
        }
        positionCalculation(angle);
        stick.updateLocation();
    }

    @Override
    public void updateLocation() {/*
        double Y = puck.location.y - location.y;
        double X = puck.location.x - location.x;


        setAngle(Math.atan2(Y, X));

        location.x = (int) (location.x + getSpeed() * Math.cos(angle));
        location.y = (int) (location.y + getSpeed() * Math.sin(angle));
        stick.updateLocation();*/
    }

    public void slideList(double newAngle){
        if(slideList.size() > slideLimit){
            slideList.addLast(newAngle);
            slideAngle = slideList.pollFirst();
        }
        else{
            slideList.addLast(newAngle);
            slideAngle = newAngle;
        }
    }


    public void updateLocationController(double xAxisPercentage, double yAxisPercentage){
        //System.out.println("x " + xAxisPercentage + "y " +yAxisPercentage);
        int xNeutral = 49;
        int yNeutral = 49;

        double distance = getDistance(xNeutral, xAxisPercentage, yNeutral, yAxisPercentage);
        double controllerX = xAxisPercentage - xNeutral;
        double controllerY = yAxisPercentage - yNeutral;

        double newAngle = Math.atan2(controllerY, controllerX);

        stick.updateLocation();
        slideList(newAngle);

        //oldAngle = slideList.getLast();


        if( distance > 24 && distance < 38){// controller grace area. allows you to turn without moving
            //System.out.println(start);
            setAngle(newAngle);
            tempAngle = angle;
            if (start == 0) {
                start = 1;
                slideList.clear();
            }
        }
        else if(distance >= 38) {
            setAngle(newAngle);
            setSpeed(playerSpeed);
            if(start == 1) { //if was stopped before, dont use the slide angle to calculate position
                positionCalculation(newAngle);
                start = 0;
                slideAngle = newAngle;
            }
            else{
                positionCalculation(slideAngle);
                tempSpeed = speed;
            }
        }
        else if (distance < 9 ) {
            if (tempSpeed != 0) {
                setAngle(tempAngle);
                positionCalculation(tempAngle);
                if (Rink.i % 10  == 0) {//call friction method every 10 bodyCheckFrames
                    speed = setSpeedFriction(frictionCoefficient);

                }
            }
        }
    }

    public void updateLocation(double x, double y){

        distance = Math.sqrt(Math.pow((location.x - x), 2)
                + Math.pow((location.y - y), 2));

        double Y = y - location.y;
        double X = x - location.x;
        double newAngle = Math.atan2(Y, X);
        setAngle(newAngle);
        stick.updateLocation();

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
            if(Rink.i%10 == 0){//call friction method every 10 bodyCheckFrames
                speed = setSpeedFriction(frictionCoefficient);
            }
        }
        else {
            setSpeed(playerSpeed);
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
        int stickHoldingPointX = (int) Math.round((location.x + (radius ) * Math.cos(angle)));
        int stickHoldingPointY = (int) Math.round((location.y + (radius ) * Math.sin(angle)));

        double distance = getDistance(puck.location.x, stickHoldingPointX, puck.location.y, stickHoldingPointY);

        if (distance <= puckGrabArea && release != 1) {

            if(puck.hold == 0){

                puck.hold = id;
            }
            else if (stealFlag){
                puck.hold = id;
                stealFlag = false;
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
        int stickHoldingPointX = (int) Math.round((location.x + (radius ) * Math.cos(angle)));
        int stickHoldingPointY = (int) Math.round((location.y + (radius ) * Math.sin(angle)));

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
        puck.setAngle(angle);
        puck.setSpeed(wristShotSpeed);
        puck.updateLocation();
    }

    public void slapShot(){
        //Rink.possession = 0;
        release = 1;
        puck.hold = 0;
        puck.setAngle(angle);
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
        positionCalculation(angle);
        stick.updateLocation();
        if (bodyCheckFrames > 2 && bodyCheckFrames < 80) {//activate it between frame 2 and frame 80

            if(bodyCheckFrames < 10){ //only make it move forward for 10 bodyCheckFrames at this speed
                speed = 5;
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
            stealFlag = true;//starts steal
        }
    }

    public void pressOneButton(){
        if (puck.hold == id) {
            wristShot();
        }
    }

    public void pressTwoButton(){
        if (puck.hold == id) {
            slapShot();
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
            setAngle(Math.atan2(Y, X));
            //goalie frozen on its track
            location.x = (int) (location.x + 5 * Math.cos(angle));
            location.y = (int) (location.y + 5 * Math.sin(angle));
        }
        else if(puck.hold == 5 || puck.hold == 6){

            Y = GameDriver.horizontalMiddle - location.y;
            X = GameDriver.verticalCenter - location.x;
            setAngle(Math.atan2(Y, X));
            stick.updateLocation();

            double puckY = GameDriver.horizontalMiddle - puck.location.y;
            double puckX = GameDriver.verticalCenter - puck.location.x;


            puck.setAngle(Math.atan2(puckY, puckX));
            puck.setSpeed(4);
            //slapShot();
            puck.location.x = (int) (puck.location.x + puck.speed * Math.cos(puck.angle));
            puck.location.y = (int) (puck.location.y + puck.speed * Math.sin(puck.angle));

        }


    }






    protected class Stick {

        //Player player;
        int x;
        int y;
        int a;
        int b;
        int length;

        public Stick(int length) {
            x = location.x;
            y = location.y;
            this.length = length;
            a = (int) (x + length * Math.cos(getAngle()));
            b = (int) (y + length * Math.sin(getAngle()));
        }

        public void updateLocation() {
            x = location.x;
            y = location.y;
            a = (int)(x + length * Math.cos(angle));
            b = (int)(y + length * Math.sin(angle));
        }

        public void draw(Graphics2D g2d) {
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(Color.black);
            g2d.drawLine(x, y, a, b);//from center of player cicle to edge of stick
        }
    }
}
