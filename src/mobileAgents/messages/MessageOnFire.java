package mobileAgents.messages;

/**
 * Message for when a node is on fire
 */
public class MessageOnFire implements Message {
    private final long timeStamp;

    /**
     * Constructs a message to kill a node
     */
    public MessageOnFire() {
        this.timeStamp = System.nanoTime();
    }

    @Override
    public String readMessage() {
        return null;
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
