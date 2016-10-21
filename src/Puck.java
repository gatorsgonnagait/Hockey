import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;

import java.awt.*;

/**
 * creates and draws a puck object on the rink
 * @author Aditi Datta
 * @author Evan Mesa
 * @version 1
 */
public class Puck extends MovingObject {

    int postTimer = 0;
    int hold = 0;
    boolean behindNet = false;

    double frictionCoefficient = .95;
    public Puck(int id, Point point, int speed, double angle, int radius, Color color) {
        super(id, point, speed, angle, radius, color);
        adjustment = 4;
        dummy_radius = radius + adjustment;

    }



    @Override
    public void setRadius(int radius) {
        super.setRadius(15);
    }


    @Override
    public void updateLocation() {
        positionCalculation(angle);
    }

    public void behindNet(){
        /*if( (location.x < leftGoalLine && location.y < topGoalPost )
            ||(location.x < leftGoalLine  && location.y > bottomGoalPost)
            ||(location.x <= leftGoalBack && location.y > topGoalPost && location.y < bottomGoalPost)
            ||(location.x > rightGoalLine && location.y < topGoalPost)
            ||(location.x > rightGoalLine  && location.y > bottomGoalPost)
            ||(location.x > rightGoalBack && location.y > topGoalPost && location.y < bottomGoalPost) ){

            behindNet = true;
        }
        else
            behindNet = false;*/

        if(location.x < leftGoalLine + dummy_radius && location.x > rightGoalLine - dummy_radius){
            behindNet = false;
        }
        else{
            behindNet = true;
        }


    }

    public boolean goalScoredLeft(){
        if(location.x > leftGoalBack
                && location.x + radius< leftGoalLine
                && location.y - dummy_radius > topGoalPost
                && location.y  + dummy_radius < bottomGoalPost ){


            setSpeed(0);
            return true;
        }
        return false;
    }

    public boolean goalScoredRight(){
        if(location.x < rightGoalBack
                && location.x - radius > rightGoalLine
                && location.y - dummy_radius > topGoalPost
                && location.y + dummy_radius < bottomGoalPost ){

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
        end.x = center.x + (int) Math.round((100*Math.cos(angle)));
        end.y = center.y + (int) Math.round((100*Math.sin(angle)));
        Line incident = new Line(center, end);
        Point tangentStart = new Point(end);
        Point tangentEnd = new Point();
        double angle1 = angle + ((Math.PI/180)*1);
        tangentEnd.x = center.x + (int) Math.round((100*Math.cos(angle1)));
        tangentEnd.y = center.y + (int) Math.round((100*Math.sin(angle1)));
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




        if(location.x <= leftBoundary + dummy_radius) {
            location.x = leftBoundary + dummy_radius;
            reflection(angle, 1);
        }
        else if ( location.x >= rightBoundary -  dummy_radius ){
            location.x = rightBoundary - dummy_radius;
            reflection(angle, 1);
        }
        else if(location.y <= topBoundary + dummy_radius ){
            location.y = topBoundary + dummy_radius;
            reflection(angle, 2);
        }
        else if (location.y >= bottomBoundary -  dummy_radius ){
            location.y = bottomBoundary - dummy_radius;
            reflection(angle, 2);
        }



        // Arcs and tangents
        if(location.x >= rightBoundary - 100 &&
                location.y >= bottomBoundary - 100){    // 4th corner
            double distance = Math.hypot(location.x-arcCenter4.x,
                    location.y-arcCenter4.y);
            if (distance >= 100- dummy_radius){
                double refAngle = reflectionAngleWithTangent(arcCenter4);
                reflection(refAngle,2);
            }
        }
        else if(location.y <= topBoundary+100 &&
                location.x <= leftBoundary+100){    // 1st corner
            double distance = Math.hypot(location.x-arcCenter1.x,
                    location.y-arcCenter1.y);
            if (distance >= 100- dummy_radius){
                double refAngle = reflectionAngleWithTangent(arcCenter1);
                reflection(refAngle,1);
            }
        }
        else if(location.x <= leftBoundary+100 &&
                location.y >= bottomBoundary - 100){    // 3rd corner
            double distance = Math.hypot(location.x-arcCenter3.x,
                    location.y-arcCenter3.y);
            if (distance >= 100- dummy_radius){
                double refAngle = reflectionAngleWithTangent(arcCenter3);
                reflection(refAngle,2);
            }
        }
        else if(location.y <= topBoundary+100 &&
                location.x >= rightBoundary - 100){     // 2nd Corner
            Point center = new Point(rightBoundary-100,topBoundary+100);
            double distance = Math.hypot(location.x-center.x, location.y-center.y);
            if (distance >= 100- dummy_radius){
                double refAngle = reflectionAngleWithTangent(arcCenter2);
                //setAngle(refAngle);
                reflection(refAngle,1);
            }
        }





    }

    public void hitGoals(){

        //inside goal posts


        if( (location.x < leftGoalLine && location.x > leftGoalBack && location.y < topGoalPost )
                || (location.x > rightGoalLine && location.x < rightGoalBack && location.y < topGoalPost) ){//top of left/right goal

            //System.out.println("top region");
            if(location.y >= topGoalPost - dummy_radius){
                location.y = topGoalPost - dummy_radius;
                //System.out.println("top");
                reflection(angle, 2);
            }
        }
        else if( (location.x < leftGoalLine  && location.x > leftGoalBack && location.y > bottomGoalPost )
                || (location.x > rightGoalLine  && location.x < rightGoalBack && location.y > bottomGoalPost ) ){//bottom of left/ right goal

            //System.out.println("bottom region");
            if(location.y <= bottomGoalPost + dummy_radius ){

                location.y = bottomGoalPost + dummy_radius;
                reflection(angle, 2);
            }
        }



        else if(location.y > topGoalPost  && location.y < bottomGoalPost ) {

            if( (location.x  < leftGoalLine && location.x > leftGoalBack ) || ( location.x > rightGoalLine && location.x < rightGoalBack) ) {

                if(location.y <= topGoalPost + 11) {
                    //System.out.println("noooo");
                    location.y = topGoalPost +11;
                    reflection(angle, 2);
                    //speed = speed/2;


                }
                else if(location.y >= bottomGoalPost - 11){
                    location.y = bottomGoalPost - 11;
                    reflection(angle, 2);
                    //speed = speed/2;
                }

            }
            else if(location.x < leftGoalBack ){//back of left goal
                    //System.out.println("back");
                if(location.x >= leftGoalBack - 11) {
                    location.x = leftGoalBack -11;
                    reflection(angle, 1);
                }
            }
            else if(location.x > rightGoalBack ){//back of right goal
                //System.out.println("back");
                if(location.x <= rightGoalBack + 11) {

                    location.x = rightGoalBack + 11;
                    reflection(angle, 1);
                }
            }


        }


        hitPosts();

    }




    public void hitPosts(){
        double distanceFromLeftTopPost = Math.sqrt(Math.pow((leftGoalLine - location.x), 2)
                + Math.pow((topGoalPost  - (location.y + adjustment)), 2));
        double distanceFromLeftBottomPost = Math.sqrt(Math.pow((leftGoalLine - location.x), 2)
                + Math.pow((bottomGoalPost - location.y), 2));

        double distanceFromRightTopPost = Math.sqrt(Math.pow((rightGoalLine - location.x), 2)
                + Math.pow((topGoalPost + adjustment - location.y), 2));
        double distanceFromRightBottomPost = Math.sqrt(Math.pow((rightGoalLine - location.x), 2)
                + Math.pow((bottomGoalPost - adjustment - location.y), 2));

        // hit the post
        if((distanceFromLeftBottomPost < radius
                || distanceFromLeftTopPost < radius
                || distanceFromRightBottomPost < radius
                || distanceFromRightTopPost < radius )
                && (location.x - dummy_radius > leftGoalLine || location.x + dummy_radius < rightGoalLine ) && postTimer > 5){

            reflection(angle, 1);
            // vertical reflection
            postTimer = 0;

        }
        postTimer++;


    }


    public void goalEdges(){

    }
}
