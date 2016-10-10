/**
 * creates and drwas a puck object on the rink
 * @author Evan Mesa
 * @author Aditi Datta
 * @version 2
 */

import javax.swing.*;
import java.awt.*;

public abstract class MovingObject extends JComponent {


    Point   location;
    double     speed;
    double  angle;
    int     radius;
    int     adjustment;
    Color   color;
    int     id;
    int     mass = 10;
    boolean colliding = false;
    int     dummy_radius;
    int     collisionFrames = 0;
    int     collisionDuration = 10;

    static int topBoundary = 100;
    static int bottomBoundary = 450;
    static int leftBoundary = 100;
    static int rightBoundary = 900;

    static int leftGoalLine = 190;
    static int rightGoalLine = 810;
    static int topGoalPost = 235;
    static int bottomGoalPost = 315;
    static int horizontalMiddle = 275;
    static int verticalCenter = 500;//ss
    static int leftGoalBack = 160;
    static int rightGoalBack = 840;

    static int goalLength = 80;
    static int goalWidth = 30;

    Point arcCenter1 = new Point(leftBoundary+100,topBoundary+100);
    Point arcCenter2 = new Point(rightBoundary-100,topBoundary+100);
    Point arcCenter3 = new Point(leftBoundary+100,bottomBoundary-100);
    Point arcCenter4 = new Point(rightBoundary - 100, bottomBoundary - 100);

    int hitWall = 0;
    boolean hitWalls = false;


    public MovingObject(int id, Point point, double speed, double angle, int radius, Color color) {
        this.id       = id;
        this.location = point;
        this.speed    = speed;
        this.angle    = angle;
        this.radius   = radius;
        this.color    = color;
        adjustment    = 0;
        dummy_radius  = radius + adjustment;
        //this.mass     = mass;
    }
    //test
    public Point getPoint() {
        return location;
    }

    public void setLocation(Point point) {
        this.location = point;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setRadius(int radius){
        this.radius = radius;
    }

    public double setSpeedFriction(double coefficient){
        double tempSpeed = speed * coefficient;
        return tempSpeed;
    }




    //test
    public abstract void hitWalls();
    public abstract void updateLocation();

    public void draw(Graphics2D g2d){
        g2d.setColor(color);
        g2d.fillOval(location.x-radius, location.y-radius, radius*2, radius*2);
    }

    public void positionCalculation(double angle){
        location.x = (int) Math.round((location.x + speed * Math.cos(angle)));
        location.y = (int) Math.round((location.y + speed * Math.sin(angle)));
    }

    public double getDistance(int x1, double x2, int y1, double y2){
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    public double getDistance(int x1, int x2, int y1, int y2){
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }


}
