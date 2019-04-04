package mobileAgents.messages;

import mobileAgents.Location;

/**
 * this is the message that gets sent back to the base from any agent discoveries in the field.
 */
public class MessageLog implements Message{
    private Location createdLoc;
    private String uid;
    private final long timeStamp;

    public MessageLog(String agentUid, Location loc) {
        this.uid = agentUid;
        this.createdLoc = loc;
        this.timeStamp = System.nanoTime();
    }
    @Override
    public String readMessage() {
        return "Agent " + uid + " is near a fire at (" + createdLoc.getX() + "," + createdLoc.getY() + ")";
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
