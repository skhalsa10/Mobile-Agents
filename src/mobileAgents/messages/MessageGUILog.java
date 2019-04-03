package mobileAgents.messages;

/**
 * this message tells the gui that it should be treated as a log message that would have gone to the log that outputs to a file.
 * the GUI renders this as white text.
 */
public class MessageGUILog implements Message {
    private final long timeStamp;
    private String log;

    /**
     * construct message with string s
     * @param s
     */
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
