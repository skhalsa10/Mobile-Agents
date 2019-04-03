package mobileAgents.messages;

/**
 * this message is used to kill the agents when needed
 */
public class MessageKillAgent implements Message {
    private final long timeStamp;

    /**
     * Constructs message to kill agent
     */
    public MessageKillAgent() {
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
