import java.awt.*;


public class Goalie1 extends Player {


    int count = 0;
    public Goalie1(int id, PointDouble point, int speed, double angle, int radius, Color color, Puck puck) {
        super(id, point, speed, angle, radius, color, puck);

    }

    public void moveGoalieDown(){
        count++;
        if(count == 4) {
            location.y = location.y + radius/10;
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
//
    @Override
    public void updateLocation() {
        stick.updateLocation();
        location.x = GameDriver.leftGoalLine + GameDriver.goalWidth/2;
        //double slope = (double) (horizontalMiddle - puck.location.y) / (leftGoalLine - puck.location.x);
        //double saveSpot = (horizontalMiddle + (210- leftGoalLine)*slope);
        double Y = puck.location.y - location.y;//makes it face puck
        double X = puck.location.x - location.x;

        //setAngle(Math.atan2(Y, X));
        double puckAngle = Math.atan2(Y,X);


        // if puck is behind goal line
        if (puck.location.x <= GameDriver.leftGoalLine){

            if(puck.location.y < location.y){
                moveGoalieUp();
                angleFacing = angleFacing - 2*Math.PI/180;
                if(angleFacing <= -Math.PI/2){
                    angleFacing = -Math.PI/2;
                }
            }
            else if(puck.location.y > location.y){
                moveGoalieDown();
                angleFacing = angleFacing + 2*Math.PI/180;
                if(angleFacing >= Math.PI/2){
                    angleFacing = Math.PI/2;
                }
            }
        }
        else {
            angleFacing = puckAngle;
            //setAngle(Math.atan2(Y, X));
            if(location.y < puck.location.y){
                moveGoalieDown();
            }
            else if (location.y > puck.location.y){
                moveGoalieUp();
            }
            /*
            if (saveSpot >= bottomGoalPost) {
                saveSpot = bottomGoalPost;
            }
            else if (saveSpot <= topGoalPost) {
                saveSpot = topGoalPost;
            }

            if (location.y < saveSpot) {
                moveGoalieDown();


            } else if (location.y > saveSpot) {
                moveGoalieUp();

            }*/
        }
    }


}
