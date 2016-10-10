import java.util.ArrayList;
import java.util.LinkedList;

public class Collision {


    //ArrayList<ArrayList<MovingObject>> adjList = new ArrayList<>(7);

    private static int WHITE = 0;
    private static int GRAY  = 1;
    private static int BLACK = 2;

    ArrayList<Integer>[] adjList;
    MovingObject[] movingObjects;
    int[] color;

    public Collision(int n){
        color = new int[n];
        adjList = new ArrayList[n];
        movingObjects = new MovingObject[n];

        for (int i = 0; i < n; i++){
            adjList[i] = new ArrayList<Integer>();
            color[i]   = WHITE;
        }
    }

    public boolean objectsCollide(MovingObject object1, MovingObject object2){

        double distance = Math.hypot(object2.location.x - object1.location.x,
                object2.location.y - object1.location.y);

        if(distance < 0)
            distance = distance * (-1);

        double distance1 = object1.radius + object2.radius;

        if( distance <= distance1) {
            //System.out.println(object1.id + " and " + object2.id + " are colliding");
            adjList[object1.id].add(object2.id);
            adjList[object2.id].add(object1.id);
            object1.colliding = true;
            object2.colliding = true;
            if(movingObjects[object1.id] == null)
                movingObjects[object1.id] = object1;
            if(movingObjects[object2.id] == null)
                movingObjects[object2.id] = object2;

            return true;
        }
        return false;
    }

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
    }

    /*
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
    }*/

/*
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
*/

    public void calculateCollisions(MovingObject ob1, MovingObject ob2){
        System.out.println("Calculating for " + ob1.color + " and " + ob2.color);
        double ob1VelocityX = ob1.speed * Math.cos(ob1.angle);
        double ob1VelocityY = ob1.speed * Math.sin(ob1.angle);

        double ob2VelocityX = ob2.speed * Math.cos(ob2.angle);
        double ob2VelocityY = ob2.speed * Math.sin(ob2.angle);

        double ob1FinalXVelocity = (ob1VelocityX * (ob1.mass - ob2.mass) +
                2 * ob2.mass * ob2VelocityX) / (ob1.mass + ob2.mass);
        double ob1FinalYVelocity = (ob1VelocityY * (ob1.mass - ob2.mass) +
                2 * ob2.mass * ob2VelocityY) / (ob1.mass + ob2.mass);
        double ob2FinalXVelocity = (ob2VelocityX * (ob2.mass - ob1.mass) +
                2 * ob1.mass * ob1VelocityX) / (ob1.mass + ob2.mass);
        double ob2FinalYVelocity = (ob2VelocityY * (ob2.mass - ob1.mass) +
                2 * ob1.mass * ob1VelocityY) / (ob1.mass + ob2.mass);

        double ob1Angle = Math.atan2(ob1FinalYVelocity, ob1FinalXVelocity);
        double ob2Angle = Math.atan2(ob2FinalYVelocity, ob2FinalXVelocity);

        int ob1TotalVelocity = (int) (ob1FinalXVelocity / Math.cos(ob1Angle));
        int ob2TotalVelocity = (int) (ob2FinalXVelocity / Math.cos(ob2Angle));

        ob1.setSpeed(ob1TotalVelocity);
        ob2.setSpeed(ob2TotalVelocity);
        System.out.println(ob1.id+" speed " + ob1.speed);
        System.out.println(ob2.id+" speed " + ob2.speed);
        ob1.setAngle(ob1Angle);
        ob2.setAngle(ob2Angle);//TE
        System.out.println(ob1.id+" angle " + ob1.angle);
        System.out.println(ob2.id+" angle " + ob2.angle);
    }
}
