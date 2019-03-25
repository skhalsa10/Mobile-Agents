package mobileAgents.messages;

/**
 * this message class is simple. it represents a sim that has completed.
 * there should only be one created and it should be at the tail of the GUI state queue
 */
public class MessageGUIEnd implements Message {


    public MessageGUIEnd(){}


    @Override
    public String readMessage() {
        return "Simulation has completed and is over!";
    }
}
