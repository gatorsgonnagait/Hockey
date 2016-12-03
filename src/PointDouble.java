/**
 * Created by alien on 11/15/16.
 */
public class PointDouble {
    double x;
    double y;
    double prevX;
    double prevY;

    public PointDouble(){
        x=0;
        y=0;
        prevX = 0;
        prevY = 0;
    }

    public PointDouble(double x, double y){
        this.x = x;
        this.y = y;
    }

    public PointDouble(PointDouble point){
        this.x = point.x;
        this.y = point.y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }



}
