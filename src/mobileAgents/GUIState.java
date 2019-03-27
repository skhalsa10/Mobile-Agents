package mobileAgents;

import mobileAgents.messages.Message;
import mobileAgents.messages.MessageGUIConfig;
import mobileAgents.messages.MessageGUIEnd;
import mobileAgents.messages.SortByTimeStamp;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

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
 * Which I do below
 *
 * @author  Siri Khalsa
 * @version 1.0 Used Concurrent Linked Queue
 * @version 2.0 is Using LinkedBlockingQueue
 *
 * We may need to use a priorityblockingQueue and sort it based on a timestamp of the messages
 *
 */
public class GUIState {
    //private LinkedBlockingQueue<Message> stateQueue;
    private PriorityBlockingQueue<Message> stateQueue;

    public GUIState(){
        //stateQueue = new LinkedBlockingQueue<>();
        stateQueue = new PriorityBlockingQueue<Message>();
    }

    /**
     * This method will add a string representing state change.
     * the protocal of this String still needs to be determined
     *
     * @param state this is a string representing state change
     * @return
     */
    public void putState(Message state){
        //Should this be add(state) or offer(state)? ... does it matter?
        stateQueue.put(state);
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

    public static void main(String[] args) {
        Message m1 = new MessageGUIConfig();
        ((MessageGUIConfig) m1).appendStr("hi");
        Message m2 = new MessageGUIEnd();
        System.out.println(m2.compareTo(m1));

        GUIState test = new GUIState();
        test.putState(m2);
        test.putState(m1);
        System.out.println(test.pollState());
    }
}
