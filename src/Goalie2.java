import java.awt.*;


public class Goalie2 extends Player {

    int count = 0;

    public Goalie2(int id, PointDouble point, int speed, double angle, int radius, Color color, Puck puck) {
        super(id, point, speed, angle, radius, color, puck);
    }

    public void moveGoalieDown(){
        count++;
        if(count == 4) {
            location.y = location.y + radius/10; // moves down one pixel per frame
            count = 0;
        }
        if(location.y >= GameDriver.bottomGoalPost) {
            location.y = GameDriver.bottomGoalPost;
            //setAngle(Math.PI/2);
        }

    }
    public void moveGoalieUp(){
        count++;
        if(count == 4) {
            location.y = location.y - radius/10;
            count = 0;
        }
        if(location.y <= GameDriver.topGoalPost) {
            location.y = GameDriver.topGoalPost;

        }
    }

    @Override
    public void updateLocation() {
        stick.updateLocation();
        location.x = GameDriver.rightGoalLine - GameDriver.goalWidth/2;

        double Y = puck.location.y - location.y;//makes it face puck
        double X = puck.location.x - location.x;

        //System.out.println(angle);

        // if puck is behind goal line
        if (puck.location.x >= GameDriver.rightGoalLine){

            if(puck.location.y < location.y){
                moveGoalieUp();
                double A = angleFacing + 4*Math.PI/180;//makes it turn clockwise
                if(A < 0)
                    A = A + 2*Math.PI;

                if(A >= 3*Math.PI/2){
                    angleFacing = 3*Math.PI/2;
                }
                else
                    angleFacing = A;
            }
            else if(puck.location.y > location.y){
                moveGoalieDown();
                double A = angleFacing - 4*Math.PI/180;//counter clockwise

                if(A <= Math.PI/2){
                    angleFacing = -3*Math.PI/2;
                }else
                    angleFacing = A;


            }
        }
        else {
            angleFacing = Math.atan2(Y, X);
            if(location.y < puck.location.y){
                moveGoalieDown();
            }
            else if (location.y > puck.location.y){
                moveGoalieUp();
            }

        }
    }



}