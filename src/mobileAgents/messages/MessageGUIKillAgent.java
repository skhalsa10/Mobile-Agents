package mobileAgents.messages;

import mobileAgents.Location;

public class MessageGUIKillAgent implements Message {
    private final long timeStamp;
    private Location loc;


    public MessageGUIKillAgent(Location loc){
        this.loc = loc;
        this.timeStamp = System.nanoTime();

    }

    public Location getLoc(){
        return loc;
    }

    @Override
    public String readMessage() {
        return "Agent at (" + loc.getX() + ","+loc.getY() + ") has been killed";
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }


    @Override
    public int compareTo(Message o) {
        long result = (this.timeStamp) - o.getTimeStamp();
        if (result > 0) {
            return 1;
        } else if (result == 0) {
            return 0;
        } else {
            return -1;
        }
    }


}
