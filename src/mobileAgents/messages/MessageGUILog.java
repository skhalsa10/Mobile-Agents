package mobileAgents.messages;

public class MessageGUILog implements Message {
    private final long timeStamp;
    private String log;

    public MessageGUILog(String s){
        this.timeStamp = System.nanoTime();
        this.log = s;
    }
    @Override
    public String readMessage() {
        return log;
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
