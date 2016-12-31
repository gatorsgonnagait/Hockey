import java.util.ArrayList;

public class Collision {


    //ArrayList<ArrayList<MovingObject>> adjList = new ArrayList<>(7);

    private static int WHITE = 0;
    private static int GRAY  = 1;
    private static int BLACK = 2;

    ArrayList<Integer>[] adjList;
    MovingObject[] movingObjects;
    int[] color;
    boolean leftObjectIsOb1;

    public Collision(int n){
        color = new int[n];
        adjList = new ArrayList[n];
        movingObjects = new MovingObject[n];

        for (int i = 0; i < n; i++){
            adjList[i] = new ArrayList<Integer>();
            color[i]   = WHITE;
        }
    }

    /*public double collisonAngle(MovingObject object1, MovingObject object2){
        double Y;
        double X;
        if(object1.location.y >= object2.location.y){
            Y = object1.location.y - object2.location.y;
            X = object1.location.x - object2.location.x;
        }
        else{
            Y = object2.location.y - object1.location.y;
            X = object2.location.x - object1.location.x;
        }
        return Math.atan2(Y, X);
    }*/

    public double adjustAngle(double A){

        if(A < 0)
            A = A + 2*Math.PI;

        if(A >=  3*Math.PI/2){
            A = 3*Math.PI/2;
        }
        return A % Math.PI;
    }


    int count = 0;
    public boolean objectsCollide(MovingObject object1, MovingObject object2){



        double distanceFromObjects = object1.getDistance(object1.location.x, object2.location.x, object1.location.y, object2.location.y);

        if(distanceFromObjects < 0)
            distanceFromObjects = distanceFromObjects * (-1);

        double collisionDistance = object1.radius + object2.radius;



        if( distanceFromObjects <= collisionDistance ) {
            /*
            adjList[object1.id].add(object2.id);
            adjList[object2.id].add(object1.id);
            if(movingObjects[object1.id] == null)
                movingObjects[object1.id] = object1;
            if(movingObjects[object2.id] == null)
                movingObjects[object2.id] = object2;
            */
            System.out.println(distanceFromObjects + " distanceFromObjects from objects");
            System.out.println(collisionDistance + " two radius");

            object1.colliding = true;
            object2.colliding = true;

            object1.collidesWith = object2;
            object2.collidesWith = object1;

            object1.location.x = object1.location.prevX;
            object1.location.y = object1.location.prevY;

            object2.location.x = object2.location.prevX;
            object2.location.y = object2.location.prevY;

            System.out.println(count + " count");
            return true;
        }

        return false;


    }

    public void calculateCollisions(MovingObject ob1, MovingObject ob2){

        MovingObject top;
        MovingObject bottom;
        if(ob1.location.x <= ob2.location.x ){
            top = ob1;
            bottom = ob2;
        }
        else{
            top = ob2;
            bottom = ob1;
        }

        //System.out.println("Calculating for " + ob1.color + " and " + ob2.color);
        double ob1VelocityX = top.speed * Math.cos(top.angle);
        double ob1VelocityY = top.speed * Math.sin(top.angle);

        double ob2VelocityX = bottom.speed * Math.cos(bottom.angle);
        double ob2VelocityY = bottom.speed * Math.sin(bottom.angle);

        double ob1FinalXVelocity = (ob1VelocityX * (top.mass - bottom.mass) +  2 * bottom.mass * ob2VelocityX) / (top.mass + bottom.mass);
        double ob1FinalYVelocity = (ob1VelocityY * (top.mass - bottom.mass) +  2 * bottom.mass * ob2VelocityY) / (top.mass + bottom.mass);
        double ob2FinalXVelocity = (ob2VelocityX * (top.mass - bottom.mass) +  2 * top.mass * ob1VelocityX) / (top.mass + bottom.mass);
        double ob2FinalYVelocity = (ob2VelocityY * (bottom.mass - top.mass) +  2 * top.mass * ob1VelocityY) / (top.mass + bottom.mass);

        double topAngle = Math.atan2(ob1FinalYVelocity, ob1FinalXVelocity);
        double bottomAngle = Math.atan2(ob2FinalYVelocity, ob2FinalXVelocity);

        double topTotalVelocity = (ob1FinalXVelocity / Math.cos(topAngle));
        double bottomTotalVelocity = (ob2FinalXVelocity / Math.cos(bottomAngle));




        //System.out.println(object1.angle*180/Math.PI + " angle before");
        double collisionAngleTop = top.collisonAngle();
        double collisionAngleBottom = bottom.collisonAngle();
        //System.out.println(collisionAngle*180/Math.PI);
        double perpendicularAngle1 = (collisionAngleTop + Math.PI/2) ;
        double perpendicularAngle2 = (collisionAngleBottom + Math.PI/2) ;
        //System.out.println(perpendicularAngle * 180/Math.PI);

        double adjustment1 = Math.PI - perpendicularAngle1;
        double adjustment2 = Math.PI - perpendicularAngle2;

        //ob1Angle = ob1.angle + adjustment1;
        //ob2Angle = ob2.angle + adjustment2;
        topAngle = adjustAngle(topAngle);
        bottomAngle = adjustAngle(bottomAngle);
        topAngle = topAngle + adjustment1;
        bottomAngle = bottomAngle + adjustment2;

        //System.out.println(adjustment1);
        //System.out.println(adjustment2);
        System.out.println(topAngle + " angle 1");
        System.out.println(bottomAngle + " angle 2");
        System.out.println(top.speed + " top object  speed");
        System.out.println(bottom.speed + " bottom object speed");
        if (top.speed == 0) {
            System.out.println(" still object one hit");
            top.angle = collisionAngleTop;
            bottom.angle = bottom.reflection(bottomAngle, 2) - adjustment2;
        }
        else if (bottom.speed == 0) {
            System.out.println(" still object two hit");
            bottom.angle = collisionAngleBottom;
            top.angle = top.reflection(topAngle, 2) - adjustment1;
        }


        else if( ( (topAngle > 0 && topAngle < Math.PI) && ( bottomAngle > Math.PI && bottomAngle < (2*Math.PI) ) )
                || ( (topAngle > Math.PI && topAngle < (2*Math.PI) )  && ( bottomAngle > 0 && bottomAngle < Math.PI ) ) ) {
            System.out.println("normal collision");
            top.angle = top.reflection(topAngle, 2) - adjustment1;
            bottom.angle = bottom.reflection(bottomAngle, 2) - adjustment2;
        }


        else if( (topAngle >= Math.PI && topAngle <= (2*Math.PI) ) && ( bottomAngle > Math.PI && bottomAngle < (2*Math.PI) ) ){

            System.out.println("pushing up");
            topAngle = (topAngle + bottomAngle)/2;
            top.angle = topAngle - adjustment1;
            bottom.angle = bottom.reflection(bottomAngle, 2) - adjustment2;

        }
        else if( (topAngle > 0 && topAngle < Math.PI) && (bottomAngle >= 0 && bottomAngle <= Math.PI  ) ){
            System.out.println("pushing down");

            bottomAngle = (topAngle + bottomAngle)/2;
            bottom.angle = topAngle - adjustment1;
            top.angle = bottom.reflection(bottomAngle, 2) - adjustment2;
        }

        System.out.println();

        top.setSpeed(topTotalVelocity);
        bottom.setSpeed(bottomTotalVelocity);
        //System.out.println(object1.angle*180/Math.PI + " angle after");
        /*ob1.location.x = ob1.location.x + 1 * Math.cos(ob1.angle);
        ob1.location.y = ob1.location.y + 1 * Math.sin(ob1.angle);

        ob2.location.x = ob2.location.x + 1 * Math.cos(ob2.angle);
        ob2.location.y = ob2.location.y + 1 * Math.sin(ob2.angle);
        */



        //System.out.println(ob1.id+" speed " + ob1.speed);
        //System.out.println(ob2.id+" speed " + ob2.speed);
        //ob1.setAngle(ob1Angle);
        //ob2.setAngle(ob2Angle);//TE
        /*
        System.out.println();
        System.out.println(ob1.id+" angle 1 " + ob1.angle);
        System.out.println(ob2.id+" angle 2 " + ob2.angle);
        */
    }

    /*UNUSED

    public void BFS(int source){

        LinkedList<Integer> queue = new LinkedList<>();
        color[source] = GRAY;
        queue.add(source);
        while(!queue.isEmpty()){
            int currObj = queue.poll();
            for (int i : adjList[currObj]){
                if(color[i] == WHITE) {
                    queue.add(i);
                    color[i] = GRAY;
                }
                if(color[i] == GRAY) {
                    calculateCollisions(movingObjects[currObj], movingObjects[i]);
                    //System.out.println(currObj + " colliding with "+ i);
                }
            }
            color[currObj] = BLACK;
        }
    }

    public void handleCollisions(){

        for (int i = 0; i < color.length; i++){
            color[i]  = WHITE;
        }
        for(int i = 0; i < color.length; i ++){
            //System.out.println("loop "+i);
            //System.out.println("Color array: "+ Arrays.toString(color));
            if(color[i] == WHITE)
                BFS(i);
        }
    }

    public void printAdjList(){
        for(int i = 1; i < adjList.length; i++){
            System.out.print(i +": ");
            for (int j : adjList[i]){
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }*/


    /*
    //commented
    public void calculateCollisions(MovingObject ob1, MovingObject ob2){
        double Y = ob1.location.y - ob1.location.y;
        double X = ob1.location.x - ob2.location.x;
        //double collisionAngle = Math.atan2(Y, X);

        double collisionAngle = Math.abs(ob1.angle - ob2.angle);

        double ob1VelocityX = ob1.speed * Math.cos(ob1.angle - collisionAngle);
        double ob1VelocityY = ob1.speed * Math.sin(ob1.angle - collisionAngle);

        double ob2VelocityX = ob2.speed * Math.cos(ob2.angle - collisionAngle);
        double ob2VelocityY = ob2.speed * Math.sin(ob2.angle - collisionAngle);

        double ob1FinalXVelocity = (ob1VelocityX * (ob1.mass - ob2.mass) +
                2 * ob2.mass * ob2VelocityX) / (ob1.mass + ob2.mass);
        double ob2FinalXVelocity = (ob2VelocityX * (ob1.mass - ob2.mass) +
                2 * ob2.mass * ob1VelocityX) / (ob1.mass + ob2.mass);

        double ob1TotalVelocity = Math.sqrt( Math.pow(ob1FinalXVelocity, 2) * Math.pow(ob1FinalXVelocity, 2)
                + ob1VelocityY * Math.pow(ob1VelocityY, 2));
        double ob2TotalVelocity = Math.sqrt( Math.pow(ob2FinalXVelocity, 2) * Math.pow(ob2FinalXVelocity, 2)
                + ob2VelocityY * Math.pow(ob2VelocityY, 2));

        double ob1Angle = Math.atan2(ob1VelocityY, ob1FinalXVelocity) + ob1.angle;
        double ob2Angle = Math.atan2(ob2VelocityY, ob2FinalXVelocity) + ob2.angle;

        ob1.setSpeed((int) ob1TotalVelocity);
        ob2.setSpeed((int) ob2TotalVelocity);
        System.out.println(ob1.speed);
        System.out.println(ob2.speed);
        ob1.setAngle(Math.abs(ob1Angle));
        ob2.setAngle(Math.abs(ob2Angle));//TE
        System.out.println(ob1.angle);
        System.out.println(ob2.angle);
    }


    public void calculateCollisions(MovingObject ob1, MovingObject ob2){
        double Y = ob1.location.y - ob1.location.y;
        double X = ob1.location.x - ob2.location.x;
        //double collisionAngle = Math.atan2(Y, X);

        double collisionAngle = Math.abs(ob1.angle - ob2.angle);

        double ob1VelocityX = ob1.speed * Math.cos(ob1.angle - collisionAngle);
        double ob1VelocityY = ob1.speed * Math.sin(ob1.angle - collisionAngle);

        double ob2VelocityX = ob2.speed * Math.cos(ob2.angle - collisionAngle);
        double ob2VelocityY = ob2.speed * Math.sin(ob2.angle - collisionAngle);

        double ob1FinalXVelocityR = (ob1VelocityX * (ob1.mass - ob2.mass) +
                2 * ob2.mass * ob2VelocityX) / (ob1.mass + ob2.mass);
        double ob2FinalXVelocityR = (ob2VelocityX * (ob2.mass - ob1.mass) +
                2 * ob1.mass * ob1VelocityX) / (ob1.mass + ob2.mass);

        double ob1FinalXVelocity = ob1FinalXVelocityR * Math.cos(collisionAngle) +
                ob1VelocityY * Math.cos(collisionAngle + (Math.PI/2));
        double ob1FinalYVelocity = ob1FinalXVelocityR * Math.sin(collisionAngle) +
                ob1VelocityY * Math.sin(collisionAngle + (Math.PI/2));
        double ob2FinalXVelocity = ob2FinalXVelocityR * Math.cos(collisionAngle) +
                ob2VelocityY * Math.cos(collisionAngle + (Math.PI/2));
        double ob2FinalYVelocity = ob2FinalXVelocityR * Math.sin(collisionAngle) +
                ob2VelocityY * Math.sin(collisionAngle + (Math.PI/2));

        double ob1Angle = Math.atan2(ob1FinalYVelocity, ob1FinalXVelocity);
        double ob2Angle = Math.atan2(ob2FinalYVelocity, ob2FinalXVelocity);

        int ob1TotalVelocity = (int) (ob1FinalXVelocity / Math.cos(ob1Angle));
        int ob2TotalVelocity = (int) (ob2FinalXVelocity / Math.cos(ob2Angle));

        ob1.setSpeed(ob1TotalVelocity);
        ob2.setSpeed(ob2TotalVelocity);
        System.out.println(ob1.speed);
        System.out.println(ob2.speed);
        ob1.setAngle(Math.abs(ob1Angle));
        ob2.setAngle(Math.abs(ob2Angle));//TE
        System.out.println(ob1.angle);
        System.out.println(ob2.angle);//ll
    }
    // commented*/



}
