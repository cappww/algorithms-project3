/**
 * Capp Wiedenhoefer
 * Native
 *  merely divides the points more or less evenly among the salesmen 
 */
//java -jar ../algtest.jar mtsp.props
import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.debug.*;
import edu.gwu.geometry.*;
import java.util.*;
import java.lang.*;

public class Naive implements MTSPAlgorithm {

    public static void main(String[] args) {
        Naive alg = new Naive();
        double x0, y0, x1, y1, x2, y2, x3, y3, x4, y4, x5, y5;
        x0 = 0.0D; y0 = 0.0D;
        x1 = 3.0D; y1 = 0.0D;
        x2 = 3.0D; y2 = 4.0D;
        x3 = 20.0D; y3 = 0.0D;
        x4 = 28.0D; y4 = 0.0D;
        x5 = 28.0D; y5 = 6.0D;
        Pointd p0 = new Pointd(x0, y0);
        Pointd p1 = new Pointd(x1, y1);
        Pointd p2 = new Pointd(x2, y2);
        Pointd p3 = new Pointd(x3, y3);
        Pointd p4 = new Pointd(x4, y4);
        Pointd p5 = new Pointd(x5, y5);

        Pointd[] points = {p0, p1, p2, p3, p4, p5};

        alg.computeTours(2, points);
    }

    public int[][] computeTours(int m, Pointd[] points){
        //Divide the tours by the amount of salesmen
        int[][] tours = new int[m][];
        for(int i=0; i<m; i++){
            if(i<points.length%m) tours[i] = new int[points.length/m+1];
            else tours[i] = new int[points.length/m];
        }

        //Select the first point, find the next closest point, find the next closest point to that etc
        boolean[] alreadyInTour = new boolean[points.length];
        double minDistance;
        double distance;
        Pointd pointA;
        Pointd pointB;
        int index = 0;
        int n;

        for(int k=0; k<m; k++){
            //Each row of tours, first find a node that isn't in a tour
            n = 0;
            while(alreadyInTour[n]) n++;
            pointA = points[n];
            tours[k][0] = n;
            alreadyInTour[n] = true;

            int iterate = 0;
            int j=1;
            while(j<tours[k].length){
                if(!alreadyInTour[iterate]){
                    minDistance = Double.MAX_VALUE;
                    //Find next closest point to pointA
                    for(int i=0; i<points.length; i++){
                        pointB = points[i];
                        if(!alreadyInTour[i]){
                            distance = Math.sqrt( Math.pow(pointA.x - pointB.x, 2) + Math.pow(pointA.y - pointB.y, 2) );
                            if(distance < minDistance){
                                minDistance = distance;
                                index = i;
                            }
                        }
                    }
                    //System.out.println(index);
                    tours[k][j] = index;
                    alreadyInTour[index] = true;
                    j++;
                }
                iterate++;
                if(iterate >= points.length) iterate = 0;
            }

        }
        return tours;
    }
    
    public String getName(){
        return "NativeAlg-CappW";
    }

    public void setPropertyExtractor(int algID, PropertyExtractor prop){

    }
}