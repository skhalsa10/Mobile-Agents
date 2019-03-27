package mobileAgents.messages;

import mobileAgents.Location;
import mobileAgents.Node;

public class MessageGUINode implements Message{

    private Location location;
    private Node.State newState;
    private final long timeStamp;

    public MessageGUINode(Location nodeLoc, Node.State newState ){
        this.location = nodeLoc;
        this.newState = newState;
        this.timeStamp = System.nanoTime();
    }

    public Location getLocation() {
        return location;
    }

    public Node.State getNewState() {
        return newState;
    }

    @Override
    public String readMessage() {
        return "Node at " + location.getX() + "," + location.getY() + " is now "+ newState;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }


    @Override
    public int compareTo(Message o) {
        long result = (this.timeStamp) - o.getTimeStamp();
        if(result>0){return 1;}
        else if (result ==0){return 0;}
        else{
            return -1;
        }
    }
}


