package mobileAgents;

import mobileAgents.messages.Message;
import mobileAgents.messages.MessageGUIEnd;
import mobileAgents.messages.MessageGUILog;

import java.io.IOException;

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
     * this base station runs differently than other nodes. the messages get sent
     */
    @Override
    public void run(){

        createAgent(true);
        Message m = null;
        //TODO what if this thread is sleeping waiting for messages while it goes on fire. a node can never receive a message while it is on fire so it will never wake up and close gracefully. we may need to send a message to itsself while it changes to onfire.
        while(state != State.ONFIRE){
            try {
                m = messages.take();
                log.logMessage(m);
                GUIStateQueue.putState(new MessageGUILog(m.readMessage()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        log.logString("BASE is now on fire!!! we doomed! ***Sings the doom song in the voice of Gir from Invader Zim*** doom de doom de doom!");
        GUIStateQueue.putState(new MessageGUILog("BASE is now on fire!!! we doomed! ***Sings the doom song in the voice of Gir from Invader Zim*** doom de doom de doom!"));
    }

}
