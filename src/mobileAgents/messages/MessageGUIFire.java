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
    private final long timeStamp;

    /**
     * construct this message with a fire.
     * @param onFire
     */
    public MessageGUIFire(Location onFire){
        fire = new Location(onFire.getX(), onFire.getY());
        nearFire = new ArrayList<>();
        this.timeStamp = System.nanoTime();
    }


    public Location getFireLoc(){
        return fire;
    }

    public ArrayList<Location> getNearFireList() {
        return nearFire;
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
        String s = "Node at " + fire.getX()+","+fire.getY()+ " now ONFIRE\n";
        output.append(s);
        for (Location l: nearFire) {
            s = "Node at " + l.getX()+","+l.getY()+ " now NEARFIRE\n";
            output.append(s);
        }
        return output.toString();
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
