package mobileAgents.messages;

public class MessageAgentKill implements Message{
    private final long timeStamp;

    public MessageAgentKill(){
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
