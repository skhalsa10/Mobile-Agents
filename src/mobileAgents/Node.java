package mobileAgents;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {
    //private final Location location;
    private Location location;
    private ArrayList<Node> neighbors = new ArrayList<>();
    private State state;
    //TODO possible timer to trigger fire spread
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private Agent agent;

    private int distanceFromBase;

    public Node() {}

    public Node(Location location, State state) {
        this.location = location;
        this.state = state;
        this.agent = null;
        distanceFromBase = 0;
    }

    public void addNeighbor(Node node) {
        neighbors.add(node);
    }


    public synchronized void setState(State nextState) {
        state = nextState;
        if(state == State.NEARFIRE) {
            startFireTimer();
        }
        else if(state == State.ONFIRE) {
            checkNeighbors();
        }
        notifyAll();
        printNode();
    }

    //might not need synch
    public synchronized void checkNeighbors() {
        //System.out.println("checkNeighbors called");
        //System.out.println("size: " + neighbors.size());
        for(Node n: neighbors) {

            if(checkCurrentState(n)) {
                //System.out.println("checkCurrentstate");
                n.setState(State.NEARFIRE);
            }
        }
    }

    public boolean checkCurrentState(Node neighbor) {
        if(neighbor.getState() == State.NOTONFIRE) {
            return true;
        }
        return false;
    }

    //public synchronized? sendMessage();

    public synchronized void startFireTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setState(State.ONFIRE);
                System.out.println("Node at " + location.getX() + " " + location.getY() + " is on fire");
            }
        }, 2000);
    }



    public void processMessage(Message message) throws InterruptedException {
        messages.put(message);
    }

    public Location getLocation() {
        return location;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

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

    public void printNode() {
        System.out.println("Node at: " + getLocation().getX() + " " + getLocation().getY() + " " + state);
    }

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





    public enum State { ONFIRE, NEARFIRE, NOTONFIRE, DEAD}

    @Override
    public void run(){
        if(this instanceof Base && agent == null) {
            createAgent(true);
        }
        while(true) {
            //printNode();

        }

    }

    public static void main (String[] args) {
        Location location = new Location(0,0);
        State state = State.NEARFIRE;
        Node node = new Node(location,state);
        node.run();
        node.setState(State.NEARFIRE);

    }

}
