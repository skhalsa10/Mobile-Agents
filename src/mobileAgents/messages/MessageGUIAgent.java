package mobileAgents.messages;

import mobileAgents.Location;

/**
 * this message is also a simple message. An agent can either be cloned in a place where it can NOT walk.or it can walk from
 * a previous location.
 *
 * If this agent is a walker set the location of where it walkedFrom if it is not a walker ignore this method. The gui will handle the rest.
 */
public class MessageGUIAgent implements Message {
    private Location agentLoc;
    private Location movedFrom;
    private final long timeStamp;

    /**
     * contstruct this message with the location of either the new agent or where the current agent moved to
     * @param newAgentLoc
     */
    public MessageGUIAgent(Location newAgentLoc){
        this.timeStamp = System.nanoTime();
        this.agentLoc = new Location(newAgentLoc.getX(),newAgentLoc.getY());
        this.movedFrom = null;

    }

    /**
     * set the location of where the agent moved from
     * @param movedFrom
     */
    public void movedFrom(Location movedFrom){
        this.movedFrom = new Location(movedFrom.getX(),movedFrom.getY());
    }

    /**
     * the location of where the agent walked to or where a new agent was created.
     * @return
     */
    public Location getAgentLoc() {
        return agentLoc;
    }

    /**
     *
     * @return the  the agent moved from while walking
     */
    public Location getMovedFrom() {
        return movedFrom;
    }

    /**
     * string representation of the Agent state change
     * @return
     */
    @Override
    public String readMessage() {
        if(movedFrom == null){
            return "Agent created at ("+agentLoc.getX()+","+agentLoc.getY()+")";
        }else {
            return "Agent moved from (" + movedFrom.getX() + "," + movedFrom.getY() + ") to (" + agentLoc.getX() + "," + agentLoc.getY() + ")";
        }
    }

    /**
     * needed for the priority queue
     * @return
     */
    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * needed for the priority queue
     * @param o
     * @return
     */
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
