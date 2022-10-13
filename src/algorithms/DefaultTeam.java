package algorithms;
import java.util.*;

import java.awt.Point;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DefaultTeam {
  	public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {
	 
	// Notre DS
   	 ArrayList<Point> result = new ArrayList<Point>();
    	//noeuds connectes au DS
    	ArrayList<Point> connected = new ArrayList<Point>();
    	//noeuds qui ne sont pas dans le DS
    	ArrayList<Point> notDs = new ArrayList<Point>();	
    	//le noeud qui sera choisi pour etre dans le DS
    	Point highestDeg = new Point();
    	//degre de chaque noeud en excluant les liens avec les noeuds deja connectes au DS ou faisant partie du DS
    	Map<Point,Integer> deg = new HashMap<Point,Integer>();
    	//initialement aucun noeud n'est dans le DS
    	for (Point p:points) notDs.add(p);
    
   	for (Point p:points) deg.put(p,degree(p,points,edgeThreshold));
    	//noeud avec le plus grand deg
    	highestDeg = Collections.max(deg.entrySet(), Map.Entry.comparingByValue()).getKey();
	//il est ajoute au DS
	result.add(highestDeg);	
	//et supprime de la liste des non DS
	notDs.remove(highestDeg); 
	//on ne va plus considerer le lien avec le DS pour choisir le plus grand deg
	deg.remove(highestDeg);
	//liste des noeuds connectes au DS
	for(Point p:notDs) {
		if((boolean)isEdge(p, highestDeg,edgeThreshold) && !(boolean)connected.contains(p) && p!=highestDeg) {
			//System.out.println("pppp");
			connected.add(p);}
		}
	//si un noeud ne faisant pas partie du DS est connecte a un noeud connecte au DS on decremente son deg
	for(Point p:notDs) {
		if((boolean)isEdge(p,highestDeg,edgeThreshold) && p != highestDeg) {
			deg.put(p, deg.get(p) - 1);
		}
		for (Point q:connected) {
			if((boolean)isEdge(p,q,edgeThreshold) && p != q) deg.put(p, deg.get(p) - 1);
		}
	}
	
   while (connected.size()+result.size() < points.size()) {
    	//on trouve a nouveau le noeud avec le plus grand deg
    	highestDeg = Collections.max(deg.entrySet(), Map.Entry.comparingByValue()).getKey();
    	//on l'ajoute au DS
    	result.add(highestDeg);
    	//on l'elimine des noeuds qui ne sont pas dans le DS
    	notDs.remove(highestDeg);
    	//les noeuds du DS ne pourront plus pris pour etre dans le DS
    	deg.remove(highestDeg);
    	//liste des noeuds connectes au DS
    	for(Point p:notDs) {
    		if((boolean)isEdge(p, highestDeg,edgeThreshold) && !(boolean)connected.contains(p) && p!=highestDeg) {
    			connected.add(p);
    		}
    	}
    	//si un noeud ne faisant pas partie du DS est connecte a un noeud connecte au DS on decremente son deg
    	for(Point p:notDs) {
    		if((boolean)isEdge(p,highestDeg,edgeThreshold) && p != highestDeg) {
    			deg.put(p, deg.get(p) - 1);
    		}
    		for (Point q:connected) {
    			if((boolean)isEdge(p,q,edgeThreshold) && p != q)
    				deg.put(p, deg.get(p) - 1);
    			}
    		}
    
	}
	System.out.println("Nodes connected to DS: " + connected.size());
	System.out.println("Nodes belonging to DS: " + result.size());
	System.out.println("----------------------");
	if (false) result = readFromFile("output0.points");
	else saveToFile("output",result);
	return result;
	}
  
	private boolean isEdge(Point p, Point q, int edgeThreshold) {
		return p.distance(q)<edgeThreshold;
	}
	private int degree(Point p, ArrayList<Point> points, int edgeThreshold) {
		int degree=-1;
		for (Point q: points) {if (isEdge(p,q,edgeThreshold)) degree++;}
		return degree;
	}
	
	
	
  //FILE PRINTER
  private void saveToFile(String filename,ArrayList<Point> result){
    int index=0;
    try {
      while(true){
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename+Integer.toString(index)+".points")));
        try {
          input.close();
        } catch (IOException e) {
          System.err.println("I/O exception: unable to close "+filename+Integer.toString(index)+".points");
        }
        index++;
      }
    } catch (FileNotFoundException e) {
      printToFile(filename+Integer.toString(index)+".points",result);
    }
  }
  private void printToFile(String filename,ArrayList<Point> points){
    try {
      PrintStream output = new PrintStream(new FileOutputStream(filename));
      int x,y;
      for (Point p:points) output.println(Integer.toString((int)p.getX())+" "+Integer.toString((int)p.getY()));
      output.close();
    } catch (FileNotFoundException e) {
      System.err.println("I/O exception: unable to create "+filename);
    }
  }

  //FILE LOADER
  private ArrayList<Point> readFromFile(String filename) {
    String line;
    String[] coordinates;
    ArrayList<Point> points=new ArrayList<Point>();
    try {
      BufferedReader input = new BufferedReader(
          new InputStreamReader(new FileInputStream(filename))
          );
      try {
        while ((line=input.readLine())!=null) {
          coordinates=line.split("\\s+");
          points.add(new Point(Integer.parseInt(coordinates[0]),
                Integer.parseInt(coordinates[1])));
        }
      } catch (IOException e) {
        System.err.println("Exception: interrupted I/O.");
      } finally {
        try {
          input.close();
        } catch (IOException e) {
          System.err.println("I/O exception: unable to close "+filename);
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("Input file not found.");
    }
    return points;
  }
}
