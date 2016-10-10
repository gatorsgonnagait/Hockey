import java.awt.*;

/**
 * Created by alien on 8/16/16.
 */

public class Line {
    int x1;
    int x2;
    int y1;
    int y2;
    int slopeX;
    int slopeY;
    double slope;
    double slopeAngle;
    int A;
    int B;
    int C;


    public Line(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        slopeX = x2 - x1;
        slopeY = y2 - y1;
        slope = ((double)slopeY)/slopeX;
        // C = y2 - mx2
        // m = sy / sx
        // -sy*x + sx*y + sy*x2 - sx*y2 = 0
        // A = -sy
        // B = sx
        // C = sy*x2 - sx*y2
        C = slopeY*x1 - slopeX*y1;
        A = (-1)*slopeY;
        B = slopeX;
        slopeAngle = Math.atan2(slopeY, slopeX);
    }

    public Line(int a, int b, int c){
        A = a;
        B = b;
        C = c;
    }

    public Line(Point x1y1, Point x2y2){
        x1 = x1y1.x;
        x2 = x2y2.x;
        y1 = x1y1.y;
        y2 = x2y2.y;
        slopeX = x2 - x1;
        slopeY = y2 - y1;
        slope = ((double)slopeY)/slopeX;
        // C = y2 - mx2
        // m = sy / sx
        // -sy*x + sx*y + sy*x2 - sx*y2 = 0
        // A = -sy
        // B = sx
        // C = sy*x2 - sx*y2
        C = slopeY*x2 - slopeX*y2;
        A = (-1)*slopeY;
        B = slopeX;
        slopeAngle = Math.atan2(slopeY, slopeX);


    }

    public double distanceFrom(int m, int n){
        //d = |Am+Bn+C| / sqrt(A^2+B^2)
        double distance = Math.abs(A*m + B*n + C) / Math.sqrt(Math.pow(A,2)+Math.pow(B,2));
        return distance;
    }

    public double distanceFrom(Point mn){
        //d = |Am+Bn+C| / sqrt(A^2+B^2)
        int m = mn.x;
        int n = mn.y;
        double distance = Math.abs(A*m + B*n + C) / Math.sqrt(Math.pow(A,2)+Math.pow(B,2));
        return distance;
    }

    public void calculateY1Y2(int x1, int x2){
        this.x1 = x1;
        this.x2 = x2;
        y1 = (C-A*x1)/B;
        y2 = (C-A*x2)/B;
    }

    public String toString(){
        return "Line{ ("+x1+", "+y1+") -> ("+x2+", "+y2+")} => ("+A+")x + (" + B+")y + (" + C +") = 0";
    }

}
