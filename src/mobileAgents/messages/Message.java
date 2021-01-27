package mobileAgents.messages;

/**
 * interface that messages use. It would have probably been better to make an abstract class here we ended up
 * coping and pastwing methods
 */
public interface Message extends Comparable<Message> {
    /**
     *
     * @return string representation of message
     */
    String readMessage();

    /**
     *
     * @return timestamp in nanoseconds
     */
    long getTimeStamp();
}
