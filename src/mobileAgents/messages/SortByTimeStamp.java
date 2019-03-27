package mobileAgents.messages;

import java.util.Comparator;

public class SortByTimeStamp implements Comparator<Message> {


    @Override
    public int compare(Message o1, Message o2) {
        //return 0;

        return (int)o1.getTimeStamp()- (int)o2.getTimeStamp();
    }
}
