package mobileAgents;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import mobileAgents.messages.Message;
import mobileAgents.messages.MessageGUIAgent;
import mobileAgents.messages.MessageGUINode;
import mobileAgents.messages.MessageKillAgent;

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
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private boolean killAgent = false;
    private GUIState GUIStateQueue;


    //sendMessage()

    /**
     * Constructs an agent
     * @param location current location of agent
     * @param node node where agent is currently on
     * @param canWalk boolean to determine if agent can walk or not
     */
    public Agent(Location location, Node node, boolean canWalk, GUIState GUIStateQueue) {
        //First agent is Alpha
        if(canWalk) {
            this.uid = "Alpha";
            System.out.println("created agent alpha");
        }
        else {
            this.uid = "" + location.getX() + "" + location.getY();
        }
        this.currentLoc = location;
        this.currentNode = node;
        this.canWalk = canWalk;
        this.GUIStateQueue = GUIStateQueue;
        MessageGUIAgent m = new MessageGUIAgent(currentNode.getLocation());
        GUIStateQueue.putState(m);
        new Thread(this).start();
    }

    /**
     * Makes a copy of the agent on each neighbor that's not on fire
     */
    private synchronized void makeCopy() {
        ArrayList<Node> neighbors = currentNode.getNeighbors();
        for(Node n: neighbors) {
            if(n.getAgent() == null && n.getState() != Node.State.ONFIRE) {
                n.createAgent(false);
                System.out.println("Agent " + uid + " created agent " + n.getAgent().getUid());
            }
        }
    }

    public String getUid() {
        return uid;
    }

    /**
     * Agent walks until a near fire node is found
     * Uses a dfs traversal
     */
    // Make sure state of next node doesn't change from near fire to on fire
    private void walk() {
        printVisitedPath();
        Node movedFromNode = currentNode;
        visitedNodes.add(currentNode);
        if(currentNode.getState() == Node.State.NEARFIRE) {
            canWalk = false;
            currentNode.setAgent(this);
            System.out.println("Agent stopped walking at: ");
            currentNode.printNode();
            //printVisitedPath();
            MessageGUIAgent m = new MessageGUIAgent(currentNode.getLocation());
            //m.movedFrom(movedFromNode.getLocation());
            GUIStateQueue.putState(m);
            makeCopy();
            return;
        }
        if(hasPath() && canWalk) {
            Node nextNode = walkToNext();
            if(nextNode != null) {
                visitedPath.push(currentNode);
                currentNode = nextNode;
                walk();
            }
            // back track
            else {
                if(!visitedPath.empty()) {
                    currentNode = visitedPath.pop();
                    walk();
                }
            }
            MessageGUIAgent m = new MessageGUIAgent(currentNode.getLocation());
            m.movedFrom(movedFromNode.getLocation());
            GUIStateQueue.putState(m);
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

    public synchronized boolean checkCurrentNodeNearFire(Node.State state){
        if(state == Node.State.NEARFIRE) {
            return true;
        }
        return false;
    }

    public synchronized void getMessageFromNode(Message m) {
        try {
            messages.put(m);
        }
        catch (InterruptedException e) {
            System.err.println(e);
        }
    }
    /**
     * Runs the agent
     */
    public void run() {
        if(canWalk) {
            walk();
        }
        while(!killAgent) {
            try {
                Message newMessage = messages.take();
                if(newMessage instanceof MessageGUINode) {
                    if(((MessageGUINode) newMessage).getNewState() == Node.State.NEARFIRE){
                        makeCopy();
                    }
                }
                if(newMessage instanceof MessageKillAgent) {
                    killAgent = true;

                }
            }
            catch(InterruptedException e) {
                System.err.println(e);
            }
        }
        System.out.println("Agent " + uid + " got killed");
    }

    /**
     * Prints agent's uid
     * For debugging purposes
     */
    public void printAgent() {
        System.out.println("Agent " + uid);
    }
}
