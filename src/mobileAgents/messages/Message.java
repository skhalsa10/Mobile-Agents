package mobileAgents.messages;

public interface Message extends Comparable<Message> {
    String readMessage();
    long getTimeStamp();
}
