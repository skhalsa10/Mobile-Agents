package mobileAgents.messages;

import mobileAgents.Location;

public class MessageLog implements Message{
    private Location createdLoc;
    private int uid;
    private final long timeStamp;

    public MessageLog(int agentUid, Location loc) {
        this.uid = agentUid;
        this.createdLoc = loc;
        this.timeStamp = System.nanoTime();
    }
    @Override
    public String readMessage() {
        return "Agent " + uid + "was created near a fire at (" + createdLoc.getX() + "," + createdLoc.getY() + ")";
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
