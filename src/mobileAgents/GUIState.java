package mobileAgents;

import mobileAgents.messages.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The purpose of this class is to have a concurrent shared data structure.
 *
 * This will be used in a Consumer/Producer type of design pattern.
 * The GUI  object acts as a consumer. all other objects that need to be rendered will  act as Producers
 * In this scenario. This class will be pretty simple though.
 *
 * I was torn between using ConcurrentLinkedQueue or LinkedBlockingQueue I ultimately decided on
 * ConcurrentLinkedQueue per this https://stackoverflow.com/questions/1426754/linkedblockingqueue-vs-concurrentlinkedqueue
 *
 * it could be the wrong decision though? .... hmmm
 *
 * Per class this seems like this could cause a busy wait. and I should probably use LinkedBlockingQueue
 *
 */
public class GUIState {
    private ConcurrentLinkedQueue<Message> stateQueue;

    public GUIState(){
        stateQueue = new ConcurrentLinkedQueue();
    }

    /**
     * This method will add a string representing state change.
     * the protocal of this String still needs to be determined
     *
     * @param state this is a string representing state change
     * @return
     */
    public boolean addState(Message state){
        //Should this be add(state) or offer(state)? ... does it matter?
        return stateQueue.add(state);
    }

    /**
     * this will remove and return the head of the queue or null if there is nothing there
     * @return
     */
    public Message pollState(){
        return stateQueue.poll();
    }

    /**
     * peeks at the head of the queue without removing anything
     * @return the message at the head of the Queue
     */
    public Message peekState(){
        return stateQueue.peek();
    }
}
