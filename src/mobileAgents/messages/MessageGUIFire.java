package mobileAgents.messages;

import mobileAgents.Location;

import java.util.ArrayList;

/**
 * this message will keep track of a node that burst into flame  and the surrounding nodes that now have a NEARFIRE state
 * you initialize the Message with a Location of the ONFIRE and add the locations of the new NEARFIRE
 */
public class MessageGUIFire implements Message {
    private Location fire;
    private ArrayList<Location> nearFire;

    /**
     * construct this message with a fire.
     * @param onFire
     */
    public MessageGUIFire(Location onFire){
        fire = new Location(onFire.getX(), onFire.getY());
        nearFire = new ArrayList<>();
    }

    /**
     * this will add the location representing a node with NEARFIRE state
     * @param nearFireLoc
     */
    public void addNearFireLoc(Location nearFireLoc){
        nearFire.add(new Location(nearFireLoc.getX(),nearFireLoc.getY()));
    }

    /**
     * this will return a string representing the message
     * @return
     */
    @Override
    public String readMessage() {
        StringBuilder output = new StringBuilder();
        output.append("Node at " + fire.getX()+","+fire.getY()+ " now ONFIRE\n");
        for (Location l: nearFire) {
            output.append("Node at " + l.getX()+","+l.getY()+ " now NEARFIRE\n");
        }
        return output.toString();
    }
}
