package mobileAgents.messages;

/**
 * this MessageGUIConfig  message basically just encapsulates the StringBuilder class.
 * There are two constructors.
 *
 * 1. an empty one that  does not start the message with any string.
 * 2. another that takes a string.
 *
 * when building this message it needs to follow that config spec like so:
 *
 * node 0 0\n
 * node 3 4\n
 * node 2 3\n
 * node 5 5\n
 * edge 0 0 2 3\n
 * edge 2 3 3 4\n
 * edge 3 4 5 5\n
 * edge 5 5 0 0\n
 * station 0 0\n
 * fire 5 5
 */
public class MessageGUIConfig implements Message {

    private StringBuilder configRep;
    private final long timeStamp;


    /**
     * construct a new MessageGUIConfig with an empty string
     */
    public MessageGUIConfig(){

        configRep = new StringBuilder();
        this.timeStamp = System.nanoTime();
    }

    /**
     * construct new message with input string as initial string
     * @param string
     */
    public MessageGUIConfig(String string){

        configRep = new StringBuilder(string);
        this.timeStamp = System.nanoTime();
    }

    /**
     * append s to the message string
     * @param s string to append to the end of the config string
     */
    public void appendStr(String s){
        configRep.append(s);
    }


    /**
     * this returns a string representation of this message.
     * @return
     */
    @Override
    public String readMessage() {
        return configRep.toString();
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
