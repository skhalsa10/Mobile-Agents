package mobileAgents.messages;

import mobileAgents.Node;

public class MessageAgentNotify implements Message{
    private final long timeStamp;
    private final Node.State state;

    public MessageAgentNotify(Node.State state){

        this.timeStamp = System.nanoTime();
        this.state = state;
    }

    public Node.State getState() {
        return state;
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
