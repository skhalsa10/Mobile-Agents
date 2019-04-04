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
import java.util.LinkedList;
import static java.lang.Thread.sleep;

/**
 * Class for forest
 * Graph that connects nodes together
 */
public class Forest {
    private ArrayList<Node> forest = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private GUIState GUIStateQueue;
    private Base baseStation;
    private Node fireNode;

    /**
     * Constructs a forest
     * @param config string of config file
     * @param GUIStateQueue Queue that is used to update gui
     */
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

    /**
     * Checks to see if the config file has a valid base station and fire node
     * @return true or false
     */
    public boolean isValidConfig() {
        return isValidBaseStation() && isValidFireNode();
    }


    /**
     * Reads and stores info for nodes, edges,
     * base station, fire node from config file
     * @param line one line in config file
     */
    private void readInfo(String line) {
        //node info
        if(line.matches("^[nN]ode [0-9]+ [0-9]+[ \\s]*")) {
            addNode(line);
        }
        // edge info
        else if(line.matches("^[eE]dge [0-9]+ [0-9]+ [0-9]+ [0-9]+[ \\s]*")) {
            addEdge(line);
        }
        // base info
        else if(line.matches( "^[sS]tation [0-9]+ [0-9]+[ \\s]*")) {
            addBaseStation(line);
        }
        // fire info
        else if(line.matches("^[fF]ire [0-9]+ [0-9]+[ \\s]*")) {
            addFireNode(line);
        }
        else if (!(line.equals(""))){
            System.err.println("Cannot read line:\n" + line +
                    "\nin config file.");
            System.exit(1);
        }
    }

    /**
     * Adds node to forest
     * @param line config file line with node info
     */
    private void addNode(String line) {
        String[] parsedLine = line.split(" ");
        int x =  Integer.parseInt(parsedLine[1]);
        int y = Integer.parseInt(parsedLine[2]);
        Location location = new Location(x, y);
        //I updated this to take the GUIStateQueue
        if(nodeExists(location) && !forest.isEmpty()) {
            System.err.println("Duplicate node found in config file:\n" +
                    line);
            System.exit(1);
        }
        Node newNode = new Node (location, Node.State.NOTONFIRE, GUIStateQueue);
        forest.add(newNode);
    }

    /**
     * Adds edge to forest
     * @param line config file line with edge info
     */
    private void addEdge(String line) {
        String[] parsedLine = line.split(" ");
        int x1 = Integer.parseInt(parsedLine[1]);
        int y1 = Integer.parseInt(parsedLine[2]);
        int x2 = Integer.parseInt(parsedLine[3]);
        int y2 = Integer.parseInt(parsedLine[4]);
        Location first = new Location(x1, y1);
        Location second = new Location(x2,y2);
        Edge newEdge = new Edge(first, second);
        if(!edges.isEmpty() && checkDuplicateEdge(newEdge)) {
            System.err.println("Duplicate edge found in config file:\n" +
                    line);
            System.exit(1);
        }
        edges.add(newEdge);
    }

    /**
     * Checks if an edge is a duplicate in config file
     * @param edge current edge being read in
     * @return true or false
     */
    private boolean checkDuplicateEdge(Edge edge) {
        for(Edge e: edges) {
            if(e.equals(edge)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if node exists in forest
     * Used to check if edges, base station and fire node are valid
     * @param location location of node
     * @return true or false
     */
    private boolean nodeExists(Location location) {
        for(Node n: forest) {
            if(location.equals(n.getLocation())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds fire node to forest
     * @param line config file line with fire node info
     */
    private void addFireNode(String line) {
        String[] parsedLine = line.split(" ");
        int x = Integer.parseInt(parsedLine[1]);
        int y = Integer.parseInt(parsedLine[2]);
        Location loc = new Location(x, y);
        fireNode = new Node(loc, Node.State.NOTONFIRE, GUIStateQueue);
    }

    /**
     * Checks if fire node is a valid node in config file
     * @return true or false
     */
    private boolean isValidFireNode() {
        Node node;
        if(nodeExists(fireNode.getLocation())) {
            node = findNode(fireNode.getLocation());
            fireNode = node;
            return true;
        }
        return false;
    }

    /**
     * Adds base station to forest
     * @param line config file line with base station info
     */
    private void addBaseStation(String line) {
        String[] parsedLine = line.split(" ");
        int x = Integer.parseInt(parsedLine[1]);
        int y = Integer.parseInt(parsedLine[2]);
        Location loc = new Location(x, y);
        baseStation = new Base(loc, Node.State.NOTONFIRE, GUIStateQueue );
    }

    /**
     * Checks if base station is a valid node in config file
     * @return true or false
     */
    private boolean isValidBaseStation() {
        Node node;
        if(nodeExists(baseStation.getLocation())) {
            node = findNode(baseStation.getLocation());
            forest.remove(node);
            forest.add(baseStation);
            return true;
        }
        return false;
    }

    /**
     * Sets Distances from base station for all nodes
     * Used for agent traversal
     */
    private void setDistances() {
        LinkedList<Node> nodesToVisit = new LinkedList<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();
        setDistance(baseStation, nodesToVisit, visitedNodes);
    }

    /**
     * Helper function for setDistances()
     * Sets the distance from the base for each neighbor node
     * Uses bfs traversal to compute distances
     * @param node current node
     * @param nodesToVisit queue of nodes to visit
     * @param visitedNodes list of visited nodes
     */
    private void setDistance(Node node, LinkedList<Node> nodesToVisit, ArrayList<Node> visitedNodes) {
        int dist = node.getDistanceFromBase();
        int neighborDist;
        visitedNodes.add(node);
        for(Node n: node.getNeighbors()) {
            neighborDist = n.getDistanceFromBase();
            if(neighborDist == 0 && !(n instanceof Base)) {
                n.setDistanceFromBase(dist+1);
                nodesToVisit.add(n);
            }
        }
        if(!nodesToVisit.isEmpty()) {
            setDistance(nodesToVisit.poll(), nodesToVisit,visitedNodes);
        }
    }

    /**
     * Connects two nodes in the forest
     * @param edge edge that connects two nodes
     */
    private void connectNodes(Edge edge) {
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

    /**
     * Finds the node that corresponds to given location
     * @param loc given location
     * @return node with given location
     */
    private Node findNode(Location loc) {
        for(Node n: forest) {
            if(n.getLocation().equals(loc)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Connects nodes together to form graph
     */
    private void connectGraph() {
        for(Edge e: edges) {
            if(nodeExists(e.getFirst()) && nodeExists(e.getSecond())) {
                connectNodes(e);
            }
        }
    }

    /**
     * Print forest
     * Used for debugging purposes
     */
    private void printForest() {
        for(Node n: forest) {
            n.printNode();
            n.printNeighbors();
            n.printDistance();
        }
    }

    /**
     * Starts node threads
     */
    private void startThreads() {
        for(Node n: forest) {
            new Thread(n).start();
        }
    }

    /**
     * Starts Simulation
     */
    public void startSimulation() {
        connectGraph();
        setDistances();
        startThreads();
        fireNode.setState(Node.State.ONFIRE);
    }

    /**
     * Main entry point for simulation
     * For debugging purposes
     * @param args path of config file of graph for simulation
     */
    public static void main(String[] args) {
        if(args.length > 0) {
            GUIState s = new GUIState();
            Forest f = new Forest(args[0], s);
            f.startSimulation();
        }
    }
}
