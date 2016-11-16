import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;

import java.awt.*;
import java.util.ArrayList;

/**
 * creates and draws a puck object on the rink
 * @author Aditi Datta
 * @author Evan Mesa
 * @version 1
 */
public class Puck extends MovingObject {

    int postTimer = 0;
    int hold = 0;


    double frictionCoefficient = .95;
    public Puck(int id, Point point, int speed, double angle, int radius, Color color) {
        super(id, point, speed, angle, radius, color);
        adjustment = radius/2;
        dummy_radius = radius + adjustment;

    }

    public void interpolationLine(){

        double X = (double) location.x;
        double Y = (double) location.y;

        for(int i = 0; i < 6; i++){
            speed = speed * .95;
            X = X + speed * Math.cos(angle);
            Y = Y + speed * Math.sin(angle);

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



    @Override
    public void setRadius(int radius) {
        super.setRadius(radius);
    }


    @Override
    public void updateLocation() {
        positionCalculation(angle);
    }


    public boolean goalScoredLeft(){
        if(location.x > GameDriver.leftGoalBack
                && location.x + radius< GameDriver.leftGoalLine
                && location.y - dummy_radius > GameDriver.topGoalPost
                && location.y  + dummy_radius < GameDriver.bottomGoalPost ){

            setSpeed(0);
            return true;
        }
        return false;
    }

    public boolean goalScoredRight(){
        if(location.x < GameDriver.rightGoalBack
                && location.x - radius > GameDriver.rightGoalLine
                && location.y - dummy_radius > GameDriver.topGoalPost
                && location.y + dummy_radius < GameDriver.bottomGoalPost ){

            setSpeed(0);
            return true;
        }
        return false;
    }


    public void reflection(double angle, int n){

        double reflectAngle =0;
        if(n == 1){
            reflectAngle = (-1) * angle + Math.PI; //verstical walls
        }
        else if(n == 2){
            reflectAngle = (-1)*angle;//horizontal angle
        }
        setAngle(reflectAngle);
    }

    double reflectionAngleWithTangent(Point center){
        double angle = angleWithArcCenter(center.x, center.y);
        Point end = new Point();
        end.x = center.x + (int) Math.round((GameDriver.rinkWidth/8 * Math.cos(angle)));
        end.y = center.y + (int) Math.round((GameDriver.rinkWidth/8 * Math.sin(angle)));
        Line incident = new Line(center, end);
        Point tangentStart = new Point(end);
        Point tangentEnd = new Point();
        double angle1 = angle + ((Math.PI/180)*1);
        tangentEnd.x = center.x + (int) Math.round((GameDriver.rinkWidth/8 * Math.cos(angle1)));
        tangentEnd.y = center.y + (int) Math.round((GameDriver.rinkWidth/8 * Math.sin(angle1)));
        Line tangent = new Line(tangentStart, tangentEnd);
        double tangentTheta = Math.atan2(tangent.slopeY, tangent.slopeX);
        double lineTheta = Math.atan2(incident.slopeY, incident.slopeX);
        double incidenceAngle = (Math.PI/180)*90 - (lineTheta - tangentTheta);
        return ((Math.PI/180)*90) + (incidenceAngle*2);
    }

    double angleWithArcCenter(int cx, int cy){
        double theta = Math.atan2((location.y-cy), (location.x-cx));
        return theta;
    }

    @Override
    public void hitWalls(){




        if(location.x <= GameDriver.leftBoundary + dummy_radius) {
            location.x = GameDriver.leftBoundary + dummy_radius;
            reflection(angle, 1);
        }
        else if ( location.x >= GameDriver.rightBoundary -  dummy_radius ){
            location.x = GameDriver.rightBoundary - dummy_radius;
            reflection(angle, 1);
        }
        else if(location.y <= GameDriver.topBoundary + dummy_radius ){
            location.y = GameDriver.topBoundary + dummy_radius;
            reflection(angle, 2);
        }
        else if (location.y >= GameDriver.bottomBoundary -  dummy_radius ){
            location.y = GameDriver.bottomBoundary - dummy_radius;
            reflection(angle, 2);
        }



        // Arcs and tangents
        if(location.x >= GameDriver.rightBoundary - GameDriver.rinkWidth/8 &&
                location.y >= GameDriver.bottomBoundary - GameDriver.rinkWidth/8){    // 4th corner
            double distance = Math.hypot(location.x-arcCenter4.x,
                    location.y-arcCenter4.y);
            if (distance >= GameDriver.rinkWidth/8 - dummy_radius){
                double refAngle = reflectionAngleWithTangent(arcCenter4);
                reflection(refAngle,2);
            }
        }
        else if(location.y <= GameDriver.topBoundary + GameDriver.rinkWidth/8  &&
                location.x <= GameDriver.leftBoundary + GameDriver.rinkWidth/8){    // 1st corner
            double distance = Math.hypot(location.x-arcCenter1.x,
                    location.y-arcCenter1.y);
            if (distance >= GameDriver.rinkWidth/8 - dummy_radius){
                double refAngle = reflectionAngleWithTangent(arcCenter1);
                reflection(refAngle,1);
            }
        }
        else if(location.x <= GameDriver.leftBoundary + GameDriver.rinkWidth/8 &&
                location.y >= GameDriver.bottomBoundary - GameDriver.rinkWidth/8){    // 3rd corner
            double distance = Math.hypot(location.x-arcCenter3.x,
                    location.y-arcCenter3.y);
            if (distance >=  GameDriver.rinkWidth/8 - dummy_radius){
                double refAngle = reflectionAngleWithTangent(arcCenter3);
                reflection(refAngle,2);
            }
        }
        else if(location.y <= GameDriver.topBoundary + GameDriver.rinkWidth/8 &&
                location.x >= GameDriver.rightBoundary - GameDriver.rinkWidth/8){     // 2nd Corner
            Point center = new Point(GameDriver.rightBoundary - GameDriver.rinkWidth/8,GameDriver.topBoundary + GameDriver.rinkWidth/8);
            double distance = Math.hypot(location.x-center.x, location.y-center.y);
            if (distance >= GameDriver.rinkWidth/8- dummy_radius){
                double refAngle = reflectionAngleWithTangent(arcCenter2);
                //setAngle(refAngle);
                reflection(refAngle,1);
            }
        }





    }

    public void hitGoals(){

        //inside goal posts


        if( (location.x < GameDriver.leftGoalLine && location.x > GameDriver.leftGoalBack && location.y < GameDriver.topGoalPost )
                || (location.x > GameDriver.rightGoalLine && location.x < GameDriver.rightGoalBack && location.y < GameDriver.topGoalPost) ){//top of left/right goal

            //System.out.println("top region");
            if(location.y >= GameDriver.topGoalPost - dummy_radius){
                location.y = GameDriver.topGoalPost - dummy_radius;
                //System.out.println("top");
                reflection(angle, 2);
            }
        }
        else if( location.x < GameDriver.leftGoalBack && location.y < GameDriver.topGoalPost){
            //System.out.println("uppder back corner of the left net");

            if(location.x >= GameDriver.leftGoalBack - dummy_radius && location.y >= GameDriver.topGoalPost - dummy_radius){
                //System.out.println("reflect off top left corner");
                location.x = GameDriver.leftGoalBack - dummy_radius;
                location.y = GameDriver.topGoalPost - dummy_radius;
                reflection(angle, 2);
            }
        }
        else if( location.x > GameDriver.rightGoalBack && location.y < GameDriver.topGoalPost){
            //System.out.println("uppder back corner of the right net");

            if(location.x <= GameDriver.rightGoalBack + dummy_radius && location.y >= GameDriver.topGoalPost - dummy_radius){
                //System.out.println("reflect off top right back corner");
                location.x = GameDriver.rightGoalBack + dummy_radius;
                location.y = GameDriver.topGoalPost - dummy_radius;
                reflection(angle, 2);
            }
        }

        else if( (location.x < GameDriver.leftGoalLine  && location.x > GameDriver.leftGoalBack && location.y > GameDriver.bottomGoalPost )
                || (location.x > GameDriver.rightGoalLine  && location.x < GameDriver.rightGoalBack && location.y > GameDriver.bottomGoalPost ) ){//bottom of left/ right goal

            //System.out.println("bottom region");
            if(location.y <= GameDriver.bottomGoalPost + dummy_radius ){

                location.y = GameDriver.bottomGoalPost + dummy_radius;
                reflection(angle, 2);
            }
        }
        else if( location.x < GameDriver.leftGoalBack && location.y > GameDriver.bottomGoalPost){
            //System.out.println("lower back corner of the left net");

            if(location.x >= GameDriver.leftGoalBack - dummy_radius && location.y <= GameDriver.bottomGoalPost + dummy_radius){
                //System.out.println("reflect off bottom left corner");
                location.x = GameDriver.leftGoalBack - dummy_radius;
                location.y = GameDriver.bottomGoalPost + dummy_radius;
                reflection(angle, 2);
            }
        }
        else if( location.x > GameDriver.rightGoalBack && location.y > GameDriver.bottomGoalPost){
            //System.out.println("lower back corner of the right net");

            if(location.x <= GameDriver.rightGoalBack - dummy_radius && location.y <= GameDriver.bottomGoalPost + dummy_radius){
                //System.out.println("reflect off bottom right corner");
                location.x = GameDriver.rightGoalBack + dummy_radius;
                location.y = GameDriver.bottomGoalPost + dummy_radius;
                reflection(angle, 2);
            }
        }



        //inside the goals
        else if(location.y > GameDriver.topGoalPost  && location.y < GameDriver.bottomGoalPost ) {

            if( (location.x  < GameDriver.leftGoalLine && location.x > GameDriver.leftGoalBack ) || ( location.x > GameDriver.rightGoalLine && location.x < GameDriver.rightGoalBack) ) {

                if(location.y <= GameDriver.topGoalPost + bigBuffer) {
                    //System.out.println("noooo");
                    location.y = GameDriver.topGoalPost + bigBuffer;
                    reflection(angle, 2);
                    //speed = speed/2;


                }
                else if(location.y >= GameDriver.bottomGoalPost - bigBuffer){
                    location.y = GameDriver.bottomGoalPost - bigBuffer;
                    reflection(angle, 2);
                    //speed = speed/2;
                }

            }
            else if(location.x < GameDriver.leftGoalBack ){//back of left goal
                    //System.out.println("back");
                if(location.x >= GameDriver.leftGoalBack - bigBuffer) {
                    location.x = GameDriver.leftGoalBack - bigBuffer;
                    reflection(angle, 1);
                }
            }
            else if(location.x > GameDriver.rightGoalBack ){//back of right goal
                //System.out.println("back");
                if(location.x <= GameDriver.rightGoalBack + bigBuffer) {

                    location.x = GameDriver.rightGoalBack + bigBuffer;
                    reflection(angle, 1);
                }
            }


        }




        hitPosts();

    }




    public void hitPosts(){
        double distanceFromLeftTopPost = Math.sqrt(Math.pow((GameDriver.leftGoalLine - location.x), 2)
                + Math.pow((GameDriver.topGoalPost  - (location.y + adjustment)), 2));
        double distanceFromLeftBottomPost = Math.sqrt(Math.pow((GameDriver.leftGoalLine - location.x), 2)
                + Math.pow((GameDriver.bottomGoalPost - location.y), 2));

        double distanceFromRightTopPost = Math.sqrt(Math.pow((GameDriver.rightGoalLine - location.x), 2)
                + Math.pow((GameDriver.topGoalPost + adjustment - location.y), 2));
        double distanceFromRightBottomPost = Math.sqrt(Math.pow((GameDriver.rightGoalLine - location.x), 2)
                + Math.pow((GameDriver.bottomGoalPost - adjustment - location.y), 2));

        // hit the post
        if((distanceFromLeftBottomPost < radius
                || distanceFromLeftTopPost < radius
                || distanceFromRightBottomPost < radius
                || distanceFromRightTopPost < radius )
                && (location.x - dummy_radius > GameDriver.leftGoalLine || location.x + dummy_radius < GameDriver.rightGoalLine ) && postTimer > 5){

            reflection(angle, 1);
            // vertical reflection
            postTimer = 0;

        }
        postTimer++;


    }

}
