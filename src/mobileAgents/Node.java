package mobileAgents;

import mobileAgents.messages.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class represent the sensor nodes of our MobileAGent Application
 *
 * @version 1
 * @author Mostly Michelle but a little of Siri
 */
public class Node implements Runnable {

    protected Location location;
    protected ArrayList<Node> neighbors = new ArrayList<>();
    protected State state;
    protected LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    protected GUIState GUIStateQueue;
    protected Agent agent;
    protected int distanceFromBase;

    //public Node() {}

    /**
     * initializes node with a location and given state is this needed?
     * @param location
     * @param state
     */
    public Node(Location location, State state, GUIState GUIStateQueue) {
        this.GUIStateQueue = GUIStateQueue;
        this.location = location;
        this.state = state;
        this.agent = null;
        distanceFromBase = 0;
    }

    /**
     * initialize node with default state of NOTONFIRE
     * @param location
     */
    public Node(Location location, GUIState GUIStateQueue) {
        this.GUIStateQueue = GUIStateQueue;
        this.location = location;
        this.state = State.NOTONFIRE;
        this.agent = null;
        distanceFromBase = 0;

    }

    /**
     * adds node to list of neighbors.
     * @param node
     */
    public void addNeighbor(Node node) {
        neighbors.add(node);
    }


    /**
     * This will set the state to the next state it will fire off appropriate methods that should happen
     * after the state changes
     * @param nextState
     */
    public synchronized void setState(State nextState) {
        state = nextState;
        if(state == State.NEARFIRE) {
            MessageGUINode message = new MessageGUINode(this.getLocation(),state);
            if(agent != null) {
                //printNode();
                agent.getMessageFromNode(message);
            }
            startFireTimer();
        }
        else if(state == State.ONFIRE) {
            //creating a Message that keeps track of what location is on fire
            MessageGUIFire m = new MessageGUIFire(this.getLocation());
            MessageKillAgent messageKillAgent = new MessageKillAgent();
            if(agent != null) {
                agent.getMessageFromNode(messageKillAgent);
            }
            killMe();
            checkNeighbors(m);
            GUIStateQueue.putState(m);
        }
        notifyAll();
        printNode();
    }

    /**
     * Sends a kill message to the node itself
     */
    private void killMe() {
        MessageKillNode m = new MessageKillNode();
        try {
            messages.put(m);
        }
        catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    /**
     * this will Iterate through all neighbors and set the state to NEARFIRE if applicable
     *
     * I have added
     *
     */
    private synchronized void checkNeighbors(MessageGUIFire m) {
        for(Node n: neighbors) {
            if(checkCurrentState(n)) {
                n.setState(State.NEARFIRE);
                m.addNearFireLoc(n.getLocation());
            }
        }
    }

    /**
     *
     * @param neighbor checks to see if neighbor can can be set to nearfire state
     * @return true if the node is NOTONFIRE state else FALSE
     */
    private boolean checkCurrentState(Node neighbor) {
        if(neighbor.getState() == State.NOTONFIRE) {
            return true;
        }
        return false;
    }

    /**
     * this method will start the fire timer which will change the state of this node to ONFIRE
     */
    private synchronized void startFireTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setState(State.ONFIRE);
                //System.out.println("Node at " + location.getX() + " " + location.getY() + " is on fire");
                timer.cancel();
            }
        }, 2000);
    }

    /**
     * Processes a given message
     * @param m message that was received
     */
    public synchronized void processMessage(Message m) {
        try {
            messages.put(m);
        }
        catch(InterruptedException e) {
            System.err.println(e);
        }
    }

    /**
     *
     * @return the Location of the Node
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @return returns a list of all
     */
    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    /**
     *
     * @return the current state of the node
     */
    public synchronized State getState() {
        notifyAll();
        return state;
    }

    /**
     * Gets the agent on this node
     * @return agent on node
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * Gets distance from base
     * Used for message routing
     * @return distance from base
     */
    public int getDistanceFromBase() {
        return distanceFromBase;
    }

    /**
     * Sets the distance from base
     * Used for message routing
     * @param distance distance from base
     */
    public void setDistanceFromBase(int distance) {
        this.distanceFromBase = distance;
    }

    /**
     * prints out the location and state of the node to System out
     */
    public void printNode() {
        System.out.println("Node at: " + getLocation().getX() + " " + getLocation().getY() + " " + state);
    }

    /**
     * this will print out all neighboring nodes for this node.
     */
    public void printNeighbors() {
        System.out.println("Neighbors: ");
        for(Node n: neighbors) {
            n.printNode();
        }
        System.out.println();
    }

    /**
     * Prints distance for debugging purposes
     */
    public void printDistance() {
        System.out.println("Has Distance: " + getDistanceFromBase());
    }

    /**
     * Create Agent at this node
     * @param canWalk determines if node can walk or not
     */
    public synchronized void createAgent(boolean canWalk) {
        Agent newAgent = new Agent(getLocation(),this,canWalk,GUIStateQueue);
        if(!canWalk) {
            agent = newAgent;
        }
        newAgent.printAgent();
        printNode();
    }

    /**
     * Sets the agent at this node
     * @param agent agent on this node
     */
    public synchronized void setAgent(Agent agent) {
        this.agent = agent;
    }


    /**
     * Checks to see if a message can be sent
     * @return true or false
     */
    private boolean canSendMessage() {
        for(Node n: neighbors) {
            if(n.getState() != State.ONFIRE) {
                return true;
            }
        }
        return false;
    }

    /**
     * In the case where there isn't a neighbor node closest to base
     * pick any node
     * @return node that was picked
     */
    private Node chooseAnyNode() {
        ArrayList<Node> availableNodes = new ArrayList<>();
        for(Node n: neighbors) {
            if(n.getState() != State.ONFIRE) {
                availableNodes.add(n);
            }
        }
        return getRandomNextNode(availableNodes);
    }

    /**
     * Randomly picks any node for the list of available nodes
     * according to distance from base
     * @param availableNodes list of available nodes
     * @return node to send message to
     */
    private Node getRandomNextNode(ArrayList<Node> availableNodes) {
        Random rand = new Random();
        int i;
        if(availableNodes.isEmpty()) {
            return chooseAnyNode();
        }
        i = rand.nextInt(availableNodes.size());
        return availableNodes.get(i);
    }

    /**
     * Picks the next node to send message to
     * @return next node to receive message
     */
    private Node pickNextNode() {
        ArrayList<Node> availableNodes = new ArrayList<>();
        for(Node n: neighbors) {
            // Send immediately to base
            if(n instanceof Base) {
                return n;
            }
            // Send message to node closest to base
            if(n.getState() != State.ONFIRE && n.getDistanceFromBase() <= distanceFromBase) {
                availableNodes.add(n);
            }
        }
        return getRandomNextNode(availableNodes);
    }

    /**
     * Node keeps running until it's on fire
     * Processes messages from its message queue to either
     * send a message to another node or to end itself
     */
    @Override
    public void run(){
        while(state != State.ONFIRE) {
            try {
                Message newMessage = messages.take();
                Node sendToNode;
                if(canSendMessage() && !(newMessage instanceof MessageKillNode)) {
                    sendToNode = pickNextNode();
                    sendToNode.processMessage(newMessage);
                }
            }
            catch(InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    public enum State { ONFIRE, NEARFIRE, NOTONFIRE, DEAD}

}
