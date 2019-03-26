package mobileAgents;

import java.util.*;

public class Agent implements Runnable {
    private final String uid;
    private Location currentLoc;
    private Node currentNode;
    private boolean canWalk;
    private Stack<Node> visitedPath = new Stack<>();
    private ArrayList<Node> visitedNodes = new ArrayList<>();

    //sendMessage()
    public Agent(Location location, Node node, boolean canWalk) {
        this.uid = "" + location.getX() + "" + location.getY();
        this.currentLoc = location;
        this.currentNode = node;
        this.canWalk = canWalk;
        visitedPath.push(this.currentNode);
        visitedNodes.add(this.currentNode);
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
    public synchronized void walk() {
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
            //System.out.println("Base agent node is at: ");
            currentNode.printNode();
        }
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
                walk();
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
    public Node walkToNext() {
        for(Node n: currentNode.getNeighbors()) {
            if(!visitedNodes.contains(n) && n.getState() != Node.State.ONFIRE) {
                /*if(!visitedNodes.contains(n)){
                    System.out.println("node is unvisited: ");
                    n.printNode();
                    printVisitedNodes();
                }*/
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
        while(canWalk) {
            if(currentNode.getState() == Node.State.NEARFIRE) {
                canWalk = false;
                System.out.println("base node at: ");
                currentNode.printNode();
            }
            startWalkTimer();
        }
       // makeCopy();
    }




}
