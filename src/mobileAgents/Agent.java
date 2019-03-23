package mobileAgents;

public class Agent implements Runnable {
    private final String uid;
    private Location currentLoc;
    private Node currentNode;
    private boolean canWalk;

    //makeCopy()
    //walk()
    //sendMessage()
    public Agent(Location location, Node node, boolean canWalk) {
        this.uid = "" + location.getX() + "" + location.getY();
        this.currentLoc = location;
        this.currentNode = node;
        this.canWalk = canWalk;
    }

    public void makeCopy() {

    }

    public void run() {}



}
