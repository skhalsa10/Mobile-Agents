package mobileAgents.messages;

import mobileAgents.Location;

import java.util.ArrayList;

/**
 * this message  class will be used to to send a list of new agents that are created to the gui. this message
 * should get sent to the GUI in the scenario that an agent is currently sitting on a blue node. as soon as the node turns
 * to a nearfire the agent replicates to all neighbors. this message can be used to send a list of all the neightbors locations where new agents will be spawned.
 * the gui than can update the state without the "Lag" that we are experiencing. and all agents will render in the list at once.
 */
public class MessageGUICopyAgents implements Message {
    private ArrayList<Location> newAgents;
    private final long timeStamp;

    /**
     * initializes this message with an empty list of new agents.
     *
     * You can always use the other methods to add more locations to the list representing new agents
     */
    public MessageGUICopyAgents(){
        this(new ArrayList<Location>());
    }

    /**
     * this will initialize the message with the input of list of new agents
     * @param newAgents
     */
    public MessageGUICopyAgents(ArrayList<Location> newAgents){
        this.timeStamp = System.nanoTime();
        this.newAgents = newAgents;
    }

    /**
     * this will add a single location of a new agent into the list of new agents
     * @param newAgentLoc
     */
    public void putNewAgentLoc(Location newAgentLoc){
        newAgents.add(newAgentLoc);
    }

    /**
     * returns the list of newly created agents
     * @return
     */
    public ArrayList<Location> getNewAgentsList() {
        return newAgents;
    }

    /**
     * this will return a string representing the message
     * @return
     */
    @Override
    public String readMessage() {
        StringBuilder output = new StringBuilder();
        String s = null;
        output.append(s);
        for (Location l: newAgents) {
            s = "New Agent has been created at (" + l.getX()+","+l.getY()+ ")\n";
            output.append(s);
        }
        return output.toString();
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
