package mobileAgents;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Timer;
import java.util.TimerTask;


public class Node implements Runnable {

    private Location location;
    private ArrayList<Node> neighbors = new ArrayList<>();
    private State state;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    
    public Node(Location location, State state) {
        this.location = location;
        this.state = state;
    }

    //another node constructor without state
    public Node(Location location) {
        this.location = location;
        this.state = State.NOTONFIRE;
    }

    public void addNeighbor(Node node) {
        neighbors.add(node);
    }


    public synchronized void setState(State nextState) {
        state = nextState;
        if(state == State.NEARFIRE) {
            spreadFire();
        }
    }

    //public synchronized? sendMessage();

    public synchronized void spreadFire() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setState(State.ONFIRE);
                System.out.println("Node at " + location.getX() + " " + location.getY() + " is on fire");
            }
        }, 5000);
    }

    public void processMessage(Message message) throws InterruptedException {
        messages.put(message);
    }


    @Override
    public void run(){}

    public enum State { ONFIRE, NEARFIRE, NOTONFIRE, DEAD}

    public static void main (String[] args) {
        Location location = new Location(0,0);
        State state = State.NEARFIRE;
        Node node = new Node(location,state);
        node.run();
        node.setState(State.NEARFIRE);

    }

}
