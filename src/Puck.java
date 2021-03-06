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
    boolean backOfNet = false;
    boolean release = true;



    public Puck(int id, PointDouble point, int speed, double angle, int radius, Color color, double mass) {
        super(id, point, speed, angle, radius, color, mass);
        adjustment = radius/2;
        dummy_radius = radius + adjustment;
        frictionCoefficient = .985;
        acceleration = 0;
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
                && location.y  > GameDriver.topGoalPost
                && location.y  < GameDriver.bottomGoalPost ){


            if (location.x >= GameDriver.rightGoalBack - radius && !backOfNet) {
                location.x = GameDriver.rightGoalBack - radius;
                angle = reflection(angle, 1);
                //System.out.println(" hit the back");
                backOfNet = true;
            }
            else if ( location.y <= GameDriver.topGoalPost + radius){
                location.y = GameDriver.topGoalPost + radius;
                angle = reflection(angle, 2);
            }
            else if ( location.y >=GameDriver.bottomGoalPost - radius){
                location.y = GameDriver.bottomGoalPost - radius;
                angle = reflection(angle, 2);
            }

            if(backOfNet){
                //System.out.println(hold + " hold");
                //System.out.println(speed + " speed");
                //System.out.println(GameDriver.goalWidth + " goal width");
                setSpeed(speed * .3);
                if(speed < .5){
                    speed = 0;
                    backOfNet = false;
                    return true;
                }
            }
        }
        return false;
    }



    double reflectionAngleWithTangent(PointDouble center){
        double angle = angleWithArcCenter(center.x, center.y);
        PointDouble end = new PointDouble();
        end.x = center.x + (GameDriver.rinkWidth/8 * Math.cos(angle));
        end.y = center.y + (GameDriver.rinkWidth/8 * Math.sin(angle));
        Line incident = new Line(center, end);
        PointDouble tangentStart = new PointDouble(end);
        PointDouble tangentEnd = new PointDouble();
        double angle1 = angle + (Math.PI/180);
        tangentEnd.x = center.x + GameDriver.rinkWidth/8 * Math.cos(angle1);
        tangentEnd.y = center.y + GameDriver.rinkWidth/8 * Math.sin(angle1);
        Line tangent = new Line(tangentStart, tangentEnd);
        double tangentTheta = Math.atan2(tangent.slopeDY, tangent.slopeDX);
        double lineTheta = Math.atan2(incident.slopeDY, incident.slopeDX);
        double incidenceAngle = (Math.PI/180)*90 - (lineTheta - tangentTheta);
        return ((Math.PI/180)*90) + (incidenceAngle*2);
    }

    double cornerReflection(double theta, int num){
        angle = angleAdjustment(angle);
        theta = angleAdjustment(theta);
        //System.out.println(theta + " theta");
        int startingAngle = 0;
        double cornerAngle = 0;
        System.out.println(num + "num");
        if(num == 1){
            startingAngle = 180;
            cornerAngle = Math.PI/2; // pi/2 to Pi
        }
        else if(num == 2){
            startingAngle = 270;
            cornerAngle = 0;// 0 to Pi/2
        }
        else if(num == 3){
            startingAngle = 90;
            cornerAngle = 0; // 0 to pi/2
        }
        else if(num == 4){
            startingAngle = 0;
            cornerAngle = Math.PI/2; // pi/2 to pi
        }
        int d = 0;
        for( int i = startingAngle; i < startingAngle + 90; i += 18){
            cornerAngle = cornerAngle + Math.PI/10; // every 18 degrees

            if(theta > i * Math.PI / 180  && theta < (i + 18) * Math.PI / 180){ //wbich of the 5 segments the puck is in
                System.out.println(d + "d");
                System.out.println(cornerAngle + " corner angle");
                /*
                double adjustment = Math.PI - cornerAngle;
                angle = angleAdjustment(angle);
                angle = angle + adjustment;
                angle = reflection(angle, 2) - adjustment;
                */
                angle =  (2 *cornerAngle) - angle;
                break;
            }
            d ++;

        }

        return angle;
    }

    double angleWithArcCenter(double cx, double cy){
        double theta = Math.atan2((location.y-cy), (location.x-cx));
        return theta;
    }

    @Override
    public void hitWalls(){


        if(location.x <= GameDriver.leftBoundary + dummy_radius) {
            location.x = GameDriver.leftBoundary + dummy_radius;
            angle = reflection(angle, 1);
        }
        else if ( location.x >= GameDriver.rightBoundary -  dummy_radius ){
            location.x = GameDriver.rightBoundary - dummy_radius;
            angle = reflection(angle, 1);
        }
        else if(location.y <= GameDriver.topBoundary + dummy_radius ){
            location.y = GameDriver.topBoundary + dummy_radius;
            angle = reflection(angle, 2);
        }
        else if (location.y >= GameDriver.bottomBoundary -  dummy_radius ){
            location.y = GameDriver.bottomBoundary - dummy_radius;
            angle = reflection(angle, 2);
        }



        // Arcs and tangents

        if(location.y <= GameDriver.topBoundary + GameDriver.rinkWidth/8  &&// top left
                location.x <= GameDriver.leftBoundary + GameDriver.rinkWidth/8){    // 1st corner
            double distance = Math.hypot(location.x-arcCenter1.x,
                    location.y-arcCenter1.y);
            if (distance >= GameDriver.rinkWidth/8 - dummy_radius){
                //double refAngle = reflectionAngleWithTangent(arcCenter1);
                //angle = reflection(refAngle,1);
                System.out.println(" in corner 1");
                double theta = angleWithArcCenter(arcCenter1.x, arcCenter1.y);
                angle = cornerReflection(theta, 1);
            }
        }
        else if(location.y <= GameDriver.topBoundary + GameDriver.rinkWidth/8 &&//top right
                location.x >= GameDriver.rightBoundary - GameDriver.rinkWidth/8){     // 2nd Corner
            Point center = new Point(GameDriver.rightBoundary - GameDriver.rinkWidth/8,GameDriver.topBoundary + GameDriver.rinkWidth/8);
            double distance = Math.hypot(location.x-center.x, location.y-center.y);
            if (distance >= GameDriver.rinkWidth/8- dummy_radius){
                //double refAngle = reflectionAngleWithTangent(arcCenter2);
                //angle = reflection(refAngle,1);
                System.out.println(" in corner 2");
                double theta = angleWithArcCenter(arcCenter2.x, arcCenter2.y);
                angle = cornerReflection(theta, 2);
            }
        }
        else if(location.x <= GameDriver.leftBoundary + GameDriver.rinkWidth/8 &&//bottom left
                location.y >= GameDriver.bottomBoundary - GameDriver.rinkWidth/8){    // 3rd corner
            double distance = Math.hypot(location.x-arcCenter3.x,
                    location.y-arcCenter3.y);
            if (distance >=  GameDriver.rinkWidth/8 - dummy_radius){
                //double refAngle = reflectionAngleWithTangent(arcCenter3);
                //angle = reflection(refAngle,2);
                System.out.println(" in corner 3");
                double theta = angleWithArcCenter(arcCenter3.x, arcCenter3.y);
                angle = cornerReflection(theta, 3);
            }
        }
        else if(location.x >= GameDriver.rightBoundary - GameDriver.rinkWidth/8 &&// bottom right
                location.y >= GameDriver.bottomBoundary - GameDriver.rinkWidth/8){    // 4th corner
            double distance = Math.hypot(location.x-arcCenter4.x, location.y-arcCenter4.y);
            if (distance >= GameDriver.rinkWidth/8 - dummy_radius){
                //double refAngle = reflectionAngleWithTangent(arcCenter4);
                //angle = reflection(refAngle,2);
                System.out.println(" in corner 4");
                double theta = angleWithArcCenter(arcCenter4.x, arcCenter4.y);
                angle = cornerReflection(theta, 4);
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
                angle = reflection(angle, 2);
            }
        }
        else if( location.x < GameDriver.leftGoalBack && location.y < GameDriver.topGoalPost){
            //System.out.println("uppder back corner of the left net");

            if(location.x >= GameDriver.leftGoalBack - dummy_radius && location.y >= GameDriver.topGoalPost - dummy_radius){
                //System.out.println("reflect off top left corner");
                location.x = GameDriver.leftGoalBack - dummy_radius;
                location.y = GameDriver.topGoalPost - dummy_radius;
                angle = reflection(angle, 2);
            }
        }
        else if( location.x > GameDriver.rightGoalBack && location.y < GameDriver.topGoalPost){
            //System.out.println("uppder back corner of the right net");

            if(location.x <= GameDriver.rightGoalBack + dummy_radius && location.y >= GameDriver.topGoalPost - dummy_radius){
                //System.out.println("reflect off top right back corner");
                location.x = GameDriver.rightGoalBack + dummy_radius;
                location.y = GameDriver.topGoalPost - dummy_radius;
                angle = reflection(angle, 2);
            }
        }

        else if( (location.x < GameDriver.leftGoalLine  && location.x > GameDriver.leftGoalBack && location.y > GameDriver.bottomGoalPost )
                || (location.x > GameDriver.rightGoalLine  && location.x < GameDriver.rightGoalBack && location.y > GameDriver.bottomGoalPost ) ){//bottom of left/ right goal

            //System.out.println("bottom region");
            if(location.y <= GameDriver.bottomGoalPost + dummy_radius ){

                location.y = GameDriver.bottomGoalPost + dummy_radius;
                angle = reflection(angle, 2);
            }
        }
        else if( location.x < GameDriver.leftGoalBack && location.y > GameDriver.bottomGoalPost){
            //System.out.println("lower back corner of the left net");

            if(location.x >= GameDriver.leftGoalBack - dummy_radius && location.y <= GameDriver.bottomGoalPost + dummy_radius){
                //System.out.println("reflect off bottom left corner");
                location.x = GameDriver.leftGoalBack - dummy_radius;
                location.y = GameDriver.bottomGoalPost + dummy_radius;
                angle = reflection(angle, 2);
            }
        }
        else if( location.x > GameDriver.rightGoalBack && location.y > GameDriver.bottomGoalPost){
            //System.out.println("lower back corner of the right net");

            if(location.x <= GameDriver.rightGoalBack + dummy_radius && location.y <= GameDriver.bottomGoalPost + dummy_radius){
                //System.out.println("reflect off bottom right corner");
                location.x = GameDriver.rightGoalBack + dummy_radius;
                location.y = GameDriver.bottomGoalPost + dummy_radius;
                angle = reflection(angle, 2);
            }
        }



        //inside the goals
        else if(location.y > GameDriver.topGoalPost  && location.y < GameDriver.bottomGoalPost ) {
            /*
            if( (location.x  < GameDriver.leftGoalLine && location.x > GameDriver.leftGoalBack ) || ( location.x > GameDriver.rightGoalLine && location.x < GameDriver.rightGoalBack) ) {

                if(location.y <= GameDriver.topGoalPost + smallBuffer) {
                    //System.out.println("noooo");
                    location.y = GameDriver.topGoalPost + smallBuffer;
                    reflection(angle, 2);
                    //speed = speed/2;


                }
                else if(location.y >= GameDriver.bottomGoalPost - bigBuffer){
                    location.y = GameDriver.bottomGoalPost - bigBuffer;
                    reflection(angle, 2);
                    //speed = speed/2;
                }

            }
            */

            if(location.x < GameDriver.leftGoalBack ){//back of left goal
                    //System.out.println("back");
                if(location.x >= GameDriver.leftGoalBack - bigBuffer) {
                    location.x = GameDriver.leftGoalBack - bigBuffer;
                    angle = reflection(angle, 1);
                }
            }
            else if(location.x > GameDriver.rightGoalBack ){//back of right goal
                //System.out.println("back");
                if(location.x <= GameDriver.rightGoalBack + bigBuffer) {

                    location.x = GameDriver.rightGoalBack + bigBuffer;
                    angle = reflection(angle, 1);
                }
            }


        }




        hitPosts();

    }




    public void hitPosts(){
        double distanceFromLeftTopPost = Math.sqrt(Math.pow((GameDriver.leftGoalLine - location.x), 2)
                + Math.pow((GameDriver.topGoalPost  - location.y), 2));
        double distanceFromLeftBottomPost = Math.sqrt(Math.pow((GameDriver.leftGoalLine - location.x), 2)
                + Math.pow((GameDriver.bottomGoalPost - location.y), 2));

        double distanceFromRightTopPost = Math.sqrt(Math.pow((GameDriver.rightGoalLine - location.x), 2)
                + Math.pow((GameDriver.topGoalPost - location.y), 2));
        double distanceFromRightBottomPost = Math.sqrt(Math.pow((GameDriver.rightGoalLine - location.x), 2)
                + Math.pow((GameDriver.bottomGoalPost - location.y), 2));

        // hit the post
        if((distanceFromLeftBottomPost < radius
                || distanceFromLeftTopPost < radius
                || distanceFromRightBottomPost < radius
                || distanceFromRightTopPost < radius)
                && (location.x - radius > GameDriver.leftGoalLine || location.x + radius < GameDriver.rightGoalLine ) && postTimer > 5){

            angle = reflection(angle, 1);
            // vertical reflection
            postTimer = 0;

        }
        postTimer++;


    }

}
