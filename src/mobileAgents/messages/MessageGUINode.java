package mobileAgents.messages;

import mobileAgents.Location;
import mobileAgents.Node;

public class MessageGUINode implements Message{

    private Location location;
    private Node.State newState;

    public MessageGUINode(Location nodeLoc, Node.State newState ){
        this.location = nodeLoc;
        this.newState = newState;
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

    public static void main(String[] args){
        MessageGUINode m = new MessageGUINode(new Location(0,0),Node.State.DEAD);

        System.out.println(m.readMessage());
    }
}


