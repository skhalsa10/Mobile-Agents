package mobileAgents;

import mobileAgents.messages.*;

import java.io.IOException;

/**
 * the Base is a special node. it does everything a node does but  it will create an agent.
 */
public class Base extends Node{

    private Log log;

    public Base(Location location, State state, GUIState GUIStateQueue) {
        super(location, state, GUIStateQueue);
        try {
            log = new Log();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * this base station runs differently than other nodes. the messages get sent to it
     * it just processes the messages and sends them to the log. and a copy to the gui.
     *
     * It does this until it is on fire and dies.
     */
    @Override
    public void run(){

        createAgent(true);
        Message m = null;
        //TODO what if this thread is sleeping waiting for messages while it goes on fire. a node can never receive a message while it is on fire so it will never wake up and close gracefully. we may need to send a message to itsself while it changes to onfire.
        boolean alive = true;
        while(state != State.ONFIRE){
            try {
                m = messages.take();
                if(m instanceof MessageOnFire) {
                    setState(State.ONFIRE);
                }
                else if (m instanceof MessageNearFire) {
                    setState(State.NEARFIRE);
                }
                else if (!(m instanceof MessageKillNode)) {
                    log.logMessage(m);
                    GUIStateQueue.putState(new MessageGUILog(m.readMessage()));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        log.logString("BASE is now on fire!!! we doomed! ***Sings the doom song in the voice of Gir from Invader Zim*** doom de doom de doom!");
        GUIStateQueue.putState(new MessageGUILog("BASE is now on fire!!! we doomed! ***Sings the doom song in the voice of Gir from Invader Zim*** doom de doom de doom!"));
    }

}
