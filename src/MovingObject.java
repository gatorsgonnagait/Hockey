/**
 * creates and drwas a puck object on the rink
 * @author Evan Mesa
 * @author Aditi Datta
 * @version 2
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    int bigBuffer = Math.round(GameDriver.rinkWidth/80);
    int smallBuffer = Math.round(GameDriver.rinkWidth/160);
    ArrayList<double[]> pointList = new ArrayList<>();

    double frictionCoefficient;
    /*

    //static int topBoundary = 100;
    //static int bottomBoundary = 450;
    //static int leftBoundary = 100;
    //static int rightBoundary = 900;

    static int leftGoalLine = 190;
    static int rightGoalLine = 810;
    static int topGoalPost = 235;
    static int bottomGoalPost = 315;
    static int leftGoalBack = 160;
    static int rightGoalBack = 840;

    static int goalLength = 80;
    static int goalWidth = 30;

    Point arcCenter1 = new Point(leftBoundary+100,topBoundary+100);
    Point arcCenter2 = new Point(rightBoundary-100,topBoundary+100);
    Point arcCenter3 = new Point(leftBoundary+100,bottomBoundary-100);
    Point arcCenter4 = new Point(rightBoundary - 100, bottomBoundary - 100);
    */
    Point arcCenter1 = new Point(GameDriver.leftBoundary + GameDriver.rinkWidth/8, GameDriver.topBoundary + GameDriver.rinkWidth/8);
    Point arcCenter2 = new Point(GameDriver.rightBoundary - GameDriver.rinkWidth/8, GameDriver.topBoundary + GameDriver.rinkWidth/8);
    Point arcCenter3 = new Point(GameDriver.leftBoundary + GameDriver.rinkWidth/8, GameDriver.bottomBoundary - GameDriver.rinkWidth/8);
    Point arcCenter4 = new Point(GameDriver.rightBoundary - GameDriver.rinkWidth/8, GameDriver.bottomBoundary - GameDriver.rinkWidth/8);


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
    /* fiz weird movement when the  objects slow down, interpolation
    when the speed gets under 1.0, calculate the line that the object would be going through
    find out end point on the line that would be the moving objects path until the speed hit zero
    calculate a few points on the line and move the object alone that line til it hits zero
     */



    public double getDistance(int x1, double x2, int y1, double y2){
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    public static double getDistance(int x1, int x2, int y1, int y2){
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    public void interpolationLine(double driftAngle){

        double X = (double) location.x;
        double Y = (double) location.y;

        for(int i = 0; i < 6; i++){
            speed = speed * frictionCoefficient;
            X = X + speed * Math.cos(driftAngle);
            Y = Y + speed * Math.sin(driftAngle);

            double[] arr = new double[]{X, Y};
            pointList.add(arr);
        }
    }

    int k = 0;
    public void stopObject(){
        if(k < pointList.size()) {

            double pixelX = pointList.get(k)[0] % (int) pointList.get(k)[0];
            double pixelY = pointList.get(k)[1] % (int) pointList.get(k)[1];

            if( (pixelX < .4 || pixelX > .6) && (pixelY < .4 || pixelY > .6) ) {

                location.x = (int) Math.round(pointList.get(k)[0]);
                location.y = (int) Math.round(pointList.get(k)[1]);
            }
            k++;
        }
        else{
            pointList.clear();
            k = 0;
        }
    }


}
