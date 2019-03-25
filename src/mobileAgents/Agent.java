package mobileAgents;

import java.util.ArrayList;

public class Agent implements Runnable {
    private final String uid;
    private Location currentLoc;
    private Node currentNode;
    private boolean canWalk;


    //walk()
    //sendMessage()
    public Agent(Location location, Node node, boolean canWalk) {
        this.uid = "" + location.getX() + "" + location.getY();
        this.currentLoc = location;
        this.currentNode = node;
        this.canWalk = canWalk;
    }

    // Haven't tested yet
    public void makeCopy() {
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
    public void walk() {
        //start from current location

        // choose a neighbor node
        //

    }

    public void run() {
        //while(canWalk) {
        // walk();
        // }
        //makeCopy();
    }




}
