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
    private ArrayList<Node> visitedNodes = new ArrayList<>();

    //sendMessage()

    /**
     * Constructs an agent
     * @param location current location of agent
     * @param node node where agent is currently on
     * @param canWalk boolean to determine if agent can walk or not
     */
    public Agent(Location location, Node node, boolean canWalk) {
        this.uid = "" + location.getX() + "" + location.getY();
        this.currentLoc = location;
        this.currentNode = node;
        this.canWalk = canWalk;
        visitedPath.push(this.currentNode);
        visitedNodes.add(this.currentNode);
        new Thread(this).start();
    }

    // Haven't tested yet
    public synchronized void makeCopy() {
        ArrayList<Node> neighbors = currentNode.getNeighbors();
        for(Node n: neighbors) {
            if(n.getAgent() != null && n.getState() != Node.State.ONFIRE) {
                n.createAgent(false);
            }
        }
    }

    // queue/stack for visited nodes?
    //Simple traversal BFS or DFS
    // fire spreads by "levels" in our simulation so DFS sufficient?
    // do we need a timer?
    public synchronized void walkOneNode() {
        if(hasPath()) {
            Node nextNode = walkToNext();
            if(nextNode != null) {
                currentNode = nextNode;
                visitedPath.push(currentNode);
                visitedNodes.add(currentNode);
            }
            // back track
            else {
                if(!visitedPath.isEmpty()) {
                    currentNode = visitedPath.pop();
                }
            }
        }
        if(currentNode.getState() == Node.State.NEARFIRE) {
            canWalk = false;
        }
        if(canWalk) {
            startWalkTimer();
        }
        System.out.println("Base agent node is at: ");
        currentNode.printNode();
    }

    public boolean hasPath() {
        for(Node n: currentNode.getNeighbors()) {
            if(n.getState() != Node.State.ONFIRE) {
                return true;
            }
        }
        return false;
    }

    public synchronized void startWalkTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                walkOneNode();
                timer.cancel();
            }
        }, 1000);
    }

    public Node walkToNextRandomNode() {
        Random rand = new Random();
        ArrayList<Node> neighbors = currentNode.getNeighbors();
        int i = rand.nextInt(neighbors.size());
        Node nextNode = neighbors.get(i);
        if(nextNode.getState() != Node.State.ONFIRE) {
            return nextNode;
        }
        return walkToNextRandomNode();
    }
    public synchronized Node walkToNext() {
        for(Node n: currentNode.getNeighbors()) {
            if(!visitedNodes.contains(n) && n.getState() != Node.State.ONFIRE) {
                return n;
            }
        }
        return null;
    }

    public void printVisitedNodes() {
        System.out.println("Visited Nodes: ");
        for(Node n: visitedPath) {
            n.printNode();
        }
    }

    public void run() {
        startWalkTimer();
       // makeCopy();
    }

    public static void main(String[] args) {

    }




}
