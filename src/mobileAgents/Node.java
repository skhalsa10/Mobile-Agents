package mobileAgents;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {
    private final Location location;
    private ArrayList<Node> neighbors = new ArrayList<>();
    private State state;
    //TODO possible timer to trigger fire spread
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    public Node(Location location, State state) {
        this.location = location;
        this.state = state;
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
                System.out.println("Change state");
            }
        }, 20000);
    }

    public void processMessage(Message message) throws InterruptedException {
        messages.put(message);
    }

    //TODO getters




    public enum State { ONFIRE, NEARFIRE, NOTONFIRE, DEAD}

    @Override
    public void run(){}

    public static void main (String[] args) {
        Location location = new Location(0,0);
        State state = State.NEARFIRE;
        Node node = new Node(location,state);
        node.run();
        node.setState(State.NEARFIRE);

    }

}
