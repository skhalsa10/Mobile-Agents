package mobileAgents;

import mobileAgents.messages.Message;
import mobileAgents.messages.MessageAgentNotify;
import mobileAgents.messages.MessageGUIFire;

import java.util.ArrayList;
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

    private Location location;
    private ArrayList<Node> neighbors = new ArrayList<>();
    private State state;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private GUIState GUIStateQueue;
    private Agent agent;
    private int distanceFromBase;

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
    }

    /**
     * initialize node with default state of NOTONFIRE
     * @param location
     */
    public Node(Location location, GUIState GUIStateQueue) {
        this.GUIStateQueue = GUIStateQueue;
        this.location = location;
        this.state = State.NOTONFIRE;
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
            startFireTimer();
            if(agent != null){
                agent.sendMessage(new MessageAgentNotify(State.NEARFIRE));
            }
        }
        else if(state == State.ONFIRE) {
            //creating a Message that keeps track of what location is on fire
            MessageGUIFire m = new MessageGUIFire(this.getLocation());
            checkNeighbors(m);
            GUIStateQueue.putState(m);
        }
        notifyAll();
        //printNode();
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

    //TODO public synchronized? sendMessage();

    /**
     * this method will start the fire timer which will change the state of this node to ONFIRE
     */
    public synchronized void startFireTimer() {
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


    //TODO still need to work on this
    public void processMessage(Message message) throws InterruptedException {
        messages.put(message);
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

    public Agent getAgent() {
        return agent;
    }


    public int getDistanceFromBase() {
        return distanceFromBase;
    }

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



    public void printDistance() {
        System.out.println("Has Distance: " + getDistanceFromBase());
    }

    public synchronized void createAgent(boolean canWalk) {
        Agent newAgent = new Agent(getLocation(),this,canWalk);
        if(!canWalk) {
            agent = newAgent;
        }
        newAgent.printAgent();
        printNode();
    }

    public synchronized void setAgent(Agent agent) {
        this.agent = agent;
    }



    @Override
    public void run(){
        if(this instanceof Base && agent == null) {
            createAgent(true);
        }
        while(true) {
            //printNode();

        }
    }

    public enum State { ONFIRE, NEARFIRE, NOTONFIRE, DEAD}

}
