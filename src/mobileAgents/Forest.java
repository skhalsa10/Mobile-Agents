package mobileAgents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Forest {
    private ArrayList<Node> forest = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    public Forest(String config) {
        Path configFile = Paths.get(config);
        try {
            InputStream in = Files.newInputStream(configFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
                readInfo(line);
            }

        }
        catch(IOException e) {
            System.err.println(e);
        }
    }

    public void readInfo(String line) {
        //node info
        if(line.matches("^[nN].*$")) {
            addNode(line);
            System.out.println("node");
        }
        // edge info
        else if(line.matches("^[eE].*$")) {
            addEdge(line);
            System.out.println("edge");
        }
        // base info
        else if(line.matches( "^[sS].*$")) {
            System.out.println("base station");

        }
        // fire info
        else if(line.matches("^[fF].*$")) {
            System.out.println("fire");
        }

    }

    public void addNode(String line) {
        String[] parsedLine = line.split(" ");
        int x =  Integer.parseInt(parsedLine[1]);
        int y = Integer.parseInt(parsedLine[2]);
        Location location = new Location(x, y);
        Node newNode = new Node (location);
        forest.add(newNode);
    }

    public void addEdge(String line) {
        String[] parsedLine = line.split(" ");
        int x1 = Integer.parseInt(parsedLine[1]);
        int y1 = Integer.parseInt(parsedLine[2]);
        int x2 = Integer.parseInt(parsedLine[3]);
        int y2 = Integer.parseInt(parsedLine[4]);
        Location first = new Location(x1, y1);
        Location second = new Location(x2,y2);
        Edge newEdge = new Edge(first, second);
        edges.add(newEdge);
    }

    public boolean nodeExists(Location location) {
        for(Node n: forest) {
        }
        return false;
    }



    public static void main(String[] args) {
        if(args.length > 0) {
            Forest f = new Forest(args[0]);
        }
    }
}
