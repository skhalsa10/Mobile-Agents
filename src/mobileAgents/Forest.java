package mobileAgents;

import mobileAgents.messages.MessageGUIConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Forest {
    private ArrayList<Node> forest = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private GUIState GUIStateQueue;

    public Forest(String config, GUIState GUIStateQueue) {
        //One GUIStateQueue needs to be shared with everything that tells the GUI something so it will be passed in here.
        this.GUIStateQueue = GUIStateQueue;
        MessageGUIConfig configMessage = new MessageGUIConfig();
        Path configFile = Paths.get(config);
        try {
            InputStream in = Files.newInputStream(configFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine()) != null) {
                configMessage.appendStr(line+"\n");
                readInfo(line);
            }

            GUIStateQueue.putState(configMessage);

        }
        catch(IOException e) {
            System.err.println(e);
        }
    }

    public void readInfo(String line) {
        //TODO handle weird config files
        //node info
        if(line.matches("^[nN].*$")) {
            addNode(line);
        }
        // edge info
        else if(line.matches("^[eE].*$")) {
            addEdge(line);
        }
        // base info
        else if(line.matches( "^[sS].*$")) {
            addBaseStation(line);

        }
        // fire info
        else if(line.matches("^[fF].*$")) {
            connectGraph();
            addFireNode(line);
        }

    }

    public void addNode(String line) {
        String[] parsedLine = line.split(" ");
        int x =  Integer.parseInt(parsedLine[1]);
        int y = Integer.parseInt(parsedLine[2]);
        Location location = new Location(x, y);
        //I updated this to take the GUIStateQueue
        Node newNode = new Node (location, Node.State.NOTONFIRE, GUIStateQueue);
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
            if(location.equals(n.getLocation())) {
                return true;
            }
        }
        return false;
    }

    public void addFireNode(String line) {
        String[] parsedLine = line.split(" ");
        int x = Integer.parseInt(parsedLine[1]);
        int y = Integer.parseInt(parsedLine[2]);
        Location loc = new Location(x, y);
        Node node;
        if(nodeExists(loc)) {
            node = findNode(loc);
            node.setState(Node.State.ONFIRE);
        }
    }

    public void addBaseStation(String line) {
        String[] parsedLine = line.split(" ");
        int x = Integer.parseInt(parsedLine[1]);
        int y = Integer.parseInt(parsedLine[2]);
        Location loc = new Location(x, y);
        Node node;
        Node newNode;
        if (nodeExists(loc)) {
            node = findNode(loc);

            newNode = new Base(loc, Node.State.NOTONFIRE, GUIStateQueue);
            forest.remove(node);
            forest.add(newNode);

        }
    }



    public void connectNodes(Edge edge) {
        Location first = edge.getFirst();
        Location second = edge.getSecond();
        for(Node n: forest) {
            if(n.getLocation().equals(first)) {
                n.addNeighbor(findNode(second));
            }
            if(n.getLocation().equals(second)) {
                n.addNeighbor(findNode(first));
            }
        }
    }

    public Node findNode(Location loc) {
        for(Node n: forest) {
            if(n.getLocation().equals(loc)) {
                return n;
            }
        }
        return null;
    }

    public void connectGraph() {
        for(Edge e: edges) {
            if(nodeExists(e.getFirst()) && nodeExists(e.getSecond())) {
                connectNodes(e);
            }
        }
    }

    public void printForest() {
        for(Node n: forest) {
            n.printNode();
            n.printNeighbors();
        }
    }

    public void startThreads() {
        for(Node n: forest) {
            new Thread(n).start();
        }
    }



    public static void main(String[] args) {
        if(args.length > 0) {
            GUIState s = new GUIState();
            Forest f = new Forest(args[0], s);
            f.connectGraph();
            //f.printForest();
            f.startThreads();

            try {
                sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i = 0;i<10;i++){
                System.out.println(s.pollState());
            }

        }
    }
}
