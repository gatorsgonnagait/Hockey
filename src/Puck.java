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



    public boolean goalScoredLeft(){
        if(location.x > leftGoalBack
                && location.x < leftGoalLine
                && location.y  - dummy_radius > topGoalPost
                && location.y  + dummy_radius< bottomGoalPost ){
            System.out.println("test");
            setSpeed(0);

            //Player.release = 0;

            return true;
        }
        return false;
    }

    public boolean goalScoredRight(){
        if(location.x < rightGoalBack
                && location.x > rightGoalLine
                && location.y - dummy_radius > topGoalPost
                && location.y + dummy_radius < bottomGoalPost ){
            setSpeed(0);

            return true;
        }
        return false;
    }


    public void reflection(double angle, int n){
        //System.out.println(getPoint());

        double reflectAngle =0;

        if(n == 1){

            reflectAngle = (-1) * angle + Math.PI;
        }
        else if(n == 2){

            reflectAngle = (-1)*angle;
            //System.out.println("reflect angle " + reflectAngle);
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


        double distanceFromLeftTopPost = Math.sqrt(Math.pow((leftGoalLine - location.x), 2)
                + Math.pow((topGoalPost + adjustment - location.y), 2));
        double distanceFromLeftBottomPost = Math.sqrt(Math.pow((leftGoalLine - location.x), 2)
                + Math.pow((bottomGoalPost - adjustment - location.y), 2));
        double distanceFromRightTopPost = Math.sqrt(Math.pow((rightGoalLine - location.x), 2)
                + Math.pow((topGoalPost + adjustment - location.y), 2));
        double distanceFromRightBottomPost = Math.sqrt(Math.pow((rightGoalLine - location.x), 2)
                + Math.pow((bottomGoalPost - adjustment - location.y), 2));

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



        // Left goal post
        if(location.x < leftGoalLine && location.y < topGoalPost){
            if(location.y >= topGoalPost- dummy_radius && location.x > leftGoalBack){
                reflection(angle, 2);
            }
        }
        else if(location.x < leftGoalLine  && location.y > bottomGoalPost){
            if(location.y <= bottomGoalPost+ dummy_radius && location.x > leftGoalBack){
                reflection(angle, 2);
            }
        }
        else if(location.x < leftGoalBack && location.y > topGoalPost &&
                location.y < bottomGoalPost){
            if(location.x >= leftGoalBack- dummy_radius)
                reflection(angle, 1);
        }
        // Right Goal post
        else if(location.x > rightGoalLine && location.y < topGoalPost){
            if(location.y >= topGoalPost- dummy_radius && location.x < rightGoalBack){
                reflection(angle, 2);
            }
        }
        else if(location.x > rightGoalLine  && location.y > bottomGoalPost){
            if(location.y <= bottomGoalPost+ dummy_radius && location.x < rightGoalBack){
                reflection(angle, 2);
            }
        }
        else if(location.x > rightGoalBack && location.y > topGoalPost &&
                location.y < bottomGoalPost){
            if(location.x <= rightGoalBack+ dummy_radius)
                reflection(angle, 1);
        }
        // hit the post,,aa
        else if((distanceFromLeftBottomPost < dummy_radius
                || distanceFromLeftTopPost < dummy_radius
                || distanceFromRightBottomPost < dummy_radius
                || distanceFromRightTopPost < dummy_radius )
                && (location.x - dummy_radius - 2 > leftGoalLine || location.x + dummy_radius + 2 < rightGoalLine ) && postTimer > 5){



            System.out.println("hit post");
            setAngle((-1) * angle + Math.PI);
            postTimer = 0;


        }
        postTimer++;

    }

    public void goalEdges(){

    }
}
