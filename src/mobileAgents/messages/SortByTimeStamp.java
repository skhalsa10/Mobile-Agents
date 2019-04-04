package mobileAgents.messages;

import java.util.Comparator;

/**
 * this is not used. but can be used to sort the messages by ascending timestamp
 */
public class SortByTimeStamp implements Comparator<Message> {


    @Override
    public int compare(Message o1, Message o2) {
        long result = o2.getTimeStamp() - o2.getTimeStamp();
        if(result>0){return 1;}
        else if (result ==0){return 0;}
        else{
            return -1;
        }
    }
}
