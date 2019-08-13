/**
 * Annealing
 * Capp Wiedenhoefer
 */

import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.debug.*;
import edu.gwu.geometry.*;
import java.util.*;
import java.lang.*;

public class Annealing implements MTSPAlgorithm {

    public static void main(String[] args) {
        Annealing alg = new Annealing();

        double x0, y0, x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, x6, y6;
        double x7, y7, x8, y8, x9, y9, x10, y10, x11, y11, x12, y12, x13, y13; 
        x0 = 0.0D; y0 = 0.0D;
        x1 = 3.0D; y1 = 0.0D;
        x2 = 3.0D; y2 = 3.0D;
        x3 = 3.0D; y3 = 1.0D;
        x4 = 0.0D; y4 = 3.0D;
        x5 = 10.0D; y5 = 0.0D;
        x6 = 13.0D; y6 = 0.0D;
        x7 = 13.0D; y7 = 3.0D;
        x8 = 10.0D; y8 = 3.0D;
        x9 = 10.0D; y9 = 1.0D;
        x10 = 0.0D; y10 = 13.0D;
        x11 = 3.0D; y11 = 13.0D;
        x12 = 10.0D; y12 = 5.0D;
        x13 = 0.0D; y13 = 5.0D;
        Pointd p0 = new Pointd(x0, y0);
        Pointd p1 = new Pointd(x1, y1);
        Pointd p2 = new Pointd(x2, y2);
        Pointd p3 = new Pointd(x3, y3);
        Pointd p4 = new Pointd(x4, y4);
        Pointd p5 = new Pointd(x5, y5);
        Pointd p6 = new Pointd(x6, y6);
        Pointd p7 = new Pointd(x7, y7);
        Pointd p8 = new Pointd(x8, y8);
        Pointd p9 = new Pointd(x9, y9);
        Pointd p10 = new Pointd(x10, y10);
        Pointd p11 = new Pointd(x11, y11);
        Pointd p12 = new Pointd(x12, y12);
        Pointd p13 = new Pointd(x13, y13);

        Pointd[] points = {p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13};

        alg.computeTours(3, points);
    }

    public int[][] computeTours(int m, Pointd[] points){
        int[][] tours = Greedy(m, points);

        double originalCost;
        double newCost;
        int random1;
        int random2;
        int temp;
        int[] newTour;
        for(int i=0; i<tours.length; i++){
            //System.out.println(Arrays.toString(tours[i]));
           // System.out.println(computeTourCost(points, tours[i]));
            //Annealing part of the program
            for(int j=0; j<100; j++){
                originalCost = computeTourCost(points, tours[i]);
                newTour = Arrays.copyOf(tours[i], tours[i].length);
                random1 = (int)Math.floor(Math.random()*newTour.length);
                random2 = (int)Math.floor(Math.random()*newTour.length);
                while(random2 == random1) random2 = (int)Math.floor(Math.random()*newTour.length);
                temp = newTour[random1];
                newTour[random1] = newTour[random2];
                newTour[random2] = temp;
                newCost = computeTourCost(points, newTour);
                if(newCost < originalCost) tours[i] = newTour;
            }
        }
        for (int i = 0; i < tours.length; i++) {
            //System.out.println(Arrays.toString(tours[i]));
           // System.out.println(computeTourCost(points, tours[i]));
        }
        
        return tours;
    }

    double computeTourCost(Pointd[] points, int[] tour){
        double totalDistance = 0.0D;
        for (int i = 0; i < tour.length - 1; i++){
            double distance = getDistance(points[tour[i]], points[tour[(i + 1)]]);
            totalDistance += distance;
        }
        totalDistance += getDistance(points[tour[(tour.length - 1)]], points[tour[0]]);
    
        return totalDistance;
    }

    double getDistance(Pointd point1, Pointd point2){
        return Math.sqrt( Math.pow(point1.x-point2.x, 2) + Math.pow(point1.y-point2.y, 2) );
    }

    int[][] Greedy (int m, Pointd[] points){
        int[][] tours = Naive(m, points);
        //Insert the results from the Naive algorithm into an array of Linked Lists
        LinkedList<Integer>[] salesmen = new LinkedList[tours.length];
        for(int i=0; i<tours.length; i++){
            salesmen[i] = new LinkedList<Integer>();
            for(int j=0; j<tours[i].length; j++){
                salesmen[i].add( tours[i][j] );
            }
        }

        int salesmanA;
        int salesmanB;
        double distanceA;
        double distanceB;
        int b;
        for(int a=0; a<salesmen.length; a++){
            salesmanA = salesmen[a].get(0);
            for(int i=0; i<salesmen.length; i++){
                b = (a+i+1)%salesmen.length;
                salesmanB = salesmen[b].get(0);
                //Check to see if points from one tour are actually closer to another tour
                for(int j=0; j<salesmen[b].size(); j++){
                    if(a == b) break;
                    distanceA = Math.sqrt(Math.pow( points[salesmanA].x - points[salesmen[b].get(j)].x, 2) + Math.pow( points[salesmanA].y - points[salesmen[b].get(j)].y, 2));
                    distanceB = Math.sqrt(Math.pow( points[salesmanB].x - points[salesmen[b].get(j)].x, 2) + Math.pow( points[salesmanB].y - points[salesmen[b].get(j)].y, 2));
                    if(distanceA < distanceB) {
                        salesmen[a].add(salesmen[b].get(j));
                        salesmen[b].remove(j);
                        j--;
                    }
                }
            }
        }
        for(int i=0; i<salesmen.length; i++){
            tours[i] = new int[salesmen[i].size()];
            for(int j=0; j<salesmen[i].size(); j++){
                tours[i][j] = salesmen[i].get(j);
            }
        }
        return tours;
    }

    int[][] Naive (int m, Pointd[] points){
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
        for(int i=0; i<tours.length; i++){
            //System.out.println(Arrays.toString(tours[i]));
        }
        return tours;
    }
    
    public String getName(){
        return "AnnealingAlg-CappW";
    }

    public void setPropertyExtractor(int algID, PropertyExtractor prop){

    }
}