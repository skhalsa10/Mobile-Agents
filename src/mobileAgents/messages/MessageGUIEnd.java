package mobileAgents.messages;

/**
 * this message class is simple. it represents a sim that has completed.
 * there should only be one created and it should be at the tail of the GUI state queue
 */
public class MessageGUIEnd implements Message {
    private final long timeStamp;

    public MessageGUIEnd(){
        this.timeStamp = System.nanoTime();
    }


    @Override
    public String readMessage() {
        return "Simulation has completed and is over!";
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
