package mobileAgents;

import java.util.*;

/**
 * Agent Class
 */
public class Agent implements Runnable {
    private final String uid;
    private Location currentLoc;
    private Node currentNode;
    private boolean canWalk;
    private Stack<Node> visitedPath = new Stack<>();
    private HashSet<Node> visitedNodes = new HashSet<>();

    //sendMessage()

    /**
     * Constructs an agent
     * @param location current location of agent
     * @param node node where agent is currently on
     * @param canWalk boolean to determine if agent can walk or not
     */
    public Agent(Location location, Node node, boolean canWalk) {
        //First agent is Alpha
        if(canWalk) {
            this.uid = "Alpha";
        }
        else {
            this.uid = "" + location.getX() + "" + location.getY();
        }
        this.currentLoc = location;
        this.currentNode = node;
        this.canWalk = canWalk;
        new Thread(this).start();
    }

    /**
     * Makes a copy of the agent on each neighbor that's not on fire
     */
    private synchronized void makeCopy() {
        System.out.println("Created in copy");
        ArrayList<Node> neighbors = currentNode.getNeighbors();
        for(Node n: neighbors) {
            if(n.getAgent() == null && n.getState() != Node.State.ONFIRE) {
                n.createAgent(false);
                //n.getAgent().printAgent();
            }
        }
    }

    /**
     * Agent walks until a near fire node is found
     * Uses a dfs traversal
     */
    // Make sure state of next node doesn't change from near fire to on fire
    private void walk() {
        visitedNodes.add(currentNode);
        if(currentNode.getState() == Node.State.NEARFIRE) {
            canWalk = false;
            currentNode.setAgent(this);
            System.out.println("Agent should stop walking");
            return;
        }
        if(hasPath() && canWalk) {
            Node nextNode = walkToNext();
            if(nextNode != null) {
                visitedPath.push(currentNode);
                currentNode = nextNode;
                //System.out.println("Agent " + uid + " is at: ");
                //currentNode.printNode();
                walk();
            }
            // back track
            else {
                if(!visitedPath.empty()) {
                    currentNode = visitedPath.pop();
                    //System.out.println("backtrack");
                    //System.out.println("Agent " + uid + " is at: ");
                    //currentNode.printNode();
                    walk();
                }
            }
        }
    }

    /**
     * Checks to see if agent has path to walk
     * @return true or false
     */
    private boolean hasPath() {
        for(Node n: currentNode.getNeighbors()) {
            if(n.getState() != Node.State.ONFIRE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function for walkToNext() method
     * Picks a the next node to walk to randomly
     * @param availableNodes nodes that agent can walk to
     * @return next node to walk to
     */
    private Node pickNextRandomNode(ArrayList<Node> availableNodes) {
        if(availableNodes.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        int i = rand.nextInt(availableNodes.size());
        return availableNodes.get(i);
    }

    /**
     * Chooses the next node to walk to
     * @return next node to walk to
     */
    private Node walkToNext() {
        ArrayList<Node> availableNodes = new ArrayList<>();
        for(Node n: currentNode.getNeighbors()) {
            if(!visitedNodes.contains(n) && n.getState() != Node.State.ONFIRE) {
                availableNodes.add(n);
            }
        }
        return pickNextRandomNode(availableNodes);
    }

    /**
     * Prints visited nodes
     * Used for debugging purposes
     */
    public void printVisitedNodes() {
        System.out.println("Visited Nodes: ");
        for(Node n: visitedNodes) {
            n.printNode();
        }
    }

    public void printVisitedPath() {
        System.out.println("Visited Path: ");
        for(Node n: visitedPath) {
            n.printNode();
        }
    }

    /**
     * Runs the agent
     */
    public void run() {
        System.out.println("In run method");
        printAgent();
        if(canWalk) {
            walk();
        }
        synchronized (this) {
            while(currentNode.getState() != Node.State.NEARFIRE) {
                currentNode.printNode();
                try {
                    System.out.println("are you waiting???");
                    this.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            makeCopy();
        }


    }

    /**
     * Prints agent's uid
     * For debugging purposes
     */
    public void printAgent() {
        System.out.println("Agent " + uid);
    }
}
